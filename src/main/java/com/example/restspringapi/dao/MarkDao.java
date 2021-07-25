package com.example.restspringapi.dao;

import com.example.restspringapi.model.Mark;

import java.util.List;
import java.util.Optional;

public interface MarkDao {
    List<Mark> getAll();

    List<Mark> getMarkListByStudentId(long studentId);

    List<Mark> getMarkListByTeacherId(long teacherId);

    void save(Mark mark);

    Optional<Mark> getMarkById(long id);

    void update(Mark mark);

    List<Mark> getMarkListByStudentIdAndTeacherId(long id, long teacherId);

    void deleteAll();
}
