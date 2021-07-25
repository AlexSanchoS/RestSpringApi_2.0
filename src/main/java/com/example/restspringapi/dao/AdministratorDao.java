package com.example.restspringapi.dao;

import com.example.restspringapi.model.Administrator;

import java.util.List;
import java.util.Optional;


public interface AdministratorDao {

    Optional<Administrator> findAdministratorByUsername(String username);

    void save(Administrator administrator);

    List<Administrator> getAll();

    boolean isAdminExistById(long id);

    void deleteAdminById(long id);

    Optional<Administrator> findAdministratorById(long id);

    void updateAdminById(long id, Administrator admin);

    void deleteAll();
}
