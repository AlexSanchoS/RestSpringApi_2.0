package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.model.Status;
import com.example.restspringapi.model.Student;
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
public class StudentDaoImpl implements StudentDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    public StudentDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Student> findStudentByUsername(String username) {
        Map<String, Object> params = Collections.singletonMap("username", username);
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM student where username = :username", params, new StudentMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }

    }

    @Override
    public Optional<Student> getById(Long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM student WHERE id = :id", Collections.singletonMap("id", id), new StudentMapper()
            ));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean existById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM student WHERE id = :id", params, new StudentMapper()
            )).isPresent();
        }catch (IncorrectResultSizeDataAccessException e){
            return false;
        }
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update("DELETE FROM student WHERE id = :id", params);
    }

    @Override
    public void save(Student student) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("name", student.getName());
        namedParameters.addValue("username", student.getUsername());
        namedParameters.addValue("password", student.getPassword());
        namedParameters.addValue("status", student.getStatus().name());
        namedParameters.addValue("dob", student.getDob());
        namedParameters.addValue("groupsId", student.getGroupId());
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "INSERT INTO student (name, username, password, status, dob, groups_id) " +
                        "values ( :name, :username, :password, :status, :dob, :groupsId)",
                namedParameters, holder);
        student.setId(holder.getKey().longValue());
    }


    @Override
    public void updateStudentById(long id, Student student) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", student.getName());
        params.put("username", student.getUsername());
        params.put("password", student.getPassword());
        params.put("status", student.getStatus().name());
        params.put("dob", student.getDob());
        params.put("groupsId", student.getGroupId());
        namedParameterJdbcOperations.update(
                "UPDATE student SET name = :name, username = :username, password = :password, status = :status, dob = :dob, groups_id =:groupsId "+
                        "WHERE id=:id", params
        );
    }

    @Override
    public List<Student> getAllStudent() {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM student", Map.of(), new StudentMapper()
        );
    }

    @Override
    public List<Student> getStudentListByGroupId(long groupId) {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM student WHERE groups_id = :groupId",
                Collections.singletonMap("groupId", groupId), new StudentMapper()
        );
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.update(
                "DELETE FROM student WHERE id > 0", Map.of()
        );
    }

    private static class StudentMapper implements RowMapper<Student>{

        @Override
        public Student mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String status = resultSet.getString("status");
            LocalDate dob = resultSet.getDate("dob")
                    .toLocalDate();
            long groupId = resultSet.getInt("groups_id");
            return new Student(id,username, password, name, Status.valueOf(status), dob, groupId);
        }
    }
}
