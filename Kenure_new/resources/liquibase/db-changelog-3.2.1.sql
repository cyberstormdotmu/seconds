--liquibase formatted sql

-- DDL

--  Changeset DHAVAL:1475655108837-137
ALTER TABLE `battery_life` 
CHANGE COLUMN `number_of_child_nodes` `number_of_child_nodes` INT NOT NULL ,
CHANGE COLUMN `estimated_battery_life_in_years` `estimated_battery_life_in_years` DOUBLE NOT NULL ;
--rollback ALTER TABLE `battery_life` CHANGE COLUMN `number_of_child_nodes` `number_of_child_nodes` VARCHAR(45) NOT NULL ,CHANGE COLUMN `estimated_battery_life_in_years` `estimated_battery_life_in_years` VARCHAR(45) NOT NULL ;

--  Changeset SAURABH:1475655108837-138
ALTER TABLE `datacollector` 
CHANGE COLUMN `iscommissioned` `iscommissioned` BIT(1) NOT NULL DEFAULT b'0' ;
--rollback ALTER TABLE `datacollector` CHANGE COLUMN `iscommissioned` `iscommissioned` BIT(1) NOT NULL DEFAULT b'1';

--  Changeset SAURABH:1475655108837-139
CREATE TABLE `log_billing_schedule` (
  `log_billing_schedule_id` int(11) NOT NULL AUTO_INCREMENT,
  `last_billing_schedule_date` date DEFAULT NULL,
  `billing_schedule_start_time` datetime DEFAULT NULL,
  `billing_schedule_end_time` datetime DEFAULT NULL,
  `no_of_bill_generated` int(11) DEFAULT NULL,
  `log_date` date DEFAULT NULL,
  PRIMARY KEY (`log_billing_schedule_id`)
);
--rollback DROP TABLE `log_billing_schedule`;

--  Changeset SAURABH:1475655108837-140
ALTER TABLE `billing_history` 
CHANGE COLUMN `total_amount` `total_amount` DOUBLE NULL DEFAULT NULL ;
--rollback ALTER TABLE `billing_history` CHANGE COLUMN `total_amount` `total_amount` INT(11) NULL DEFAULT NULL;

--  Changeset SAURABH:1475655108837-141
ALTER TABLE `tariff_transaction` 
CHANGE COLUMN `rate` `rate` DOUBLE NULL DEFAULT NULL ;
--rollback ALTER TABLE `tariff_transaction` CHANGE COLUMN `rate` `rate` VARCHAR(45) NULL DEFAULT NULL;

--  Changeset SAURABH:1475655108837-142
ALTER TABLE `tariff_plan` 
CHANGE COLUMN `updated_ts` `updated_ts` TIMESTAMP NULL DEFAULT NULL ,
CHANGE COLUMN `deleted_ts` `deleted_ts` TIMESTAMP NULL DEFAULT NULL ;
--rollback ALTER TABLE `tariff_plan` CHANGE COLUMN `updated_ts` `updated_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,CHANGE COLUMN `deleted_ts` `deleted_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP;

--  Changeset SAURABH:1475655108837-143
ALTER TABLE `consumer_meter_transaction`
DROP FOREIGN KEY `consumer_meter_register_id_FK`;
--rollback ALTER TABLE `consumer_meter_transaction` ADD CONSTRAINT `consumer_meter_register_id_FK` FOREIGN KEY (`register_id`) REFERENCES `consumer_meter` (`register_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--  Changeset SAURABH:1475655108837-144
ALTER TABLE `consumer_meter` 
CHANGE COLUMN `register_id` `register_id` VARCHAR(10) NULL DEFAULT NULL;
--rollback ALTER TABLE `consumer_meter` CHANGE COLUMN `register_id` `register_id` INT(10) NULL DEFAULT NULL ;

--  Changeset SAURABH:1475655108837-145
ALTER TABLE `consumer_meter_transaction` 
DROP COLUMN `historical_reading_timestamp`,
DROP COLUMN `historical_status`,
DROP COLUMN `historical_battery_voltage`,
DROP COLUMN `historical_meter_reading`,
CHANGE COLUMN `register_id` `register_id` VARCHAR(10) NULL DEFAULT NULL ,
CHANGE COLUMN `current_meter_reading` `current_meter_reading` BIGINT(11) NULL DEFAULT NULL ,
CHANGE COLUMN `current_battery_voltage` `current_battery_voltage` INT NULL DEFAULT NULL;
--rollback ALTER TABLE `consumer_meter_transaction` ADD COLUMN `historical_reading_timestamp` DATETIME NULL DEFAULT NULL, ADD COLUMN `historical_status` INT(5) NULL DEFAULT NULL, ADD COLUMN `historical_battery_voltage` INT(11) NULL DEFAULT NULL, ADD COLUMN `historical_meter_reading` INT(11) NULL DEFAULT NULL, CHANGE COLUMN `register_id` `register_id` INT(10) NULL DEFAULT NULL , CHANGE COLUMN `current_meter_reading` `current_meter_reading` INT(11) NULL DEFAULT NULL , CHANGE COLUMN `current_battery_voltage` `current_battery_voltage` DOUBLE NULL DEFAULT NULL;

--  Changeset SAURABH:1475655108837-146
ALTER TABLE `consumer_meter` 
DROP COLUMN `endpoint_integrity`,
CHANGE COLUMN `bill_date` `bill_date` DATE NULL DEFAULT NULL ,
ADD COLUMN `estimated_remainaing_battery_life_in_year` DOUBLE NULL DEFAULT 0,
ADD COLUMN `total_no_of_latest_reads` INT(11) NULL DEFAULT '0',
ADD COLUMN `total_no_of_reads` INT(11) NULL DEFAULT '0';
--rollback ADD COLUMN `endpoint_integrity` VARCHAR(10) NULL DEFAULT NULL ,CHANGE COLUMN `bill_date` `bill_date` DATETIME NULL DEFAULT NULL ,DROP COLUMN `estimated_remainaing_battery_life_in_year` ,DROP COLUMN `total_no_of_latest_reads` ,DROP COLUMN `total_no_of_reads`;

--  Changeset SAURABH:1475655108837-147
ALTER TABLE `datacollector` 
ADD COLUMN `battery_voltage` INT NULL DEFAULT NULL;
--rollback ALTER TABLE `datacollector` DROP COLUMN `battery_voltage`;

--  Changeset SAURABH:1475655108837-148
ALTER TABLE `datacollector` 
CHANGE COLUMN `isconnection_ok` `isconnection_ok` BIT(1) NULL DEFAULT NULL ,
CHANGE COLUMN `isconfig_ok` `isconfig_ok` BIT(1) NULL DEFAULT NULL ;
--rollback ALTER TABLE `datacollector` CHANGE COLUMN `isconnection_ok` `isconnection_ok` BIT(1) NULL DEFAULT b'0' ,CHANGE COLUMN `isconfig_ok` `isconfig_ok` BIT(1) NULL DEFAULT b'0';

--  Changeset SAURABH:1475655108837-149
ALTER TABLE `datacollector` 
ADD COLUMN `islevel1comm_started` BIT(1) NULL DEFAULT NULL,
ADD COLUMN `islevelncomm_started` BIT(1) NULL DEFAULT NULL;
--rollback ALTER TABLE `datacollector` DROP COLUMN `islevel1comm_started`,DROP COLUMN `islevelncomm_started`;

--  Changeset SAURABH:1475655108837-150
ALTER TABLE `site` 
ADD COLUMN `mri_to_display` VARCHAR(45) NULL DEFAULT NULL;
--rollback ALTER TABLE `datacollector` DROP COLUMN `mri_to_display`;
