package model;

import component.Food;

import java.util.ArrayList;
import java.util.HashMap;

public class Global {
    private static HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap = new HashMap<>();

    public Global() {};

    public static void syncGlobalVariable(String itemName, HashMap<String, String> updatedRecord) {
        if (foodHashMap.containsKey(itemName)) {

        }
    }

    public static void setFoodHashMap() {

    }

    public static HashMap<String, ArrayList<ArrayList<Food>>> getFoodHashMap() {
        return foodHashMap;
    }
}
