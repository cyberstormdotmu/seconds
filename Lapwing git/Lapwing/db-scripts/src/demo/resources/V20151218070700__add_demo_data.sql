INSERT INTO ORGANISATIONS(NAME,REGISTRATION_NUMBER,INDUSTRY,NUMBER_OF_EMPLOYEES) VALUES ('Shoal Ltd','1111','telecom','1000');

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
VALUES ('oliver.squires@ishoal.com', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Oliver', 'Squires', currval('organisations_id_seq'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'BUYER');
INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'ADMIN');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (currval('users_id_seq'), currval('organisations_id_seq'), TRUE);

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
VALUES ('andrew.salkeld@ishoal.com', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Andrew', 'Salkeld', currval('organisations_id_seq'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'BUYER');
INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'ADMIN');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (currval('users_id_seq'), currval('organisations_id_seq'), TRUE);

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
VALUES ('christopher.salkeld@ishoal.com', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Chris', 'Salkeld', currval('organisations_id_seq'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'BUYER');
INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'ADMIN');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (currval('users_id_seq'), currval('organisations_id_seq'), TRUE);

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
VALUES ('andy.kerr@ishoal.com', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Andy', 'Kerr', currval('organisations_id_seq'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'BUYER');
INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'ADMIN');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (currval('users_id_seq'), currval('organisations_id_seq'), TRUE);

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
VALUES ('simon.urquhart@ishoal.com', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Simon', 'Urquhart', currval('organisations_id_seq'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'BUYER');
INSERT INTO USER_ROLES(USER_ID, ROLE_ID) VALUES (currval('users_id_seq'), 'ADMIN');

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (currval('users_id_seq'), currval('organisations_id_seq'), TRUE);
