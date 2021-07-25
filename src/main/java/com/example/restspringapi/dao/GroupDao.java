package com.example.restspringapi.dao;

import com.example.restspringapi.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao {

    boolean isGroupExistsById(long id);

    List<Group> getAll();

    void deleteById(long id);

    Optional<Group> getGroupByName(String name);

    void save(Group group);

    Optional<Group> getGroupById(long id);

    void deleteAll();

    void update(Group group);
}
