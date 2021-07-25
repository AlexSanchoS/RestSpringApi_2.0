package com.example.restspringapi.service;

import com.example.restspringapi.dao.*;
import com.example.restspringapi.model.Group;
import com.example.restspringapi.model.Mark;
import com.example.restspringapi.model.Student;
import com.example.restspringapi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final AdministratorDao administratorDao;
    private final PasswordEncoder passwordEncoder;
    private final GroupDao groupDao;
    private final MarkDao markDao;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public StudentService(StudentDao studentDao,
                          TeacherDao teacherDao,
                          AdministratorDao administratorDao,
                          PasswordEncoder passwordEncoder,
                          GroupDao groupDao,
                          MarkDao markDao,
                          JwtTokenProvider jwtTokenProvider) {
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.administratorDao = administratorDao;
        this.passwordEncoder = passwordEncoder;
        this.groupDao = groupDao;
        this.markDao = markDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void deleteStudentById(long id) {
        if (id < 0) {
            throw new IllegalStateException("Id shod be more then 0");
        }
        boolean exist = studentDao.existById(id);
        if (exist) {
            studentDao.deleteById(id);
        } else
            throw new IllegalStateException(
                    String.format("Student with id %s does not exist", id)
            );
    }

    public void addNewStudent(Student student) {
        try {
            studentValidation(student);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(String.format("Student %s is invalid", student));
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentDao.save(student);
    }

    public void updateStudent(long id, Student studentForUpdate) {
        if (studentForUpdate == null) {
            throw new IllegalStateException("Student can't be null");
        }
        Optional<Student> optionalStudentFromDB = studentDao.getById(id);
        if (optionalStudentFromDB.isEmpty()) {
            throw new IllegalStateException(String.format("Student with id %s doesn't exist", id));
        }
        Student studentFromDB = optionalStudentFromDB.get();

        //name
        if (studentForUpdate.getName() != null
                && !studentForUpdate.getName().equals(studentFromDB.getName())
                && studentForUpdate.getName().length() > 2) {
            studentFromDB.setName(studentForUpdate.getName());
        }

        //username
        if (studentForUpdate.getUsername() != null
                && !studentForUpdate.getUsername().equals(studentFromDB.getUsername())
                && studentForUpdate.getUsername().length() > 2) {
            if (studentDao.findStudentByUsername(studentForUpdate.getUsername()).isEmpty() &&
                    teacherDao.findTeacherByUsername(studentForUpdate.getUsername()).isEmpty() &&
                    administratorDao.findAdministratorByUsername(studentForUpdate.getUsername()).isEmpty()) {
                studentFromDB.setUsername(studentForUpdate.getUsername());
            }
        }

        //password
        if (studentForUpdate.getPassword() != null
                && studentForUpdate.getPassword().length() > 4
                && !passwordEncoder.encode(studentForUpdate.getPassword()).equals(studentFromDB.getPassword())) {
            studentFromDB.setPassword(passwordEncoder.encode(studentForUpdate.getPassword()));
        }

        //dob
        if (studentForUpdate.getDob() != null
                && studentForUpdate.getDob() != studentFromDB.getDob()
                && !new Date().before(java.sql.Date.valueOf(studentForUpdate.getDob()))) {
            studentFromDB.setDob(studentForUpdate.getDob());
        }

        //status
        if (studentForUpdate.getStatus() != null &&
                !studentForUpdate.getStatus().equals(studentFromDB.getStatus())) {
            studentFromDB.setStatus(studentForUpdate.getStatus());
        }

        //groupId
        if (studentForUpdate.getGroupId() > 0
                && studentForUpdate.getGroupId() != studentFromDB.getGroupId()) {
            if (!groupDao.isGroupExistsById(studentForUpdate.getGroupId())) {
                throw new IllegalStateException(
                        String.format("Group with id %s doesn't exist", studentForUpdate.getGroupId())
                );
            }
            studentFromDB.setGroupId(studentForUpdate.getGroupId());
        }

        studentDao.updateStudentById(id, studentFromDB);
    }

    private void studentValidation(Student student) {
        if (student == null) {
            throw new IllegalStateException("Student can`t be null");
        }

        //name validation
        if (student.getName() == null || student.getName().length() < 2) {
            throw new IllegalStateException(String.format("Name %s is incorrect", student.getName()));
        }
        //password validation
        if (student.getPassword() == null) {
            throw new IllegalStateException("Password can't be null");
        }
        if (student.getPassword().length() < 4) {
            throw new IllegalStateException(
                    String.format("Password %s is to short", student.getPassword())
            );
        }

        //dob validation
        if (student.getDob() == null) {
            throw new IllegalStateException("Date of birthday can't be null");
        }
        if (new Date().before(java.sql.Date.valueOf(student.getDob()))) {
            throw new IllegalStateException(
                    String.format("Date of birthday %s is to early", student.getDob())
            );
        }

        //id validation
        if (student.getId() > 0) {
            Optional<Student> studentById = studentDao.getById(student.getId());
            if (studentById.isPresent()) {
                throw new IllegalStateException(String.format("Student with id %s is already exist", student.getId()));
            }
        }

        //username validation
        if (student.getUsername() == null) {
            throw new IllegalStateException("Username can't be null");
        }
        if (student.getUsername().length() < 2) {
            throw new IllegalStateException(String.format("Username %s is to short", student.getUsername()));
        }

        if (studentDao.findStudentByUsername(student.getUsername()).isPresent() ||
                administratorDao.findAdministratorByUsername(student.getUsername()).isPresent() ||
                teacherDao.findTeacherByUsername(student.getUsername()).isPresent()) {
            throw new IllegalStateException(
                    String.format("Student with username %s is already exist", student.getUsername())
            );
        }


        //group validation
        if (student.getGroupId() <= 0) {
            throw new IllegalStateException("Group id shod be > 0");
        }
        if (!groupDao.isGroupExistsById(student.getGroupId())) {
            throw new IllegalStateException(
                    String.format("Group with id %s doesn't exist", student.getGroupId())
            );
        }
    }

    public List<Student> getAllStudent() {
        return studentDao.getAllStudent();
    }

    public List<Student> getStudentForGroupById(long groupId) {
        if (!groupDao.isGroupExistsById(groupId)) {
            throw new IllegalStateException(
                    String.format("Group with id %s doesn't exist", groupId)
            );
        }
        return studentDao.getStudentListByGroupId(groupId);
    }

    public List<Student> getStudentForGroupByName(String groupName) {
        Optional<Group> groupByName = groupDao.getGroupByName(groupName);
        if (groupByName.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Group with name %s doesn't exist", groupName)
            );
        }
        return studentDao.getStudentListByGroupId(groupByName.get().getId());
    }

    public void changeGroupForStudent(long studentId, long groupId) {
        if (!groupDao.isGroupExistsById(groupId)) {
            throw new IllegalStateException(
                    String.format("Group with id %s doesn't exist", groupId)
            );
        }
        Optional<Student> studentOptional = studentDao.getById(studentId);
        if (studentOptional.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Student with id %s doesn't exist")
            );
        }
        Student student = studentOptional.get();
        student.setGroupId(groupId);
        studentDao.updateStudentById(student.getId(), student);
    }

    public void changeGroupForStudents(long oldGroupId, long newGroupId) {
        if (oldGroupId == newGroupId) {
            return;
        }
        if (!groupDao.isGroupExistsById(oldGroupId)) {
            throw new IllegalStateException(
                    String.format("Group with id %s doesn't exist", oldGroupId)
            );
        }
        if (!groupDao.isGroupExistsById(newGroupId)) {
            throw new IllegalStateException(
                    String.format("Group with id %s doesn't exist", newGroupId)
            );
        }

        List<Student> oldGroupStudentList = studentDao.getStudentListByGroupId(oldGroupId);
        for (Student student : oldGroupStudentList) {
            student.setGroupId(newGroupId);
            studentDao.updateStudentById(student.getId(), student);

        }
    }

    public List<Mark> getMarkListForStudentByToken(String token) {
        String username = jwtTokenProvider.getUsername(token);
        Optional<Student> studentByUsername = studentDao.findStudentByUsername(username);
        if (studentByUsername.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Student with username %s doesn't exist", username)
            );
        }
        return markDao.getMarkListByStudentId(studentByUsername.get().getId());
    }

    public List<Student> getGroupmateListByToken(String token) {
        String username = jwtTokenProvider.getUsername(token);
        Optional<Student> studentByUsername = studentDao.findStudentByUsername(username);
        if (studentByUsername.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Student with username %s doesn't exist", username)
            );
        }

        List<Student> studentListByGroupId = studentDao.getStudentListByGroupId(studentByUsername.get().getId());
        studentListByGroupId
                .stream()
                .forEach(student -> {
                    student.setUsername("");
                    student.setPassword("");
                });
        return studentListByGroupId;

    }

    public List<Mark> getMarkListForStudentByTokenAndTeacherId(String token, long teacherId) {
        String username = jwtTokenProvider.getUsername(token);
        if (teacherId<1 || !teacherDao.existById(teacherId)){
            throw new IllegalStateException(
                    String.format("Teacher with id %s doesn't exist")
            );
        }
        Optional<Student> studentByUsername = studentDao.findStudentByUsername(username);
        if (studentByUsername.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Student with username %s doesn't exist", username)
            );
        }
        return markDao.getMarkListByStudentIdAndTeacherId(studentByUsername.get().getId(), teacherId);
    }
}
