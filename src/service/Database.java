package service;

import component.Food;
import general.General;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private Connection con;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public void createDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");     // driver for mysql
            this.con = DriverManager.getConnection(
                    // url = "jdbc:[db]://[ip address]:[port]/[db schema]"
                    "jdbc:mysql://localhost:3306/foodorder", "root", "Nightfall44");
        }
        catch (ClassNotFoundException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

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

                for (var col: column) {
                    if (resultSetMetaData.getColumnLabel(1).equalsIgnoreCase(col)) {
                        itemName = resultSet.getString(col);

                        if (!itemNameChked.equalsIgnoreCase(itemName)) {
                            itemNameChked = itemName;
                            foodNameAndDets = new HashMap<>();
                        }
                    }
                    else if (resultSetMetaData.getColumnLabel(2).equalsIgnoreCase(col)) {
                        foodName = resultSet.getString(col);
                    }
                    else if (resultSetMetaData.getColumnLabel(3).equalsIgnoreCase(col)) {
                        price = Double.parseDouble(resultSet.getString(col));
                    }
                    else if (resultSetMetaData.getColumnLabel(4).equalsIgnoreCase(col)) {
                        remainQty = Integer.parseInt(resultSet.getString(col));
                    }
                    else if (resultSetMetaData.getColumnLabel(5).equalsIgnoreCase(col)) {
                        maxQty = Integer.parseInt(resultSet.getString(col));
                    }
                }
                foodDets.add(new Food(price, remainQty, maxQty));
                foodNameAndDets.putIfAbsent(foodName, foodDets);
                menuItems.putIfAbsent(itemName, foodNameAndDets);
            }
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return menuItems;
    }

    public Food createNewFood(String sql, String newFoodName) {
        Food newFood = new Food();
        String query = String.format("SELECT name AS foodName, " +
                "price, " +
                "remainQty, " +
                "maxQty\n" +
                "FROM foodorder.food\n" +
                "WHERE name = '%s';", newFoodName);

        try {
            // check if the new food already exists
            Food foodRSet = this.executeReadOp(query, "foodName", "price", "remainQty", "maxQty");
            if (foodRSet.getFoodName() != null) {
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
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return newFood;
    }

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
                    "WHERE f.name = '%s';", foodName);

            Food foodRSet = this.executeReadOp(query, "itemName", "foodName", "price", "remainQty", "maxQty");
            Food foodDets = new Food(foodRSet.getFoodName(), foodRSet.getPrice(), foodRSet.getRemainQty(),
                    foodRSet.getMaxQty());
            latestFoodDets.put(foodRSet.getItemName(), foodDets);
        }
        return latestFoodDets;
    }

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
                for (var col: columns) {
                    if (resultSetMetaData.getColumnLabel(1).equalsIgnoreCase(col)) {
                        itemName = resultSet.getString(col);
                    }
                    else if (resultSetMetaData.getColumnLabel(2).equalsIgnoreCase(col)) {
                        foodName = resultSet.getString(col);
                    }
                    else if (resultSetMetaData.getColumnLabel(3).equalsIgnoreCase(col)) {
                        price = Double.parseDouble(resultSet.getString(col));
                    }
                    else if (resultSetMetaData.getColumnLabel(4).equalsIgnoreCase(col)) {
                        remainQty = Integer.parseInt(resultSet.getString(col));
                    }
                    else if (resultSetMetaData.getColumnLabel(5).equalsIgnoreCase(col)) {
                        maxQty = Integer.parseInt(resultSet.getString(col));
                    }
                }
            }
            food = !itemName.equals("") ? new Food(itemName, foodName, price, remainQty, maxQty) :
                    new Food(foodName, price, remainQty, maxQty);
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return food;
    }

    private int executeManipulateOp(String sql) {
        int rowCtn = 0;
        createDBConnection();

        try {
            this.preparedStatement = this.con.prepareStatement(sql);
            rowCtn = this.preparedStatement.executeUpdate();
            this.con.close();
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return rowCtn;
    }
}
