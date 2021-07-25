DELETE FROM administrator WHERE id > 0;
DELETE FROM teacher WHERE id > 0;
DELETE FROM groups WHERE id > 0;
DELETE FROM student WHERE id > 0;
DELETE FROM mark WHERE id > 0;

INSERT INTO administrator (id, name, username, password, status) values (1, 'alex', 'admin', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'ACTIVE');
INSERT INTO teacher (id, name, username, password, status, dob) values (1, 'teache1', 'teacher1', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'ACTIVE', '1970-01-02');
INSERT INTO teacher (id, name, username, password, status, dob) values (2, 'teache2', 'teache2', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'ACTIVE', '1972-03-02');
INSERT INTO groups (id, name) values (1, 'CS14');
INSERT INTO groups (id, name) values (2, 'CS15');
INSERT INTO student (id, name, username, password, status, dob, groups_id) values (1, 'st1', 'student1', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'ACTIVE', '1997-01-08', 1);
INSERT INTO student (id, name, username, password, status, dob, groups_id) values (2, 'st2', 'student2', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'ACTIVE', '1998-01-08', 1);
INSERT INTO student (id, name, username, password, status, dob, groups_id) values (3, 'st3', 'student3', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'BANED', '1999-01-08', 1);
INSERT INTO student (id, name, username, password, status, dob, groups_id) values (4, 'st4', 'student4', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'ACTIVE', '1997-02-08', 2);
INSERT INTO student (id, name, username, password, status, dob, groups_id) values (5, 'st5', 'student5', '$2a$10$/71vjc4vvDG7HRKME/tc5OF0fczBjUSlbho71sAh/m1vwdD4KjkLa', 'ACTIVE', '1997-03-08', 2);