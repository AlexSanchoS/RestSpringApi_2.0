package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.MarkDao;
import com.example.restspringapi.model.Mark;
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
public class MarkDaoImpl implements MarkDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public MarkDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Mark> getAll() {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM mark", Map.of(), new MarkMapper()
        );
    }

    @Override
    public List<Mark> getMarkListByStudentId(long studentId) {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM mark WHERE student_id = :studentId",
                Collections.singletonMap("studentId", studentId), new MarkMapper()
        );
    }

    @Override
    public List<Mark> getMarkListByTeacherId(long teacherId) {
        return namedParameterJdbcOperations.query(
                "SELECT * FROM mark WHERE teacher_id = :teacherId",
                Collections.singletonMap("teacherId", teacherId), new MarkMapper()
        );
    }

    @Override
    public void save(Mark mark) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("rating", mark.getRating());
        namedParameters.addValue("studentId", mark.getStudentId());
        namedParameters.addValue("teacherId", mark.getTeacherId());
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "INSERT INTO mark (rating, student_id, teacher_id) " +
                        "VALUES (:rating, :studentId, :teacherId)", namedParameters, holder
        );
        mark.setId(holder.getKey().longValue());
    }

    @Override
    public Optional<Mark> getMarkById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject(
                    "SELECT * FROM mark WHERE id = :id", Collections.singletonMap("id", id), new MarkMapper()
            ));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Mark mark) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", mark.getId());
        params.put("rating", mark.getRating());
        params.put("studentId", mark.getStudentId());
        params.put("teacherId", mark.getTeacherId());
        namedParameterJdbcOperations.update(
                "UPDATE mark SET rating = :rating, student_id = :studentId, teacher_id = :teacherId WHERE id = :id", params
        );
    }

    @Override
    public List<Mark> getMarkListByStudentIdAndTeacherId(long studentId, long teacherId) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        params.put("teacherId", teacherId);
        return namedParameterJdbcOperations.query(
                "SELECT * FROM mark WHERE student_id = :studentId and teacher_id = :teacherId",
                params, new MarkMapper()
        );
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.update(
                "DELETE FROM mark WHERE id > 0", Map.of()
        );
    }

    private static class MarkMapper implements RowMapper<Mark> {

        @Override
        public Mark mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getInt("id");
            int rating = rs.getInt("rating");
            long studentId = rs.getInt("student_id");
            long teacherId = rs.getInt("teacher_id");
            return new Mark(id, rating, studentId, teacherId);
        }
    }
}
