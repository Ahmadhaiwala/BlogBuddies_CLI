package services;

import Model.Notification;
import Model.User;
import db.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static cli.General.*;

public class SocialServices {

    public static ArrayList<User> getFriends(int userId) {
        ArrayList<User> friendsList = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                print("Error occurred while getting DB connection");
                return friendsList;
            }

            String sql ="SELECT users.emailid, users.UserName, users.User_Password, users.fullname, users.bio, users.join_on, users.user_id\n" +
                    "FROM friends\n" +
                    "JOIN users ON users.user_id = friends.friend_id\n" +
                    "WHERE friends.user_id = ?;\n";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String emailid = rs.getString("emailid");
                String User_Name = rs.getString("UserName");
                String password = rs.getString("User_Password");
                String fullName = rs.getString("fullname");
                String bio = rs.getString("bio");
                Date date = rs.getDate("join_on");
                int uid = rs.getInt("user_id");

                User friend = new User(uid, User_Name, password, fullName, emailid, bio, date);
                friendsList.add(friend);
            }

        } catch (Exception e) {
            print(" Error fetching friends: " + e.getMessage());
        }

        return friendsList;
    }
    public ArrayList<User> getReqDetail(User user) {


        ArrayList<User> friendRequests = new ArrayList<>();

        String sql = "\n" +
                "SELECT emailid, UserName, User_Password, fullname, bio, join_on, user_id FROM users WHERE user_id IN ( SELECT sender_id FROM friend_requests WHERE receiver_id = ?);";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
             stmt.setInt(1,user.Userid);


            ResultSet a = stmt.executeQuery();

            while (a.next()) {


                String emailid = a.getString(1);
                String User_Name = a.getString(2);
                String password = a.getString(3);
                String fullName = a.getString(4);
                String bio = a.getString(5);
                Date date = a.getDate(6);
                int userId = a.getInt(7);

                User as = new User( userId, User_Name, password, fullName, emailid, bio, date);
                System.out.println(userId);

                friendRequests.add(as);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Or log it if you're feeling clean
        }

        return friendRequests;
    }
   public void sendFriendReq(int myid,int userid,User user){
       Notification n = new Notification(userid, user.User_Name, "sent you a friend request");
       NotificationServices.addNotification(n);

       String sql = "INSERT INTO friend_requests (sender_id, receiver_id) VALUES (?, ?)";

       try (Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {

           stmt.setInt(1, myid);
           stmt.setInt(2, userid);

           int rows = stmt.executeUpdate();

           if (rows > 0) {
               printColor("Friend req sent successfully",GREEN);
           } else {
               printColor("Friend req failed",RED);
           }

       } catch (Exception e) {
           printColor(" Error in services. Please try again later.", RED);
           e.printStackTrace(); // Log error for debugging
       }
   }
    public boolean hasPendingFriendRequest(int senderId, int receiverId) {
        String sql = "SELECT COUNT(*) FROM friend_requests WHERE sender_id = ? AND receiver_id = ? AND status = 'pending'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            printColor(" Error checking existing friend request", RED);
            e.printStackTrace();
        }

        return false;
    }
    public boolean areFriends(int userId1, int userId2) {
        String sql = "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            printColor("⚠️ Error checking friendship", RED);
            e.printStackTrace();
        }

        return false;
    }
    public boolean undoFriendRequest(int senderId, int receiverId) {
        String sql = "DELETE FROM friend_requests WHERE sender_id = ? AND receiver_id = ? AND status = 'pending'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            printColor(" Error undoing friend request", RED);
            e.printStackTrace();
            return false;
        }
    }
    public boolean acceptFriendRequest(int senderId, int receiverId) {
        String updateStatusSQL = "UPDATE friend_requests SET status = 'accepted' WHERE sender_id = ? AND receiver_id = ?";
        String insertFriendSQL = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?), (?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // Start transaction

            try (PreparedStatement updateStmt = con.prepareStatement(updateStatusSQL);
                 PreparedStatement insertStmt = con.prepareStatement(insertFriendSQL)) {

                updateStmt.setInt(1, senderId);
                updateStmt.setInt(2, receiverId);
                int updated = updateStmt.executeUpdate();

                if (updated > 0) {
                    insertStmt.setInt(1, receiverId);
                    insertStmt.setInt(2, senderId);
                    insertStmt.setInt(3, senderId);
                    insertStmt.setInt(4, receiverId);
                    insertStmt.executeUpdate();

                    con.commit();
                    return true;
                }
                con.rollback();
            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean rejectFriendRequest(int senderId, int receiverId) {
        String sql = "UPDATE friend_requests SET status = 'rejected' WHERE sender_id = ? AND receiver_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean removeFriend(int userId1, int userId2) {
        String sql = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            stmt.setInt(3, userId2);
            stmt.setInt(4, userId1);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }






}

