package db;

import cli.General;

import java.sql.*;

public class DBConnection {
    public static Connection getConnection(){
        try{

            Connection con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/blogdb","postgres","AhmadHaiwala004");
            String url = "jdbc:postgresql://dpg-d2iqbler433s73e46360-a.oregon-postgres.render.com:5432/blogdb_vuzk?sslmode=require";
            String user = "blogdb_vuzk_user";
            String password = "JgMjGnje1L6dL9b9DqJuZX5JbeJ9S6I9";
            //Connection con = DriverManager.getConnection(url, user, password);
            return con;
        }catch(Exception e){
            General.printColor("Erorr occured while connection",General.RED);

        }
        return null;



    }
}
