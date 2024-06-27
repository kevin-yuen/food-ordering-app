DROP TABLE IF EXISTS foodorder.item;

CREATE TABLE foodorder.item (
	itemid int NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL
);

INSERT INTO foodorder.item (itemid, name)
VALUES
(1, "Burgers"),
(2, "Dogs"),
(3, "Drinks"),
(4, "Fries"),
(5, "Milkshake Mix-ins"),
(6, "Sandwiches"),
(7, "Toppings");

SELECT * FROM foodorder.item;
