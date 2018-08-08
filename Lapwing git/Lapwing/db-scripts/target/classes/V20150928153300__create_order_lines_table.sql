CREATE TABLE ORDER_LINES (
  ID         SERIAL     NOT NULL,
  ORDER_ID 	 INTEGER 	NOT NULL,
  PRODUCT_ID INTEGER    NOT NULL,

  CONSTRAINT PK_ORDER_LINES PRIMARY KEY (ID),
  CONSTRAINT FK_ORDER_LINES_ORDERS FOREIGN KEY (ORDER_ID) REFERENCES ORDERS(ID),
  CONSTRAINT FK_ORDER_LINES_PRODUCTS FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)
);