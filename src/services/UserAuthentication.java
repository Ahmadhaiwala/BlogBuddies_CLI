package services;

import cli.General;
import db.DBConnection;

import java.awt.geom.RectangularShape;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthentication {
    String Username;
    String password;
    public static Boolean authenticateUser(String pass,String user)  {
        try{
            Connection con= DBConnection.getConnection();
            PreparedStatement pm= con.prepareStatement("select username,user_password from users where username=? and user_password=?");
            pm.setString(1,pass);
            pm.setString(2,user);

            ResultSet a=pm.executeQuery();
            return a.next();
        } catch (Exception e) {
            System.out.println(e);
        }


        return  false;


    }
}
