package com.example.restspringapi.config;

import com.example.restspringapi.dao.AdministratorDao;
import com.example.restspringapi.model.Administrator;
import com.example.restspringapi.model.Status;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DBConfig {

    @Bean
    CommandLineRunner commandLineRunner(AdministratorDao administratorDao,
                                        PasswordEncoder passwordEncoder){
        return args -> {

            System.out.println(passwordEncoder.encode("password"));

        };
    }
}
