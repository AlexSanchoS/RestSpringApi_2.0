package com.example.restspringapi.model;

import com.example.restspringapi.security.ApplicationUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Teacher extends User{

    public Teacher(
            long id,
            String name,
            String username,
            String password,
            Status status,
            LocalDate dob){
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.status = status;
        this.dob = dob;
    }

    public Teacher(
            String name,
            String username,
            String password,
            Status status,
            LocalDate dob){
        this.name = name;
        this.username = username;
        this.password = password;
        this.status = status;
        this.dob = dob;
    }

    private LocalDate dob;

    public ApplicationUserRole getApplicationUserRole() {
        return ApplicationUserRole.TEACHER;
    }
}
