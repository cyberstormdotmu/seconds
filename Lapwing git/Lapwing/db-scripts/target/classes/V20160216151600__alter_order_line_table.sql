/**
    Make the product code unique.
 */
ALTER TABLE ORDER_LINES ALTER COLUMN NET_AMOUNT TYPE NUMERIC(12,2);
ALTER TABLE ORDER_LINES ALTER COLUMN VAT_AMOUNT TYPE NUMERIC(12,2);