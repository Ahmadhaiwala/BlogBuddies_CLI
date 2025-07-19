package db;

import cli.General;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection(){
        try{

            Connection con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/blogdb","postgres","AhmadHaiwala004");

            return con;
        }catch(Exception e){
            General.printColor("Erorr occured while connection",General.RED);

        }
        return null;

    }
}
