package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.TeacherDao;
import com.example.restspringapi.model.Status;
import com.example.restspringapi.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    public TeacherDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Teacher> findTeacherByUsername(String username) {
        Map<String, Object> param = Collections.singletonMap("username", username);
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM teacher WHERE username = :username", param, new TeacherMapper()
            ));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Teacher> getAllTeacher() {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM teacher", Map.of(), new TeacherMapper()
        );
    }

    @Override
    public void deleteTeacherById(long id) {
        namedParameterJdbcOperations.update(
                "DELETE FROM teacher WHERE id = :id", Collections.singletonMap("id", id));
    }

    @Override
    public boolean existById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM teacher WHERE id = :id",
                    Collections.singletonMap("id", id), new TeacherMapper()
            )).isPresent();
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    @Override
    public void save(Teacher teacher) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("name", teacher.getName());
        namedParameters.addValue("username", teacher.getUsername());
        namedParameters.addValue("password", teacher.getPassword());
        namedParameters.addValue("status", teacher.getStatus().name());
        namedParameters.addValue("dob", teacher.getDob());
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "INSERT INTO teacher (name, username, password, status, dob) " +
                        "VALUES (:name, :username, :password, :status, :dob)", namedParameters, holder
        );
        teacher.setId(holder.getKey().longValue());
    }

    @Override
    public Optional<Teacher> findTeacherById(long id) {
        try{
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM teacher where id = :id",
                    Collections.singletonMap("id", id), new TeacherMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void updateTeacherById(long id, Teacher teacher) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", teacher.getName());
        params.put("username", teacher.getUsername());
        params.put("password", teacher.getPassword());
        params.put("status", teacher.getStatus().name());
        params.put("dob", teacher.getDob());
        namedParameterJdbcOperations.update(
                "UPDATE teacher SET name = :name, username = :username, password = :password, " +
                        "status = :status, dob = :dob WHERE id = :id", params
        );
    }

    @Override
    public Optional<Teacher> getById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM teacher WHERE id = :id",
                    Collections.singletonMap("id", id), new TeacherMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.update(
                "DELETE FROM teacher WHERE id>0", Map.of()
        );
    }

    private static class TeacherMapper implements RowMapper<Teacher> {

        @Override
        public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String status = resultSet.getString("status");
            LocalDate dob = resultSet.getDate("dob").toLocalDate();
            return new Teacher(id, name, username, password, Status.valueOf(status), dob);
        }
    }
}
