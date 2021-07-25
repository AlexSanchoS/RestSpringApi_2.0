package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.AdministratorDao;
import com.example.restspringapi.model.Administrator;
import com.example.restspringapi.model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdministratorDaoImplTest {

    @Autowired
    private AdministratorDao administratorDao;

    private Administrator administrator = new Administrator(1L,
            "alex",
            "admin",
            "password",
            Status.ACTIVE);


    @AfterEach
    void afterTestClearTableAdmin(){
        administratorDao.deleteAll();
    }


    @Test
    void shouldFindAdministratorByUsername() {
        administratorDao.save(administrator);
        Optional<Administrator> administratorById = administratorDao.findAdministratorByUsername(administrator.getUsername());
        assertEquals(administratorById.get(), administrator);
    }

    @Test
    void shouldReturnEmptyOptionalIfAdministratorByUsernameDoesNotExist() {
        Optional<Administrator> administratorById = administratorDao.findAdministratorByUsername(administrator.getUsername());
        assertTrue(administratorById.isEmpty());
    }

    @Test
    void shouldReturnEmptyOptionalIfAdministratorByIdDoesNotExist() {
        Optional<Administrator> administratorById = administratorDao.findAdministratorById(administrator.getId());
        assertTrue(administratorById.isEmpty());
    }


    @Test
    void shouldGetTwoAdmins() {
        Administrator administrator2 = new Administrator(2L,
                "admin2",
                "admin2",
                "password",
                Status.ACTIVE);
        administratorDao.save(administrator);
        administratorDao.save(administrator2);
        List<Administrator> all = administratorDao.getAll();
        assertTrue(all.size()==2);
    }

    @Test
    void shouldReturnTrueIfAdminExistById() {
        administratorDao.save(administrator);
        boolean adminExistById = administratorDao.isAdminExistById(administrator.getId());
        assertTrue(adminExistById);
    }

    @Test
    void shouldReturnFalseIfAdminDoesNotExistById() {
        boolean adminExistById = administratorDao.isAdminExistById(1);
        assertFalse(adminExistById);
    }

    @Test
    void shouldDeleteAdminById() {
        administratorDao.save(administrator);
        administratorDao.deleteAdminById(administrator.getId());
        assertFalse(administratorDao.isAdminExistById(administrator.getId()));
    }

    @Test
    void ShouldFindAdministratorById() {
        administratorDao.save(administrator);
        Optional<Administrator> administratorById = administratorDao.findAdministratorById(administrator.getId());
        assertEquals(administratorById.get(), administrator);
    }

    @Test
    void shouldUpdateAdminById() {
        administratorDao.save(administrator);
        administrator.setName("test");
        administratorDao.updateAdminById(administrator.getId(), administrator);
        Optional<Administrator> administratorByUsername = administratorDao.findAdministratorByUsername(administrator.getUsername());
        String actualName = administratorByUsername.get().getName();
        String expectedName = "test";
        assertEquals(actualName, expectedName);
    }
}