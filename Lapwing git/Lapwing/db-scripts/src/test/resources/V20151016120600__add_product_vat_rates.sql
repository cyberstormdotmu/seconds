INSERT INTO PRODUCT_VAT_RATES (PRODUCT_ID, VAT_RATE_ID, START_DATETIME)
  VALUES (
    (SELECT ID FROM PRODUCTS WHERE PRODUCT_CODE = 'HPELITE840'),
    (SELECT ID FROM VAT_RATES WHERE VAT_CODE = 'STANDARD'),
    TO_TIMESTAMP('2015-09-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS')
  );

INSERT INTO PRODUCT_VAT_RATES (PRODUCT_ID, VAT_RATE_ID, START_DATETIME)
  VALUES (
    (SELECT ID FROM PRODUCTS WHERE PRODUCT_CODE = 'HPSPECTRE'),
    (SELECT ID FROM VAT_RATES WHERE VAT_CODE = 'STANDARD'),
    TO_TIMESTAMP('2015-09-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS')
  );
