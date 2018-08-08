DELIMITER $$

/* This is an example script for moving away data from the accounting
   tables for data ware housing. 
   
   It will create new tables prefixed with "_ba_" to store the removed 
   data; these tables can later be moved elsewahere. If the backup tables
   exists the script will fail.
   
   This is a MySQL-specific script.
   
   The single input parameters should be the timestamp from which
   transactions should be removed. */

DROP PROCEDURE IF EXISTS `create_backup_transfer` $$
CREATE PROCEDURE `create_backup_transfer`(IN start_date BIGINT(20))
BEGIN

  /* 
   * START CREATE TABLES
   * 
   * Create backup tables but we're not dropping
   * if they exist (safety).
   */
  CREATE TABLE _ba_transaction AS
    SELECT *
    FROM `transaction`
    WHERE `timestamp` <= start_date;

  CREATE TABLE _ba_entry AS
    SELECT e.id, e.amount, e.account_id, e.transaction_id
    FROM entry e JOIN `transaction` t ON e.transaction_id = t.id
    WHERE t.`timestamp` <= start_date;

  /* we need this index later */
  CREATE INDEX `accountid_idx` ON _ba_entry (account_id);

  CREATE TABLE _ba_transaction_attr AS
    SELECT a.id, a.akey, a.value, a.transaction_id
    FROM transactionattribute a JOIN `transaction` t ON a.transaction_id = t.id
    WHERE t.`timestamp` <= start_date;

  CREATE TABLE _ba_balance AS
    SELECT b.id, b.balance, b.entry_id
    FROM balancecheckpoint b JOIN
      (entry e JOIN `transaction` t ON e.transaction_id = t.id) ON b.entry_id = e.id
    WHERE t.`timestamp` <= start_date;
  /*
   * END CREATE TABLES
   */

  /* now that we're backed up, delete */
  DELETE b
    FROM balancecheckpoint b JOIN
      (entry e JOIN `transaction` t ON e.transaction_id = t.id) ON b.entry_id = e.id
    WHERE t.`timestamp` <= start_date;

  DELETE e
    FROM entry e JOIN `transaction` t ON e.transaction_id = t.id
    WHERE t.`timestamp` <= start_date;

  DELETE t
    FROM `transaction` t WHERE t.`timestamp` <= start_date;

  /* this is where we start summing the accounts we just
     backed up: iterate through all accounts, sum their balance
     and insert initial entry */
  BEGIN

    /* boolean to use for cursor control and an int for cursor value */
    DECLARE no_more_rows BOOLEAN;
    DECLARE account BIGINT(20);

    /* a decimal for summing an account, one for any initial entry
       and an int for the last entry */
    DECLARE account_balance DECIMAL(19,2);
    DECLARE initial_balance DECIMAL(19,2);
    DECLARE last_entry BIGINT(20);

    /* get a cursor for all involved accounts */
    DECLARE account_cursor CURSOR FOR
      SELECT DISTINCT e.account_id FROM _ba_entry e;

    /* handler for cursor end, set above boolean */
    DECLARE CONTINUE HANDLER FOR NOT FOUND
      SET no_more_rows = TRUE;


    /* open cursor and start loop */
    OPEN account_cursor;
    account_loop: LOOP

      FETCH account_cursor INTO account;

      /* break loop here if we're at the end */
      IF no_more_rows THEN
        LEAVE account_loop;
      END IF;

      /* get last entry id */
      SELECT e.id INTO last_entry
        FROM _ba_entry e
        WHERE e.account_id = account
        ORDER BY e.id DESC
        LIMIT 1;

      /* sum account */
      SELECT SUM(e.amount) INTO account_balance
        FROM _ba_entry e
        WHERE e.account_id = account;

      /* get inital entry */
      SELECT SUM(e.amount) INTO initial_balance
        FROM entry e
        WHERE e.account_id = account AND transaction_id IS NULL;

      /* remove any old initial entry */
      DELETE FROM entry
        WHERE transaction_id IS NULL AND account_id = account;

      /* insert new inital entry */
      INSERT INTO entry VALUES (last_entry, account_balance + COALESCE(initial_balance, 0), account, NULL);

    END LOOP account_loop;
    CLOSE account_cursor;

    /* Thats it! */

  END;

END $$

DELIMITER ;