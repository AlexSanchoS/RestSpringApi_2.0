package com.example.restspringapi.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {


    private HttpStatus status;

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }
}
