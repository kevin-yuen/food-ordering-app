package model;

/**
 * This class is responsible for interacting with the database by retrieving and updating data per request from
 * ServerController.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Food;
import service.Database;

import java.util.HashMap;

public class CartModel {
    private Database db;
    public CartModel(Database db) {
        this.db = db;
    }

    /**
     * Request DB to update the current remaining quantity of the food
     *
     * This function requests DB to perform the followings::
     * 1. whether such food exists in foodorder.food and
     * 2. whether the matching food item has sufficient remaining quantity (i.e. at the same quantity as the customer
     * order)
     *
     * If both conditions are met, the current remaining quantity will be re-calculated by subtracting the order
     * quantity from the current remaining quantity.
     *
     * If the second condition fails, the current remaining quantity will be returned.
     *
     * @param   itemName    name of the item category where the food is being ordered by the customer
     * @param   foodName    name of the food that the customer orders
     * @param   reqQty      order quantity that customer requests for
     * @return              the latest remaining quantity along with food name, price, max. quantity, and item category name
     */
    public HashMap<String, Food> sendDBRequestToUpdateRemainQty(String itemName, String foodName, int reqQty) {
        Food updatedFoodDets;
        HashMap<String, Food> foodDetsWithUpdatedRemainQty = new HashMap<>();

        String queryCkQtyInStock = String.format("SELECT i.name AS itemName, " +
                "f.name AS foodName, " +
                "f.price, " +
                "f.remainQty, " +
                "f.maxQty\n" +
                "FROM foodorder.food f\n" +
                "INNER JOIN foodorder.item i\n" +
                "ON f.itemId = i.itemId\n" +
                "WHERE f.name = \"%s\"\n" +
                "AND i.name = \"%s\";", foodName, itemName);
        Food foodRSetCkQtyInStock = db.executeReadOp(queryCkQtyInStock,
                "itemName", "foodName", "price", "remainQty", "maxQty");

        if (foodRSetCkQtyInStock.getItemName() != null || foodRSetCkQtyInStock.getFoodName() != null) {
            String query = String.format("SELECT i.name AS itemName, " +
                    "f.name AS foodName, " +
                    "f.price, " +
                    "f.remainQty, " +
                    "f.maxQty\n" +
                    "FROM foodorder.food f\n" +
                    "INNER JOIN foodorder.item i\n" +
                    "ON f.itemId = i.itemId\n" +
                    "WHERE f.name = \"%s\";", foodName);

            if (foodRSetCkQtyInStock.getRemainQty() >= 1) {
                if (foodRSetCkQtyInStock.getRemainQty() >= reqQty) {
                    foodDetsWithUpdatedRemainQty = db.updateFoodDets(String.format("UPDATE foodorder.food f\n" +
                            "INNER JOIN foodorder.item i\n" +
                            "ON f.itemId = i.itemId\n" +
                            "SET f.remainQty = (f.remainQty - %d)\n" +
                            "WHERE f.name = \"%s\";", reqQty, foodName), foodName);
                    updatedFoodDets = foodDetsWithUpdatedRemainQty.get(itemName);
                } else {
                    updatedFoodDets = db.executeReadOp(query, "foodName", "price", "remainQty",
                            "maxQty");
                }
            } else {
                updatedFoodDets = db.executeReadOp(query, "foodName", "price", "remainQty",
                        "maxQty");
            }
            foodDetsWithUpdatedRemainQty.put(foodRSetCkQtyInStock.getItemName(), updatedFoodDets);
        }
        return foodDetsWithUpdatedRemainQty;
    }
}
