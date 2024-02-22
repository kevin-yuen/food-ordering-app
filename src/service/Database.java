package service;

/**
 * This class 1) helps set up the connectivity between MySQL server and the system, 2) executes data retrieval and
 * manipulation operations in DB, and 3) returns DB results.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Food;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileReader;
import java.util.Properties;

public class Database {
    private String loginUsername;
    private String loginPassword;
    private Connection con;
    private Statement statement;
    private PreparedStatement preparedStatement;

    /**
     * This function sets up FileReader object and points to the "dbconfig" file and reads the db login info from the
     * file.
     */
    private void readDBLoginAcct() {
        try {
            FileReader reader = new FileReader("dbconfig");
            Properties properties = new Properties();

            properties.load(reader);

            loginUsername = properties.getProperty("username");
            loginPassword = properties.getProperty("password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function sets up connectivity between MySQL server and the system. DB login username and password are not
     * included in food-ordering-app repo as the information is sensitive.
     */
    public void createDBConnection() {
        try {
            readDBLoginAcct();
            Class.forName("com.mysql.cj.jdbc.Driver");     // driver for mysql
            this.con = DriverManager.getConnection(
                    // url = "jdbc:[db]://[ip address]:[port]/[db schema]"
                    "jdbc:mysql://localhost:3306/foodorder", loginUsername, loginPassword);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * This function retrieves the latest details of each food item stored in foodorder.food whenever it is called.
     * Since the food items are stored in ascending order based on the latest creation datetime in foodorder.food table,
     * and each food item will be stored as HashMap (e.g. {Burgers={Hamburger, [8.99, 93, 100]}}, it is mandatory to
     * check the item category name of every food record before adding to the HashMap variable.
     *
     * @param   sql     to retrieve the latest details of each food item
     * @param   column  the specified column names to look for from the DB result
     * @return          the latest details of each food item stored in foodorder.food
     */
    public Map<String, HashMap<String, List<Food>>> queryLatestMenuItemsDets(String sql, String... column) {
        Map<String, HashMap<String, List<Food>>> menuItems = new HashMap<>();
        HashMap<String, List<Food>> foodNameAndDets = new HashMap<>();
        List<Food> foodDets;

        String itemName = null, foodName= null;
        double price = 0.0d;
        int remainQty = 0, maxQty = 0;

        createDBConnection();

        try {
            this.statement = this.con.createStatement();
            ResultSet resultSet = this.statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            String itemNameChked = "";

            while (resultSet.next()) {
                foodDets = new ArrayList<>();

                for (var col : column) {
                    if (resultSetMetaData.getColumnLabel(1).equalsIgnoreCase(col)) {
                        itemName = resultSet.getString(col);

                        if (!itemNameChked.equalsIgnoreCase(itemName)) {
                            itemNameChked = itemName;
                            foodNameAndDets = new HashMap<>();
                        }
                    } else if (resultSetMetaData.getColumnLabel(2).equalsIgnoreCase(col)) {
                        foodName = resultSet.getString(col);
                    } else if (resultSetMetaData.getColumnLabel(3).equalsIgnoreCase(col)) {
                        price = Double.parseDouble(resultSet.getString(col));
                    } else if (resultSetMetaData.getColumnLabel(4).equalsIgnoreCase(col)) {
                        remainQty = Integer.parseInt(resultSet.getString(col));
                    } else if (resultSetMetaData.getColumnLabel(5).equalsIgnoreCase(col)) {
                        maxQty = Integer.parseInt(resultSet.getString(col));
                    }
                }
                foodDets.add(new Food(price, remainQty, maxQty));
                foodNameAndDets.putIfAbsent(foodName, foodDets);
                menuItems.putIfAbsent(itemName, foodNameAndDets);
            }
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return menuItems;
    }

    /**
     * This function creates a new food item if the food does not already exist in DB. The food's name, price, remaining
     * quantity, max. quantity, and corresponding item ID will be created upon the update execution.
     *
     * An empty food object will be returned if the food is already exist under the specified item category in DB.
     *
     * @param  sql          insert a new food item which includes name, price, remaining quantity, max. quantity, and item id
     * @param  newFoodName  the new food name based on user's input
     * @return              possible returns:
     *                          the details of the new food item that is being created
     *                          empty food object
     */
    public Food createNewFood(String sql, String newFoodName) {
        Food newFood = new Food();
        String query = String.format("SELECT name AS foodName, " +
                "price, " +
                "remainQty, " +
                "maxQty\n" +
                "FROM foodorder.food\n" +
                "WHERE name = \"%s\";", newFoodName);

        try {
            // check if the new food already exists
            Food foodRSet = this.executeReadOp(query, "foodName", "price", "remainQty", "maxQty");
            if (foodRSet.getFoodName() == "") {
                this.executeManipulateOp(sql);

                String queryGetNewFood = String.format("SELECT i.name AS itemName,\n" +
                        "f.name AS foodName,\n" +
                        "f.price,\n" +
                        "f.remainQty,\n" +
                        "f.maxQty\n" +
                        "FROM foodorder.food f\n" +
                        "INNER JOIN foodorder.item i\n" +
                        "WHERE f.itemId = i.itemId\n" +
                        "ORDER BY foodId DESC\n" +
                        "LIMIT 1;");

                newFood = this.executeReadOp(queryGetNewFood, "itemName", "foodName", "price", "remainQty", "maxQty");
            }
            this.con.close();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return newFood;
    }

    /**
     * This function updates details of the given food name in DB and retrieves the most updated details of the food
     * from DB after DB update.
     * Possible field value updates:    foodorder.food.price
     *                                  foodorder.food.remainQty
     *                                  foodorder.food.maxQty
     *
     * @param   sql         update food price of the specific food under the specific item category
     * @param   foodName    the given food name which the food price needs to be updated
     * @return              the most updated food details along with the name of its item category
     */
    public HashMap<String, Food> updateFoodDets(String sql, String foodName) {
        HashMap<String, Food> latestFoodDets = new HashMap<>();
        int rowCountUpdated = executeManipulateOp(sql);

        if (rowCountUpdated >= 1) {
            String query = String.format("SELECT i.name AS itemName, " +
                    "f.name AS foodName, " +
                    "f.price, " +
                    "f.remainQty, " +
                    "f.maxQty\n" +
                    "FROM foodorder.food f\n" +
                    "INNER JOIN foodorder.item i\n" +
                    "ON f.itemId = i.itemId\n" +
                    "WHERE f.name = \"%s\";", foodName);

            Food foodRSet = this.executeReadOp(query, "itemName", "foodName", "price", "remainQty", "maxQty");
            Food foodDets = new Food(foodRSet.getFoodName(), foodRSet.getPrice(), foodRSet.getRemainQty(),
                    foodRSet.getMaxQty());
            latestFoodDets.put(foodRSet.getItemName(), foodDets);
        }
        return latestFoodDets;
    }

    /**
     * This function retrieves the latest food details of a particular food from DB based on the given food name and
     * if the food name is found under the specified item category, a new Food object will be created along with its
     * item name based on the latest details from DB; otherwise, a new Food object will be created without item name.
     *
     * @param   sql         look up a specific food record from foodorder.food
     * @param   columns     foodorder.item.name
     *                      foodorder.food.name
     *                      foodorder.food.price
     *                      foodorder.food.remainQty
     *                      foodorder.food.maxQty
     * @return              possible returns:
     *                          food object with its item name
     *                          food object without item name
     *                          empty food object
     */
    public Food executeReadOp(String sql, String... columns) {
        Food food = new Food();
        createDBConnection();

        try {
            String itemName = "", foodName = "";
            double price = 0.0d;
            int remainQty = 0, maxQty = 0;
            ResultSet resultSet;

            this.statement = this.con.createStatement();
            resultSet = this.statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            while (resultSet.next()) {
                for (var col : columns) {
                    if (resultSetMetaData.getColumnLabel(1).equalsIgnoreCase(col)) {
                        itemName = resultSet.getString(col);
                    } else if (resultSetMetaData.getColumnLabel(2).equalsIgnoreCase(col)) {
                        foodName = resultSet.getString(col);
                    } else if (resultSetMetaData.getColumnLabel(3).equalsIgnoreCase(col)) {
                        price = Double.parseDouble(resultSet.getString(col));
                    } else if (resultSetMetaData.getColumnLabel(4).equalsIgnoreCase(col)) {
                        remainQty = Integer.parseInt(resultSet.getString(col));
                    } else if (resultSetMetaData.getColumnLabel(5).equalsIgnoreCase(col)) {
                        maxQty = Integer.parseInt(resultSet.getString(col));
                    }
                }
            }
            food = !itemName.equals("") ? new Food(itemName, foodName, price, remainQty, maxQty) :
                    new Food(foodName, price, remainQty, maxQty);
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return food;
    }

    /**
     * This function executes update operation on food detail(s) in DB.
     * Possible field value updates:    foodorder.food.price
     *                                  foodorder.food.remainQty
     *                                  foodorder.food.maxQty
     *
     * @param   sql     update food price of the specific food under the specific item category
     * @return          count of updated rows
     */
    private int executeManipulateOp(String sql) {
        int rowCtn = 0;
        createDBConnection();

        try {
            this.preparedStatement = this.con.prepareStatement(sql);
            rowCtn = this.preparedStatement.executeUpdate();
            this.con.close();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return rowCtn;
    }
}
