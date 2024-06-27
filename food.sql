DROP TABLE IF EXISTS foodorder.food;

CREATE TABLE foodorder.food (
	id 	int NOT NULL PRIMARY KEY,
	itemid int NOT NULL,
    name varchar(255) NOT NULL,
    price decimal(5,2) NOT NULL,
    remainQty int NOT NULL,
    maxQty int NOT NULL,
    FOREIGN KEY (itemid) REFERENCES item(itemid)
);

INSERT INTO foodorder.food (id, itemid, name, price, remainQty, maxQty)
VALUES
(1, 1, "Hamburger", 2.50, 100, 100),
(2, 1, "CheeseBurger", 3.50, 100, 100),
(3, 1, "Bacon Burger", 5.00, 50, 50),
(4, 1, "Bacon CheeseBurger", 4.50, 25, 25),
(5, 1, "Little Bacon Burger", 7.50, 10, 10),
(6, 1, "Little Bacon CheeseBurger", 8.00, 20, 20),
(7, 2, "Hot Dog", 2.50, 20, 20),
(8, 2, "Cheese Dog", 3.00, 10, 10),
(9, 2, "Bacon Dog", 3.50, 15, 15),
(10, 2, "Bacon Cheese Dog", 4.00, 20, 20),
(11, 3, "Cola Coca", 1.50, 10, 10),
(12, 3, "Fanta", 1.50, 10, 10),
(13, 3, "Coke Zero", 0.50, 20, 20),
(14, 4, "Five Guys Style", 1.50, 30, 30),
(15, 4, "Cajun Style", 2.50, 30, 30),
(16, 5, "Bacon", 0.50, 10, 10),
(17, 5, "Bananas", 0.50, 20, 20),
(18, 5, "StrawBerries", 0.20, 50, 50),
(19, 5, "Chocolate", 0.50, 10, 10),
(20, 5, "Oreo Cookies", 0.60, 20, 20),
(21, 6, "Veggie Sandwich", 2.50, 10, 10),
(22, 6, "Cheese Veggie Sandwich", 3.50, 25, 25),
(23, 6, "Grilled Cheese", 5.50, 20, 20),
(24, 6, "BLT", 5.50, 30, 30),
(25, 7, "Mayo", 0.50, 10, 10),
(26, 7, "Lettuce", 0.50, 25, 25),
(27, 7, "Pickles", 0.50, 10, 10),
(28, 7, "Tomatoes", 0.50, 20, 20),
(29, 7, "Grilled Onions", 0.50, 30, 30),
(30, 7, "Grilled Mushrooms", 0.50, 30, 30),
(31, 7, "Ketchup", 0.50, 50, 50),
(32, 7, "Mustard", 0.50, 50, 50);

SELECT * FROM foodorder.food;
