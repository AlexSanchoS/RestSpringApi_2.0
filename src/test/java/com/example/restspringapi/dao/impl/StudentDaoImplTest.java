package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.GroupDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.model.Group;
import com.example.restspringapi.model.Status;
import com.example.restspringapi.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentDaoImplTest {

    private static Group groupForTest = new Group(1L, "CS1");

    private Student studentForTest = new Student(1L,
            "student",
            "password",
            "name",
            Status.ACTIVE,
            LocalDate.of(1997, 8, 1),
    1l);

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private GroupDao groupDao;

    @AfterEach
    void afterTestClearTableStudent(){
        studentDao.deleteAll();
        groupDao.deleteAll();
    }


    @Test
    void shouldReturnStudentByUsername() {
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        Optional<Student> studentByUsername = studentDao.findStudentByUsername(studentForTest.getUsername());
        assertTrue(studentByUsername.isPresent());
    }

    @Test
    void shouldReturnEmptyStudentByUsername() {
        Optional<Student> studentByUsername = studentDao.findStudentByUsername(studentForTest.getUsername());
        assertTrue(studentByUsername.isEmpty());
    }

    @Test
    void shouldReturnStudentById() {
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        Optional<Student> byId = studentDao.getById(studentForTest.getId());
        assertTrue(byId.isPresent());
    }

    @Test
    void shouldReturnEmptyById() {
        Optional<Student> byId = studentDao.getById(studentForTest.getId());
        assertTrue(byId.isEmpty());
    }

    @Test
    void shouldReturnTrueIfExistById() {
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        assertTrue(studentDao.existById(studentForTest.getId()));
    }

    @Test
    void shouldReturnFalseIfDoesNotExistById() {
        assertFalse(studentDao.existById(studentForTest.getId()));
    }

    @Test
    void shouldDeleteById() {
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        studentDao.deleteById(studentForTest.getId());
        assertFalse(studentDao.existById(studentForTest.getId()));
    }

    @Test
    void shouldUpdateStudentById() {
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        studentForTest.setName("test1");
        studentDao.updateStudentById(studentForTest.getId(), studentForTest);
        Optional<Student> byId = studentDao.getById(studentForTest.getId());
        assertTrue(byId.get().getName().equals("test1"));
    }

    @Test
    void shouldReturnTwoStudents() {
        Student studentForTest2 = new Student(2L,
                "student2",
                "password",
                "name",
                Status.ACTIVE,
                LocalDate.of(1997, 8, 1),
                1l);
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentForTest2.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        studentDao.save(studentForTest2);
        assertEquals(studentDao.getAllStudent().size(), 2);
    }

    @Test
    void shouldReturnTwoStudentByGroupId() {
        Student studentForTest2 = new Student(2L,
                "student2",
                "password",
                "name",
                Status.ACTIVE,
                LocalDate.of(1997, 8, 1),
                1l);
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentForTest2.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        studentDao.save(studentForTest2);
        assertEquals(studentDao.getStudentListByGroupId(studentForTest.getGroupId()).size(), 2);

    }
}