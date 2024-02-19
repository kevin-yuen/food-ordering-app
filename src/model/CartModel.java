package model;

import component.Food;
import service.Database;

import java.util.HashMap;

public class CartModel {
    private Database db;
    public CartModel(Database db) {
        this.db = db;
    }

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
