package cli;

import java.util.ArrayList;
import java.sql.SQLException;
import java.util.*;

import Model.Blog;
import Model.Comment;
import Model.Notification;
import Model.User;

import services.BlogServices;
import services.NotificationServices;
import services.SocialServices;

import DS.BlogStack;
import DS.BlogLinkedList;

import static cli.General.*;
import static services.BlogServices.getUserFromBlogId;
import static services.NotificationServices.*;

public class BlogMenu {

    Scanner sc = new Scanner(System.in);
    User currentUser;

    BlogStack blogHistory = new BlogStack();
    BlogLinkedList recentSearchResults = new BlogLinkedList();
    HashMap<Integer, String> userCache = new HashMap<>();

    public BlogMenu(User user) {
        this.currentUser = user;
    }

    public void showMenu() {
        do {
            printSection(" Blog Menu", CYAN);
            printColor("1. Create New Blog", YELLOW);
            printColor("2. View All Blogs (Public + Friends Private)", GREEN);
            printColor("3. View My Blogs", BLUE);
            printColor("4. Delete My Blog", PURPLE);
            printColor("5. Search Blogs", YELLOW);
            printColor("6. View Last Blog", CYAN);
            printColor("7. Back to Main Menu", RED);

            print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            try {
                switch (choice) {
                    case 1 -> createBlog();
                    case 2 -> viewAllBlogs();
                    case 3 -> viewMyBlogs();
                    case 4 -> deleteBlog();
                    case 5 -> searchBlog();
                    case 6 -> viewLastViewedBlog();
                    case 7 -> {
                        printColor(" Going back to main menu...", PURPLE);
                        return;
                    }
                    default -> printColor(" Invalid choice. Try again.", RED);
                }
            } catch (Exception e) {
                printColor(" Invalid input! Please enter a number.", RED);
            }

        } while (true);
    }

    // View last viewed blog
    void viewLastViewedBlog() throws SQLException {
        if (blogHistory.isEmpty()) {
            printColor("No blogs in history yet!", RED);
        } else {
            Blog last = blogHistory.pop();
            showBlogDetail(last);
        }
    }

    // Search blogs
    void searchBlog() {
        printColor(" Enter keyword to search in blogs:", BLUE);
        String keyword = sc.nextLine();

        recentSearchResults.clear();
        recentSearchResults.addAll(BlogServices.searchBlogs(currentUser.Userid,keyword));

        if (recentSearchResults.isEmpty()) {
            printColor(" No blogs matched your search.", RED);
            return;
        }

        int i = 0;
        ArrayList<Blog> rec = recentSearchResults.getList();
        for (Blog blog : rec) {
            printColor(i + ". " + blog.title + " (by @" + blog.username + ")", GREEN);
            i++;
        }

        printColor(i + ". Back", RED);
        print("Choose blog number to view details or back: ");

        try {
            int choice = Integer.parseInt(sc.nextLine());

            if (choice == i) return;

            if (choice >= 0 && choice < recentSearchResults.size()) {
                Blog selected = recentSearchResults.get(choice);
                blogHistory.push(selected);
                showBlogDetail(selected);
            } else {
                printColor(" Invalid blog number.", RED);
            }

        } catch (Exception e) {
            printColor(" Please enter a valid number!", RED);
        }
    }


    void createBlog() {
        printColor(" Enter blog title:", BLUE);
        String title = sc.nextLine().trim();
        while (title.isEmpty()) {
            printColor(" Title cannot be empty!", RED);
            title = sc.nextLine().trim();
        }

        printColor(" Enter blog content:", BLUE);
        String content = sc.nextLine().trim();
        while (content.isEmpty()) {
            printColor(" Content cannot be empty!", RED);
            content = sc.nextLine().trim();
        }

        printColor(" Select blog visibility (1 = Public, 2 = Private):", YELLOW);
        String visibility = "public";
        String choice = sc.nextLine();
        if (choice.equals("2")) {
            visibility = "private";
        }

        BlogServices.createBlog(currentUser.Userid, title, content, visibility);

        // Notify friends
        SocialServices s = new SocialServices();
        ArrayList<User> friends = s.getFriends(currentUser.Userid);
        for (User friend : friends) {
            String f = currentUser.User_Name + " just uploaded a new blog";
            Notification nf = new Notification(friend.Userid, currentUser.User_Name, f);
            addNotification(nf);
        }
    }

    // View all blogs (public + private of friends)
    void viewAllBlogs() throws SQLException {
        ArrayList<Blog> blogs = BlogServices.getVisibleBlogs(currentUser.Userid);

        if (blogs.isEmpty()) {
            printColor(" No blogs found.", RED);
            return;
        }

        while (true) {
            printColor(" All Blogs:", CYAN);
            int i = 0;
            for (Blog blog : blogs) {
                printColor(i + ". " + blog.title + " (by @" + blog.username + ") [" + blog.visibility + "]", GREEN);
                userCache.put(blog.userId, blog.username);
                i++;
            }
            printColor(i + ". Back", RED);

            print("Choose blog  to view details or back: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                if (choice == i) return;

                if (choice >= 0 && choice < blogs.size()) {
                    Blog selected = blogs.get(choice);
                    blogHistory.push(selected);
                    showBlogDetail(selected);
                } else {
                    printColor(" Invalid blog number.", RED);
                }

            } catch (Exception e) {
                printColor(" Please enter a valid number!", RED);
            }
        }
    }

    // Show blog details
    void showBlogDetail(Blog blog) throws SQLException {
        printColor("\n Blog Detail:", CYAN);
        printColor("------------------------------------", PURPLE);
        printColor("Blog username: " + blog.username, GREEN);
        printColor("Title: " + blog.title, BLUE);
        printColor("Content: " + blog.content, CYAN);
        printColor("Visibility: " + blog.visibility, YELLOW);
        printColor("Date: " + blog.createdOn.toString(), GREEN);
        printColor("------------------------------------", PURPLE);

        while (true) {
            printColor("\n1.  Comment on this blog", CYAN);
            printColor("2. View comments", YELLOW);
            printColor("3.  Back", RED);
            print("Choose an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                    printColor("Type your comment:", BLUE);
                    String comment = sc.nextLine();
                    BlogServices.addComment(blog.blogId, currentUser.Userid, comment);
                    NotificationServices.addNotification(new Notification(
                            getUserFromBlogId(blog.blogId).Userid,
                            currentUser.User_Name,
                            currentUser.User_Name + " commented on your blog"
                    ));
                }
                case "2" -> viewCommentsForBlog(blog.blogId);
                case "3" -> {
                    printColor(" Returning to blog list...", GREEN);
                    return;
                }
                default -> printColor(" Invalid option. Try again.", RED);
            }
        }
    }

    // View my blogs (with visibility)
    void viewMyBlogs() {
        ArrayList<Blog> blogs = BlogServices.getBlogsByUser(currentUser.Userid,currentUser.Userid);

        if (blogs.isEmpty()) {
            printColor(" You haven't posted any blogs yet.", RED);
            return;
        }

        printColor(" Your Blogs:", CYAN);
        for (Blog blog : blogs) {
            printColor("----------------------------------------------------", YELLOW);
            printColor("Blog ID: " + blog.blogId, GREEN);
            printColor("Title: " + blog.title, BLUE);
            printColor("Content: " + blog.content, CYAN);
            printColor("Visibility: " + blog.visibility, YELLOW);
            printColor("Date: " + blog.createdOn.toString(), PURPLE);
        }
        printColor("----------------------------------------------------", GREEN);
    }

    void deleteBlog() {
        printColor(" Enter Blog ID to delete:", RED);
        try {
            int blogId = Integer.parseInt(sc.nextLine());
            BlogServices.deleteBlog(blogId, currentUser.Userid);
        } catch (Exception e) {
            printColor("Invalid input! Blog ID must be a number.", RED);
        }
    }

    void viewCommentsForBlog(int blogId) {
        ArrayList<Comment> comments = BlogServices.getCommentsForBlog(blogId);

        if (comments.isEmpty()) {
            printColor(" No comments found for this blog.", RED);
            return;
        }

        printColor(" Comments:", CYAN);
        for (Comment c : comments) {
            String username = userCache.getOrDefault(c.userId, "User#" + c.userId);
            printColor("--------------------------", GREEN);
            printColor("Comment: " + c.content, BLUE);
            printColor("By: @" + username, PURPLE);
            printColor("On: " + c.commentedOn.toString(), CYAN);
        }
    }
}
