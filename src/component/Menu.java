package component;

import source.Database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu {
    private HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap = new HashMap<>();

    public Menu() {}

    private ArrayList<ArrayList<String>> getLatestMenuItemsFromDB(Database dbConnector) {
        ArrayList<ArrayList<String>> latestMenuItemsFromDB = dbConnector.queryLatestItemDetails(
                "select itemType.name as itemName,\n" +
                        "foodItem.name as foodName,\n" +
                        "foodItem.price as price,\n" +
                        "foodItem.remainQty as remainQty,\n" +
                        "foodItem.maxQty as maxQty,\n" +
                        "foodItem.isInStock as isInStock\n" +
                        "from foodorder.food foodItem\n" +
                        "inner join foodorder.item itemType\n" +
                        "on foodItem.itemid = itemType.itemid;",
                "itemName", "foodName", "price", "remainQty", "maxQty", "isInStock");

        return latestMenuItemsFromDB;
    }

    public void composeLatestMenu(Database db) {
        ArrayList<ArrayList<String>> latestItemsToShowOnMenu = getLatestMenuItemsFromDB(db);
//        System.out.println("LATEST ITEM TO SHOW ON MENU " + latestItemsToShowOnMenu);
//
//        for (var ele: latestItemsToShowOnMenu) {
//            System.out.println(ele);
//            System.out.println(ele.get(1));
//        }

        ArrayList<ArrayList<Food>> foodByType;
        ArrayList<Food> food;

        for (var element: latestItemsToShowOnMenu) {
            food = new ArrayList<>();
            foodByType = new ArrayList<>();

            // compose a single food
            String foodName = element.get(1);
            double price = Double.parseDouble(element.get(2));
            int remainQty = Integer.parseInt(element.get(3)), maxQty = Integer.parseInt(element.get(4));
            boolean isInStock = Boolean.parseBoolean(element.get(5));

            Food singleFood = new Food(foodName, price, remainQty, maxQty, isInStock);
            food.add(singleFood);

            if (!foodHashMap.containsKey(element.get(0))) {
                foodByType.add(food);
                foodHashMap.put(element.get(0), foodByType);
            }
            else {
                ArrayList<ArrayList<Food>> existingFoodTypeList = foodHashMap.get(element.get(0));
                existingFoodTypeList.add(food);
            }
        }

        //System.out.println("food hash map: " + foodHashMap);
    }

    public HashMap<String, ArrayList<ArrayList<Food>>> getFoodHashMap() {
        return foodHashMap;
    }

    // Get total count of items on menu to draw out the menu
    public int countItemOnMenu() {
        AtomicInteger totalItems = new AtomicInteger();

        foodHashMap.forEach((key, value) -> {
            totalItems.addAndGet(value.size() + 1);     // add 1 to include the key in each key-value pair
        });
        return totalItems.intValue();
    }
}