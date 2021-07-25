package com.example.restspringapi.security;

import com.example.restspringapi.model.Status;
import com.example.restspringapi.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private String username;
    private String password;
    private Set<SimpleGrantedAuthority> simpleGrantedAuthoritys;
    private boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return simpleGrantedAuthoritys;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static SecurityUser userToSecurityUser(User user){
        return new SecurityUser(user.getUsername(),
                user.getPassword(),
                user.getApplicationUserRole().getSimpleGrantedAuthorities(),
                user.getStatus().equals(Status.ACTIVE));
    }
}
