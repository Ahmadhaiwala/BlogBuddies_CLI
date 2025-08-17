package services;

import Model.Blog;
import Model.Comment;
import Model.User;
import cli.General;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;

import static cli.General.*;

public class BlogServices {

    public static void createBlog(int userId, String title, String content) {
        String sql = "INSERT INTO blogs (user_id, title, content) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                printColor("Blog created successfully!", GREEN);
            }

        } catch (Exception e) {
            printColor(" Error creating blog!", RED);
            e.printStackTrace();
        }
    }

    public static ArrayList<Blog> getAllBlogs() {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = " SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on FROM blogs b JOIN users u ON b.user_id = u.user_id ORDER BY b.created_on DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                blogs.add(new Blog(
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_on")
                ));
            }

        } catch (Exception e) {
            print(" Failed to load blogs.");
            e.printStackTrace();
        }

        return blogs;
    }


    public static ArrayList<Blog> getBlogsByUser(int userId) {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = " SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on FROM blogs b JOIN users u ON b.user_id = u.user_id WHERE b.user_id = ? ORDER BY b.created_on DESC ";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                blogs.add(new Blog(
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_on")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return blogs;
    }


    public static void deleteBlog(int blogId, int userId) {
        String sql = "DELETE FROM blogs WHERE blog_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blogId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                printColor(" Blog deleted successfully", GREEN);
            } else {
                printColor(" No blog deleted. Maybe it's not yours?", RED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static User getUserFromBlogId(int blogid) throws SQLException{
        String sql= " select  emailid, UserName, User_Password, fullname, bio, join_on, user_id  from users where user_id=(select user_id from blogs where blog_id=?)";
        Connection con=DBConnection.getConnection();
        PreparedStatement pm = con.prepareStatement(
                sql
        );

        pm.setInt(1, blogid);
        ResultSet rs = pm.executeQuery();

        if (rs.next()) {
            String emailid = rs.getString(1);
            String User_Name = rs.getString(2);
            String password = rs.getString(3);
            String fullName = rs.getString(4);
            String bio = rs.getString(5);
            Date date = rs.getDate(6);
            int userId = rs.getInt(7);

            return new User( userId,User_Name, password, fullName, emailid, bio, date);
        }
       return null;



    }



    public static void addComment(int blogId, int userId, String content) {
        String sql = "INSERT INTO comments (blog_id, user_id, content) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blogId);
            ps.setInt(2, userId);
            ps.setString(3, content);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                printColor(" Comment added!", CYAN);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Comment> getCommentsForBlog(int blogId) {
        ArrayList<Comment> comments = new ArrayList<>();
        String sql = """
        SELECT c.comment_id, c.blog_id, c.user_id, u.username, c.content, c.commented_on
        FROM comments c
        JOIN users u ON c.user_id = u.user_id
        WHERE c.blog_id = ?
        ORDER BY c.commented_on DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blogId);
            ResultSet rs = ps.executeQuery();

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
            e.printStackTrace();
        }

        return comments;
    }
    public static ArrayList<Blog> searchBlogs(String keyword) {
        ArrayList<Blog> blogs = new ArrayList<>();

        String sql = """
        SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on
        FROM blogs b
        JOIN users u ON b.user_id = u.user_id
        WHERE b.title LIKE ? OR b.content LIKE ? OR u.username LIKE ?
        ORDER BY b.created_on DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String likeQuery = "%" + keyword + "%";
            ps.setString(1, likeQuery);
            ps.setString(2, likeQuery);
            ps.setString(3, likeQuery);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                blogs.add(new Blog(
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_on")
                ));
            }

        } catch (Exception e) {
            print(" Failed to search blogs.");
            e.printStackTrace();
        }

        return blogs;
    }



}
