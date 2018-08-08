CREATE TABLE PRODUCT_SPECS (
      ID         SERIAL      NOT NULL,
      SPEC_TYPE  VARCHAR(64)  NOT NULL,
      SPEC_VALUE VARCHAR(512) NOT NULL,
      PRODUCT_ID INTEGER      NOT NULL,

      CONSTRAINT PK_PRODUCT_SPECS PRIMARY KEY (ID),
      CONSTRAINT FK_PRODUCT_SPECS_PRODUCTS FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)
);