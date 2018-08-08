--ALTER TABLE USER_ROLES DROP CONSTRAINT FK_USER_ROLES_ROLES ;
DROP TABLE USER_ROLES;

CREATE TABLE USER_ROLES (
  USER_ID INTEGER     NOT NULL,
  ROLE_ID VARCHAR(24) NOT NULL,

  CONSTRAINT PK_USER_ROLES PRIMARY KEY (USER_ID, ROLE_ID),
  CONSTRAINT FK_USER_ROLES_USERS FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);

DROP TABLE ROLES;

ALTER TABLE USERS ADD CONSTRAINT UQ_USERS_USER_NAME UNIQUE (USER_NAME);

--ALTER TABLE ORGANISATIONS ADD CONSTRAINT UQ_ORGANISATIONS_NAME UNIQUE (NAME);
ALTER TABLE ORGANISATIONS ADD COLUMN REGISTRATION_NUMBER VARCHAR(128);

--ALTER TABLE ROLES ADD CONSTRAINT UQ_ROLES_NAME UNIQUE (NAME);

CREATE TABLE contacts (
  id            SERIAL       NOT NULL,
  title         VARCHAR(12),
  firstname     VARCHAR(128) NOT NULL,
  surname       VARCHAR(128) NOT NULL,
  email_address VARCHAR(256) NOT NULL,
  password      VARCHAR(256) NOT NULL,
  phone_number  VARCHAR(10)  NOT NULL,

  CONSTRAINT pk_contacts PRIMARY KEY (ID)
);

CREATE TABLE bank_accounts (
  id             SERIAL       NOT NULL,
  account_name   VARCHAR(64)  NOT NULL,
  sortcode       VARCHAR(8)   NOT NULL,
  account_number INTEGER      NOT NULL,
  bank_name      VARCHAR(64)  NOT NULL,
  building_name  VARCHAR(64)  NOT NULL,
  street_address VARCHAR(128) NOT NULL,
  locality       VARCHAR(64),
  post_town      VARCHAR(64)  NOT NULL,
  post_code      VARCHAR(7)   NOT NULL,

  CONSTRAINT pk_bank_accounts PRIMARY KEY (ID)
);

CREATE TABLE addresses (
  id              SERIAL       NOT NULL,
  department_name VARCHAR(64)  NOT NULL,
  building_name   VARCHAR(64)  NOT NULL,
  street_address  VARCHAR(128) NOT NULL,
  locality        VARCHAR(64)  NOT NULL,
  post_town       VARCHAR(64)  NOT NULL,
  postcode        VARCHAR(7)   NOT NULL,

  CONSTRAINT pk_addresses PRIMARY KEY (ID)
);

CREATE TABLE buyer_profiles (
  id              SERIAL      NOT NULL,
  reference       VARCHAR(36) NOT NULL,
  user_id         INTEGER     NOT NULL,
  organisation_id INTEGER     NULL,

  CONSTRAINT pk_buyer_profiles PRIMARY KEY (ID),
  CONSTRAINT UQ_buyer_profiles_reference UNIQUE (reference),
  CONSTRAINT FK_buyer_profiles_user FOREIGN KEY (user_id) REFERENCES users (ID),
  CONSTRAINT FK_buyer_profiles_organisation FOREIGN KEY (organisation_id) REFERENCES organisations (ID)
);