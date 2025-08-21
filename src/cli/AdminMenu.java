package cli;

import Model.Blog;
import Model.Comment;
import Model.User;
import Model.Report;
import Model.Notification;
import db.DBConnection;
import services.AdminServices;
import services.ReportServices;
import services.NotificationServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static cli.General.*;

public class AdminMenu {

    private Scanner sc = new Scanner(System.in);

    public void showMenu() {

        while (true) {
            printColor("\n---- ADMIN PANEL ---", GREEN);
            printColor("1. View all users", CYAN);
            printColor("2. Delete a user", CYAN);
            printColor("3. View all blogs", CYAN);
            printColor("4. Delete a blog", CYAN);
            printColor("5. View all comments", CYAN);
            printColor("6. Delete a comment", CYAN);
            printColor("7. View reports", CYAN);
            printColor("8. Review a report", CYAN);
            printColor("9. Delete a report", CYAN);
            printColor("10. Exit", CYAN);
            printColor("11. Report Analytics/Search", CYAN);

            printColor("Enter your choice:", YELLOW);
            String choice = sc.nextLine();
            try {
                switch (choice) {

                    case "1" : viewAllUsers();
                        break;
                    case "2" : deleteUser();
                        break;
                    case "3" : viewAllBlogs();
                        break;
                    case "4" : deleteBlog();
                        break;
                    case "5" : viewAllComments();
                        break;
                    case "6" : deleteComment();
                        break;
                    case "7" : viewReports();
                        break;
                    case "8" : reviewReport();
                        break;
                    case "9" : deleteReport();
                        break;
                    case "10" : {
                        printColor("Exiting Admin Panel...", GREEN);
                        return;
                    }
                    case "11" : reportAnalyticsMenu();
                        break;
                    default : printColor("Invalid choice! Try again.", RED);
                        break;
                }
            }
            catch (NumberFormatException e){}

        }
    }

    private void viewAllUsers() {
        ArrayList<User> users = AdminServices.getAllUsers();
        if (users.isEmpty()) {
            printColor("No users found.", RED);
            return;
        }
        for (User u : users) {
            printColor("ID: " + u.Userid + ", Username: " + u.User_Name +
                    ", Email: " + u.email + ", Fullname: " + u.fullName, CYAN);
        }
    }

    void deleteUser() {
        printColor("Enter user ID to delete:", YELLOW);
        int id = Integer.parseInt(sc.nextLine());
        boolean deleted = AdminServices.deleteUser(id);
        if (deleted) {
            NotificationServices.addNotification(
                    new Notification( id, "Admin", "Your account has been removed due to policy violations.")
            );
        }
        printColor(deleted ? "User deleted successfully!" : "Failed to delete user.", deleted ? GREEN : RED);
    }

    void viewAllBlogs() {
        ArrayList<Blog> blogs = AdminServices.getAllBlogs();
        if (blogs.isEmpty()) {
            printColor("No blogs found.", RED);
            return;
        }
        for (Blog b : blogs) {
            printColor("ID: " + b.blogId + ", Author: " + b.username + ", Title: " + b.title, CYAN);
        }
    }

    void deleteBlog() {
        printColor("Enter blog ID to delete(or enter -1 if don't want to delete any one):", YELLOW);
        int id = Integer.parseInt(sc.nextLine());
        if(id==-1){
            return;
        }
        boolean deleted = AdminServices.deleteBlogByAdmin(id);

        if (deleted) {
            int authorId = AdminServices.getBlogAuthor(id);
            if (authorId != -1) {
                Notification notification = new Notification(

                        authorId,
                        "Admin",
                        "Your blog post has been removed for violating community guidelines."
                );
                NotificationServices.addNotification(notification);
            }
        }

        printColor(deleted ? "Blog deleted successfully!" : "Failed to delete blog.", deleted ? GREEN : RED);
    }


    void viewAllComments() {
        ArrayList<Comment> comments = AdminServices.getAllComments();
        if (comments.isEmpty()) {
            printColor("No comments found.", RED);
            return;
        }
        for (Comment c : comments) {
            printColor("Comment ID: " + c.commentId + ", Blog ID: " + c.blogId +
                    ", User: " + c.username + ", Content: " + c.content, CYAN);
        }
    }

    void deleteComment() {
        printColor("Enter comment ID to delete:", YELLOW);
        int id = Integer.parseInt(sc.nextLine());
        boolean deleted = AdminServices.deleteComment(id);

        if (deleted) {
            int authorId = AdminServices.getCommentAuthor(id); // should return -1 if not found
            if (authorId != -1) {
                Notification notification = new Notification(

                        authorId,
                        "Admin",
                        "Your comment was removed for violating community rules."
                );
                NotificationServices.addNotification(notification);
            }
        }

        printColor(deleted ? "Comment deleted successfully!" : "Failed to delete comment.", deleted ? GREEN : RED);
    }


    void viewReports() {
        ArrayList<Report> reports = ReportServices.getAllReports();
        if (reports.isEmpty()) {
            printColor("No reports found.", RED);
            return;
        }
        for (Report r : reports) {
            printColor("Report ID: " + r.reportId +
                    ", Reporter: " + r.reporterId +
                    ", Reported User: " + r.reportedUserId +
                    ", Reported Blog: " + r.blogId +
                    ", Reported Comment: " + r.commentId +
                    ", Reason: " + r.reason +
                    ", Status: " + r.status +
                    ", Created: " + r.createdOn, CYAN);
        }
    }

    void reviewReport() {
        printColor("Enter report ID to review:", YELLOW);
        int reportId = Integer.parseInt(sc.nextLine());

        printColor("Choose action: 1. Mark Reviewed  2. Dismiss  3. Delete Blog  4. Delete Comment  5. Delete User", YELLOW);
        String action = sc.nextLine();

        boolean success = false;
        switch (action) {
            case "1" : success = ReportServices.updateReportStatus(reportId, "reviewed");
            break;
            case "2" : success = ReportServices.updateReportStatus(reportId, "dismissed");
            break;
            case "3" : {
                success = ReportServices.takeActionOnReport(reportId, "delete_blog");
                if (success) sendNotificationFromReport(reportId, "Your blog has been removed due to reports.");
            }
            break;
            case "4" : {
                success = ReportServices.takeActionOnReport(reportId, "delete_comment");
                if (success) sendNotificationFromReport(reportId, "Your comment has been removed due to reports.");
            }
            break;
            case "5" : {
                success = ReportServices.takeActionOnReport(reportId, "delete_user");
                if (success) sendNotificationFromReport(reportId, "Your account has been removed due to repeated reports.");
            }
            break;
            default : printColor("Invalid choice!", RED);
            break;
        }

        if (success) {
            printColor("Report updated successfully!", GREEN);
        } else {
            printColor("Failed to update report.", RED);
        }
    }

    void deleteReport() {
        printColor("Enter report ID to delete:", YELLOW);
        int id = Integer.parseInt(sc.nextLine());
        boolean deleted = ReportServices.deleteReport(id);
        printColor(deleted ? "Report deleted successfully!" : "Failed to delete report.", deleted ? GREEN : RED);
    }

     void sendNotificationFromReport(int reportId, String message) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT reported_user_id, reported_blog_id, reported_comment_id FROM reports WHERE report_id = ?"
             )) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("reported_user_id");
                if (userId != 0) {
                    NotificationServices.addNotification(
                            new Notification( userId, "Admin", message)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reportAnalyticsMenu() {
        while (true) {
            printColor("\n--- REPORT ANALYTICS ---", GREEN);
            printColor("1. Count reports on a user", CYAN);
            printColor("2. Count reports on a blog", CYAN);
            printColor("3. Count reports on a comment", CYAN);
            printColor("4. Search reports by keyword (e.g., violent, sensitive)", CYAN);
            printColor("5. Search reports by user ID", CYAN);
            printColor("6. Search reports by blog ID", CYAN);
            printColor("7. Back", CYAN);

            printColor("Enter your choice:", YELLOW);
            String choice = sc.nextLine();

            switch (choice) {
                case "1" : {
                    printColor("Enter user ID:", YELLOW);
                    int id = Integer.parseInt(sc.nextLine());
                    int count = ReportServices.countReportsByUser(id);
                    printColor("User " + id + " has " + count + " reports.", CYAN);
                }
                break;
                case "2" : {
                    printColor("Enter blog ID:", YELLOW);
                    int id = Integer.parseInt(sc.nextLine());
                    int count = ReportServices.countReportsByBlog(id);
                    printColor("Blog " + id + " has " + count + " reports.", CYAN);
                }
                break;
                case "3" : {
                    printColor("Enter comment ID:", YELLOW);
                    int id = Integer.parseInt(sc.nextLine());
                    int count = ReportServices.countReportsByComment(id);
                    printColor("Comment " + id + " has " + count + " reports.", CYAN);
                }
                break;
                case "4" : {
                    printColor("Enter keyword to search (e.g., violent, sensitive):", YELLOW);
                    String keyword = sc.nextLine();

                    ArrayList<Report> results = ReportServices.searchReportsByReason(keyword);

                    if (results.isEmpty()) {
                        printColor("No reports found for keyword: " + keyword, RED);
                    } else {
                        printColor("Reports matching keyword '" + keyword + "':\n", GREEN);

                        results.forEach(r -> {
                            printColor("--------------------------------------", BLUE);
                            printColor("Report ID   : " + r.reportId, CYAN);
                            printColor("Reported By : " + r.reporterId, CYAN);
                            printColor("Reported User: " + r.reportedUserId, CYAN);
                            printColor("Blog ID     : " + r.blogId, CYAN);
                            printColor("Comment ID  : " + r.commentId, CYAN);
                            printColor("Reason      : " + r.reason, CYAN);
                            printColor("Action Taken: " + r.status, CYAN);
                            printColor("--------------------------------------\n", BLUE);
                        });
                    }
                }
                break;
                case "5" : {
                    printColor("Enter user ID:", YELLOW);
                    int id = Integer.parseInt(sc.nextLine());
                    ArrayList<Report> results = ReportServices.searchReportsByUser(id);
                    results.forEach(r -> printColor(r.toString(), CYAN));
                }
                break;
                case "6" : {
                    printColor("Enter blog ID:", YELLOW);
                    int id = Integer.parseInt(sc.nextLine());
                    ArrayList<Report> results = ReportServices.searchReportsByBlog(id);
                    results.forEach(r -> printColor(r.toString(), CYAN));
                }
                break;
                case "7" : {
                    return;
                }

                default : printColor("Invalid choice!", RED);
            }
        }
    }
}
