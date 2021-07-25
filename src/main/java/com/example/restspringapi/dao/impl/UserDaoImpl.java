package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.AdministratorDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.dao.TeacherDao;
import com.example.restspringapi.dao.UserDao;
import com.example.restspringapi.model.Administrator;
import com.example.restspringapi.model.Student;
import com.example.restspringapi.model.Teacher;
import com.example.restspringapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final StudentDao studentDao;
    private final AdministratorDao administratorDao;
    private final TeacherDao teacherDao;

    @Autowired
    public UserDaoImpl(StudentDao studentDao,
                       AdministratorDao administratorDao,
                       TeacherDao teacherDao) {
        this.studentDao = studentDao;
        this.administratorDao = administratorDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {

        Optional<Student> studentByUsername = studentDao.findStudentByUsername(username);
        if (studentByUsername.isPresent()){
            return Optional.of(studentByUsername.get());
        }
        Optional<Teacher> teacherByUsername = teacherDao.findTeacherByUsername(username);
        if (teacherByUsername.isPresent()){
            return Optional.of(teacherByUsername.get());
        }
        Optional<Administrator> administratorByUsername = administratorDao.findAdministratorByUsername(username);
        if (administratorByUsername.isPresent()){
            return Optional.of(administratorByUsername.get());
        }
        throw new UsernameNotFoundException(String.format("User with username %s not found", username));
    }
}
