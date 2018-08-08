--liquibase formatted sql

-- DDL

--  Changeset DARSHIT:1475655108837-113
ALTER TABLE `user`
ADD COLUMN `normal_customer_id` INT(11) NULL DEFAULT NULL;
--rollback ALTER TABLE `user` DROP COLUMN `normal_customer_id`;

--  Changeset DARSHIT:1475655108837-114
ALTER TABLE `user` 
ADD INDEX `normal_customer_user_FK_idx` (`normal_customer_id` ASC);

ALTER TABLE `user` 
ADD CONSTRAINT `normal_customer_user_FK`
  FOREIGN KEY (`normal_customer_id`)
  REFERENCES `user` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
--rollback ALTER TABLE `user` DROP FOREIGN KEY `normal_customer_user_FK`;ALTER TABLE `user` DROP INDEX `normal_customer_user_FK_idx`;
  
--  Changeset DHAVAL:1475655108837-115
CREATE TABLE `battery_life` (
`battery_life_id` INT(11) NOT NULL AUTO_INCREMENT,
`number_of_child_nodes` VARCHAR(5) NULL,
`estimated_battery_life_in_years` VARCHAR(10) NULL,
PRIMARY KEY (`battery_life_id`));
--rollback DROP TABLE `battery_life`;

--  Changeset DHAVAL:1475655108837-116
ALTER TABLE `battery_life` 
CHANGE COLUMN `number_of_child_nodes` `number_of_child_nodes` VARCHAR(5) NOT NULL ,
CHANGE COLUMN `estimated_battery_life_in_years` `estimated_battery_life_in_years` VARCHAR(10) NOT NULL ;
--rollback ALTER TABLE `battery_life` CHANGE COLUMN `number_of_child_nodes` `number_of_child_nodes` VARCHAR(5) NULL DEFAULT NULL ,CHANGE COLUMN `estimated_battery_life_in_years` `estimated_battery_life_in_years` VARCHAR(10) NULL DEFAULT NULL ;

-- Changeset SAURABH:1475655108837-117
ALTER TABLE `district_utility_meter` CHANGE COLUMN `district_utility_meter_serial_number` `district_utility_meter_serial_number` VARCHAR(50) NOT NULL;
--rollback ALTER TABLE `district_utility_meter` CHANGE COLUMN `district_utility_meter_serial_number` `district_utility_meter_serial_number` VARCHAR(50) NULL DEFAULT NULL;

-- Changeset SAURABH:1475655108837-118
ALTER TABLE `contact_details`
CHANGE COLUMN `address1` `address1` VARCHAR(225) DEFAULT NULL,
CHANGE COLUMN `email1` `email1` VARCHAR(30) DEFAULT NULL;
--rollback ALTER TABLE `contact_details` CHANGE COLUMN `address1` `address1` VARCHAR(225) NOT NULL ,CHANGE COLUMN `email1` `email1` VARCHAR(30) NOT NULL;