package cli;

import DS.SearchHistoryLinkedList;
import DS.SearchStack;
import Model.Notification;
import Model.User;
import services.NotificationServices;
import services.UserServices;

import java.util.ArrayList;
import java.util.Scanner;

import static cli.General.*;
import static cli.General.BLUE;
import static cli.General.CYAN;
import static cli.General.RED;
import static cli.General.RESET;
import static cli.General.printColor;


import java.util.*;

public class UserMenu {
    public User user;
    static Scanner sc = new Scanner(System.in);




    SearchStack searchHistory = new SearchStack();  // your stack impl
    SearchHistoryLinkedList recentlyViewedProfiles = new SearchHistoryLinkedList(); // your linkedlist impl

    HashMap<String, Integer> viewCounts = new HashMap<>();


    public void Home() {

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (true) {
            printSection("Home",PURPLE);
            System.out.println(BLUE);
            print("\uD83D\uDC65 1. Friends Menu");
            print("\uD83D\uDD0D 2. Search Users");

            print("\uD83D\uDCE9 3.Blog Menu");
            print("\uD83D\uDCE9 4. View Inbox");
            print("5. Logout");
            print("\uD83E\uDDD1 6. My Profile");
            print("\uD83D\uDC40 7. View Another User's Profile");
            print("8. View History & Stats");
            print("9.Edit ur profile");

            System.out.println(RESET);

            print("Enter choice: ");


            try {

                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        SocialMenu a= new SocialMenu();
                           a.FriendsMenu(user);

                        break;
                    case 2:searchUser();

                        break;
                    case 3:
                        BlogMenu b=new BlogMenu(user);
                        b.showMenu();
                        break;
                    case 4 : viewInbox();
                    break;

                    case 5:return;

                    case 6: ViewOwnProfile();
                        return;

                    case  7:ViewUserProfile();

                        break;
                    case 8 : viewStats();
                      break;
                    case 9:
                        editProfile();
                        break;





                    default:
                        printColor("Only choices 1, 2, ...7 are valid!", RED);
                }
            } catch (Exception e) {
                printColor("Please enter a valid number!", RED);
            }

            System.out.println();
        }


    }
    public void ViewUserProfile() {
        printColor("Enter username:", PURPLE);
        String username = sc.nextLine();

        User user = UserServices.getUserByUsername(username);
        if (user == null) {
            printColor("No user found", RED);
            return;
        }


        if (recentlyViewedProfiles.contains(username)) {
            recentlyViewedProfiles.remove(username); // move to front
        }
        recentlyViewedProfiles.addFirst(username);
        if (recentlyViewedProfiles.size() > 5) {
            recentlyViewedProfiles.removeLast(); // keep only last 5
        }


        viewCounts.put(username, viewCounts.getOrDefault(username, 0) + 1);

        printColor("UserName: " + user.User_Name, CYAN);
        printColor("FullName: " + user.fullName, CYAN);
        printColor("Bio: " + user.bio, CYAN);
        printColor("Joined On: " + user.joinedOn.toString(), CYAN);
        printColor("Email: " + user.email, CYAN);

        printColor(" Viewed " + viewCounts.get(username) + " times", GREEN);
    }
    public void viewStats() {
        printSection(" Your Activity", BLUE);

        printColor(" Last 5 Search Keywords:", CYAN);
        ArrayList<String> history=searchHistory.getList();
        for (String keyword : history) {
            print(" " + keyword);
        }

        printColor("\nRecently Viewed Profiles:", CYAN);
        ArrayList<String> a=recentlyViewedProfiles.getList();
        for (String user : a) {
            print(" " + user + " (viewed " + viewCounts.getOrDefault(user, 0) + " times)");
        }
    }


    public void  ViewOwnProfile(){
        try{
            printColor("UserName:"+user.User_Name.toString(),CYAN);
            printColor("FullName:"+user.fullName,CYAN);
            printColor("Bio:"+user.bio.toString(),CYAN);
            printColor("joinedOn:"+user.joinedOn.toString(),CYAN);

            printColor("Email:"+user.email.toString(),CYAN);
        }catch (NullPointerException e){
            printColor("Could'nt find information please verify ur detail",RED);
        }

    }
    public void editProfile() {
        General.printColor("=== Edit Profile === [Leave blank to keep]", General.YELLOW);

        Scanner sc = new Scanner(System.in);

        General.printColor("Full Name [" + user.fullName + "]: ", General.BLUE);
        String name = sc.nextLine();
        if (!name.trim().isEmpty()) user.fullName = name;

        General.printColor("Email [" + user.email + "]: ", General.BLUE);
        String email = sc.nextLine();
        if (!email.trim().isEmpty()) user.email = email;

        General.printColor("Bio [" + user.bio + "]: ", General.BLUE);
        String bio = sc.nextLine();
        if (!bio.trim().isEmpty()) user.bio = bio;

        General.printColor("Password  ", General.BLUE);
        String pass = sc.nextLine();
        if (!pass.trim().isEmpty()) user.Password = pass;

        UserServices us = new UserServices();
        if (us.updateUserProfile(user)) {
            General.printColor(" Profile updated successfully!", General.GREEN);
        } else {
            General.printColor(" Failed to update profile!", General.RED);
        }
    }


    public void searchUser() {
        printColor("Enter UserName:", BLUE);
        String s = sc.nextLine();


        searchHistory.push(s);

        ArrayList<User> ls = UserServices.searchUser(s);
        if (ls != null && !ls.isEmpty()) {
            printColor("Results......", CYAN);
            for (User l : ls) {
                print("Username: " + l.User_Name);
            }
        } else {
            printColor("No user found", RED);
        }
    }
    public void viewInbox() {
        ArrayList<Notification> notifications = NotificationServices.getNotificationsForUser(user.Userid);

        printSection(" Notification Inbox", PURPLE);

        if (notifications.isEmpty()) {
            printColor(" No new notifications!", RED);
            return;
        }

        int i = 1;
        for (Notification notif : notifications) {
            printColor(i + ". " + notif.toString(), CYAN);
            i++;
        }

        NotificationServices.markAllAsSeen(user.Userid);
    }









}
