package services;

import Model.User;
import cli.UserMenu;
import db.DBConnection;
import cli.General;

import java.sql.*;
import java.util.ArrayList;

public class UserServices {
    User u;

    public static boolean authenticateUser(String username, String password, UserMenu menuRef) {


        String sql = "SELECT * FROM users WHERE username = ? AND user_password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("user_id");
                String email = rs.getString("emailid");
                String name = rs.getString("fullname");
                String bio = rs.getString("bio");
                Date joinOn = rs.getDate("join_on");


                menuRef.user = new User(userId, username, password, name, email, bio, joinOn);

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public void insertNewUser(User u) {
        String sql = "INSERT INTO users (emailid, UserName, User_Password, fullname, bio, join_on) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, u.email);
            stmt.setString(2, u.User_Name);
            stmt.setString(3, (u.Password));
            stmt.setString(4, u.fullName);
            stmt.setString(5, u.bio);
            stmt.setTimestamp(6, new java.sql.Timestamp(u.joinedOn.getTime()));


            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                General.printColor("Problem in getting sign up,please try again later",General.RED);
            } else {
                General.printColor("Thanks for using app Have a good day",General.GREEN);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUserByUsername(String username) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pm = con.prepareStatement(
                    "SELECT emailid, UserName, User_Password, fullname, bio, join_on, user_id FROM users WHERE username = ?"
            );
            pm.setString(1, username);
            ResultSet rs = pm.executeQuery();

            if (rs.next()) {
                String emailid = rs.getString(1);
                String User_Name = rs.getString(2);
                String password = rs.getString(3);
                String fullName = rs.getString(4);
                String bio = rs.getString(5);
                Date date = rs.getDate(6);
                int userId = rs.getInt(7);

                return new User( User_Name, password, fullName, emailid, bio, date);
            }
        } catch (Exception e) {
            General.printColor("Error in getUserByUsername: " + e.getMessage(), General.RED);
        }
        return null;
    }


    public static ArrayList<User> searchUser(String pattern) {
        ArrayList<User> ls = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pm = con.prepareStatement(
                    "SELECT emailid, UserName, User_Password, fullname, bio, join_on, user_id FROM users WHERE username LIKE ?"
            );
            pm.setString(1, "%" + pattern + "%");
            ResultSet rs = pm.executeQuery();

            while (rs.next()) {
                String emailid = rs.getString(1);
                String User_Name = rs.getString(2);
                String password = rs.getString(3);
                String fullName = rs.getString(4);
                String bio = rs.getString(5);
                Date date = rs.getDate(6);
                int userId = rs.getInt(7);

                User as = new User( userId,User_Name, password, fullName, emailid, bio, date);
                ls.add(as);
            }

        } catch (Exception e) {
            General.printColor("Search error: " + e.getMessage(), General.RED);
        }
        return ls; // Even if empty, never return null
    }
}
