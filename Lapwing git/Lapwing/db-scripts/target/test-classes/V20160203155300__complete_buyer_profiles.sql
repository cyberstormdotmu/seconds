DELETE FROM BUYER_PROFILES WHERE USER_ID = (SELECT ID FROM USERS WHERE USER_NAME = 'tom@bosl.co.uk');

INSERT INTO CONTACTS(TITLE, FIRSTNAME, SURNAME, EMAIL_ADDRESS, PHONE_NUMBER)
VALUES ('Mr', 'Tom', 'Fake', 'tom@bosl.co.uk', '0113 9876543');

INSERT INTO ADDRESSES(DEPARTMENT_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POSTCODE)
VALUES ('Sales', 'BOSL House', 'High Street', 'Bramham', 'Wetherby', 'LS23 6QR');

INSERT INTO BANK_ACCOUNTS(ACCOUNT_NAME, SORTCODE, ACCOUNT_NUMBER, BANK_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POST_CODE)
VALUES ('BOSL', '11-22-33', '12345678', 'HSBC', 'Bank House', 'High Street', 'Bramham', 'Wetherby', 'LS23 6QR');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, CONTACT_ID, DELIVERY_ADDRESS_ID, BANK_ACCOUNT_ID, COMPLETED, VERSION)
VALUES ((SELECT ID FROM USERS WHERE USER_NAME = 'tom@bosl.co.uk'),
        (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Bramham Office Services Ltd'),
        currval('contacts_id_seq'), currval('addresses_id_seq'), currval('bank_accounts_id_seq'), TRUE, 1);


DELETE FROM BUYER_PROFILES WHERE USER_ID = (SELECT ID FROM USERS WHERE USER_NAME = 'melissa@niddhoney.co.uk');

INSERT INTO CONTACTS(TITLE, FIRSTNAME, SURNAME, EMAIL_ADDRESS, PHONE_NUMBER)
VALUES ('Miss', 'Melissa', 'Apis', 'melissa@niddhoney.co.uk', '01423 711711');

INSERT INTO ADDRESSES(DEPARTMENT_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POSTCODE)
VALUES ('Marketing', 'The Hive', 'High Street', 'Pateley Bridge', 'Harrogate', 'HG3 5QF');

INSERT INTO BANK_ACCOUNTS(ACCOUNT_NAME, SORTCODE, ACCOUNT_NUMBER, BANK_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POST_CODE)
VALUES ('Nidd Honey', '11-22-33', '12345678', 'HSBC', 'HSBC House', 'Parliament Street', null, 'Harrogate', 'HG1 1QQ');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, CONTACT_ID, DELIVERY_ADDRESS_ID, BANK_ACCOUNT_ID, COMPLETED, VERSION)
VALUES ((SELECT ID FROM USERS WHERE USER_NAME = 'melissa@niddhoney.co.uk'),
        (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Nidderdale Honey Ltd'),
        currval('contacts_id_seq'), currval('addresses_id_seq'), currval('bank_accounts_id_seq'), TRUE, 1);


DELETE FROM BUYER_PROFILES WHERE USER_ID = (SELECT ID FROM USERS WHERE USER_NAME = 'demo@the-shoal.com');

INSERT INTO CONTACTS(TITLE, FIRSTNAME, SURNAME, EMAIL_ADDRESS, PHONE_NUMBER)
VALUES ('Mr', 'Shoal', 'Demo', 'demo@the-shoal.com', '0113 9876543');

INSERT INTO ADDRESSES(DEPARTMENT_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POSTCODE)
VALUES ('Sales', 'Shoal Demo Ltd', 'High Street', 'Bramham', 'Wetherby', 'LS23 6QR');

INSERT INTO BANK_ACCOUNTS(ACCOUNT_NAME, SORTCODE, ACCOUNT_NUMBER, BANK_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POST_CODE)
VALUES ('Shoal Demo Ltd', '11-22-33', '12345678', 'HSBC', 'Bank House', 'High Street', 'Bramham', 'Wetherby', 'LS23 6QR');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, CONTACT_ID, DELIVERY_ADDRESS_ID, BANK_ACCOUNT_ID, COMPLETED, VERSION)
VALUES ((SELECT ID FROM USERS WHERE USER_NAME = 'demo@the-shoal.com'),
        (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Shoal Demo Org'),
        currval('contacts_id_seq'), currval('addresses_id_seq'), currval('bank_accounts_id_seq'), TRUE, 1);


DELETE FROM BUYER_PROFILES WHERE USER_ID = (SELECT ID FROM USERS WHERE USER_NAME = 'oliver.squires@ishoal.com');

INSERT INTO CONTACTS(TITLE, FIRSTNAME, SURNAME, EMAIL_ADDRESS, PHONE_NUMBER)
VALUES ('Mr', 'Oliver', 'Squires', 'oliver.squires@ishoal.com', '0113 9876543');

INSERT INTO ADDRESSES(DEPARTMENT_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POSTCODE)
VALUES ('Sales', 'Shoal Demo Ltd', 'High Street', 'Bramham', 'Wetherby', 'LS23 6QR');

INSERT INTO BANK_ACCOUNTS(ACCOUNT_NAME, SORTCODE, ACCOUNT_NUMBER, BANK_NAME, BUILDING_NAME, STREET_ADDRESS, LOCALITY, POST_TOWN, POST_CODE)
VALUES ('Shoal Demo Ltd', '11-22-33', '12345678', 'HSBC', 'Bank House', 'High Street', 'Bramham', 'Wetherby', 'LS23 6QR');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, CONTACT_ID, DELIVERY_ADDRESS_ID, BANK_ACCOUNT_ID, COMPLETED, VERSION)
VALUES ((SELECT ID FROM USERS WHERE USER_NAME = 'oliver.squires@ishoal.com'),
        (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Shoal'),
        currval('contacts_id_seq'), currval('addresses_id_seq'), currval('bank_accounts_id_seq'), TRUE, 1);

