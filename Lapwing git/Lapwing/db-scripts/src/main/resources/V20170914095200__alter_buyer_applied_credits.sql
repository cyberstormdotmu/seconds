/*ALTER TABLE BUYER_APPLIED_CREDITS ADD COLUMN BUYER_CREDIT_ID INTEGER;*/

ALTER TABLE BUYER_APPLIED_CREDITS ADD COLUMN VENDOR_CREDIT_ID INTEGER;

ALTER TABLE BUYER_APPLIED_CREDITS ADD CONSTRAINT FK_BUYER_APPLIED_CREDITS_BUYER_VENDOR_CREDITS FOREIGN KEY (VENDOR_CREDIT_ID) REFERENCES BUYER_VENDOR_CREDIT (ID);