ALTER TABLE PRODUCTS
  DROP COLUMN SKU,
  DROP COLUMN VAT_ID,
  ALTER COLUMN DESCRIPTION TYPE VARCHAR(4096);