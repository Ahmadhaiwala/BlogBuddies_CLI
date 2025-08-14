package cli;

import Model.Blog;
import Model.Comment;
import Model.Notification;
import Model.User;
import services.BlogServices;
import services.NotificationServices;
import services.SocialServices;

import static services.BlogServices.getUserFromBlogId;
import static services.NotificationServices.*;

import java.sql.SQLException;
import java.util.*;

import static cli.General.*;

public class BlogMenu {

     Scanner sc = new Scanner(System.in);
     User currentUser;


    Stack<Blog> blogHistory = new Stack<>();
    LinkedList<Blog> recentSearchResults = new LinkedList<>();
    HashMap<Integer, String> userCache = new HashMap<>();

    public BlogMenu(User user) {
        this.currentUser = user;
    }

    public void showMenu() {
        Scanner sc = new Scanner(System.in);

        do {
            printSection(" Blog Menu", CYAN);
            printColor("1. Create New Blog", YELLOW);
            printColor("2. View All Blogs", GREEN);
            printColor("3. View My Blogs", BLUE);
            printColor("4. Delete My Blog", PURPLE);
            printColor("5. Search Blogs", YELLOW);
            printColor("6. View Last Blog", CYAN); // NEW
            printColor("7. Back to Main Menu", RED);

            print("Enter choice: ");
            int choice=sc.nextInt();

            try {
//                choice = sc.nextInt();
                switch (choice) {
                    case 1 :createBlog();
                       break;
                    case 2  :viewAllBlogs();
                       break;
                    case 3 : viewMyBlogs();
                    break;
                    case 4 : deleteBlog();
                       break;
                    case 5 : searchBlog();
                     break;
                    case 6 : viewLastViewedBlog();
                      break;
                    case 7 :printColor(" Going back to main menu...", PURPLE);
                    return;

                    default : printColor(" Invalid choice. Try again.", RED);
                     break;
                }
            } catch (Exception e) {
                printColor(" Invalid input! Please enter a number.", RED);
            }

        } while (true);
    }

    void viewLastViewedBlog() throws SQLException {
        if (blogHistory.isEmpty()) {
            printColor("⚠️ No blogs in history yet!", RED);
        } else {
            Blog last = blogHistory.pop(); // LIFO!
            showBlogDetail(last);
        }
    }

    void searchBlog() {
        printColor(" Enter keyword to search in blogs:", BLUE);
        String keyword = sc.nextLine();

        recentSearchResults.clear();
        recentSearchResults.addAll(BlogServices.searchBlogs(keyword));

        if (recentSearchResults.isEmpty()) {
            printColor(" No blogs matched your search.", RED);
            return;
        }

        int i = 0;
        for (Blog blog : recentSearchResults) {
            printColor(i + ". " + blog.title + " (by @" + blog.username + ")", GREEN);
            i++;
        }

        printColor(i + ". Back", RED);
        print("Choose blog number to view details or back: ");

        try {
            int choice = Integer.parseInt(sc.nextLine());

            if (choice ==   i) return;

            if (choice >= 0 && choice < recentSearchResults.size()) {
                Blog selected = recentSearchResults.get(choice);
                blogHistory.push(selected); // Track history
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
        String title = sc.nextLine();

        printColor(" Enter blog content:", BLUE);
        String content = sc.nextLine();

        BlogServices.createBlog(currentUser.Userid, title, content);
        SocialServices s= new SocialServices();
        ArrayList<User> as= s.getFriends(currentUser.Userid);
        for (int i = 0; i < as.size(); i++) {
            String f=currentUser.User_Name+"currently upload new blog";
            Notification nf= new Notification(as.get(i).Userid,currentUser.User_Name,f);
            addNotification(nf);
        }
    }

    void viewAllBlogs() {
        ArrayList<Blog> blogs = BlogServices.getAllBlogs();

        if (blogs.isEmpty()) {
            printColor(" No blogs found.", RED);
            return;
        }

        while (true) {
            printColor(" All Blogs:", CYAN);
            int i = 0;
            for (Blog blog : blogs) {
                printColor(i + ". " + blog.title + " (by User Name: " + blog.username + ")", GREEN);
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

    void showBlogDetail(Blog blog) throws SQLException {
        printColor("\n Blog Detail:", CYAN);
        printColor("------------------------------------", PURPLE);
        printColor("Blog username: " + blog.username, GREEN);
        printColor("Title: " + blog.title, BLUE);
        printColor("Content: " + blog.content, CYAN);
        printColor("By User ID: " + blog.userId, YELLOW);
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
                    NotificationServices.addNotification(new Notification(getUserFromBlogId(blog.blogId).Userid,

                            currentUser.User_Name, currentUser.User_Name+"have commented on ur blog"));

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

    void viewMyBlogs() {
        ArrayList<Blog> blogs = BlogServices.getBlogsByUser(currentUser.Userid);

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
