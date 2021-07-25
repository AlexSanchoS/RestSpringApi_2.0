package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.AdministratorDao;
import com.example.restspringapi.model.Administrator;
import com.example.restspringapi.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdministratorDaoImpl implements AdministratorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    public AdministratorDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Administrator> findAdministratorByUsername(String username) {
        Map<String, Object> params = Collections.singletonMap("username", username);
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "select * from administrator where username = :username", params, new AdministratorMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void save(Administrator administrator) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("name", administrator.getName());
        namedParameters.addValue("username", administrator.getUsername());
        namedParameters.addValue("password", administrator.getPassword());
        namedParameters.addValue("status", administrator.getStatus().name());
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "INSERT INTO administrator (name, username, password, status) " +
                        "values (:name, :username,:password, :status)", namedParameters, holder);
        administrator.setId(holder.getKey().longValue());
    }

    @Override
    public List<Administrator> getAll() {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM administrator",
                Collections.singletonMap("", ""), new AdministratorMapper()
        );
    }

    @Override
    public boolean isAdminExistById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM administrator WHERE id = :id",
                    Collections.singletonMap("id", id), new AdministratorMapper()
            )).isPresent();
        }catch (IncorrectResultSizeDataAccessException e){
            return false;
        }
    }

    @Override
    public void deleteAdminById(long id) {
        namedParameterJdbcOperations.update(
                "DELETE FROM administrator WHERE id = :id",
                Collections.singletonMap("id", id)
        );
    }

    @Override
    public Optional<Administrator> findAdministratorById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM administrator WHERE id = :id",
                    Collections.singletonMap("id", id), new AdministratorMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void updateAdminById(long id, Administrator admin) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", admin.getName());
        params.put("username", admin.getUsername());
        params.put("password", admin.getPassword());
        params.put("status", admin.getStatus().name());
        namedParameterJdbcOperations.update(
                "UPDATE administrator SET name = :name, username = :username, password = :password, " +
                        "status = :status WHERE id = :id", params
        );
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.update(
                "DELETE FROM administrator WHERE id > 0", Collections.singletonMap("", "")
        );
    }

    private static class AdministratorMapper implements RowMapper<Administrator> {

        @Override
        public Administrator mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String status = resultSet.getString("status");

            return new Administrator(id,name, username, password, Status.valueOf(status));
        }
    }
}
