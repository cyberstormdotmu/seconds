ALTER TABLE ORDER_LINES
    ADD COLUMN VAT_RATE NUMERIC(4,2),
    ADD COLUMN NET_AMOUNT NUMERIC(9,2),
    ADD COLUMN VAT_AMOUNT NUMERIC(9,2);