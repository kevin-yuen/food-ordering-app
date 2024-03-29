package model;

/**
 * This class is responsible for interacting with the database by retrieving, creating, updating, and deleting data per
 * request from ServerController.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Food;
import service.Database;

import java.util.*;

public class MenuModel {
    private Database db;

    public MenuModel(Database db) {
        this.db = db;
    }

    /**
     * This function requests DB to retrieve the latest details of each food from foodorder.food in MySQL server
     *
     * @return the list of details of each food
     */
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

    /**
     * This function requests DB to update the price of the given food if user request is to update the food price
     * or update the max. quantity of the given food if user request is to update the max. quantity. Either
     * food price or max. quantity will be passed as argument to perform the update.
     *
     * To update max. quantity, DB will be asked to check for the current max. quantity of the food, and the update
     * will only be executed if the max. quantity request is greater than the current max. quantity.
     *
     * Upon max. quantity update completion, remaining quantity will be re-calculated based on the updated max. quantity.
     * example 1:
     * maxQty       100     ---<<updated to 200>>------->      maxQty       200
     * remainQty    100     ---<<incremented by 100>>--->      remainQty    200
     *
     * example2:
     * maxQty       100     ---<<updated to 200>>------->      maxQty       200
     * remainQty    50      ---<<incremented by 100>>--->      remainQty    150
     *
     * @param itemName          the option number of user's selected item
     * @param foodName          the food name per user input
     * @param requestedPrice    the new food price which user requests to update to
     * @param requestedMaxQty   the new max. quantity which user requests to update to
     * @return                  possible returns:
     *                            the new food price along with its other food details
     *                            the new max. quantity and updated remaining quantity along with its other food details
     *                            empty HashMap object
     */
    public HashMap<String, Food> sendDBRequestToUpdateFoodDets(String itemName, String foodName,
                                                                             Double requestedPrice,
                                                                             Integer requestedMaxQty) {
        HashMap<String, Food> dbResponse = new HashMap<>();
        Optional oPrice = Optional.ofNullable(requestedPrice), oReqMaxQty = Optional.ofNullable(requestedMaxQty);
        HashMap<String, Food> tempDBResponse = new HashMap<>();

        if (oPrice.isPresent() && !oReqMaxQty.isPresent()) {
            tempDBResponse = db.updateFoodDets(String.format("UPDATE food f " +
                            "INNER JOIN item i " +
                            "SET f.price = %f " +
                            "WHERE f.name = \"%s\" " +
                            "AND f.itemId = i.itemId " +
                            "AND i.name = \"%s\";", requestedPrice, foodName, itemName),
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
                            "WHERE i.name = \"%s\"\n" +
                            "AND f.name = \"%s\";", itemName, foodName);
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
                                    "WHERE f.name = \"%s\" " +
                                    "AND f.itemId = i.itemId " +
                                    "AND i.name = \"%s\";", requestedMaxQty, fName, iName),
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

    /**
     * This function requests DB to update the remaining quantity of the food by adding the current remaining quantity
     * to the difference between the new max. quantity and current max quantity.
     *
     * @param   itemName        the item category of the food which the max. quantity needs to be updated
     * @param   foodName        the name of the food which the max. quantity needs to be updated
     * @param   currentMaxQty   the current max. quantity of the food
     * @param   requestedMaxQty the max. quantity to be recorded for the given food name
     * @return                  the food object with the latest remaining quantity and its item category
     */
    private HashMap<String, Food> sendDBRequestToCalculateRemainQty(String itemName, String foodName, int currentMaxQty,
                                                           int requestedMaxQty) {
        HashMap<String, Food> updatedRemainQty = db.updateFoodDets(String.format("UPDATE foodorder.food f\n" +
                "INNER JOIN foodorder.item i\n" +
                "SET f.remainQty = (f.remainQty + (%d - %d))\n" +
                "WHERE i.name = \"%s\"\n" +
                "AND f.name = \"%s\"\n" +
                "AND f.itemid = i.itemid;", requestedMaxQty, currentMaxQty, itemName, foodName), foodName);

        return updatedRemainQty;
    }

    /**
     * This function requests DB to create a new food record by inserting all the mandatory details into foodorder.food.
     * Mandatory details:
     *      foodorder.name (foodName)
     *      foodorder.itemId
     *      foodorder.price
     *      foodorder.remainQty
     *      foodorder.maxQty
     *
     * @param   itemName    the name of the item category based on user's selected item category where he wants to create a new food under
     * @param   foodName    the name of the new food to be recorded in the DB
     * @param   price       the price of the new food to be recorded in the DB
     * @param   maxQty      the max. quantity of the new food to be recorded in the DB
     * @return              the details of the new food that is being created along with its item category id
     */
    public HashMap<String, Food> sendDBRequestToCreateNewFood(String itemName, String foodName, double price, int maxQty) {
        HashMap<String, Food> newFoodHashMap = new HashMap<>();
        Food newFood = db.createNewFood(String.format("INSERT INTO foodorder.food (" +
                        "name, itemId, price, remainQty, maxQty)\n" +
                        "VALUES (\"%s\", (\n" +
                        "SELECT itemId\n" +
                        "FROM foodorder.item\n" +
                        "WHERE name = \"%s\"), %f, %d, %d);", foodName, itemName, price, maxQty, maxQty), foodName);

        if (newFood.getFoodName() != null) newFoodHashMap.put(itemName, newFood);
        return newFoodHashMap;
    }
}
