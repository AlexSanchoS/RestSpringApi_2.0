package com.example.restspringapi.controllers;

import com.example.restspringapi.model.Mark;
import com.example.restspringapi.model.Student;
import com.example.restspringapi.service.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/allMarks")
    @PreAuthorize("hasAnyAuthority('student:read')")
    public List<Mark> getAllMarkForStudent(@RequestHeader("Authorization") String token){
        return studentService.getMarkListForStudentByToken(token);
    }

    @GetMapping("/allGroupmate")
    @PreAuthorize("hasAnyAuthority('student:read')")
    public List<Student> getAllGroupMate(@RequestHeader("Authorization") String token){
        return studentService.getGroupmateListByToken(token);
    }

    @GetMapping("/allMark/{teacherId}")
    @PreAuthorize("hasAnyAuthority('student:read')")
    public List<Mark> getAllMarkForTeacher(@PathVariable("teacherId") long teacherId, @RequestHeader("Authorization") String token){
        return studentService.getMarkListForStudentByTokenAndTeacherId(token, teacherId);
    }
}
