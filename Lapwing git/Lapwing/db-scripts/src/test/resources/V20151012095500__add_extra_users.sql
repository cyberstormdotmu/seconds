INSERT INTO ORGANISATIONS(NAME) VALUES ('Bramham Office Services Ltd');

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
    VALUES ('tom@bosl.co.uk', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Tom', 'Fake', (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Bramham Office Services Ltd'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID)
VALUES (
  (SELECT ID FROM USERS WHERE USER_NAME = 'tom@bosl.co.uk'),
  (SELECT ID FROM ROLES WHERE NAME = 'BUYER')
);

INSERT INTO ORGANISATIONS(NAME) VALUES ('Nidderdale Honey Ltd');

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
    VALUES ('melissa@niddhoney.co.uk', '$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be', 'Melissa', 'Apis', (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Nidderdale Honey Ltd'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID)
VALUES (
  (SELECT ID FROM USERS WHERE USER_NAME = 'melissa@niddhoney.co.uk'),
  (SELECT ID FROM ROLES WHERE NAME = 'BUYER')
);

