package model;

import component.Food;

import java.sql.*;
import java.util.ArrayList;
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
                updatedFood = fetchRowUpdate(query, name, price, remainQty, maxQty);
            }

            this.con.close();
        }
        catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return updatedFood;
    }

    private ArrayList<Food> fetchRowUpdate(String sql, String name, String price, String remainQty,
                                                   String maxQty) {
        ArrayList<Food> updatedFood = new ArrayList<>();

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
}
