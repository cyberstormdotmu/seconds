INSERT INTO ORGANISATIONS(NAME) VALUES ('Shoal Demo Org');

INSERT INTO USERS(USER_NAME, PASSWORD, FORENAME, SURNAME, ORGANISATION_ID)
    VALUES ('demo@the-shoal.com', '$2a$10$d5fTz.bSZ.qWSNGOxB2hferRyv8vyERrrRUo/mcy1M36sPNqGpGji', 'Demo', 'User', (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Shoal Demo Org'));

INSERT INTO USER_ROLES(USER_ID, ROLE_ID)
VALUES (
  (SELECT ID FROM USERS WHERE USER_NAME = 'demo@the-shoal.com'),
  (SELECT ID FROM ROLES WHERE NAME = 'BUYER')
);

