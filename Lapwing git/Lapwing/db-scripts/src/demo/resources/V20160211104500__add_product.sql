INSERT INTO PRODUCTS(PRODUCT_CODE, NAME, DESCRIPTION, CATEGORY_ID, VENDOR_ID,PRODUCT_STOCK, TERMS_AND_CONDITIONS,REVIEW,SUITABILITY,PROCESS_NOTIFICATION,SUBMIT_NOTIFICATION)
VALUES ('HPLASERJETM527DN',
        'HP LaserJet Enterprise MFP M527dn',
        'HP Office Laser Multifunction Printers are designed for SMBs and small workteams in larger companies, delivering enhanced productivity, simplified workflows and reduced costs',
        (SELECT ID FROM CATEGORIES WHERE CATEGORY_NAME = 'Printers'),
        currval('vendors_id_seq'),
        40000,
        'OFFER valid for 1 week only','review','suitability',TRUE,FALSE
);

INSERT INTO PRODUCT_VAT_RATES (PRODUCT_ID, VAT_RATE_ID, START_DATETIME)
VALUES (currval('products_id_seq'), (SELECT ID FROM VAT_RATES WHERE VAT_CODE = 'STANDARD'), TO_TIMESTAMP('2015-09-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Print Speed', 'Up to 45 ppm (black)', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Duplex Printing', 'Yes', currval('products_id_seq'));

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
VALUES(currval('products_id_seq'), 1, 'HP LaserJet', 'http://demo.the-shoal.com/productimages/c04831962.png');

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
VALUES(currval('products_id_seq'), 2, 'HP LaserJet', 'http://demo.the-shoal.com/productimages/c04831761.png');

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
VALUES(currval('products_id_seq'), 3, 'HP LaserJet', 'http://demo.the-shoal.com/productimages/c04831704.png');


INSERT INTO OFFERS(OFFER_REFERENCE, VERSION, PRODUCT_ID, START_DATE_TIME, END_DATE_TIME, CURRENT_VOLUME)
VALUES ('HPLASERJETM527DN', 1, currval('products_id_seq'), TO_TIMESTAMP('2015-10-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-12-15 23:59:59', 'YYYY-MM-DD HH24:MI:SS'), 0);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 0, 199, 1700.00, 1690.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 200, 399, 1650.00, 1640.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 400, 599, 1600.00, 1590.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 600, 699, 1550.00, 1540.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 700, 799, 1500.00, 1490.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 800, 899, 1400.00, 1390.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 900, 999, 1350.00, 1340.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 1000, 1199, 1300.00, 1290.00, 10.00,0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 1200, 1399, 1200.00, 1190.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 1400, 1599, 1150.00, 1140.00, 10.00, 0.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 1600, null, 1100.00, 1090.00, 10.00, 0.00);
