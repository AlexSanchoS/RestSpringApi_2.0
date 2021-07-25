package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.TeacherDao;
import com.example.restspringapi.model.Status;
import com.example.restspringapi.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherDaoImplTest {

    @Autowired
    private TeacherDao teacherDao;

    Teacher teacherForTest = new Teacher(1L,
            "Alex",
            "Teacher",
            "Password",
            Status.ACTIVE,
            LocalDate.of(1995, 5, 12));

    @AfterEach
    void afterTestClearTableTeacher(){
        teacherDao.deleteAll();
    }

    @Test
    void shouldReturnTeacherByUsername() {
        teacherDao.save(teacherForTest);
        Optional<Teacher> teacherByUsername = teacherDao.findTeacherByUsername(teacherForTest.getUsername());
        assertEquals(teacherByUsername.get(), teacherForTest);
    }

    @Test
    void shouldReturnEmptyIfTeacherByUsernameDoesNotExist() {
        Optional<Teacher> teacherByUsername = teacherDao.findTeacherByUsername(teacherForTest.getUsername());
        assertTrue(teacherByUsername.isEmpty());
    }

    @Test
    void shouldReturnTwoTeacher() {
        Teacher teacherForTest2 = new Teacher(2L,
                "Alex2",
                "Teacher2",
                "Password",
                Status.ACTIVE,
                LocalDate.of(1995, 5, 12));
        teacherDao.save(teacherForTest);
        teacherDao.save(teacherForTest2);
        assertTrue(teacherDao.getAllTeacher().size()==2);
    }

    @Test
    void shouldDeleteTeacherById() {
        teacherDao.save(teacherForTest);
        teacherDao.deleteTeacherById(teacherForTest.getId());
        assertFalse(teacherDao.existById(teacherForTest.getId()));
    }

    @Test
    void shouldReturnTrueIfExistById() {
        teacherDao.save(teacherForTest);
        assertTrue(teacherDao.existById(teacherForTest.getId()));
    }

    @Test
    void shouldReturnFalseIfNotExistById() {
        assertFalse(teacherDao.existById(teacherForTest.getId()));
    }


    @Test
    void shouldReturnTeacherByIdIfExist() {
        teacherDao.save(teacherForTest);
        Optional<Teacher> teacherById = teacherDao.findTeacherById(teacherForTest.getId());
        assertTrue(teacherById.isPresent());
    }

    @Test
    void shouldReturnEmptyIfTeacherNotExistById() {
        Optional<Teacher> teacherById = teacherDao.findTeacherById(teacherForTest.getId());
        assertTrue(teacherById.isEmpty());
    }

    @Test
    void shouldUpdateTeacherById() {
        teacherDao.save(teacherForTest);
        teacherForTest.setName("test");
        teacherDao.updateTeacherById(teacherForTest.getId(), teacherForTest);
        Optional<Teacher> teacherById = teacherDao.findTeacherById(teacherForTest.getId());
        assertTrue(teacherById.get().getName().equals("test"));
    }

    @Test
    void shouldReturnEmptyTeacherByIdIfNotExist() {
        Optional<Teacher> byId = teacherDao.getById(teacherForTest.getId());
        assertTrue(byId.isEmpty());
    }
}