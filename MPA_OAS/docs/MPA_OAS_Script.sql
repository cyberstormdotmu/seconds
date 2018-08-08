-- appointment_master
CREATE TABLE `mpa_oas`.`appointment_master` (
  `REF_NO` VARCHAR(19) NOT NULL COMMENT 'Reference number to identify the appointment',
  `ID_NO` VARCHAR(32) NOT NULL COMMENT 'National Registration Identity Card or Foreigner identification number',
  `ID_TYPE` VARCHAR(15) NOT NULL COMMENT 'Identity Card type',
  `APT_TYPE` VARCHAR(15) NULL DEFAULT 'Single' COMMENT 'Types of appointment i.e. Group or Single',
  `APT_DATE` DATE NOT NULL COMMENT 'The date for the scheduled appointment',
  `APT_TIME` TIME NOT NULL COMMENT 'The time for the scheduled appointment',
  `CONTACT_NO` VARCHAR(18) NULL COMMENT 'Contact number of customers ',
  `EMAIL` VARCHAR(100) NULL COMMENT 'Email address of customers',
  `COMPANY` VARCHAR(100) NULL COMMENT 'Name of the company of the customers',
  `REMARK` TINYTEXT NULL COMMENT 'Free text for customers to input',
  `APP_STATUS` VARCHAR(18) NOT NULL COMMENT 'Status of the scheduled appointment',
  `CHECK_IN_DATE` DATE NOT NULL COMMENT 'The check-in date for the scheduled appointment',
  `CHECK_IN_TIME` TIME NOT NULL COMMENT 'The check-in time for the scheduled appointment',
  `PROCESS_DATE` DATE NULL COMMENT 'The process date for the scheduled appointment',
  `PROCESS_TIME` TIME NULL COMMENT 'The process time for the scheduled appointment',
  `COMPLETE_DATE` DATE NULL COMMENT 'The complete date for the scheduled appointment',
  `COMPLETE_TIME` TIME NULL COMMENT 'The complete time for the scheduled appointment',
  `QUEUE_NO` VARCHAR(17) NOT NULL COMMENT 'System auto generated sequential number with format YYYYMMDD00000001',
  `UPDATE_USERID` VARCHAR(20) NULL DEFAULT 'CURRENT_USER()' COMMENT 'Update User Id',
  `LAST_ACTION` VARCHAR(12) NULL DEFAULT 'INSERTED' COMMENT 'Last Action Taken',
  `TIME_STAMP` DATETIME NULL COMMENT 'Update Time stamp',
  PRIMARY KEY (`REF_NO`),
  UNIQUE INDEX `REF_NO_UNIQUE` (`REF_NO` ASC),
  UNIQUE INDEX `QUEUE_NO_UNIQUE` (`QUEUE_NO` ASC))
COMMENT = 'Master table for all Appointments';

-- appointment_master_hist
CREATE TABLE `mpa_oas`.`appointment_master_hist` (
  `HIST_ID` VARCHAR(20) NOT NULL COMMENT 'The History Id',
  `REF_NO` VARCHAR(19) NOT NULL COMMENT 'Reference number to identify the appointment',
  `ID_NO` VARCHAR(32) NOT NULL COMMENT 'National Registration Identity Card or Foreigner identification number',
  `ID_TYPE` VARCHAR(15) NOT NULL COMMENT 'Identity Card type',
  `APT_TYPE` VARCHAR(15) NULL DEFAULT 'Single' COMMENT 'Types of appointment i.e. Group or Single',
  `APT_DATE` DATE NOT NULL COMMENT 'The date for the scheduled appointment',
  `APT_TIME` TIME NOT NULL COMMENT 'The time for the scheduled appointment',
  `CONTACT_NO` VARCHAR(18) NULL COMMENT 'Contact number of customers ',
  `EMAIL` VARCHAR(100) NULL COMMENT 'Email address of customers',
  `COMPANY` VARCHAR(100) NULL COMMENT 'Name of the company of the customers',
  `REMARK` TINYTEXT NULL COMMENT 'Free text for customers to input',
  `APP_STATUS` VARCHAR(18) NOT NULL COMMENT 'Status of the scheduled appointment',
  `CHECK_IN_DATE` DATE NOT NULL COMMENT 'The check-in date for the scheduled appointment',
  `CHECK_IN_TIME` TIME NOT NULL COMMENT 'The check-in time for the scheduled appointment',
  `PROCESS_DATE` DATE NULL COMMENT 'The process date for the scheduled appointment',
  `PROCESS_TIME` TIME NULL COMMENT 'The process time for the scheduled appointment',
  `COMPLETE_DATE` DATE NULL COMMENT 'The complete date for the scheduled appointment',
  `COMPLETE_TIME` TIME NULL COMMENT 'The complete time for the scheduled appointment',
  `QUEUE_NO` VARCHAR(17) NOT NULL COMMENT 'System auto generated sequential number with format YYYYMMDD00000001',
  `UPDATE_USERID` VARCHAR(20) NULL DEFAULT 'CURRENT_USER()' COMMENT 'Update User Id',
  `LAST_ACTION` VARCHAR(12) NULL DEFAULT 'INSERTED' COMMENT 'Last Action Taken',
  `TIME_STAMP` DATETIME NULL COMMENT 'Update Time stamp',
  PRIMARY KEY (`HIST_ID`))  
COMMENT = 'History table for appoinment_master';

-- user_master
CREATE TABLE `mpa_oas`.`user_master` (
  `USER_ID` VARCHAR(20) NOT NULL COMMENT 'User Unique Id',
  `PASSWORD` VARCHAR(32) NOT NULL COMMENT 'User Password',
  `FIRST_NAME` VARCHAR(14) NOT NULL COMMENT 'First Name',
  `MIDDLE_NAME` VARCHAR(25) NULL COMMENT 'Middle Name',
  `LAST_NAME` VARCHAR(25) NULL COMMENT 'Last Name',
  `DATE_OF_JOINING` DATE NULL COMMENT 'The date of joining MPA',
  `ROLE` VARCHAR(20) NOT NULL DEFAULT 'AS_MP_COUNTER' COMMENT 'User Role [Admin/Counter]',
  `COUNTER` VARCHAR(25) NULL COMMENT 'Counter number',
  `UPDATE_USERID` VARCHAR(20) NULL DEFAULT 'CURRENT_USER()' COMMENT 'Update User Id',
  `LAST_ACTION` VARCHAR(12) NULL DEFAULT 'INSERTED' COMMENT 'Last Action Taken',
  `TIME_STAMP` DATETIME NULL COMMENT 'Update Time stamp',
  PRIMARY KEY (`USER_ID`))  
COMMENT = 'User Master Table';

-- transaction_master
CREATE TABLE `mpa_oas`.`transaction_master` (
  `REF_NO` VARCHAR(19) NOT NULL COMMENT 'Reference number to identify the appointment',
  `TRANSACTION_TYPE` VARCHAR(50) NOT NULL COMMENT 'The Transaction type',
  `TRANSACTION_SUB_TYPE` VARCHAR(50) NOT NULL COMMENT 'The Transaction sub-type',
  `OTHER_SUB_TYPE` VARCHAR(50) NULL COMMENT 'The Other Sub Type',
  `TRANSACTION_QTY` TINYINT NOT NULL DEFAULT 1 COMMENT 'The Transaction Qty', -- update
  `UPDATE_USERID` VARCHAR(20) NULL DEFAULT 'CURRENT_USER()' COMMENT 'Update User Id',
  `LAST_ACTION` VARCHAR(12) NULL DEFAULT 'INSERTED' COMMENT 'Last Action Taken',
  `TIME_STAMP` DATETIME NULL COMMENT 'Update Time stamp',
  PRIMARY KEY (`REF_NO`, `TRANSACTION_TYPE`, `TRANSACTION_SUB_TYPE`),
  FOREIGN KEY (`REF_NO`)
  REFERENCES `mpa_oas`.`appointment_master` (`REF_NO`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION)
COMMENT = 'Master table for Transactions';

-- appointment_period_block
CREATE TABLE `mpa_oas`.`appointment_period_block` (
  `REF_NO` VARCHAR(19) NOT NULL COMMENT 'Reference number to identify the appointment',
  `PERIOD_START_DATE` DATE NOT NULL COMMENT 'The start date for the block out period',
  `PERIOD_START_TIME` TIME NOT NULL COMMENT 'The start time for the block out period',
  `PERIOD_END_DATE` DATE NOT NULL COMMENT 'The end date for the block out period',
  `PERIOD_END_TIME` TIME NOT NULL DEFAULT 1 COMMENT 'The end time for the block out period',
  `PERIOD_STATUS` VARCHAR(18) NOT NULL DEFAULT 'Pending' COMMENT 'Status of the period to be blocked',
  `REMARK` TINYTEXT NULL COMMENT 'Remarks for user to input free text',
  PRIMARY KEY (`PERIOD_START_DATE`, `PERIOD_START_TIME`, `PERIOD_END_DATE`, `PERIOD_END_TIME`),
  FOREIGN KEY (`REF_NO`)
  REFERENCES `mpa_oas`.`appointment_master` (`REF_NO`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION)
COMMENT = 'Appointment Period blocks records';

-- transaction_log
CREATE TABLE `mpa_oas`.`transaction_log` (
  `LOG_ID` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'System auto generated sequential number ',
  `REF_NO` VARCHAR(19) NOT NULL COMMENT 'Reference number to identify the appointment',
  `LOG_TYPE` VARCHAR(30) NOT NULL COMMENT 'The type of transaction log',
  `STAFF_ID` VARCHAR(30) NOT NULL COMMENT 'The unique identifier of an MPA staff',
  `LOG_DATE` DATE NOT NULL COMMENT 'The date for the action performed',
  `LOG_TIME` TIME NOT NULL COMMENT 'The time for the action performed',
  PRIMARY KEY (`LOG_ID`),
  INDEX `TRANSACTION_LOG_FK1_idx` (`REF_NO` ASC),
  INDEX `TRANSACTION_LOG_FK2_idx` (`STAFF_ID` ASC),
  CONSTRAINT `TRANSACTION_LOG_FK1`
    FOREIGN KEY (`REF_NO`)
    REFERENCES `mpa_oas`.`appointment_master` (`REF_NO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `TRANSACTION_LOG_FK2`
    FOREIGN KEY (`STAFF_ID`)
    REFERENCES `mpa_oas`.`user_master` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Transactio Log table';

-- appointment_craft
CREATE TABLE `mpa_oas`.`appointment_craft` (
  `REF_NO` VARCHAR(19) NOT NULL COMMENT 'Reference number to identify the appointment',
  `CRAFT_NO` VARCHAR(32) NOT NULL COMMENT 'The craft number',
  PRIMARY KEY (`REF_NO`, `CRAFT_NO`),
  CONSTRAINT `APPOINMENT_CRAFT_FK1`
    FOREIGN KEY (`REF_NO`)
    REFERENCES `mpa_oas`.`appointment_master` (`REF_NO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Appointment Craft details';
