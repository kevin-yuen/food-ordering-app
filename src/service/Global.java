package service;

/**
 * This class ensures that the global variable, menuHashMap, is up-to-date. Ad-hoc request of viewing the entire food
 * menu will directly extract the food item data from menuHashMap.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Food;

import java.util.*;

public class Global {
    private static Map<String, HashMap<String, List<Food>>> menuHashMap = new HashMap<>();

    public Global() {};

    // Synchronize the global variable, menuHashMap
    //
    // This function ensures that the global variable, menuHashMap, retains the most updated food details data
    // after DB update.
    //
    // @param dbResponse = the most updated details of the food retrieved from DB
    //
    public static void syncMenuHashMap(HashMap<String, Food> dbResponse) {
        String itemName = dbResponse.keySet().iterator().next().toString(),
                foodName = dbResponse.get(itemName).getFoodName();
        double price = dbResponse.get(itemName).getPrice();
        int remainQty = dbResponse.get(itemName).getRemainQty(), maxQty = dbResponse.get(itemName).getMaxQty();

        List<Food> newFoodDets = new ArrayList<>() {
            {add(new Food(price, remainQty, maxQty));}
        };

        if (menuHashMap.containsKey(itemName)) {
            HashMap<String, List<Food>> foodsMenuHashMap = menuHashMap.get(itemName);
            boolean isFoodExist = false;

            for (Map.Entry<String, List<Food>> e : foodsMenuHashMap.entrySet() ) {
                String menuHashMapFoodName = e.getKey();
                if (menuHashMapFoodName.equalsIgnoreCase(foodName)) {
                    isFoodExist = true;
                    e.setValue(newFoodDets);
                }
            }

            if (!isFoodExist) foodsMenuHashMap.put(foodName, newFoodDets);
        }
    }

    // Update the global variable, menuHashMap
    //
    // This function updates the global variable, menuHashMap, after initial data retrieval from MySQL server before
    // the program continues.
    //
    // @param dbResponse = the most updated details of the food retrieved from DB
    //
    public static void setMenuHashMap(Map<String, HashMap<String, List<Food>>> latestMenuItems) {
        menuHashMap = latestMenuItems;
    }

    // Retrieve the global variable, menuHashMap
    //
    // This function retrieves the global variable, menuHashMap.
    //
    // @return the latest list of food items
    //
    public static Map<String, HashMap<String, List<Food>>> getMenuHashMap() {
        return menuHashMap;
    }
}
