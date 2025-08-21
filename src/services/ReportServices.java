package services;

import Model.Report;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static db.DBConnection.getConnection;

public class ReportServices {


    public static void addBlogReport(int reporterId, int reportedUserId, int blogId, String reason) {
        String sql = "INSERT INTO reports (reporter_id, reported_user_id, reported_blog_id, reason, created_on) " +
                "VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reporterId);
            stmt.setInt(2, reportedUserId);
            stmt.setInt(3, blogId);
            stmt.setString(4, reason);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int ReportCountOfBlog(int blog_id){
        String sql="select count(*) from reports where reported_blog_id=? ";
        try(Connection con= DBConnection.getConnection()){
            PreparedStatement pr= con.prepareStatement(sql);
            pr.setInt(1,blog_id);
            ResultSet rs=pr.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int ReportCountOnUser(int reported_user_id){
        String sql="select count(*) from reports where reported_user_id=? ";
        try(Connection con= DBConnection.getConnection()){
            PreparedStatement pr= con.prepareStatement(sql);
            pr.setInt(1,reported_user_id);
            ResultSet rs=pr.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int ReportCountOnComment(int reported_user_id){
        String sql="select count(*) from reports where re=? ";
        try(Connection con= DBConnection.getConnection()){
            PreparedStatement pr= con.prepareStatement(sql);
            pr.setInt(1,reported_user_id);
            ResultSet rs=pr.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updateReportStatus(int reportId, String status) {
        String sql = "UPDATE reports SET status = ? WHERE report_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reportId);

            int rows = ps.executeUpdate();
            return rows > 0; // true if update success
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getActionOnReport(int reportId) {
        String sql = "SELECT status FROM reports WHERE report_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // inside ReportServices.java

    public static int countReportsByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM reports WHERE reported_user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int countReportsByBlog(int blogId) {
        String sql = "SELECT COUNT(*) FROM reports WHERE reported_blog_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, blogId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int countReportsByComment(int commentId) {
        String sql = "SELECT COUNT(*) FROM reports WHERE reported_comment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<Report> searchReportsByReason(String keyword) {
        ArrayList<Report> results = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE LOWER(reason) LIKE LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapReport(rs)); // assume you have mapReport() method
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static ArrayList<Report> searchReportsByUser(int userId) {
        ArrayList<Report> results = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE reported_user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapReport(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static ArrayList<Report> searchReportsByBlog(int blogId) {
        ArrayList<Report> results = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE reported_blog_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, blogId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapReport(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    public static Report mapReport(ResultSet rs) throws SQLException {
        return new Report(
                rs.getInt("report_id"),
                rs.getInt("reporter_id"),
                rs.getInt("reported_user_id"),
                rs.getInt("reported_blog_id"),
                rs.getInt("reported_comment_id"),
                rs.getString("reason"),
                rs.getString("status"),
                 rs.getDate("created_on").toLocalDate()
        );
    }

    public static void addCommentReport(int reporterId, int reportedUserId, int commentId, String reason) {
        String sql = "INSERT INTO reports (reporter_id, reported_user_id, comment_id, reason, created_on) " +
                "VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reporterId);
            stmt.setInt(2, reportedUserId);
            stmt.setInt(3, commentId);
            stmt.setString(4, reason);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addUserReport(int reporterId, int reportedUserId, String reason) {
        String sql = "INSERT INTO reports (reporter_id, reported_user_id, reason, created_on) " +
                "VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reporterId);
            stmt.setInt(2, reportedUserId);
            stmt.setString(3, reason);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Report> getAllReports() {
        ArrayList<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY created_on DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Report report = new Report(
                        rs.getInt("report_id"),
                        rs.getInt("reporter_id"),
                        rs.getInt("reported_user_id"),
                        rs.getInt("reported_blog_id"),
                        rs.getInt("reported_comment_id"),
                        rs.getString("reason"),
                        rs.getDate("created_on")
                );
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }


    public static Boolean deleteReport(int reportId) {
        String sql = "DELETE FROM reports WHERE report_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reportId);
           int count = stmt.executeUpdate();
            if(count>0){
                return true;
            }else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean takeActionOnReport(int reportId, String action) {
        String fetchSql = "SELECT reported_user_id, reported_blog_id, reported_comment_id FROM reports WHERE report_id = ?";
        String updateReportSql = "UPDATE reports SET status = ? WHERE report_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement fetchStmt = conn.prepareStatement(fetchSql);
             PreparedStatement updateReportStmt = conn.prepareStatement(updateReportSql)) {


            fetchStmt.setInt(1, reportId);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                int reportedUser = rs.getInt("reported_user_id");
                int reportedBlog = rs.getInt("reported_blog_id");
                int reportedComment = rs.getInt("reported_comment_id");


                if ("delete_user".equalsIgnoreCase(action) && reportedUser > 0) {
                    String sql = "DELETE FROM users WHERE user_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, reportedUser);
                        ps.executeUpdate();
                    }
                } else if ("delete_blog".equalsIgnoreCase(action) && reportedBlog > 0) {
                    String sql = "DELETE FROM blogs WHERE blog_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, reportedBlog);
                        ps.executeUpdate();
                    }
                } else if ("delete_comment".equalsIgnoreCase(action) && reportedComment > 0) {
                    String sql = "DELETE FROM comments WHERE comment_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, reportedComment);
                        ps.executeUpdate();
                    }
                }


                updateReportStmt.setString(1, "action_taken");
                updateReportStmt.setInt(2, reportId);
                updateReportStmt.executeUpdate();

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
