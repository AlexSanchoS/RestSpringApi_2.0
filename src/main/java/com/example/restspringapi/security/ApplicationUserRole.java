package com.example.restspringapi.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.restspringapi.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {

    STUDENT(Set.of(STUDENT_READ, MARK_READ, GROUP_READ)),
    TEACHER(Set.of(STUDENT_READ, MARK_READ, MARK_WRITE, GROUP_READ)),
    ADMIN(Set.of(STUDENT_READ, STUDENT_WRITE, MARK_READ, MARK_WRITE, GROUP_READ, GROUP_WRITE, TEACHER_READ, TEACHER_WRITE, ADMIN_READ, ADMIN_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(){
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }

}
