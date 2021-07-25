package com.example.restspringapi.dao;

import com.example.restspringapi.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    Optional<Student> findStudentByUsername(String username);

    Optional<Student> getById(Long id);

    boolean existById(long id);

    void deleteById(long id);

    void save(Student student);

    void updateStudentById(long id, Student student);

    List<Student> getAllStudent();

    List<Student> getStudentListByGroupId(long groupId);

    void deleteAll();
}
