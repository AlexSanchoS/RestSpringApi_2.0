package com.example.restspringapi.model;

import com.example.restspringapi.security.ApplicationUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Student extends User {

    public Student(long id,
                   String username,
                   String password,
                   String name,
                   Status status,
                   LocalDate dob,
                   long groupId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.status = status;
        this.dob = dob;
        this.groupId = groupId;
    }
    public Student(
                   String username,
                   String password,
                   String name,
                   Status status,
                   LocalDate dob,
                   long groupId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.status = status;
        this.dob = dob;
        this.groupId = groupId;
    }

    private LocalDate dob;

    private long groupId;


    @Override
    public ApplicationUserRole getApplicationUserRole() {
        return ApplicationUserRole.STUDENT;
    }
}
