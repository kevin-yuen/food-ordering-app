package component;

import model.Database;

import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends Person {
    private String name;
    Scanner scanner = new Scanner(System.in);
    Database db = new Database();

    public Admin() {
        this.name = "Store Manager";
    }

    public String initiateChangeItemOperation() {
        // per operationCdeOnItem
        // if 1 or 2 or 3
        // then ask for food name
        // and if 1
        // then execute updateFoodPrice()
        // and if 2
        // then execute addFood()
        // and if 3
        // then execute updateFoodMaxQty()
        String foodName = "";
        while (true) {
            System.out.print("Enter food name: ");
            foodName = scanner.nextLine().trim();

            if (foodName.equalsIgnoreCase("")) {
                System.out.println("Please enter food name.");
                continue;
            }
            break;
        }

        return foodName;
    }

    public void updateFoodPrice(String itemName) {
        // ask user to enter new food price
        // pass the food name and food price to UPDATE SQL statement
        // compile the UPDATE SQL statement
        // ... (database.java executes the statement and returns the food name and the new food price in Hashmap)...
        // find the corresponding food name in the global variable foodHashMap
        // update the food price in the global variable foodHashMap....{foodName=foodPrice}
        System.out.println("ITEM NAME: " + itemName);

        String foodName = initiateChangeItemOperation();
        double foodPrice = 0.0d;
        String updateQuery;

        while (foodPrice <= 0.0) {
            System.out.print("Enter food price: ");
            foodPrice = scanner.nextDouble();

            if (foodPrice <= 0.0) {
                System.out.println("You haven't set the price yet.");
                continue;
            }
            break;
        }

        updateQuery = String.format("UPDATE food f " +
                "INNER JOIN item i " +
                "SET f.price = %f " +
                "WHERE f.name = '%s' " +
                "AND f.itemId = i.itemId;", foodPrice, foodName, itemName);

        ArrayList<Food> updatedFoodDetail = db.updateFoodDetail(updateQuery, foodName, "name", "price",
                "remainQty", "maxQty");
        System.out.println("Row update from DB: " + updatedFoodDetail);

//        if (!updatedFoodDetail.getFoodName().equalsIgnoreCase(null)) {
//            //Global.syncGlobalVariable(itemName, updatedFoodDetail);
//        }
//        else {
//
//        }
    }

    public void addFood(String foodName) {
        // retrieve unique item types from the global variable foodHashMap
        // ask user to select item type
        // and ask user to enter new food price
        // and ask user to enter the max qty
        // pass item type, food name, food price, max qty to UPDATE SQL statement
        // compile the UPDATE SQL statement
        // ... (database.java
        // execute the statement
        // and set the remaining qty to max qty
        // and return the food name, food price, remaining qty, max qty in Hashmap)...
        // find the corresponding food name in the global variable foodHashMap
        // and add the food item to the item type in the global variable foodHashMap...{foodName=[foodPrice, ...]}
    }

    public void updateFoodMaxQty(String foodName) {
        // ask user to enter max qty of the food
        // pass the food name and max qty to UPDATE SQL statement
        // compile the UPDATE SQL statement
        // ... (database.java executes the statement and returns the food name and the new max qty in Hashmap)...
        // find the corresponding food name in the global variable foodHashMap
        // update the max qty in the global variable foodHashMap...{foodName=maxQty}
    }
}
