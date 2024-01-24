package service;

import component.Food;
import controller.ServerController;

import java.util.ArrayList;
import java.util.HashMap;

public class Global {
    private static HashMap<String, ArrayList<ArrayList<Food>>> menuHashMap = new HashMap<>();

    public Global() {};

    // Synchronize the global variable, menuHashMap
    //
    // This function ensures that the global variable, menuHashMap, retains the most updated food details data
    // after DB update.
    //
    // @param dbResponse = the most updated details of the food retrieved from DB
    //
    public static void syncMenuHashMap(HashMap<String, ArrayList<Food>> dbResponse) {
        String itemName = dbResponse.keySet().iterator().next().toString();
        Food newFoodDetail = dbResponse.get(itemName).get(0);

        if (menuHashMap.containsKey(itemName)) {
            ArrayList<ArrayList<Food>> currentFoods = menuHashMap.get(itemName);

            for (var currentFoodDetail: currentFoods) {
                String foodName = currentFoodDetail.get(0).getFoodName();

                if (foodName.equalsIgnoreCase(newFoodDetail.getFoodName())) {
                    currentFoodDetail.set(0, newFoodDetail);
                }
            }
        }
        //System.out.println("UPDATED MENU HASH MAP: " + menuHashMap);
    }

    public static void setMenuHashMap(HashMap<String, ArrayList<ArrayList<Food>>> latestMenuItems) {
        menuHashMap = latestMenuItems;
    }

    public static HashMap<String, ArrayList<ArrayList<Food>>> getMenuHashMap() {
        return menuHashMap;
    }
}
