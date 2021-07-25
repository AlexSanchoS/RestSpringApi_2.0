package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.*;
import com.example.restspringapi.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoImplTest {

    @Autowired
    private AdministratorDao administratorDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private UserDao userDao;

    @AfterEach
    void clearTablesAfterTests(){
        administratorDao.deleteAll();
        teacherDao.deleteAll();
        studentDao.deleteAll();
    }

    @Test
    void shouldReturnAdmin() {
        Administrator administrator = new Administrator(1L,
                "name",
                "username",
                "password",
                Status.ACTIVE);
        administratorDao.save(administrator);
        assertEquals(userDao.findUserByUsername(administrator.getUsername()).get().getClass(), administrator.getClass());
    }

    @Test
    void shouldReturnTeacher() {
        Teacher teacher = new Teacher(1L,
                "name",
                "username",
                "password",
                Status.ACTIVE,
                LocalDate.of(1999, 1,1));
        teacherDao.save(teacher);
        assertEquals(userDao.findUserByUsername(teacher.getUsername()).get().getClass(), teacher.getClass());
    }

    @Test
    void shouldReturnStudent() {
        Group group = new Group(1L, "cs1");
        groupDao.save(group);
        Student student = new Student(1L,
                "username",
                "password",
                "name",
                Status.ACTIVE,
                LocalDate.of(1999, 1,1),
                group.getId());
        studentDao.save(student);
        assertEquals(userDao.findUserByUsername(student.getUsername()).get().getClass(), student.getClass());
    }

    @Test
    void shouldThrowUsernameNotFoundException() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDao.findUserByUsername("username");
        });

        String expectedMessage = "User with username username not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



}