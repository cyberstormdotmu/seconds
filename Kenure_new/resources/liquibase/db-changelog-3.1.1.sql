--liquibase formatted sql

-- DDL

--  Changeset DHAVAL:1475655108837-126
ALTER TABLE `datacollector` 
ADD COLUMN `asset_inspection_date` DATETIME NULL DEFAULT NULL AFTER `isconfig_ok`;
--rollback ALTER TABLE `datacollector` DROP COLUMN `asset_inspection_date`;

--  Changeset DHAVAL:1475655108837-127
ALTER TABLE `site_installation_files` 
ADD COLUMN `no_of_datacollectors` INT(11) NULL DEFAULT NULL AFTER `no_of_endpoints`;
--rollback ALTER TABLE `site_installation_files` DROP COLUMN `no_of_datacollectors`;

--  Changeset SAURABH:1475655108837-128
ALTER TABLE `site`
CHANGE COLUMN `tag` `tag` INT(5) NULL DEFAULT '1';
--rollback ALTER TABLE `site` CHANGE COLUMN `tag` `tag` INT(5) NULL DEFAULT '0';

--  Changeset DHAVAL:1475655108837-129
ALTER TABLE `district_utility_meter` 
ADD COLUMN `start_billing_date` DATETIME NULL DEFAULT NULL AFTER `customer_id`,
ADD COLUMN `end_billing_date` DATETIME NULL DEFAULT NULL AFTER `start_billing_date`;
--rollback ALTER TABLE `district_utility_meter` DROP COLUMN `end_billing_date`,DROP COLUMN `start_billing_date`;

--  Changeset DHAVAL:1475655108837-130
ALTER TABLE `system_config` 
ADD COLUMN `simpro_username` VARCHAR(45) NULL DEFAULT NULL AFTER `no_of_bytes_per_packet`,
ADD COLUMN `simpro_password` VARCHAR(45) NULL DEFAULT NULL AFTER `simpro_username`,
ADD COLUMN `simpro_acc_no` VARCHAR(45) NULL DEFAULT NULL AFTER `simpro_password`,
ADD COLUMN `simpro_url` VARCHAR(255) NULL DEFAULT NULL AFTER `simpro_acc_no`,
ADD COLUMN `abnormal_threshold` DOUBLE NULL DEFAULT NULL AFTER `simpro_url`;
--rollback ALTER TABLE `system_config` DROP COLUMN `abnormal_threshold`,DROP COLUMN `simpro_url`,DROP COLUMN `simpro_acc_no`,DROP COLUMN `simpro_password`,DROP COLUMN `simpro_username`;

--  Changeset SAURABH:1475655108837-131
CREATE TABLE `datacollector_alerts` (
  `datacollector_alerts_id` INT(11) NOT NULL AUTO_INCREMENT,
  `datacollector_id` INT(11) NOT NULL,
  `alert` VARCHAR(500) NULL,
  `alert_ack` BIT(1) NOT NULL DEFAULT b'0',
  `alert_date` DATETIME NULL,
  PRIMARY KEY (`datacollector_alerts_id`),
  INDEX `datacollector_id_datacollector_alerts_FK_idx` (`datacollector_id` ASC),
  CONSTRAINT `datacollector_id_datacollector_alerts_FK`
  FOREIGN KEY (`datacollector_id`)
  REFERENCES `datacollector` (`datacollector_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION);
--rollback DROP TABLE `datacollector_alerts`;

--  Changeset SAURABH:1475655108837-132
CREATE TABLE `datacollector_message_queue` (
  `datacollector_message_queue_id` INT(11) NOT NULL AUTO_INCREMENT,
  `datacollector_id` INT(11) NOT NULL,
  `register_id` VARCHAR(11) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `time_added` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `message_color` VARCHAR(45) NOT NULL,
  `created_by` INT(11) NOT NULL DEFAULT '0',
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` INT(11) NULL DEFAULT '0',
  `updated_ts` TIMESTAMP NULL,
  `deleted_by` INT(11) NULL DEFAULT '0',
  `deleted_ts` TIMESTAMP NULL,
  PRIMARY KEY (`datacollector_message_queue_id`),
  INDEX `datacollector_id_message_queue_FK_idx` (`datacollector_id` ASC),
  CONSTRAINT `datacollector_id_message_queue_FK`
    FOREIGN KEY (`datacollector_id`)
    REFERENCES `datacollector` (`datacollector_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--rollback DROP TABLE `datacollector_message_queue`;
    
--  Changeset DHAVAL:1475655108837-133
ALTER TABLE `district_utility_meter` 
DROP COLUMN `end_billing_date`,
DROP COLUMN `start_billing_date`,
DROP COLUMN `reading_date`,
DROP COLUMN `current_reading`;
--rollback ALTER TABLE `district_utility_meter` ADD COLUMN `reading_date` DATETIME NULL DEFAULT NULL AFTER `customer_id`,ADD COLUMN `current_reading` INT(11) NULL DEFAULT NULL AFTER `reading_date`,ADD COLUMN `start_billing_date` DATETIME NULL DEFAULT NULL AFTER `current_reading`,ADD COLUMN `end_billing_date` DATETIME NULL DEFAULT NULL AFTER `start_billing_date`;

--  Changeset DHAVAL:1475655108837-134
CREATE TABLE `district_meter_transaction` (
  `district_meter_transaction_id` INT(11) NOT NULL AUTO_INCREMENT,
  `district_meter_id` INT(11) NULL DEFAULT NULL,
  `current_reading` INT(11) NULL DEFAULT '0',
  `start_billing_date` DATETIME NULL DEFAULT NULL,
  `end_billing_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`district_meter_transaction_id`),
  INDEX `district_meter_id_FK_idx` (`district_meter_id` ASC),
  CONSTRAINT `district_meter_id_FK`
    FOREIGN KEY (`district_meter_id`)
    REFERENCES `district_utility_meter` (`district_utility_meter_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--rollback DROP TABLE `district_meter_transaction`;

--  Changeset DHAVAL:1475655108837-135
ALTER TABLE `billing_history` 
ADD COLUMN `is_estimated` BIT NULL DEFAULT 0 AFTER `bill_date`;
--rollback ALTER TABLE `billing_history` DROP COLUMN `is_estimated`;
