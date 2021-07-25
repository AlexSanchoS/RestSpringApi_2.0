package com.example.restspringapi.dao;

import com.example.restspringapi.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherDao {

    Optional<Teacher> findTeacherByUsername(String username);

    List<Teacher> getAllTeacher();

    void deleteTeacherById(long id);

    boolean existById(long id);

    void save(Teacher teacher);

    Optional<Teacher> findTeacherById(long id);

    void updateTeacherById(long id, Teacher teacher);

    Optional<Teacher> getById(long id);

    void deleteAll();
}
