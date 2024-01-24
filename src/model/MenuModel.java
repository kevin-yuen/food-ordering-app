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

    public HashMap<String, ArrayList<Food>> sendDBRequestToUpdateFoodDetails(String itemName, String foodName, Double price,
                                                            Integer maxQty) {
        HashMap<String, ArrayList<Food>> dbResponse = new HashMap<>();
        Optional oPrice = Optional.ofNullable(price), oMaxQty = Optional.ofNullable(maxQty);
        String setStat = oPrice.isPresent() ? String.format("f.price = %f", price) :
                String.format("f.maxQty = %d", maxQty);

        ArrayList<Food> updatedFoodDetail = db.updateFoodDetail(String.format("UPDATE food f " +
                                "INNER JOIN item i " +
                                "SET %s " +
                                "WHERE f.name = '%s' " +
                                "AND f.itemId = i.itemId " +
                                "AND i.name = '%s';", setStat, foodName, itemName),
                foodName, "name", "price",
                "remainQty", "maxQty");

//        if (oMaxQty.isPresent()) {
//            updatedFoodDetail = db.updateFoodDetail(String.format(""),
//                    foodName, "name", "price",
//                    "remainQty", "maxQty");
//        }

        if (updatedFoodDetail.size() > 0) {
            dbResponse.put(itemName, updatedFoodDetail);
        }
        return dbResponse;
    }

    private HashMap<String, ArrayList<ArrayList<Food>>> getMenuHashMap() {
        return menuHashMap;
    }
}
