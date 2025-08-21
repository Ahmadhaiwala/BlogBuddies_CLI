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


    public static void createBlog(int userId, String title, String content, String visibility) {
        String sql = """
            INSERT INTO blogs (user_id, title, content, visibility)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.setString(4, visibility);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                printColor(" Blog created successfully! (" + visibility + ")", GREEN);
            }

        } catch (Exception e) {
            printColor(" Error creating blog!", RED);
            e.printStackTrace();
        }
    }


    public static ArrayList<Blog> getAllPublicBlogs() {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = """
            SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on, b.visibility
            FROM blogs b
            JOIN users u ON b.user_id = u.user_id
            WHERE b.visibility = 'public'
            ORDER BY b.created_on DESC
        """;

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
                        rs.getDate("created_on"),
                        rs.getString("visibility")
                ));
            }

        } catch (Exception e) {
            print(" Failed to load public blogs.");
            e.printStackTrace();
        }

        return blogs;
    }

    public static ArrayList<Blog> getMyBlogs() throws SQLException {
        ArrayList<Blog> blog= new ArrayList<>();
        String sql="select blog_id,user_id,username,title,content,created_on,visisblity from blog where user_id=?";
        Connection con=DBConnection.getConnection();
        PreparedStatement pre= con.prepareStatement(sql);
        ResultSet rs=pre.executeQuery();
        while(rs.next()){
           blog.add(new Blog(
                    rs.getInt("blog_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getDate("created_on"),
                    rs.getString("visibility")))
        ;}
        return blog;

    }
    public static ArrayList<Blog> getAllBlogs() {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = " SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on, b.visibility\n" +
                "            FROM blogs b\n" +
                "            JOIN users u ON b.user_id = u.user_id\n" +
                "            WHERE b.visibility = 'public'\n" +
                "            ORDER BY b.created_on DESC";

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
                        rs.getDate("created_on"),
                        rs.getString("visibility")
                ));
            }

        } catch (Exception e) {
            print(" Failed to load blogs.");
            e.printStackTrace();
        }

        return blogs;
    }


    public static ArrayList<Blog> getBlogsByUser(int currentUserId, int userId) {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = """
            SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on, b.visibility
            FROM blogs b
            JOIN users u ON b.user_id = u.user_id
            WHERE b.user_id = ?
              AND (b.visibility = 'public' OR b.user_id = ?)
            ORDER BY b.created_on DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, currentUserId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                blogs.add(new Blog(
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_on"),
                        rs.getString("visibility")
                ));
            }

        } catch (Exception e) {
            print(" Failed to load user's blogs.");
            e.printStackTrace();
        }

        return blogs;
    }


    public static ArrayList<Blog> getPrivateBlogsForUser(int currentUserId) {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = """
            SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on, b.visibility
            FROM blogs b
            JOIN users u ON b.user_id = u.user_id
            JOIN friends f ON (
                (f.user_id = b.user_id AND f.friend_id = ?)
                OR (f.friend_id = b.user_id AND f.user_id = ?)
            )
            WHERE b.visibility = 'private'
            ORDER BY b.created_on DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, currentUserId);
            ps.setInt(2, currentUserId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                blogs.add(new Blog(
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_on"),
                        rs.getString("visibility")
                ));
            }

        } catch (Exception e) {
            print(" Failed to load private blogs.");
            e.printStackTrace();
        }

        return blogs;
    }


    public static Boolean deleteBlog(int blogId, int userId) {
        String sql = "DELETE FROM blogs WHERE blog_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blogId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
               return true;
            } else {
               return false;
            }

        } catch (Exception e) {
            print(" Error deleting blog.");
            e.printStackTrace();
        }
        return false;
    }


    public static User getUserFromBlogId(int blogid) throws SQLException {
        String sql = """
            SELECT emailid, username, user_password, fullname, bio, join_on, user_id
            FROM users
            WHERE user_id = (SELECT user_id FROM blogs WHERE blog_id = ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blogid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("user_password"),
                        rs.getString("fullname"),
                        rs.getString("emailid"),
                        rs.getString("bio"),
                        rs.getDate("join_on")
                );
            }
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
                printColor("💬 Comment added!", CYAN);
            }

        } catch (Exception e) {
            print("Error adding comment.");
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
            print("Failed to load comments.");
            e.printStackTrace();
        }

        return comments;
    }


    public static ArrayList<Blog> getVisibleBlogs(int currentUserId) {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = """
            SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on, b.visibility
            FROM blogs b
            JOIN users u ON b.user_id = u.user_id
            WHERE b.visibility = 'public'
               OR b.user_id = ?
               OR (
                    b.visibility = 'private'
                    AND b.user_id IN (
                        SELECT f.friend_id FROM friends f WHERE f.user_id = ?
                    )
               )
            ORDER BY b.created_on DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, currentUserId);
            ps.setInt(2, currentUserId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                blogs.add(new Blog(
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_on"),
                        rs.getString("visibility")
                ));
            }

        } catch (Exception e) {
            print(" Failed to load visible blogs.");
            e.printStackTrace();
        }

        return blogs;
    }

    // ------------------ SEARCH ------------------
    public static ArrayList<Blog> searchBlogs(int userId, String keyword) {
        ArrayList<Blog> blogs = new ArrayList<>();
        String sql = """
            SELECT b.blog_id, b.user_id, u.username, b.title, b.content, b.created_on, b.visibility
            FROM blogs b
            JOIN users u ON b.user_id = u.user_id
            WHERE 
                (
                    b.visibility = 'public'
                    OR b.user_id = ?
                    OR (b.visibility = 'private' AND b.user_id IN (
                            SELECT f.friend_id FROM friends f WHERE f.user_id = ?
                    ))
                )
                AND (u.username LIKE ? OR b.title LIKE ? OR b.content LIKE ?)
            ORDER BY b.created_on DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String likeQuery = "%" + keyword + "%";

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setString(3, likeQuery);
            ps.setString(4, likeQuery);
            ps.setString(5, likeQuery);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                blogs.add(new Blog(
                        rs.getInt("blog_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_on"),
                        rs.getString("visibility")
                ));
            }

        } catch (Exception e) {
            print(" Failed to search blogs.");
            e.printStackTrace();
        }

        return blogs;
    }
}
