package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.GroupDao;
import com.example.restspringapi.model.Group;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GroupDaoImpl implements GroupDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GroupDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public boolean isGroupExistsById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM groups WHERE id = :id", params, new GroupMapper()
            )).isPresent();
        }catch (IncorrectResultSizeDataAccessException e){
            return false;
        }
    }

    @Override
    public List<Group> getAll() {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM groups", Map.of(), new GroupMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update(
                "DELETE FROM groups WHERE id = :id", Collections.singletonMap("id", id)
        );
    }

    @Override
    public Optional<Group> getGroupByName(String name) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM groups WHERE name = :name",
                    Collections.singletonMap("name", name), new GroupMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void save(Group group) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                 "INSERT INTO groups (name) VALUES (:name)",
                 new MapSqlParameterSource().addValue("name", group.getName()), holder
         );
        group.setId(holder.getKey().longValue());
    }

    @Override
    public Optional<Group> getGroupById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM groups WHERE id = :id", Collections.singletonMap("id", id), new GroupMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.update(
                "DELETE FROM groups WHERE id > 0", Map.of()
        );
    }

    @Override
    public void update(Group group) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", group.getId());
        params.put("name", group.getName());
        namedParameterJdbcOperations.update(
                "UPDATE groups SET id = :id, name = :name", params
        );
    }


    private static class GroupMapper implements RowMapper<Group>{

        @Override
        public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getInt("id");
            String name = rs.getString("name");
            return new Group(id, name);
        }
    }
}
