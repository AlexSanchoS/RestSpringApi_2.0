package com.example.restspringapi.service;

import com.example.restspringapi.dao.AdministratorDao;
import com.example.restspringapi.dao.GroupDao;
import com.example.restspringapi.dao.StudentDao;
import com.example.restspringapi.dao.TeacherDao;
import com.example.restspringapi.model.Administrator;
import com.example.restspringapi.model.Teacher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {
    private final AdministratorDao administratorDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final GroupDao groupDao;
    private final PasswordEncoder passwordEncoder;

    public AdministratorService(AdministratorDao administratorDao,
                                StudentDao studentDao,
                                TeacherDao teacherDao,
                                GroupDao groupDao,
                                PasswordEncoder passwordEncoder) {
        this.administratorDao = administratorDao;
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.groupDao = groupDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Administrator> getAllAdmin() {
        return administratorDao.getAll();
    }


    public void deleteAdminById(long id) {
        if(!administratorDao.isAdminExistById(id)){
            throw new IllegalStateException(
                    String.format("Admin with id %s doesn't exist", id)
            );
        }
        administratorDao.deleteAdminById(id);
    }

    public void addNewAdmin(Administrator administrator) {
        try {
            administratorValidation(administrator);
        }catch (IllegalStateException e) {
            throw new IllegalStateException(
                    String.format("Admin %s isn't valid", administrator)
            );
        }
        administrator.setPassword(passwordEncoder.encode(administrator.getPassword()));
        administratorDao.save(administrator);

    }

    private void administratorValidation(Administrator administrator) {
        //null
        if (administrator == null) {
            throw new IllegalStateException("Admin can't be null");
        }

        //name
        if (administrator.getName() == null) {
            throw new IllegalStateException("Name can't be null");
        }
        if (administrator.getName().length() < 2) {
            throw new IllegalStateException(
                    String.format("Name is to short", administrator.getName()));
        }

        //password
        if (administrator.getPassword() == null) {
            throw new IllegalStateException("Password can't be null");
        }
        if (administrator.getPassword().length() < 4) {
            throw new IllegalStateException(
                    String.format("Password %s is to short", administrator.getPassword()));
        }

        //status
        if (administrator.getStatus() == null) {
            throw new IllegalStateException("Status can't be null");
        }

        //username
        if (administrator.getUsername() == null) {
            throw new IllegalStateException("Username can't be null");
        }
        if (administrator.getUsername().length() < 4) {
            throw new IllegalStateException(
                    String.format("Username %s is to short", administrator.getUsername())
            );
        }
        if (teacherDao.findTeacherByUsername(administrator.getUsername()).isPresent() ||
                studentDao.findStudentByUsername(administrator.getUsername()).isPresent() ||
                administratorDao.findAdministratorByUsername(administrator.getUsername()).isPresent()) {
            throw new IllegalStateException(
                    String.format("User with username %s is already exist", administrator.getUsername())
            );
        }
    }

    public void updateAdmin(long id, Administrator adminForUpdate) {
        if (adminForUpdate == null) {
            throw new IllegalStateException("Admin can't be null");
        }
        Optional<Administrator> optionalAdminFromDB = administratorDao.findAdministratorById(id);
        if (optionalAdminFromDB.isEmpty()) {
            throw new IllegalStateException(String.format("Admin with id %s doesn't exist", id));
        }
        Administrator adminFromDB = optionalAdminFromDB.get();

        //name
        if (adminForUpdate.getName() != null
                && !adminForUpdate.getName().equals(adminFromDB.getName())
                && adminForUpdate.getName().length() > 2) {
            adminFromDB.setName(adminForUpdate.getName());
        }

        //username
        if (adminForUpdate.getUsername() != null
                && !adminForUpdate.getUsername().equals(adminFromDB.getUsername())
                && adminForUpdate.getUsername().length() > 2) {
            if (teacherDao.findTeacherByUsername(adminForUpdate.getUsername()).isEmpty()&&
                    studentDao.findStudentByUsername(adminForUpdate.getUsername()).isEmpty() &&
                    administratorDao.findAdministratorByUsername(adminForUpdate.getUsername()).isEmpty()) {
                adminFromDB.setUsername(adminForUpdate.getUsername());
            }
        }

        //password
        if (adminForUpdate.getPassword() != null
                && adminForUpdate.getPassword().length() > 4
                && !passwordEncoder.encode(adminForUpdate.getPassword()).equals(adminFromDB.getPassword())) {
            adminFromDB.setPassword(passwordEncoder.encode(adminForUpdate.getPassword()));
        }


        if (adminForUpdate.getStatus()!=null&&
                !adminForUpdate.getStatus().equals(adminFromDB.getStatus())){
            adminFromDB.setStatus(adminForUpdate.getStatus());
        }

        administratorDao.updateAdminById(id, adminFromDB);

    }
}
