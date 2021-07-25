package com.example.restspringapi.service;

import com.example.restspringapi.dao.GroupDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.model.Group;
import com.example.restspringapi.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;

    @Mock
    private StudentDao studentDao;

    private GroupService underTest;

    @BeforeEach
    void setUp() {
        underTest = new GroupService(groupDao, studentDao);
    }


    @Test
    void canGetAllGroup() {
        underTest.getAllGroup();
        verify(groupDao).getAll();
    }


    @Test
    void shouldThrowExceptionWhenDeletingGroupWithIdThatNotExist() {
        given(groupDao.isGroupExistsById(1)).willReturn(false);

        assertThatThrownBy(() -> underTest.deleteGroupById(1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Groups with id 1 isn't exist");
    }

    @Test
    void shouldThrowExceptionWhenDeletingGroupWithStudents() {
        given(groupDao.isGroupExistsById(1)).willReturn(true);
        given(studentDao.getStudentListByGroupId(1)).willReturn(List.of(new Student()));

        assertThatThrownBy(() -> underTest.deleteGroupById(1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group with id 1 has student");
    }

    @Test
    void canDeleteGroupById() {
        given(groupDao.isGroupExistsById(1)).willReturn(true);
        given(studentDao.getStudentListByGroupId(1)).willReturn(List.of());
        underTest.deleteGroupById(1);
        verify(groupDao).deleteById(1);
    }

    @Test
    void shouldThrowExceptionIfGroupNullWhenAddNewGroup() {
        assertThatThrownBy(() -> underTest.addNewGroup(null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group can't be null");
    }

    @Test
    void shouldThrowExceptionIfGroupNameNullWhenAddNewGroup() {
        Group group = new Group(1L, null);
        assertThatThrownBy(() -> underTest.addNewGroup(group))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Name can't be null");
    }

    @Test
    void shouldThrowExceptionIfGroupNameShortWhenAddNewGroup() {
        Group group = new Group(1L, "1");
        assertThatThrownBy(() -> underTest.addNewGroup(group))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Name %s is to short", group.getName()));
    }

    @Test
    void shouldThrowExceptionIfGroupNameExistWhenAddNewGroup() {
        Group group = new Group(1L, "cs1");
        given(groupDao.getGroupByName(group.getName())).willReturn(Optional.of(group));
        assertThatThrownBy(() -> underTest.addNewGroup(group))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Group with name %s is already exist", group.getName()));
    }

    @Test
    void canAddNewGroup() {
        Group group = new Group(1L, "cs1");
        given(groupDao.getGroupByName(group.getName())).willReturn(Optional.empty());
        underTest.addNewGroup(group);
        verify(groupDao).save(group);
    }

    @Test
    void shouldThrowExceptionIfGroupNullWhenUpdateGroup() {
        assertThatThrownBy(() -> underTest.updateGroup(1, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group can't be null");
    }

    @Test
    void shouldThrowExceptionIfGroupDoesNotExistWhenUpdateGroup(){
        Group group = new Group(1L, "cs1");
        given(groupDao.getGroupById(group.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.updateGroup(group.getId(), group))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Group with id %s doesn't exist", group.getId()));

    }

    @Test
    void canUpdateGroup(){
        Group group = new Group(1L, "cs1");
        given(groupDao.getGroupById(group.getId())).willReturn(Optional.of(group));
        Group group1 = new Group(1L, "cs2");
        underTest.updateGroup(group1.getId(), group1);
        verify(groupDao).update(group1);

    }
}