INSERT INTO ORDERS (
  BUYER_ID, REFERENCE, STATUS, VERSION, CREATED_DATETIME, MODIFIED_DATETIME, PAYMENT_STATUS, BUYER_ORGANISATION_ID, VENDOR_ID, INVOICE_DATE, PAYMENT_DUE_DATE
)
VALUES (
  (SELECT ID FROM USERS WHERE USER_NAME = 'melissa@niddhoney.co.uk'),
  '0JMT-PZ71CB-88WQ',
  'CONFIRMED', 3, TIMESTAMP'2016-02-11 14:42:00Z', TIMESTAMP'2016-02-11 14:42:00Z', 'PAID',
  (SELECT ID FROM ORGANISATIONS WHERE NAME = 'Nidderdale Honey Ltd'),
  (SELECT ID FROM VENDORS WHERE NAME = 'HP'),
  DATE'2016-02-11',
  DATE'2017-11-11'
);

INSERT INTO order_lines (order_id, product_id, quantity, initial_price_band_id, current_price_band_id, created_datetime, modified_datetime, offer_id, vat_rate, net_amount, vat_amount
)
VALUES (
  currval('orders_id_seq'),
  (SELECT id FROM products WHERE product_code = 'HPELITE840'),
  100,
  (SELECT opb.id FROM offer_price_bands opb JOIN offers o ON opb.offer_id = o.id JOIN products p ON o.product_id = p.id WHERE p.product_code = 'HPELITE840' AND opb.min_volume = 0),
  (SELECT opb.id FROM offer_price_bands opb JOIN offers o ON opb.offer_id = o.id JOIN products p ON o.product_id = p.id WHERE p.product_code = 'HPELITE840' AND opb.min_volume = 0),
  TIMESTAMP'2016-02-11 14:42:00Z', TIMESTAMP'2016-02-11 14:42:00Z',
  (SELECT o.id FROM offers o JOIN products p ON o.product_id = p.id WHERE p.product_code = 'HPELITE840'),
  20.00,
  103000.00,
  20600.00
);

INSERT INTO order_payments (order_id, payment_reference, received_date, payment_type, amount, user_reference, created_datetime
)
VALUES (
  currval('orders_id_seq'),
  '6b5dacfc-6eef-4ec1-8759-1b327d0c7807',
  TIMESTAMP'2016-02-11 14:42:00Z',
  'CARD_PAYMENT',
  123600.00,
  'ch_17dLe2CGgFMweSz5mWeNNiMv',
  TIMESTAMP'2016-02-11 14:42:00Z'
);

INSERT INTO order_addresses (order_id, is_invoice_address, is_delivery_address, organisation_name, department_name, building_name, street_address, locality, post_town, postcode
)
VALUES (
  currval('orders_id_seq'),
  true,
  true,
  'Nidderdale Honey Ltd',
  'Marketing',
  'The Hive',
  'High Street',
  'Pateley Bridge',
  'Harrogate',
  'HG3 5QF'
);