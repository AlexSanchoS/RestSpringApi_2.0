package com.example.restspringapi.dao.impl;

import com.example.restspringapi.dao.GroupDao;
import com.example.restspringapi.model.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupDaoImplTest {

    @Autowired
    private GroupDao groupDao;

    private Group groupForTest = new Group(1L,
            "1CS");

    @AfterEach
    void afterTestClearTableGroup(){
        groupDao.deleteAll();
    }

    @Test
    void shouldReturnTrueIfGroupExistsById() {
        groupDao.save(groupForTest);
        assertTrue(groupDao.isGroupExistsById(groupForTest.getId()));
    }

    @Test
    void shouldReturnFalseIfGroupDoesNotExistsById() {
        assertFalse(groupDao.isGroupExistsById(groupForTest.getId()));
    }

    @Test
    void shouldReturnTwoGroups() {
        Group groupForTest2 = new Group(2L, "CS2");
        groupDao.save(groupForTest);
        groupDao.save(groupForTest2);
        assertTrue(groupDao.getAll().size()==2);
    }

    @Test
    void shouldDeleteById() {
        groupDao.save(groupForTest);
        groupDao.deleteById(groupForTest.getId());
        assertFalse(groupDao.isGroupExistsById(groupForTest.getId()));
    }

    @Test
    void shouldReturnGroupByNameIfExist() {
        groupDao.save(groupForTest);
        Optional<Group> groupByName = groupDao.getGroupByName(groupForTest.getName());
        assertEquals(groupByName.get(), groupForTest);
    }

    @Test
    void shouldReturnEmptyIfGroupDoesNotExistByName() {
        Optional<Group> groupByName = groupDao.getGroupByName("name");
        assertTrue(groupByName.isEmpty());
    }

    @Test
    void shouldReturnGroupByIdIfExist() {
        groupDao.save(groupForTest);
        Optional<Group> groupByName = groupDao.getGroupById(groupForTest.getId());
        assertEquals(groupByName.get(), groupForTest);
    }

    @Test
    void shouldReturnEmptyIfGroupDoesNotExistById() {
        Optional<Group> groupByName = groupDao.getGroupById(2);
        assertTrue(groupByName.isEmpty());
    }
}