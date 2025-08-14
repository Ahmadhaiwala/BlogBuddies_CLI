package services;

import db.DBConnection;
import Model.Notification;

import java.sql.*;
import java.util.ArrayList;

public class NotificationServices {

    public static void addNotification(Notification n) {
        String sql = "INSERT INTO notifications (user_id, from_user, message) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, n.userId);
            stmt.setString(2, n.fromUser);
            stmt.setString(3, n.message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Notification> getNotificationsForUser(int userId) {
        ArrayList<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_on DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("from_user"),
                        rs.getString("message"),
                        rs.getTimestamp("created_on"),
                        rs.getBoolean("seen")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void markAllAsSeen(int userId) {
        String sql = "UPDATE notifications SET seen = TRUE WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
