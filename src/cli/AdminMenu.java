package cli;

import Model.Blog;
import Model.Comment;
import Model.User;
import services.AdminServices;

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
            printColor("7. Exit", CYAN);

            printColor("Enter your choice:", YELLOW);
            String choice = sc.nextLine();

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
                case "7" : {
                    printColor("Exiting Admin Panel...", GREEN);
                    return;
                }

                default :printColor("Invalid choice! Try again.", RED);
                break;
            }
        }
    }

    private void viewAllUsers() {
        ArrayList<User> users = AdminServices.getAllUsers();
        if (users.isEmpty()) {
            printColor("No users found.", RED);
            return;
        }
        for (User u : users) {
            printColor("ID: " + u.Userid + ", Username: " + u.User_Name + ", Email: " + u.email + ", Fullname: " + u.fullName, CYAN);
        }
    }

    private void deleteUser() {
        printColor("Enter user ID to delete:", YELLOW);
        int id = Integer.parseInt(sc.nextLine());
        boolean deleted = AdminServices.deleteUser(id);
        printColor(deleted ? "User deleted successfully!" : "Failed to delete user.", deleted ? GREEN : RED);
    }

    private void viewAllBlogs() {
        ArrayList<Blog> blogs = AdminServices.getAllBlogs();
        if (blogs.isEmpty()) {
            printColor("No blogs found.", RED);
            return;
        }
        for (Blog b : blogs) {
            printColor("ID: " + b.blogId + ", Author: " + b.username + ", Title: " + b.title, CYAN);
        }
    }

    private void deleteBlog() {
        printColor("Enter blog ID to delete:", YELLOW);
        int id = Integer.parseInt(sc.nextLine());
        boolean deleted = AdminServices.deleteBlogByAdmin(id);
        printColor(deleted ? "Blog deleted successfully!" : "Failed to delete blog.", deleted ? GREEN : RED);
    }

    private void viewAllComments() {
        ArrayList<Comment> comments = AdminServices.getAllComments();
        if (comments.isEmpty()) {
            printColor("No comments found.", RED);
            return;
        }
        for (Comment c : comments) {
            printColor("Comment ID: " + c.commentId + ", Blog ID: " + c.blogId + ", User: " + c.username + ", Content: " + c.content, CYAN);
        }
    }

    private void deleteComment() {
        printColor("Enter comment ID to delete:", YELLOW);
        int id = Integer.parseInt(sc.nextLine());
        boolean deleted = AdminServices.deleteComment(id);
        printColor(deleted ? "Comment deleted successfully!" : "Failed to delete comment.", deleted ? GREEN : RED);
    }
}
