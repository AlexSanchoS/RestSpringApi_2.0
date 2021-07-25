package com.example.restspringapi.service;

import com.example.restspringapi.dao.AdministratorDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.dao.TeacherDao;
import com.example.restspringapi.model.Student;
import com.example.restspringapi.model.Teacher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherDao teacherDao;
    private final StudentDao studentDao;
    private final AdministratorDao administratorDao;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherDao teacherDao, StudentDao studentDao, AdministratorDao administratorDao, PasswordEncoder passwordEncoder) {
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.administratorDao = administratorDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Teacher> getAllTeacher() {
        return teacherDao.getAllTeacher();
    }

    public void deleteTeacherById(long id) {
        if (!teacherDao.existById(id)) {
            throw new IllegalStateException(
                    String.format("Teacher with id %s doesn't exist", id));
        }
        teacherDao.deleteTeacherById(id);
    }

    public void addNewTeacher(Teacher teacher) {
        try {
            teacherValidation(teacher);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    String.format("Teacher %s isn't valid", teacher));
        }
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherDao.save(teacher);
    }

    private void teacherValidation(Teacher teacher) {
        //null
        if (teacher == null) {
            throw new IllegalStateException("Teacher can't be null");
        }

        //name
        if (teacher.getName() == null) {
            throw new IllegalStateException("Name can't be null");
        }
        if (teacher.getName().length() < 2) {
            throw new IllegalStateException(
                    String.format("Name is to short", teacher.getName()));
        }

        //password
        if (teacher.getPassword() == null) {
            throw new IllegalStateException("Password can't be null");
        }
        if (teacher.getPassword().length() < 4) {
            throw new IllegalStateException(
                    String.format("Password %s is to short", teacher.getPassword()));
        }

        //status
        if (teacher.getStatus() == null) {
            throw new IllegalStateException("Status can't be null");
        }

        //dob
        if (teacher.getDob() == null) {
            throw new IllegalStateException("Date of birthday can't be null");
        }
        if (new Date().before(java.sql.Date.valueOf(teacher.getDob()))) {
            throw new IllegalStateException("Teacher is to young");
        }

        //username
        if (teacher.getUsername() == null) {
            throw new IllegalStateException("Username can't be null");
        }
        if (teacher.getUsername().length() < 4) {
            throw new IllegalStateException(
                    String.format("username %s is to short", teacher.getUsername())
            );
        }
        if (teacherDao.findTeacherByUsername(teacher.getUsername()).isPresent() ||
                studentDao.findStudentByUsername(teacher.getUsername()).isPresent() ||
                administratorDao.findAdministratorByUsername(teacher.getUsername()).isPresent()) {
            throw new IllegalStateException(
                    String.format("User with username %s is already exist", teacher.getUsername())
            );
        }
    }

    public void updateTeacher(long id, Teacher teacherForUpdate) {
        if (teacherForUpdate == null) {
            throw new IllegalStateException("Teacher can't be null");
        }
        Optional<Teacher> optionalTeacherFromDB = teacherDao.findTeacherById(id);
        if (optionalTeacherFromDB.isEmpty()) {
            throw new IllegalStateException(String.format("Student with id %s doesn't exist", id));
        }
        Teacher teacherFromDB = optionalTeacherFromDB.get();

        //name
        if (teacherForUpdate.getName() != null
                && !teacherForUpdate.getName().equals(teacherFromDB.getName())
                && teacherForUpdate.getName().length() > 2) {
            teacherFromDB.setName(teacherForUpdate.getName());
        }

        //username
        if (teacherForUpdate.getUsername() != null
                && !teacherForUpdate.getUsername().equals(teacherFromDB.getUsername())
                && teacherForUpdate.getUsername().length() > 2) {
            if (teacherDao.findTeacherByUsername(teacherForUpdate.getUsername()).isEmpty()&&
            studentDao.findStudentByUsername(teacherForUpdate.getUsername()).isEmpty() &&
            administratorDao.findAdministratorByUsername(teacherForUpdate.getUsername()).isEmpty()) {
                teacherFromDB.setUsername(teacherForUpdate.getUsername());
            }
        }

        //password
        if (teacherForUpdate.getPassword() != null
                && teacherForUpdate.getPassword().length() > 4
                && !passwordEncoder.encode(teacherForUpdate.getPassword()).equals(teacherFromDB.getPassword())) {
            teacherFromDB.setPassword(passwordEncoder.encode(teacherForUpdate.getPassword()));
        }

        //dob
        if (teacherForUpdate.getDob()!=null
                && teacherForUpdate.getDob()!=teacherFromDB.getDob()
                && !new Date().before(java.sql.Date.valueOf(teacherForUpdate.getDob()))){
            teacherFromDB.setDob(teacherForUpdate.getDob());
        }

        if (teacherForUpdate.getStatus()!=null&&
        !teacherForUpdate.getStatus().equals(teacherFromDB.getStatus())){
            teacherFromDB.setStatus(teacherForUpdate.getStatus());
        }


        teacherDao.updateTeacherById(id, teacherFromDB);
    }
}
