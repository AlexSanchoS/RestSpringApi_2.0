package com.example.restspringapi.controllers;

import com.example.restspringapi.model.*;
import com.example.restspringapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdministratorService administratorService;
    private final GroupService groupService;
    private final MarkService markService;

    @Autowired
    public AdminController(StudentService studentService,
                           TeacherService teacherService,
                           AdministratorService administratorService,
                           GroupService groupService,
                           MarkService markService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.administratorService = administratorService;
        this.groupService = groupService;
        this.markService = markService;
    }

    @GetMapping("/allStudent")
    @PreAuthorize("hasAuthority('student:read')")
    public List<Student> allStudent(){
        return studentService.getAllStudent();
    }

    @GetMapping("/studentForGroup/{groupId}")
    @PreAuthorize("hasAuthority('student:read')")
    public List<Student> studentForGroup(@PathVariable("groupId") long groupId){
        return studentService.getStudentForGroupById(groupId);
    }

    @GetMapping("/studentForGroupByName/{groupName}")
    @PreAuthorize("hasAuthority('student:read')")
    public List<Student> studentForGroup(@PathVariable("groupName") String groupName){
        return studentService.getStudentForGroupByName(groupName);
    }

    @DeleteMapping(path = "/deleteStudent/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") long id){
        studentService.deleteStudentById(id);
    }

    @PostMapping(path = "/addStudent")
    @PreAuthorize("hasAuthority('student:write')")
    public void addStudent(@RequestBody Student student){
        studentService.addNewStudent(student);
    }

    @PutMapping(path = "/updateStudent/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") long id, @RequestBody Student student){
        studentService.updateStudent(id, student);
    }




    @GetMapping("/allTeacher")
    @PreAuthorize("hasAuthority('teacher:read')")
    public List<Teacher> allTeacher(){
        return teacherService.getAllTeacher();
    }

    @DeleteMapping(path = "/deleteTeacher/{teacherId}")
    @PreAuthorize("hasAuthority('teacher:write')")
    public void deleteTeacher(@PathVariable("teacherId") long id){
        teacherService.deleteTeacherById(id);
    }

    @PostMapping(path = "/addTeacher")
    @PreAuthorize("hasAuthority('teacher:write')")
    public void addTeacher(@RequestBody Teacher teacher){
        teacherService.addNewTeacher(teacher);
    }

    @PutMapping(path = "/updateTeacher/{teacherId}")
    @PreAuthorize("hasAuthority('teacher:write')")
    public void updateTeacher(@PathVariable("teacherId") long id, @RequestBody Teacher teacher){
        teacherService.updateTeacher(id, teacher);
    }

    @GetMapping("/allAdmin")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Administrator> getAllAdmin(){
        return administratorService.getAllAdmin();
    }

    @DeleteMapping(path = "/deleteAdmin/{adminId}")
    @PreAuthorize("hasAuthority('admin:write')")
    public void deleteAdminById(@PathVariable("adminId") long id){
        administratorService.deleteAdminById(id);
    }

    @PostMapping(path = "/addAdmin")
    @PreAuthorize("hasAuthority('admin:write')")
    public void addAdmin(@RequestBody Administrator administrator){
        administratorService.addNewAdmin(administrator);
    }

    @PutMapping(path = "/updateAdmin/{adminId}")
    @PreAuthorize("hasAuthority('admin:write')")
    public void updateAdmin(@PathVariable("adminId") long id, @RequestBody Administrator administrator){
        administratorService.updateAdmin(id, administrator);
    }

    @GetMapping("/allGroup")
    @PreAuthorize("hasAuthority('group:read')")
    public List<Group> getAllGroup(){
        return groupService.getAllGroup();
    }

    @DeleteMapping(path = "/deleteGroup/{groupId}")
    @PreAuthorize("hasAuthority('admin:write')")
    public void deleteGroupById(@PathVariable("groupId") long id){
        groupService.deleteGroupById(id);
    }

    @PostMapping(path = "/addGroup")
    @PreAuthorize("hasAuthority('group:write')")
    public void addAdmin(@RequestBody Group group){
        groupService.addNewGroup(group);
    }

    @PutMapping(path = "/updateGroup/{groupId}")
    @PreAuthorize("hasAuthority('group:write')")
    public void updateAdmin(@PathVariable("groupId") long id, @RequestBody Group group){
        groupService.updateGroup(id, group);
    }

    @PutMapping(path = "/changeGroupForStudent/{studentId}/{groupId}")
    @PreAuthorize("hasAuthority('group:write')")
    public void changeGroupForStudent(@PathVariable("studentId") long studentId, @PathVariable("groupId") long groupId){
        studentService.changeGroupForStudent(studentId, groupId);
    }

    @PutMapping(path = "/changeGroupForStudents/{oldGroupId}/{newGroupId}")
    @PreAuthorize("hasAuthority('group:write')")
    public void changeGroupForStudents(@PathVariable("oldGroupId") long oldGroupId, @PathVariable("newGroupId") long newGroupId){
        studentService.changeGroupForStudents(oldGroupId, newGroupId);
    }

    @GetMapping("/allMark")
    @PreAuthorize("hasAuthority('mark:read')")
    public List<Mark> getAllMark(){
        return markService.getAllMark();
    }

    @GetMapping("/allMarksForStudent/{studentId}")
    @PreAuthorize("hasAuthority('mark:read')")
    public List<Mark> getAllMarksForStudentById(@PathVariable("studentId") long studentId){
        return markService.getAllMarksForStudentById(studentId);
    }

    @GetMapping("/allMarksForTeacher/{teacherId}")
    @PreAuthorize("hasAuthority('mark:read')")
    public List<Mark> getAllMarksForTeacherById(@PathVariable("teacherId") long teacherId){
        return markService.getAllMarksForTeacherById(teacherId);
    }


    @PostMapping(path = "/addMark")
    @PreAuthorize("hasAuthority('mark:write')")
    public void addAdmin(@RequestBody Mark mark){
        markService.addNewMark(mark);
    }

    @PutMapping(path = "/updateMark/{markId}")
    @PreAuthorize("hasAuthority('mark:write')")
    public void updateAdmin(@PathVariable("markId") long id, @RequestBody Mark mark){
        markService.updateGroup(id, mark);
    }

    @DeleteMapping(path = "/deleteMark/{markId}")
    @PreAuthorize("hasAuthority('mark:write')")
    public void deleteMarkById(@PathVariable("markId") long id){
        markService.deleteMarkById(id);
    }


}
