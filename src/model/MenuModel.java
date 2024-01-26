package model;

import component.Food;
import service.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class MenuModel {
    private HashMap<String, ArrayList<ArrayList<Food>>> menuHashMap = new HashMap<>();
    private Database db;

    public MenuModel(Database db) {
        this.db = db;
    }

    // Add food details of each food to menuHashMap
    //
    // This function adds food details of each food retrieved from database to menuHashMap.
    // Food details: food name, price, remain quantity, and max quantity
    //
    private void composeMenuHashMap(ArrayList<ArrayList<String>> latestDBMenuItems) {
        ArrayList<ArrayList<Food>> foodByType;
        ArrayList<Food> food;

        for (var element: latestDBMenuItems) {
            food = new ArrayList<>();
            foodByType = new ArrayList<>();

            // compose a single food
            String foodName = element.get(1);
            double price = Double.parseDouble(element.get(2));
            int remainQty = Integer.parseInt(element.get(3)), maxQty = Integer.parseInt(element.get(4));

            Food singleFood = new Food(foodName, price, remainQty, maxQty);
            food.add(singleFood);

            if (!menuHashMap.containsKey(element.get(0))) {
                foodByType.add(food);
                menuHashMap.put(element.get(0), foodByType);
            }
            else {
                ArrayList<ArrayList<Food>> existingFoodTypeList = menuHashMap.get(element.get(0));
                existingFoodTypeList.add(food);
            }
        }
    }

    // Fetch the latest food details of each food from foodorder.food
    //
    // This function retrieves the latest food details of each food from foodorder.food in MySQL server
    //
    // @return The list of food details of each food
    //
    public HashMap<String, ArrayList<ArrayList<Food>>> getLatestMenuItemsFromDB() {
        ArrayList<ArrayList<String>> latestMenuItemsFromDB = db.queryLatestItemDetails(
                "select itemType.name as itemName,\n" +
                        "foodItem.name as foodName,\n" +
                        "foodItem.price as price,\n" +
                        "foodItem.remainQty as remainQty,\n" +
                        "foodItem.maxQty as maxQty \n" +
                        "from foodorder.food foodItem\n" +
                        "inner join foodorder.item itemType\n" +
                        "on foodItem.itemid = itemType.itemid;",
                "itemName", "foodName", "price", "remainQty", "maxQty");

        composeMenuHashMap(latestMenuItemsFromDB);
        return getMenuHashMap();
    }

    public HashMap<String, ArrayList<Food>> sendDBRequestToUpdateFoodDetails(String itemName, String foodName,
                                                                             Double requestedPrice,
                                                                             Integer requestedMaxQty) {
        HashMap<String, ArrayList<Food>> dbResponse = new HashMap<>();
        Optional oPrice = Optional.ofNullable(requestedPrice), oReqMaxQty = Optional.ofNullable(requestedMaxQty);
        ArrayList<Food> updatedFoodDetail = new ArrayList<>();

        if (oPrice.isPresent() && !oReqMaxQty.isPresent()) {
            updatedFoodDetail = db.updateFoodDetail(String.format("UPDATE food f " +
                            "INNER JOIN item i " +
                            "SET f.price = %f " +
                            "WHERE f.name = '%s' " +
                            "AND f.itemId = i.itemId " +
                            "AND i.name = '%s';", requestedPrice, foodName, itemName),
                    foodName, "name", "price",
                    "remainQty", "maxQty");

            if (updatedFoodDetail.size() > 0) {
                dbResponse.put(itemName, updatedFoodDetail);
            }
        }
        else if (!oPrice.isPresent() && oReqMaxQty.isPresent()) {
            // retrieve the current max quantity
            HashMap<String, ArrayList<String>> currentQtyDetails = db.retrieveCurrentQty(String.format(
                    "SELECT f.name, f.maxQty, f.remainQty\n" +
                            "FROM food f\n" +
                            "INNER JOIN item i\n" +
                            "ON f.itemId = i.itemId\n" +
                            "WHERE i.name = '%s'\n" +
                            "AND f.name = '%s';", itemName, foodName), "name",
                    "remainQty", "maxQty");

            String food = currentQtyDetails.keySet().iterator().next().toString();
            int curRemainQty = Integer.parseInt(currentQtyDetails.get(food).get(0)),
                    curMaxQty = Integer.parseInt(currentQtyDetails.get(food).get(1));

            // update the current max quantity to the requested max qty
            if (curRemainQty == curMaxQty && curMaxQty < requestedMaxQty ||
                    curRemainQty == curMaxQty ||
                    curRemainQty < curMaxQty && curMaxQty < requestedMaxQty ||
                    curRemainQty < curMaxQty && curMaxQty > requestedMaxQty) {
                updatedFoodDetail = db.updateFoodDetail(String.format("UPDATE food f " +
                                "INNER JOIN item i " +
                                "SET f.maxQty = %d " +
                                "WHERE f.name = '%s' " +
                                "AND f.itemId = i.itemId " +
                                "AND i.name = '%s';", requestedMaxQty, foodName, itemName),
                        foodName, "name", "price",
                        "remainQty", "maxQty");
            }

            if (updatedFoodDetail.size() > 0) {
                // re-calculate remain quantity since max. quantity has been updated
                updatedFoodDetail = sendDBRequestToUpdateRemainQty(itemName, food, curMaxQty, requestedMaxQty);
                dbResponse.put(itemName, updatedFoodDetail);
            }
        }
        return dbResponse;
    }

    private ArrayList<Food> sendDBRequestToUpdateRemainQty(String itemName, String foodName, int currentMaxQty,
                                                           int requestedMaxQty) {
        ArrayList<Food> updatedRemainQty = db.updateFoodDetail(String.format("UPDATE foodorder.food f\n" +
                "INNER JOIN foodorder.item i\n" +
                "SET f.remainQty = (f.remainQty + (%d - %d))\n" +
                "WHERE i.name = '%s'\n" +
                "AND f.name = '%s'\n" +
                "AND f.itemid = i.itemid;", requestedMaxQty, currentMaxQty, itemName, foodName), foodName,
                "name", "price", "remainQty", "maxQty");

        return updatedRemainQty;
    }

    public HashMap<String, ArrayList<Food>> sendDBRequestToCreateNewFood(String itemName, String foodName, double price, int maxQty) {
        HashMap<String, ArrayList<Food>> newFoodHashMap = new HashMap<>();
        ArrayList<Food> newFood = db.createNewFood(String.format("INSERT INTO foodorder.food (" +
                        "name, itemId, price, remainQty, maxQty)\n" +
                        "VALUES ('%s', (\n" +
                        "SELECT itemId\n" +
                        "FROM foodorder.item\n" +
                        "WHERE name = '%s'), %f, %d, %d);", foodName, itemName, price, maxQty, maxQty),
                foodName, "name", "price", "remainQty", "maxQty");

        if (newFood.size() > 0) newFoodHashMap.put(itemName, newFood);
        return newFoodHashMap;
    }

    private HashMap<String, ArrayList<ArrayList<Food>>> getMenuHashMap() {
        return menuHashMap;
    }
}
