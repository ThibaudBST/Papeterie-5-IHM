package fr.eni.papeterie.dal.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.eni.papeterie.dal.Settings;

public class JdbcTools {

    private static String urldb;
    private static String userdb;
    private static String passworddb;

    static {

        try {
            Class.forName(Settings.getProperty("driverjdbc"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("lul");
        }
        urldb = Settings.getProperty("url");
        userdb = Settings.getProperty("user");
        passworddb = Settings.getProperty("password");
        System.out.println("urldb="+urldb+";userdb="+userdb+";passworddb="+passworddb);
    }



    public static Connection getConnection() throws SQLException{
        //Connection connection = DriverManager.getConnection(urldb);

        return DriverManager.getConnection(urldb, userdb, passworddb);
    }



}


