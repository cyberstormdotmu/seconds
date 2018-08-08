--liquibase formatted sql

-- DDL

--  Changeset PARTH:1475655108837-103
-- Changed type of battery voltage columns from INT to DOUBLE
ALTER TABLE `consumer_meter_transaction` 
CHANGE COLUMN `current_battery_voltage` `current_battery_voltage` DOUBLE NULL DEFAULT NULL ,
CHANGE COLUMN `historical_battery_voltage` `historical_battery_voltage` DOUBLE NULL DEFAULT NULL ;
--rollback ALTER TABLE `consumer_meter_transaction` CHANGE COLUMN `current_battery_voltage` `current_battery_voltage` INT(11) NULL DEFAULT NULL , CHANGE COLUMN `historical_battery_voltage` `historical_battery_voltage`INT(11) NULL DEFAULT NULL ;

--  Changeset PARTH:1475655108837-104
-- Added new column endpoint_integrity to consumer_meter
ALTER TABLE `consumer_meter` 
ADD COLUMN `endpoint_integrity` VARCHAR(10) NULL AFTER `district_utility_meter_id`;
--rollback ALTER TABLE `consumer_meter` DROP COLUMN `endpoint_integrity`;

--  Changeset PARTH:1475655108837-105
-- Added new columns to consumer_meter table
ALTER TABLE `consumer_meter` 
ADD COLUMN `address1` VARCHAR(255) NULL AFTER `endpoint_integrity`,
ADD COLUMN `address2` VARCHAR(255) NULL AFTER `address1`,
ADD COLUMN `address3` VARCHAR(255) NULL AFTER `address2`,
ADD COLUMN `address4` VARCHAR(255) NULL AFTER `address3`,
ADD COLUMN `zipcode` VARCHAR(10) NULL AFTER `address4`,
ADD COLUMN `last_meter_reading` INT(11) NULL AFTER `zipcode`,
ADD COLUMN `last_meter_reading_ts` DATETIME NULL AFTER `last_meter_reading`;
--rollback ALTER TABLE `consumer_meter` DROP COLUMN `last_meter_reading_ts`,DROP COLUMN `last_meter_reading`,DROP COLUMN `zipcode`,DROP COLUMN `address3`,DROP COLUMN `address2`,DROP COLUMN `address1`;

--  Changeset URVASHI:1475655108837-106
ALTER TABLE `consumer_meter` 
ADD COLUMN `left_billing_digit` INT NULL DEFAULT -1 AFTER `last_meter_reading_ts`,
ADD COLUMN `right_billing_digit` INT NULL DEFAULT -1 AFTER `left_billing_digit`,
ADD COLUMN `decimal_position` INT NULL DEFAULT -1 AFTER `right_billing_digit`,
ADD COLUMN `leakage_threshold` INT NULL DEFAULT -1 AFTER `decimal_position`,
ADD COLUMN `backflow_limit` INT NULL DEFAULT -1 AFTER `leakage_threshold`,
ADD COLUMN `usage_threshold` INT NULL DEFAULT -1 AFTER `backflow_limit`,
ADD COLUMN `usage_interval` INT NULL DEFAULT -1 AFTER `usage_threshold`,
ADD COLUMN `kvalue` INT NULL DEFAULT -1 AFTER `usage_interval`,
ADD COLUMN `direction` INT NULL DEFAULT -1 AFTER `kvalue`,
ADD COLUMN `utility_code` INT NULL DEFAULT -1 AFTER `direction`,
ADD COLUMN `required_repeater_nodes` INT NULL DEFAULT -1 AFTER `utility_code`,
ADD COLUMN `require_repeater_levels` INT NULL DEFAULT -1 AFTER `required_repeater_nodes`,
ADD COLUMN `endpoint_mode` INT NULL DEFAULT -1 AFTER `require_repeater_levels`,
ADD COLUMN `firmware_version` VARCHAR(45) NULL AFTER `endpoint_mode`;
--rollback ALTER TABLE `consumer_meter` DROP COLUMN `left_billing_digit`,DROP COLUMN `right_billing_digit`,DROP COLUMN `decimal_position`,DROP COLUMN `leakage_threshold`,DROP COLUMN `backflow_limit`,DROP COLUMN `usage_threshold`,DROP COLUMN `usage_interval`,DROP COLUMN `kvalue`,DROP COLUMN `direction`,DROP COLUMN `utility_code`,DROP COLUMN `required_repeater_nodes`,DROP COLUMN `require_repeater_levels`,DROP COLUMN `endpoint_mode`,DROP COLUMN `firmware_version`;

--  Changeset SAURABH:1475655108837-107
ALTER TABLE `datacollector` 
ADD COLUMN `mb_per_month` DOUBLE NULL DEFAULT '0' AFTER `alerts_ack`;
--rollback ALTER TABLE `datacollector` DROP COLUMN `mb_per_month`;

--  Changeset DARSHIT:1475655108837-108
CREATE TABLE `normal_customer` (
  `normal_customer_id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT(11) NOT NULL,
  PRIMARY KEY (`normal_customer_id`));
--rollback DROP TABLE `normal_customer`;
 
--  Changeset DARSHIT:1475655108837-109 
ALTER TABLE `normal_customer` 
ADD INDEX `normal_customer_customer_FK_idx` (`customer_id` ASC);

ALTER TABLE `normal_customer` 
ADD CONSTRAINT `normal_customer_customer_FK`
  FOREIGN KEY (`customer_id`)
  REFERENCES `customer` (`customer_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
--rollback ALTER TABLE `normal_customer` DROP FOREIGN KEY `normal_customer_customer_FK`;ALTER TABLE `normal_customer` DROP INDEX `normal_customer_customer_FK_idx`;
  
--  Changeset DARSHIT:1475655108837-110
ALTER TABLE `region` 
ADD COLUMN `created_by` INT(11)  NULL DEFAULT '0' AFTER `time_zone`,
ADD COLUMN `created_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER `created_by`,
ADD COLUMN `updated_by` INT(11) NULL DEFAULT '0' AFTER `created_ts`,
ADD COLUMN `updated_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER `updated_by`,
ADD COLUMN `deleted_by` INT(11) NULL DEFAULT '0' AFTER `updated_ts`,
ADD COLUMN `deleted_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER `deleted_by`;
--rollback ALTER TABLE `region` DROP COLUMN `created_by`,DROP COLUMN `created_ts`,DROP COLUMN `updated_by`,DROP COLUMN `updated_ts`,DROP COLUMN `deleted_by`,DROP COLUMN `deleted_ts`;

--  Changeset DARSHIT:1475655108837-111
ALTER TABLE `tariff_plan` 
ADD COLUMN `created_by` INT(11)  NULL DEFAULT '0' AFTER `customer_id`,
ADD COLUMN `created_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER `created_by`,
ADD COLUMN `updated_by` INT(11) NULL DEFAULT '0' AFTER `created_ts`,
ADD COLUMN `updated_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER `updated_by`,
ADD COLUMN `deleted_by` INT(11) NULL DEFAULT '0' AFTER `updated_ts`,
ADD COLUMN `deleted_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER `deleted_by`;
--rollback ALTER TABLE `tariff_plan` DROP COLUMN `created_by`,DROP COLUMN `created_ts`,DROP COLUMN `updated_by`,DROP COLUMN `updated_ts`,DROP COLUMN `deleted_by`,DROP COLUMN `deleted_ts`;