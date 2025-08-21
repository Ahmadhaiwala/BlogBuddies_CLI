package services;

import Model.Blog;
import Model.Comment;
import Model.User;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;

import static cli.General.*;

public class AdminServices {

    public static boolean authenticateAdmin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            printColor("Error during admin login: " + e.getMessage(), RED);
            return false;
        }
    }
    public static int getBlogAuthor(int blogId) {
        String sql = "SELECT user_id FROM blogs WHERE blog_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, blogId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // not found
    }

    public static int getCommentAuthor(int commentId) {
        String sql = "SELECT user_id FROM comments WHERE comment_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, commentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // not found
    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("UserName"),
                        rs.getString("User_Password"),
                        rs.getString("fullname"),
                        rs.getString("emailid"),
                        rs.getString("bio"),
                        rs.getDate("join_on")
                ));
            }
        } catch (Exception e) {
            printColor("Error fetching users: " + e.getMessage(), RED);
        }
        return users;
    }


    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            printColor("Error deleting user: " + e.getMessage(), RED);
            return false;
        }
    }


    public static ArrayList<Blog> getAllBlogs() {
        return BlogServices.getAllBlogs(); // Already exists in BlogServices
    }


    public static boolean deleteBlogByAdmin(int blogId) {
        String sql = "DELETE FROM blogs WHERE blog_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blogId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            printColor("Error deleting blog: " + e.getMessage(), RED);
            return false;
        }
    }


    public static ArrayList<Comment> getAllComments() {
        ArrayList<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.comment_id, c.blog_id, c.user_id, u.username, c.content, c.commented_on " +
                "FROM comments c JOIN users u ON c.user_id = u.user_id ORDER BY c.commented_on DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                comments.add(new Comment(
                        rs.getInt("comment_id"),
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("content"),
                        rs.getDate("commented_on")
                ));
            }
        } catch (Exception e) {
            printColor("Error fetching comments: " + e.getMessage(), RED);
        }
        return comments;
    }


    public static boolean deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            printColor("Error deleting comment: " + e.getMessage(), RED);
            return false;
        }
    }

}
