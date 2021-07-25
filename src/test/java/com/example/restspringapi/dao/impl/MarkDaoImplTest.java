package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.GroupDao;
import com.example.restspringapi.dao.MarkDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.dao.TeacherDao;
import com.example.restspringapi.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarkDaoImplTest {

    @Autowired
    private MarkDao markDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private TeacherDao teacherDao;

    private Mark markForTest = new Mark(1L,10, 1L, 1L);

    private Teacher teacherForTest = new Teacher(1L,
            "name",
            "username",
            "password",
            Status.ACTIVE,
            LocalDate.of(1994, 2,1));

    private Student studentForTest = new Student(
            1L,
            "username",
            "password",
            "name",
            Status.ACTIVE,
            LocalDate.of(1997, 1, 2),
            1L
    );

    private Group groupForTest = new Group(1L, "CS1");

    @BeforeEach
    void preparationTeacherStudentGroupMarkForTest(){
        groupDao.save(groupForTest);
        studentForTest.setGroupId(groupForTest.getId());
        studentDao.save(studentForTest);
        teacherDao.save(teacherForTest);
        markForTest.setTeacherId(teacherForTest.getId());
        markForTest.setStudentId(studentForTest.getId());
        markDao.save(markForTest);
    }

    @AfterEach
    void afterTestClearTables(){
        markDao.deleteAll();
        studentDao.deleteAll();
        teacherDao.deleteAll();
        groupDao.deleteAll();
    }

    @Test
    void shouldReturnTwoMarks() {
        Mark markForTest2 = new Mark(2L,12, studentForTest.getId(), teacherForTest.getId());
        markDao.save(markForTest2);
        assertEquals(markDao.getAll().size(),2);
    }

    @Test
    void shouldReturnTwoMarksByStudentId() {
        Mark markForTest2 = new Mark(2L,12, studentForTest.getId(), teacherForTest.getId());
        markDao.save(markForTest2);
        assertEquals(markDao.getMarkListByStudentId(studentForTest.getId()).size(),2);
    }

    @Test
    void shouldReturnTwoMarksByTeacherId() {
        Mark markForTest2 = new Mark(2L,12, studentForTest.getId(), teacherForTest.getId());
        markDao.save(markForTest2);
        assertEquals(markDao.getMarkListByTeacherId(teacherForTest.getId()).size(),2);
    }

    @Test
    void shouldReturnMarkById() {
        assertTrue(markDao.getMarkById(markForTest.getId()).isPresent());
    }

    @Test
    void shouldReturnEmptyMarkById() {
        markDao.deleteAll();
        assertTrue(markDao.getMarkById(markForTest.getId()).isEmpty());
    }

    @Test
    void shouldUpdateMark() {
        markForTest.setRating(1);
        markDao.update(markForTest);
        assertEquals(markDao.getMarkById(markForTest.getId()).get().getRating(), 1);
    }

    @Test
    void shouldReturnMarkListByStudentIdAndTeacherId() {
        assertEquals(markDao.getMarkListByStudentIdAndTeacherId(
                studentForTest.getId(),
                teacherForTest.getId()).size(), 1);
    }
}