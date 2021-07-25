-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema rest_spring_api
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema rest_spring_api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `rest_spring_api` DEFAULT CHARACTER SET utf8 ;
USE `rest_spring_api` ;

-- -----------------------------------------------------
-- Table `rest_spring_api`.`administrator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rest_spring_api`.`administrator` (
                                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                                 `name` VARCHAR(255) NOT NULL,
                                                                 `username` VARCHAR(45) NOT NULL,
                                                                 `password` VARCHAR(255) NOT NULL,
                                                                 `status` VARCHAR(45) NOT NULL,
                                                                 PRIMARY KEY (`id`),
                                                                 UNIQUE INDEX `username_UNIQUE` (`username` ASC))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rest_spring_api`.`groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rest_spring_api`.`groups` (
                                                          `id` INT NOT NULL AUTO_INCREMENT,
                                                          `name` VARCHAR(45) NOT NULL,
                                                          PRIMARY KEY (`id`),
                                                          UNIQUE INDEX `name_UNIQUE` (`name` ASC))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rest_spring_api`.`student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rest_spring_api`.`student` (
                                                           `id` INT NOT NULL AUTO_INCREMENT,
                                                           `name` VARCHAR(255) NOT NULL,
                                                           `username` VARCHAR(45) NOT NULL,
                                                           `password` VARCHAR(255) NOT NULL,
                                                           `status` VARCHAR(45) NOT NULL,
                                                           `dob` DATE NOT NULL,
                                                           `groups_id` INT NOT NULL,
                                                           PRIMARY KEY (`id`, `groups_id`),
                                                           UNIQUE INDEX `username_UNIQUE` (`username` ASC),
                                                           INDEX `fk_student_groups_idx` (`groups_id` ASC),
                                                           CONSTRAINT `fk_student_groups`
                                                               FOREIGN KEY (`groups_id`)
                                                                   REFERENCES `rest_spring_api`.`groups` (`id`)
                                                                   ON DELETE NO ACTION
                                                                   ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rest_spring_api`.`teacher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rest_spring_api`.`teacher` (
                                                           `id` INT NOT NULL AUTO_INCREMENT,
                                                           `name` VARCHAR(255) NOT NULL,
                                                           `username` VARCHAR(45) NOT NULL,
                                                           `password` VARCHAR(255) NOT NULL,
                                                           `status` VARCHAR(45) NOT NULL,
                                                           `dob` DATE NOT NULL,
                                                           PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rest_spring_api`.`mark`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rest_spring_api`.`mark` (
                                                        `id` INT NOT NULL AUTO_INCREMENT,
                                                        `rating` INT NOT NULL,
                                                        `student_id` INT NOT NULL,
                                                        `teacher_id` INT NOT NULL,
                                                        PRIMARY KEY (`id`, `student_id`, `teacher_id`),
                                                        INDEX `fk_mark_student1_idx` (`student_id` ASC),
                                                        INDEX `fk_mark_teacher1_idx` (`teacher_id` ASC),
                                                        CONSTRAINT `fk_mark_student1`
                                                            FOREIGN KEY (`student_id`)
                                                                REFERENCES `rest_spring_api`.`student` (`id`)
                                                                ON DELETE NO ACTION
                                                                ON UPDATE NO ACTION,
                                                        CONSTRAINT `fk_mark_teacher1`
                                                            FOREIGN KEY (`teacher_id`)
                                                                REFERENCES `rest_spring_api`.`teacher` (`id`)
                                                                ON DELETE NO ACTION
                                                                ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
