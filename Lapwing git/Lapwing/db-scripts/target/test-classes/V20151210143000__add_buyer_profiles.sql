INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (
  (SELECT ID
   FROM USERS
   WHERE USER_NAME = 'oliver.squires@ishoal.com'),
    (SELECT ID
    FROM ORGANISATIONS
    WHERE NAME = 'Shoal'),
    TRUE
);

INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (
  (SELECT ID
   FROM USERS
   WHERE USER_NAME = 'tom@bosl.co.uk'),
  (SELECT ID
   FROM ORGANISATIONS
   WHERE NAME = 'Bramham Office Services Ltd'),
  TRUE
);


INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (
  (SELECT ID
   FROM USERS
   WHERE USER_NAME = 'melissa@niddhoney.co.uk'),
  (SELECT ID
   FROM ORGANISATIONS
   WHERE NAME = 'Nidderdale Honey Ltd'),
  TRUE
);


INSERT INTO BUYER_PROFILES (USER_ID, ORGANISATION_ID, COMPLETED)
VALUES (
  (SELECT ID
   FROM USERS
   WHERE USER_NAME = 'demo@the-shoal.com'),
  (SELECT ID
   FROM ORGANISATIONS
   WHERE NAME = 'Shoal Demo Org'),
  TRUE
);




