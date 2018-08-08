INSERT INTO VENDORS(NAME) VALUES ('HP');

INSERT INTO PRODUCTS(PRODUCT_CODE, NAME, DESCRIPTION, VENDOR_ID, PRODUCT_STOCK, TERMS_AND_CONDITIONS)
VALUES ('HPELITE840', 'HP EliteBook 840 G2 Laptop', 'The HP EliteBook 840 thin and light notebook allows users to be ultra-productive in and out of the office. Work with confidence thanks to proven technologies, security, performance, and management features that will meet all your enterprise needs.', currval('vendors_id_seq'), 40000, 'OFFER valid for 1 week only');

INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Processor', 'Intel Core i5 processor', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Memory', '4GB DDR3L-1600 SDRAM (1 x 4GB)', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Drives', '256GB SATA TLC SSD', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Display', '14" HD+ SVA anti-flare flat LED-backlit (1600x900)', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Graphics', 'Intel HD Graphics 5500', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Audio', 'HD audio with DTS Studio Sound', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Input', 'Spill resistant backlit keyboard', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Connectivity', 'Intel I218-LM GbE (10/100/100NIC + Intel Dual Band Wireless-AC 7265 802.11a/b/g/n/ac and Bluetooth 4.0', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Power', 'HP Long Life 3-cell, 50 WHr Li-ion polymer prismatic, 65 W Smart AC Adapter', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Weight', 'Starting at 1.34 kg', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Security', 'Security lock slot (lock supplied separately, TPM 1.2/2.0, Integrated smart card reader, Fingerprint reader', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Software', 'HP BIOSphere, HP Touchpoint Manager, HP 3D DriveGuard, HP Connection Manager, HP Wireless Hotspot, HP PageLift, HP Recovery Manager, HP Support Assistant, HP ePrint', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Dimensions', '33.9 x 23.7, 2.1 cm', currval('products_id_seq'));
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Warranty', '3 year limited warranty (optional Care Packs available - sold separately', currval('products_id_seq'));

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
    VALUES(currval('products_id_seq'), 1, 'HP laptop', 'http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c03889408_390x286.jpg');

INSERT INTO OFFERS(VERSION, PRODUCT_ID, START_DATE_TIME, END_DATE_TIME, CURRENT_VOLUME)
VALUES (1, currval('products_id_seq'), TO_TIMESTAMP('2015-09-17 00:00:00.530', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-03-17 23:59:59', 'YYYY-MM-DD HH24:MI:SS'), 36);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 0, 999, 1030.00, 930.00, 30.00, 20.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 1000, 1999, 930.00, 820.00, 30.00,20.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 2000, 2999, 830.00, 710.00, 30.00, 20.00);

INSERT INTO OFFER_PRICE_BANDS(OFFER_ID, MIN_VOLUME, MAX_VOLUME, BUYER_PRICE, VENDOR_PRICE, AGENCY_MARGIN, DISTRIBUTOR_MARGIN)
VALUES (currval('offers_id_seq'), 3000, null, 730.00, 600.00, 30.00, 20.00);

