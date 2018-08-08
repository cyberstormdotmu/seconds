CREATE TABLE SUPPLIER_PAYMENTS (
  ID                    SERIAL          NOT NULL,
  TRANSFER_FROM         VARCHAR(15)     NOT NULL,
  TRANSFER_TO           VARCHAR(15)     NOT NULL,
  RECEIVED_DATE         DATE            NOT NULL,
  PAYMENT_TYPE          VARCHAR(15)     NOT NULL,
  AMOUNT                NUMERIC(9,2)    NOT NULL,
  USER_REFERENCE        VARCHAR(128)    NOT NULL,
  CREATED_DATE          TIMESTAMP       NOT NULL,
  RECORD_PAYMENT_STATUS VARCHAR(20)     NOT NULL,
  PAYMENT_RECORD_DATE   TIMESTAMP       NOT NULL,
  OFFER_REFERENCE       VARCHAR(36)     NOT NULL,

  CONSTRAINT PK_SUPPLIER_PAYMENTS PRIMARY KEY (ID)
);