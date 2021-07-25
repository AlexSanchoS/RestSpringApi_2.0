package com.example.restspringapi.model;

import com.example.restspringapi.security.ApplicationUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    protected long id;
    protected String name;
    protected String username;
    protected String password;
    protected Status status;


    public ApplicationUserRole getApplicationUserRole() {
        return null;
    }

}
