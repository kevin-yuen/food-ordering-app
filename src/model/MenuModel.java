package model;

import component.Food;
import service.Database;

import java.util.*;

public class MenuModel {
    private Database db;

    public MenuModel(Database db) {
        this.db = db;
    }

    // Fetch the latest food details of each food from foodorder.food
    //
    // This function retrieves the latest food details of each food from foodorder.food in MySQL server
    //
    // @return The list of food details of each food
    //
    public Map<String, HashMap<String, List<Food>>> getLatestMenuItemsFromDB() {
        Map<String, HashMap<String, List<Food>>> latestMenuItemsFromDB;
        latestMenuItemsFromDB = db.queryLatestMenuItemsDets(
                "SELECT itemType.name AS itemName,\n" +
                        "foodItem.name AS foodName,\n" +
                        "foodItem.price AS price,\n" +
                        "foodItem.remainQty AS remainQty,\n" +
                        "foodItem.maxQty AS maxQty \n" +
                        "FROM foodorder.food foodItem\n" +
                        "INNER JOIN foodorder.item itemType\n" +
                        "ON foodItem.itemid = itemType.itemid;",
                "itemName", "foodName", "price", "remainQty", "maxQty");


        return latestMenuItemsFromDB;
    }

    public HashMap<String, Food> sendDBRequestToUpdateFoodDetails(String itemName, String foodName,
                                                                             Double requestedPrice,
                                                                             Integer requestedMaxQty) {
        HashMap<String, Food> dbResponse = new HashMap<>();
        Optional oPrice = Optional.ofNullable(requestedPrice), oReqMaxQty = Optional.ofNullable(requestedMaxQty);
        HashMap<String, Food> tempDBResponse = new HashMap<>();

        if (oPrice.isPresent() && !oReqMaxQty.isPresent()) {
            tempDBResponse = db.updateFoodDets(String.format("UPDATE food f " +
                            "INNER JOIN item i " +
                            "SET f.price = %f " +
                            "WHERE f.name = '%s' " +
                            "AND f.itemId = i.itemId " +
                            "AND i.name = '%s';", requestedPrice, foodName, itemName),
                    foodName);

            if (tempDBResponse.size() > 0) {
                dbResponse.put(itemName, tempDBResponse.get(itemName));
            }
        } else if (!oPrice.isPresent() && oReqMaxQty.isPresent()) {
            // retrieve the current max quantity
            String query = String.format(
                    "SELECT i.name AS itemName, " +
                            "f.name AS foodName, " +
                            "f.price, " +
                            "f.remainQty, " +
                            "f.maxQty\n" +
                            "FROM food f\n" +
                            "INNER JOIN item i\n" +
                            "ON f.itemId = i.itemId\n" +
                            "WHERE i.name = '%s'\n" +
                            "AND f.name = '%s';", itemName, foodName);
            Food foodRSet = db.executeReadOp(query, "itemName", "foodName", "price", "remainQty", "maxQty");
            String iName = null, fName = null;
            int curRemainQty, curMaxQty = 0;

            if (foodRSet.getItemName() != null || foodRSet.getFoodName() != null) {
                iName = foodRSet.getItemName();
                fName = foodRSet.getFoodName();
                curRemainQty = foodRSet.getRemainQty();
                curMaxQty = foodRSet.getMaxQty();

                // update the current max quantity to the requested max qty
                if (curRemainQty == curMaxQty && curMaxQty < requestedMaxQty ||
                        curRemainQty == curMaxQty ||
                        curRemainQty < curMaxQty && curMaxQty < requestedMaxQty) {
                    tempDBResponse = db.updateFoodDets(String.format("UPDATE food f " +
                                    "INNER JOIN item i " +
                                    "SET f.maxQty = %d " +
                                    "WHERE f.name = '%s' " +
                                    "AND f.itemId = i.itemId " +
                                    "AND i.name = '%s';", requestedMaxQty, fName, iName),
                            fName);
                }
            }

            if (tempDBResponse.size() > 0) {
                // re-calculate remain quantity since max. quantity has been updated
                HashMap<String, Food> updatedRemainQty = sendDBRequestToCalculateRemainQty(iName, fName, curMaxQty, requestedMaxQty);
                dbResponse.put(itemName, updatedRemainQty.get(itemName));
            }
        }
        return dbResponse;
    }

    private HashMap<String, Food> sendDBRequestToCalculateRemainQty(String itemName, String foodName, int currentMaxQty,
                                                           int requestedMaxQty) {
        HashMap<String, Food> updatedRemainQty = db.updateFoodDets(String.format("UPDATE foodorder.food f\n" +
                "INNER JOIN foodorder.item i\n" +
                "SET f.remainQty = (f.remainQty + (%d - %d))\n" +
                "WHERE i.name = '%s'\n" +
                "AND f.name = '%s'\n" +
                "AND f.itemid = i.itemid;", requestedMaxQty, currentMaxQty, itemName, foodName), foodName);

        return updatedRemainQty;
    }

    public HashMap<String, Food> sendDBRequestToCreateNewFood(String itemName, String foodName, double price, int maxQty) {
        HashMap<String, Food> newFoodHashMap = new HashMap<>();
        Food newFood = db.createNewFood(String.format("INSERT INTO foodorder.food (" +
                        "name, itemId, price, remainQty, maxQty)\n" +
                        "VALUES ('%s', (\n" +
                        "SELECT itemId\n" +
                        "FROM foodorder.item\n" +
                        "WHERE name = '%s'), %f, %d, %d);", foodName, itemName, price, maxQty, maxQty), foodName);

        newFoodHashMap.put(itemName, newFood);
        return newFoodHashMap;
    }
}
