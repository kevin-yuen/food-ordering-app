package component;

import general.General;
import service.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Admin extends Person {
    Scanner scanner = new Scanner(System.in);
    Database db = new Database();

    public Admin(String name) {
        super(name);
    }

    private String initiateChangeItemOperation() {
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

    public String updateFoodPrice() {
        // ask user to enter new food price
        // pass the food name and food price to UPDATE SQL statement
        // compile the UPDATE SQL statement
        // ... (database.java executes the statement and returns the food name and the new food price in Hashmap)...
        // find the corresponding food name in the global variable foodHashMap
        // update the food price in the global variable foodHashMap....{foodName=foodPrice}
        //System.out.println("ITEM NAME: " + itemName);
        String foodName = initiateChangeItemOperation();
        double foodPrice = 0.0d;

        while (foodPrice <= 0.0) {
            System.out.print("Enter food price: ");
            foodPrice = scanner.nextDouble();

            if (foodPrice <= 0.0) {
                System.out.println("You haven't set the price yet.");
                continue;
            }
            break;
        }

        return General.createKeyValueForPriceUpdate(foodName, foodPrice);
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

    public String updateFoodMaxQty() {
        // ask user to enter max qty of the food
        // pass the food name and max qty to UPDATE SQL statement
        // compile the UPDATE SQL statement
        // ... (database.java executes the statement and returns the food name and the new max qty in Hashmap)...
        // find the corresponding food name in the global variable foodHashMap
        // update the max qty in the global variable foodHashMap...{foodName=maxQty}

        // formula to re-calculate remainQty
        // if new max qty > current max qty, remainQty = remainQty + (updated max qty - remainQty)
        // if new max qty < current max qty, remainQty = new max qty - (current max qty - remainQty)
        // if new max qty = current max qty, no change on remainQty

        String foodName = initiateChangeItemOperation();
        int foodMaxQty = 0;

        while (foodMaxQty <= 0) {
            System.out.print("Enter max. quantity in stock: ");
            foodMaxQty = scanner.nextInt();

            if (foodMaxQty <= 0) {
                System.out.println("You haven't set the max. quantity yet.");
                continue;
            }
            break;
        }

        return General.createKeyValueForQuantityUpdate(foodName, foodMaxQty);
    }
}
