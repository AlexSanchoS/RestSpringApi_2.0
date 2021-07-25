package com.example.restspringapi.service;

import com.example.restspringapi.dao.MarkDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.dao.TeacherDao;
import com.example.restspringapi.model.Mark;
import com.example.restspringapi.model.Student;
import com.example.restspringapi.model.Teacher;
import com.example.restspringapi.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarkService {

    private final MarkDao markDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MarkService(MarkDao markDao,
                       StudentDao studentDao,
                       TeacherDao teacherDao,
                       JwtTokenProvider jwtTokenProvider) {
        this.markDao = markDao;
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<Mark> getAllMarksForStudentById(long studentId) {
        Optional<Student> optionalStudent = studentDao.getById(studentId);
        if (optionalStudent.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Student with id %s doesn't exist", studentId)
            );
        }
        return markDao.getMarkListByStudentId(studentId);
    }

    public List<Mark> getAllMark() {
        return markDao.getAll();
    }

    public List<Mark> getAllMarksForTeacherById(long teacherId) {
        Optional<Teacher> optionalTeacher = teacherDao.getById(teacherId);
        if (optionalTeacher.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Teacher with id %s doesn't exist", teacherId)
            );
        }
        return markDao.getMarkListByTeacherId(teacherId);

    }

    public void addNewMark(Mark mark) {
        if (mark == null) {
            throw new IllegalStateException("Mark can't be null");
        }
        if (mark.getRating() < 1 || mark.getRating() > 12) {
            throw new IllegalStateException("Mark shod be between 1 and 12");
        }
        if (teacherDao.getById(mark.getTeacherId()).isEmpty()) {
            throw new IllegalStateException(
                    String.format("Teacher with id %s doesn't exist", mark.getTeacherId())
            );
        }
        if (studentDao.getById(mark.getStudentId()).isEmpty()) {
            throw new IllegalStateException(
                    String.format("Student with id %s doesn't exist", mark.getStudentId())
            );
        }

        markDao.save(mark);
    }

    public void updateGroup(long id, Mark markForUpdate) {
        if (markForUpdate == null) {
            throw new IllegalStateException("Mark can't be null");
        }
        Optional<Mark> optionalMark = markDao.getMarkById(id);
        if (optionalMark.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Mark with id %s doesn't exist", id)
            );
        }
        Mark markFromDB = optionalMark.get();
        if (markForUpdate.getRating() != markFromDB.getRating() &&
                markForUpdate.getRating() > 0 &&
                markForUpdate.getRating() < 13) {
            markFromDB.setRating(markForUpdate.getRating());
        }
        if (markForUpdate.getStudentId() != markFromDB.getStudentId() &&
                markForUpdate.getStudentId() > 0 &&
                studentDao.getById(markForUpdate.getStudentId()).isPresent()) {
            markFromDB.setStudentId(markForUpdate.getStudentId());
        }

        if (markForUpdate.getTeacherId() != markFromDB.getTeacherId() &&
                markForUpdate.getTeacherId() > 0 &&
                teacherDao.getById(markForUpdate.getTeacherId()).isPresent()) {
            markFromDB.setTeacherId(markForUpdate.getTeacherId());
        }

        markDao.update(markFromDB);
    }

    public void deleteMarkById(long id) {
        if (id<1 || markDao.getMarkById(id).isEmpty()){
            throw new IllegalStateException(
                    String.format("Mark with id %s doesn't exist", id)
            );
        }
    }

    public List<Mark> getAllMarksForTeacherByToken(String token) {
        String username = jwtTokenProvider.getUsername(token);
        Optional<Teacher> optionalTeacher = teacherDao.findTeacherByUsername(username);
        if (optionalTeacher.isEmpty()){
            throw new IllegalStateException(
                    String.format("Teacher with username doesn't exists", username)
            );
        }
        return markDao.getMarkListByTeacherId(optionalTeacher.get().getId());
    }

    public void addNewMarkForTeacher(String token, Mark mark) {
        if (mark == null) {
            throw new IllegalStateException("Mark can't be null");
        }
        if (mark.getRating() < 1 || mark.getRating() > 12) {
            throw new IllegalStateException("Mark shod be between 1 and 12");
        }
        String username = jwtTokenProvider.getUsername(token);
        Optional<Teacher> optionalTeacher = teacherDao.findTeacherByUsername(username);
        if (optionalTeacher.isEmpty()){
            throw new IllegalStateException(
                    String.format("Teacher with username doesn't exists", username)
            );
        }
        Optional<Student> byId = studentDao.getById(mark.getStudentId());
        if (byId.isEmpty()){
            throw new IllegalStateException(
                    String.format("Student with id %s doesn't exists", mark.getStudentId())
            );
        }
        mark.setTeacherId(optionalTeacher.get().getId());
        markDao.save(mark);
    }
}
