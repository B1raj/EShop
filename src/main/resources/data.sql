-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_AT) VALUES (1, 'Laptop', 'Mac Book Pro', '2000', true, 'Admin', CURRENT_TIMESTAMP());
INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_AT) VALUES (2, 'Mouse', 'Dell Mouse', '20', true, 'Admin', CURRENT_TIMESTAMP());
INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_AT) VALUES (3, 'Keyboard', 'Logitec', '40', true, 'Admin', CURRENT_TIMESTAMP());
INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_AT) VALUES (4, 'Monitor', 'Toshiba Monitor', '200', true, 'Admin', CURRENT_TIMESTAMP());
INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE, IS_AVAILABLE, CREATED_BY, CREATED_AT) VALUES (5, 'CPU', 'DELL CPU', '500', true, 'Admin', CURRENT_TIMESTAMP());


INSERT INTO DISCOUNT (ID, PRODUCT, DISCOUNT_PRODUCT, DISCOUNT_PERCENT, DISCOUNT_TYPE, CREATED_BY, CREATED_AT) VALUES (1, 1, 2, 100, 'BUNDLE', 'Admin', CURRENT_TIMESTAMP());
INSERT INTO DISCOUNT (ID, PRODUCT, DISCOUNT_PRODUCT, DISCOUNT_PERCENT, DISCOUNT_TYPE, CREATED_BY, CREATED_AT) VALUES (2, 3, 3, 50, 'DISCOUNT','Admin', CURRENT_TIMESTAMP());


INSERT INTO CART (ID, USER, CREATED_BY, CREATED_AT) VALUES (1, 1,  'Admin', CURRENT_TIMESTAMP());
INSERT INTO CART (ID, USER, CREATED_BY, CREATED_AT) VALUES (2, 1, 'Admin', CURRENT_TIMESTAMP());


INSERT INTO CART_ORDER (ID, CART_ID, PRODUCT_ID, QUANTITY, CREATED_BY, CREATED_AT) VALUES (1, 1, 1, 1,'Admin', CURRENT_TIMESTAMP());
INSERT INTO CART_ORDER (ID, CART_ID, PRODUCT_ID, QUANTITY, CREATED_BY, CREATED_AT) VALUES (2, 1, 2, 1,'Admin', CURRENT_TIMESTAMP());
INSERT INTO CART_ORDER (ID, CART_ID, PRODUCT_ID, QUANTITY, CREATED_BY, CREATED_AT) VALUES (3, 1, 3, 2,'Admin', CURRENT_TIMESTAMP());
INSERT INTO CART_ORDER (ID, CART_ID, PRODUCT_ID, QUANTITY, CREATED_BY, CREATED_AT) VALUES (4, 1, 4, 1,'Admin', CURRENT_TIMESTAMP());