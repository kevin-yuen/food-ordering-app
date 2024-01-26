package service;

import component.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    public ArrayList<ArrayList<String>> queryLatestItemDetails(String sql, String... column) {
        ArrayList<ArrayList<String>> itemList = new ArrayList<>();
        ArrayList<String> item;

        try {
            this.statement = this.con.createStatement();
            ResultSet rs = this.statement.executeQuery(sql);

            while (rs.next()) {
                item = new ArrayList<>();
                for (var col: column) {
                    String value = rs.getString(col);
                    item.add(value);
                }

                itemList.add(item);
            }
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return itemList;
    }

    public ArrayList<Food> updateFoodDetail(String sql, String arg, String name, String price, String remainQty,
                                                    String maxQty) {
        ArrayList<Food> updatedFood = new ArrayList<>();
        createDBConnection();

        try {
            this.preparedStatement = this.con.prepareStatement(sql);
            int rowCountUpdated = this.preparedStatement.executeUpdate();

            if (rowCountUpdated >= 1) {
                String query = String.format("SELECT name, %s, %s, %s FROM food WHERE name = '%s'", price,
                        remainQty, maxQty, arg);
                updatedFood = retrieveLatestFoodDetails(query, name, price, remainQty, maxQty);
            }

            this.con.close();
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return updatedFood;
    }

    public ArrayList<Food> createNewFood(String sql, String newFoodName, String name, String price, String remainQty,
                                         String maxQty) {
        ArrayList<Food> newFood = new ArrayList<>();
        String queryToCheckExistings = String.format("SELECT * FROM food WHERE name = '%s'", newFoodName);
        createDBConnection();

        try {
            // check if the new food already exists
            this.statement = this.con.createStatement();
            ResultSet rs = this.statement.executeQuery(queryToCheckExistings);

            if (!rs.next()) {
                this.preparedStatement = this.con.prepareStatement(sql);
                this.preparedStatement.executeUpdate();

                String queryToGetTheNewFood = String.format("SELECT name, %s, %s, %s FROM food " +
                                "ORDER BY foodID " +
                                "DESC LIMIT 1;", price, remainQty, maxQty);
                newFood = retrieveLatestFoodDetails(queryToGetTheNewFood, name, price, remainQty, maxQty);
            }

            this.con.close();
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        System.out.println("NEW FOOD: " + newFood);
        return newFood;
    }

    private ArrayList<Food> retrieveLatestFoodDetails(String sql, String name, String price, String remainQty,
                                                   String maxQty) {
        ArrayList<Food> updatedFood = new ArrayList<>();
        createDBConnection();

        try {
            this.statement = this.con.createStatement();
            ResultSet rs = this.statement.executeQuery(sql);

            while (rs.next()) {
                String foodName = rs.getString(name);
                double parsedPrice = Double.parseDouble(rs.getString(price));
                int parsedRemainQty = Integer.parseInt(rs.getString(remainQty)),
                        parsedMaxQty = Integer.parseInt(rs.getString(maxQty));

                Food updatedFoodDetail = new Food(foodName, parsedPrice, parsedRemainQty, parsedMaxQty);
                updatedFood.add(updatedFoodDetail);
            }
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return updatedFood;
    }

    // Retrieve the latest max. quantity of the food
    //
    // This function retrieves the latest max. quantity of the food to re-calculate remainQty
    public HashMap<String, ArrayList<String>> retrieveCurrentQty(String sql, String columnFoodName,
                                                                 String columnRemainQty, String columnMaxQty) {
        ArrayList<String> quantities = new ArrayList<>();
        HashMap<String, ArrayList<String>> foodWithQuantities = new HashMap<>();
        createDBConnection();

        try {
            this.statement = this.con.createStatement();
            ResultSet rs = this.statement.executeQuery(sql);

            while (rs.next()) {
                quantities.add(rs.getString(columnRemainQty));
                quantities.add(rs.getString(columnMaxQty));

                foodWithQuantities.put(rs.getString(columnFoodName), quantities);
            }
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return foodWithQuantities;
    }
}
