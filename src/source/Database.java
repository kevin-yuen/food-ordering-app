package source;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private Connection con;
    private Statement statement;

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
}
