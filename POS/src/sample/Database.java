package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection ConnectDb()
    {
        Connection connect;
        try {
            Class.forName("org.sqlite.JDBC");
             connect = DriverManager.getConnection("jdbc:sqlite:PointOfSale.db");
            return connect;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
