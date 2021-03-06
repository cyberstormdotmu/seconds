SET @VENDOR_ID = SELECT ID FROM VENDORS WHERE NAME = 'HP';

SET @CATEGORY_ID = SELECT ID FROM CATEGORIES WHERE CATEGORY_NAME = 'Power User';

INSERT INTO PRODUCTS(PRODUCT_CODE, NAME, DESCRIPTION, VENDOR_ID, CATEGORY_ID, PRODUCT_STOCK, TERMS_AND_CONDITIONS)
VALUES ('HPELITE840', 'HP EliteBook 840 G2 Laptop', 'The HP EliteBook 840 thin and light notebook allows users to be ultra-productive in and out of the office. Work with confidence thanks to proven technologies, security, performance, and management features that will meet all your enterprise needs.', @VENDOR_ID, @CATEGORY_ID,40000, 'OFFER valid for 1 week only');

SET @PRODUCT_ID = SELECT ID FROM PRODUCTS WHERE PRODUCT_CODE = 'HPELITE840';

INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Processor', 'Intel Core i5 processor', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Memory', '4GB DDR3L-1600 SDRAM (1 x 4GB)', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Drives', '256GB SATA TLC SSD', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Display', '14" HD+ SVA anti-flare flat LED-backlit (1600x900)', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Graphics', 'Intel HD Graphics 5500', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Audio', 'HD audio with DTS Studio Sound', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Input', 'Spill resistant backlit keyboard', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Connectivity', 'Intel I218-LM GbE (10/100/100NIC + Intel Dual Band Wireless-AC 7265 802.11a/b/g/n/ac and Bluetooth 4.0', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Power', 'HP Long Life 3-cell, 50 WHr Li-ion polymer prismatic, 65 W Smart AC Adapter', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Weight', 'Starting at 1.34 kg', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Security', 'Security lock slot (lock supplied separately, TPM 1.2/2.0, Integrated smart card reader, Fingerprint reader', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Software', 'HP BIOSphere, HP Touchpoint Manager, HP 3D DriveGuard, HP Connection Manager, HP Wireless Hotspot, HP PageLift, HP Recovery Manager, HP Support Assistant, HP ePrint', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Dimensions', '33.9 x 23.7, 2.1 cm', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Warranty', '3 year limited warranty (optional Care Packs available - sold separately', @PRODUCT_ID);

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
    VALUES(@PRODUCT_ID, 1, 'HP laptop', 'http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c03889408_390x286.jpg');

INSERT INTO product_vat_rates(PRODUCT_ID, VAT_RATE_ID, START_DATETIME) VALUES (@PRODUCT_ID, 1, '2008-01-01');

SET @CATEGORY_ID = SELECT ID FROM CATEGORIES WHERE CATEGORY_NAME = 'Convertibles';

INSERT INTO PRODUCTS(PRODUCT_CODE, NAME, DESCRIPTION, VENDOR_ID, CATEGORY_ID,PRODUCT_STOCK, TERMS_AND_CONDITIONS)
VALUES ('HPSPECTRE', 'HP Spectre 13-4109na x360 Convertible Laptop', 'Time to demand more from your notebook. Function meets flexibility in the elegantly designed x360. Any way you bend it, the Spectre x360 delivers.', @VENDOR_ID, @CATEGORY_ID,40000, 'OFFER valid for 1 week only');

SET @PRODUCT_ID = SELECT ID FROM PRODUCTS WHERE PRODUCT_CODE = 'HPSPECTRE';

INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Processor', 'Intel Core i7 processor', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Memory', '8 GB DDR3L SDRAM (onboard)', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Drives', '512 GB M.2 SSD', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Display', '33.8 cm (13.3") diagonal QHD IPS LED-backlit touch screen (2560 x 1440)', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Graphics', 'Intel HD Graphics 520', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Audio', 'Bang & Olufsen Dual speakers', @PRODUCT_ID);

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
    VALUES(@PRODUCT_ID, 1, 'HP Spectre x360 laptop', 'http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/P5P13EA-ABU_1_1560x1144.jpg');

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
    VALUES(@PRODUCT_ID, 2, 'HP Spectre x360 laptop', 'http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/P5P13EA-ABU_3_1560x1144.jpg');

INSERT INTO product_vat_rates(PRODUCT_ID, VAT_RATE_ID, START_DATETIME) VALUES (@PRODUCT_ID, 1, '2008-01-01');


INSERT INTO PRODUCTS(PRODUCT_CODE, NAME, DESCRIPTION, VENDOR_ID, CATEGORY_ID,PRODUCT_STOCK, TERMS_AND_CONDITIONS)
VALUES ('HPPAVILLIONDV7', 'HP Pavilion dv7-6c52ea Entertainment Notebook PC', 'This is an expired model which is left here for reference purposes.', @VENDOR_ID, @CATEGORY_ID,40000, 'OFFER valid for 1 week only');

SET @PRODUCT_ID = SELECT ID FROM PRODUCTS WHERE PRODUCT_CODE = 'HPPAVILLIONDV7';

INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Processor', 'Intel Core i7 processor', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Memory', '8 GB DDR3L SDRAM (onboard)', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Drives', '1TB SATA', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Display', '43,9 cm (17.3") HD+ LED BrightView (1600 x 900)', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Graphics', 'AMD Radeon HD 7470M (1 GB DDR5 dedicated)', @PRODUCT_ID);
INSERT INTO PRODUCT_SPECS(SPEC_TYPE, SPEC_VALUE, PRODUCT_ID) VALUES ('Audio', 'Beats™ Audio with HP Triple Bass Reflex Subwoofer', @PRODUCT_ID);

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
VALUES(@PRODUCT_ID, 1, 'HP Pavillion DV7', 'http://product-images.www8-hp.com/digmedialib/prodimg/lowres/c02756743.png');

INSERT INTO PRODUCT_IMAGES(PRODUCT_ID, PRIORITY, DESCRIPTION, URL)
VALUES(@PRODUCT_ID, 2, 'HP Pavillion DV7', 'http://product-images.www8-hp.com/digmedialib/prodimg/lowres/c02756689.png');

INSERT INTO product_vat_rates(PRODUCT_ID, VAT_RATE_ID, START_DATETIME) VALUES (@PRODUCT_ID, 1, '2008-01-01');