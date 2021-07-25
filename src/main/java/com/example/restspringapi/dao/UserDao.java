package com.example.restspringapi.dao;

import com.example.restspringapi.model.User;

import java.util.Optional;


public interface UserDao {
    Optional<User> findUserByUsername(String username);
}
