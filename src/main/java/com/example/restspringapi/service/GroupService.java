package com.example.restspringapi.service;

import com.example.restspringapi.dao.GroupDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.model.Group;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupDao groupDao;
    private final StudentDao studentDao;

    public GroupService(GroupDao groupDao,
                        StudentDao studentDao) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    public List<Group> getAllGroup() {
        return groupDao.getAll();
    }

    public void deleteGroupById(long id) {
        if (!groupDao.isGroupExistsById(id)){
            throw new IllegalStateException(
                    String.format("Groups with id %s isn't exist", id)
            );
        }
        if (studentDao.getStudentListByGroupId(id).size()>0){
            throw new IllegalStateException(
                    String.format("Group with id %s has student", id)
            );
        }
        groupDao.deleteById(id);
    }

    public void addNewGroup(Group group) {
        if (group==null){
            throw new IllegalStateException("Group can't be null");
        }
        if (group.getName()==null){
            throw new IllegalStateException("Name can't be null");
        }
        if (group.getName().length()<2){
            throw new IllegalStateException(
                    String.format("Name %s is to short", group.getName())
            );
        }
        if (!groupDao.getGroupByName(group.getName()).isEmpty()){
            throw new IllegalStateException(
                    String.format("Group with name %s is already exist", group.getName())
            );
        }
        groupDao.save(group);
    }

    public void updateGroup(long id, Group groupForUpdate) {
        if (groupForUpdate==null){
            throw new IllegalStateException("Group can't be null");
        }
        Optional<Group> groupById = groupDao.getGroupById(id);
        if (groupById.isEmpty()){
            throw new IllegalStateException(
                    String.format("Group with id %s doesn't exist", id)
            );
        }
        Group groupFromDb = groupById.get();

        if (groupForUpdate.getName()!=null &&
                groupForUpdate.getName().length()>1 &&
        !groupForUpdate.getName().equals(groupFromDb.getName())){
            groupFromDb.setName(groupForUpdate.getName());
        }
        groupDao.update(groupFromDb);
    }


}
