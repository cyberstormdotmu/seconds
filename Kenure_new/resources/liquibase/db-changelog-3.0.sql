--liquibase formatted sql

-- DDL

--  Changeset DARSHIT:1475655108837-119
ALTER TABLE `datacollector` 
ADD COLUMN `mode` VARCHAR(45) NULL DEFAULT NULL AFTER `mb_per_month`,
ADD COLUMN `isconnection_ok` BIT(1) NOT NULL DEFAULT b'0' AFTER `mode`,
ADD COLUMN `isconfig_ok` BIT(1) NOT NULL DEFAULT b'0' AFTER `isconnection_ok`;
--rollback ALTER TABLE `datacollector` DROP COLUMN `mode`,DROP `isconnection_ok`,DROP `isconfig_ok`;

--  Changeset DARSHIT:1475655108837-120
ALTER TABLE `site` 
ADD COLUMN `current_status` VARCHAR(45) NULL DEFAULT NULL AFTER `region_id`,
ADD COLUMN `commissioning_start_time` DATETIME NULL DEFAULT NULL AFTER `current_status`,
ADD COLUMN `commissioning_end_time` DATETIME NULL DEFAULT NULL AFTER `commissioning_start_time`,
ADD COLUMN `commissioning_type` VARCHAR(15) NULL DEFAULT NULL AFTER `commissioning_end_time`,
ADD COLUMN `schedule_time` DATETIME NULL AFTER `commissioning_type`,
ADD COLUMN `tag` INT(5) NULL DEFAULT '0' AFTER `schedule_time`,
ADD COLUMN `route_file_name` VARCHAR(100) NULL AFTER `tag`,
ADD COLUMN `route_file_last_update` DATETIME NULL AFTER `route_file_name`,
ADD COLUMN `route_file_last_update_by_name` VARCHAR(45) NULL AFTER `route_file_last_update`,
ADD COLUMN `no_of_endpoint_files` INT(5) NOT NULL DEFAULT '0' AFTER `route_file_last_update_by_name`,
ADD COLUMN `no_of_dc_files` INT(5) NOT NULL DEFAULT '0' AFTER `no_of_endpoint_files`,
ADD COLUMN `level_1_comm_start_time` DATETIME NULL AFTER `no_of_dc_files`,
ADD COLUMN `level_n_comm_start_time` DATETIME NULL AFTER `level_1_comm_start_time`;
--rollback ALTER TABLE `site` DROP `current_status`,DROP `commissioning_start_time`,DROP `commissioning_end_time`,DROP `commissioning_type`,DROP `schedule_time`,DROP `tag`,DROP `route_file_name`,DROP `route_file_last_update`,DROP `route_file_last_update_by_name`,DROP `no_of_endpoint_files`,DROP `no_of_dc_files`,DROP `level_1_comm_start_time`,DROP `level_n_comm_start_time`;

--  Changeset DARSHIT:1475655108837-121
CREATE TABLE `site_installation_files` (
  `site_installation_files_id` INT(11) NOT NULL AUTO_INCREMENT,
  `site_id` INT(11) NULL,
  `file_name` VARCHAR(45) NULL,
  `no_of_endpoints` INT(11) NULL,
  `file_uploaded` BIT(1) NULL,
  `file_verified` BIT(1) NULL,
  `installer_id` INT NULL,
  PRIMARY KEY (`site_installation_files_id`));
--rollback DROP TABLE `site_installation_files`;

--  Changeset DARSHIT:1475655108837-122
ALTER TABLE `consumer_meter` 
ADD COLUMN `my_slot` INT(4) NULL AFTER `require_repeater_levels`,
ADD COLUMN `parent_slot` INT(4) NULL AFTER `my_slot`,
ADD COLUMN `iscommissioned` BIT(1) NOT NULL DEFAULT b'0' AFTER `parent_slot`;
--rollback ALTER TABLE `consumer_meter` DROP `my_slot`,DROP `parent_slot`,DROP `iscommissioned`;

--  Changeset DARSHIT:1475655108837-123
ALTER TABLE `site_installation_files` 
ADD INDEX `site_installation_files_site_FK_idx` (`site_id` ASC);

ALTER TABLE `site_installation_files` 
ADD CONSTRAINT `site_installation_files_site_FK`
  FOREIGN KEY (`site_id`)
  REFERENCES `site` (`site_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
--rollback ALTER TABLE `site_installation_files` DROP FOREIGN KEY `site_installation_files_site_FK`;ALTER TABLE `site_installation_files` DROP INDEX `site_installation_files_site_FK_idx`;

--  Changeset DARSHIT:1475655108837-124
ALTER TABLE `site_installation_files` 
ADD INDEX `installer_installer_id_FK_idx` (`installer_id` ASC);
  
ALTER TABLE `site_installation_files` 
ADD CONSTRAINT `installer_installer_id_FK`
FOREIGN KEY (`installer_id`)
REFERENCES `installer` (`installer_id`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
--rollback ALTER TABLE `site_installation_files` DROP FOREIGN KEY `installer_installer_id_FK`;ALTER TABLE `site_installation_files` DROP INDEX `installer_installer_id_FK_idx`;
