package com.example.restspringapi.controllers;

import com.example.restspringapi.model.Mark;
import com.example.restspringapi.service.MarkService;
import com.example.restspringapi.service.TeacherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final MarkService markService;

    public TeacherController(TeacherService teacherService, MarkService markService) {
        this.teacherService = teacherService;
        this.markService = markService;
    }


    @GetMapping("/allMark")
    @PreAuthorize("hasAnyAuthority('mark:read')")
    public List<Mark> getAllMarkFor(){
        return markService.getAllMark();
    }

    @GetMapping("/allMarkForTeacher")
    @PreAuthorize("hasAnyAuthority('mark:read')")
    public List<Mark> getAllMarkForCurrentTeacher(@RequestHeader("Authorization") String token){
        return markService.getAllMarksForTeacherByToken(token);
    }

    @PostMapping("/addNewMark")
    @PreAuthorize("hasAnyAuthority('mark:write')")
    public void addNewMark(@RequestHeader("Authorization") String token, @RequestBody Mark mark){
        markService.addNewMarkForTeacher(token, mark);
    }
}
