CREATE TABLE CATEGORIES (
  ID            SERIAL      NOT NULL,
  CATEGORY_NAME VARCHAR(32) NOT NULL,
  PARENT_ID     INTEGER     NULL,

  CONSTRAINT PK_CATEGORIES PRIMARY KEY (ID),
  CONSTRAINT FK_CATEGORIES_CATEGORIES FOREIGN KEY (PARENT_ID) REFERENCES CATEGORIES (ID)
);
