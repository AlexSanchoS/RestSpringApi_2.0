package com.example.restspringapi.security;

import com.example.restspringapi.dao.UserDao;
import com.example.restspringapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userDao.findUserByUsername(username);
        if (user.isPresent()) {
            return SecurityUser.userToSecurityUser(user.get());
        }
        throw new UsernameNotFoundException(String.format("User with username %s not found", username));

    }
}
