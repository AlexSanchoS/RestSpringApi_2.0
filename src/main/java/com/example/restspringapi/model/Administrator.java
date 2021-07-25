package com.example.restspringapi.model;

import com.example.restspringapi.security.ApplicationUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Administrator extends User{

    public Administrator(long id, String name, String username, String password, Status status) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    @Override
    public ApplicationUserRole getApplicationUserRole() {
        return ApplicationUserRole.ADMIN;
    }
}
