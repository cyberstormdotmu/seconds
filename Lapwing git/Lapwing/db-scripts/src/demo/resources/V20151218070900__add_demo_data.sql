INSERT INTO ORGANISATIONS(NAME, REGISTRATION_NUMBER,INDUSTRY,NUMBER_OF_EMPLOYEES) VALUES ('Nidderdale Honey Ltd', '12345678','telecom','1000');

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
VALUES ('melissa@niddhoney.co.uk', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Melissa', 'Apis', currval('organisations_id_seq'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'BUYER');

INSERT INTO CONTACTS(TITLE, FIRSTNAME, SURNAME, EMAIL_ADDRESS, PHONE_NUMBER)
VALUES ('Miss', 'Melissa', 'Apis', 'melissa@niddhoney.co.uk', '01423 711711');

INSERT INTO ADDRESSES(DEPARTMENT_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POSTCODE)
VALUES ('Marketing', 'The Hive', 'High Street', 'Pateley Bridge', 'Harrogate', 'HG3 5QF');

INSERT INTO BANK_ACCOUNTS(ACCOUNT_NAME, SORTCODE, ACCOUNT_NUMBER, BANK_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POST_CODE)
VALUES ('Nidd Honey', '11-22-33', '12345678', 'HSBC', 'HSBC House', 'Parliament Street', null, 'Harrogate', 'HG1 1QQ');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, CONTACT_ID, DELIVERY_ADDRESS_ID, BANK_ACCOUNT_ID, COMPLETED, VERSION)
VALUES (currval('users_id_seq'), currval('organisations_id_seq'), currval('contacts_id_seq'), currval('addresses_id_seq'), currval('bank_accounts_id_seq'), TRUE, 1);

