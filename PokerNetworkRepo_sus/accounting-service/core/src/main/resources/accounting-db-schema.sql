SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `accounting` DEFAULT CHARACTER SET utf8 ;
USE `accounting`;

-- -----------------------------------------------------
-- Table `accounting`.`account`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`account` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `closed` DATETIME NULL DEFAULT NULL ,
  `created` DATETIME NOT NULL ,
  `currencyCode` VARCHAR(255) NOT NULL ,
  `fractionalDigits` INT(11) NOT NULL ,
  `lastModified` DATETIME NULL DEFAULT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `negativeBalanceAllowed` BIT(1) NOT NULL ,
  `status` INT(11) NOT NULL ,
  `type` VARCHAR(255) NULL DEFAULT NULL ,
  `userId` BIGINT(20) NULL DEFAULT NULL ,
  `walletId` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `user_id_idx` ON `accounting`.`account` (`userId` ASC) ;

CREATE INDEX `wallet_id_idx` ON `accounting`.`account` (`walletId` ASC) ;


-- -----------------------------------------------------
-- Table `accounting`.`accountattribute`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`accountattribute` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `akey` VARCHAR(255) NULL DEFAULT NULL ,
  `value` LONGTEXT NULL DEFAULT NULL ,
  `account_id` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `FK716EA70FA03319DC`
    FOREIGN KEY (`account_id` )
    REFERENCES `accounting`.`account` (`id` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `FK716EA70FA03319DC` ON `accounting`.`accountattribute` (`account_id` ASC) ;

CREATE INDEX `account_attr_key_idx` ON `accounting`.`accountattribute` (`akey` ASC) ;


-- -----------------------------------------------------
-- Table `accounting`.`transaction`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`transaction` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `comment` VARCHAR(255) NULL DEFAULT NULL ,
  `externalId` VARCHAR(255) NULL DEFAULT NULL ,
  `timestamp` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `externalId` ON `accounting`.`transaction` (`externalId` ASC) ;


-- -----------------------------------------------------
-- Table `accounting`.`entry`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`entry` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `amount` DECIMAL(19,2) NULL DEFAULT NULL ,
  `account_id` BIGINT(20) NULL DEFAULT NULL ,
  `transaction_id` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `FK400185221CF1F3C`
    FOREIGN KEY (`transaction_id` )
    REFERENCES `accounting`.`transaction` (`id` ),
  CONSTRAINT `FK4001852A03319DC`
    FOREIGN KEY (`account_id` )
    REFERENCES `accounting`.`account` (`id` ))
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `FK4001852A03319DC` ON `accounting`.`entry` (`account_id` ASC) ;

CREATE INDEX `FK400185221CF1F3C` ON `accounting`.`entry` (`transaction_id` ASC) ;


-- -----------------------------------------------------
-- Table `accounting`.`balancecheckpoint`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`balancecheckpoint` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `balance` DECIMAL(19,2) NULL DEFAULT NULL ,
  `entry_id` BIGINT(20) NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `FK4EBEFD64C421A03C`
    FOREIGN KEY (`entry_id` )
    REFERENCES `accounting`.`entry` (`id` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `FK4EBEFD64C421A03C` ON `accounting`.`balancecheckpoint` (`entry_id` ASC) ;


-- -----------------------------------------------------
-- Table `accounting`.`currencyrate`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`currencyrate` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `rate` DECIMAL(10,7) NOT NULL ,
  `sourceCC` VARCHAR(255) NOT NULL ,
  `targetCC` VARCHAR(255) NOT NULL ,
  `timestamp` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `accounting`.`transactionattribute`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`transactionattribute` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `akey` VARCHAR(255) NULL DEFAULT NULL ,
  `value` LONGTEXT NULL DEFAULT NULL ,
  `transaction_id` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `FKAEDA857E21CF1F3C`
    FOREIGN KEY (`transaction_id` )
    REFERENCES `accounting`.`transaction` (`id` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `FKAEDA857E21CF1F3C` ON `accounting`.`transactionattribute` (`transaction_id` ASC) ;

CREATE INDEX `transaction_attr_key_idx` ON `accounting`.`transactionattribute` (`akey` ASC) ;


-- -----------------------------------------------------
-- Table `accounting`.`transactionerror`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `accounting`.`transactionerror` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `closed` DATETIME NULL DEFAULT NULL ,
  `comment` VARCHAR(255) NULL DEFAULT NULL ,
  `data` LONGBLOB NOT NULL ,
  `lastModifed` DATETIME NOT NULL ,
  `received` DATETIME NOT NULL ,
  `status` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
