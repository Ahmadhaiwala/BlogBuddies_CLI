package cli;

import DS.StackInteger;
import Model.User;
import services.SocialServices;
import services.UserServices;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

import static cli.General.*;

public class SocialMenu {
    User user;
    static Scanner sc = new Scanner(System.in);
    StackInteger undoStack = new StackInteger();

    public void FriendsMenu(User user) {
        this.user = user;

        try {
            int choice;
            do {
                printSection("Friend Section", YELLOW);
                printColor("1. View Friends", PURPLE);
                printColor("2. Send Friend Request", CYAN);
                printColor("3. View Incoming Friend Requests", YELLOW);
                printColor("4. Remove Friend", GREEN);
                printColor("5. Undo Last Friend Request", BLUE);
                printColor("6. Back to Main Menu", YELLOW);  // 👈 NEW


                print("Enter choice: ");

                while (!sc.hasNextInt()) {
                    printColor("Please enter a valid number!", RED);
                    sc.next();
                }
                choice = sc.nextInt();


                switch (choice) {
                    case 1 -> viewFriends();
                    case 2 -> sendFriendReq();
                    case 3 -> viewIncomingFriendRequests();
                    case 4 -> removeFriend();

                    case 5 -> undoLastFriendRequest();
                    case 6 -> printColor("↩ Returning to main menu.", GREEN);
                    default -> printColor(" Invalid choice! Try again.", RED);
                }

            } while (choice != 6);

        } catch (Exception e) {
            printColor(" An unexpected error occurred.", RED);
            e.printStackTrace();
        }
    }
    public void removeFriend() {
        try{} catch (NumberFormatException e) {
            printColor("Invalid choice!! only no allowed",RED);
        }
        ArrayList<User> friends = SocialServices.getFriends(user.Userid);

        if (friends == null || friends.isEmpty()) {
            printColor(" You have no friends to remove.", RED);
            return;
        }

        printColor(" Select a friend to remove:", CYAN);
        for (int i = 0; i < friends.size(); i++) {
            printColor(i + ". " + friends.get(i).User_Name, PURPLE);
        }

        print("Enter number of friend to remove: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice >= 0 && choice < friends.size()) {
            User selected = friends.get(choice);
            boolean removed = new SocialServices().removeFriend(user.Userid, selected.Userid);
            printColor(removed ? " Friend removed." : "Could not remove.", removed ? YELLOW : BRIGHT_RED);
        } else {
            printColor("Invalid selection.", RED);
        }
    }

    public void sendFriendReq() {
        SocialServices fr = new SocialServices();
        printColor("Enter username to send friend request:", BLUE);
        sc.nextLine();
        String username = sc.nextLine();

        ArrayList<User> foundUsers = UserServices.searchUser(username);

        if (foundUsers.isEmpty()) {
            printColor(" No users found with that username.", RED);
            return;
        }

        User selectedUser = null;

        if (foundUsers.size() == 1) {
            User matchedUser = foundUsers.get(0);
            printColor("Do you mean " + matchedUser.User_Name + "?", CYAN);
            printColor("Yes/No", YELLOW);
            String choice = sc.nextLine();

            while (!choice.equalsIgnoreCase("Yes") && !choice.equalsIgnoreCase("No")) {
                printColor("Please enter valid choice", RED);
                printColor("Yes/No", YELLOW);
                choice = sc.nextLine();
            }

            if (choice.equalsIgnoreCase("No")) {
                printColor(" Friend request canceled.", RED);
                return;
            }

            selectedUser = matchedUser;

        } else {
            try {
                for (int i = 0; i < foundUsers.size(); i++) {
                    printColor(i + ". " + foundUsers.get(i).User_Name, PURPLE);
                }

                printColor("Select user by number:", BLUE);
                int index = sc.nextInt();
                sc.nextLine(); // Clear newline

                if (index < 0 || index >= foundUsers.size()) {
                    printColor(" Invalid selection!", RED);
                    return;
                }

                selectedUser = foundUsers.get(index);

            } catch (InputMismatchException e) {
                printColor(" Only numbers are allowed!", RED);
                sc.nextLine(); // Clear junk input
                return;
            } catch (Exception e) {
                printColor(" Unexpected error during selection.", RED);
                e.printStackTrace();
                return;
            }
        }

        // Self check
        if (selectedUser.Userid == user.Userid) {
            printColor(" You cannot send a friend request to yourself.", RED);
            return;
        }


        if (fr.areFriends(user.Userid, selectedUser.Userid)) {
            printColor(" You are already friends with this user!", YELLOW);
            return;
        }


        if (fr.hasPendingFriendRequest(user.Userid, selectedUser.Userid)) {
            printColor(" Friend request already sent and is pending!", RED);
            return;
        }


        fr.sendFriendReq(user.Userid, selectedUser.Userid,user);
        undoStack.push(selectedUser.Userid); // Push to undo stack
    }

    public void undoLastFriendRequest() {
        if (!undoStack.isEmpty()) {
            int lastUserId = undoStack.pop();
            SocialServices fr = new SocialServices();

            boolean undone = fr.undoFriendRequest(user.Userid, lastUserId);
            if (undone) {
                printColor(" Last friend request undone!", GREEN);
            } else {
                printColor(" Undo failed or request was already accepted.", RED);
            }
        } else {
            printColor(" No sent friend request to undo!", YELLOW);
        }
    }

    public void viewIncomingFriendRequests() {
        SocialServices fs = new SocialServices();
        ArrayList<User> incoming = fs.getReqDetail(user);

        if (incoming == null || incoming.isEmpty()) {
            printColor("No incoming friend requests found!", RED);
            return;
        }

        printColor(" Incoming Friend Requests:", CYAN);
        for (int i = 0; i < incoming.size(); i++) {
            User u = incoming.get(i);
            printColor(i + ". " + u.User_Name + " | " + u.fullName, PURPLE);
        }

        print("Select request to respond to (number) or -1 to go back: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice >= 0 && choice < incoming.size()) {
            User selected = incoming.get(choice);
            printColor("Do you want to accept or reject this request? (accept/reject): ", CYAN);
            String response = sc.nextLine().toLowerCase();

            SocialServices fr = new SocialServices();

            if (response.equals("accept")) {
                boolean ok = fr.acceptFriendRequest(selected.Userid, user.Userid);
                printColor(ok ? "Friend added!" : " Failed to accept request", ok ? GREEN : RED);
            } else if (response.equals("reject")) {
                boolean ok = fr.rejectFriendRequest(selected.Userid, user.Userid);
                printColor(ok ? " Request rejected" : " Could not reject", ok ? YELLOW : RED);
            } else {
                printColor("Invalid response. Skipping.", RED);
            }
        }
    }


    public void viewFriends() {
        ArrayList<User> friends = SocialServices.getFriends(user.Userid);

        if (friends == null || friends.isEmpty()) {
            printColor(" You have no friends yet!", RED);
            return;
        }

        printColor("👥 Your Friends List:", CYAN);
        for (User u : friends) {
            printColor("----------------------------------", CYAN);
            print("Email: ");
            printColor(u.email, YELLOW);
            print("Bio: ");
            printColor(u.bio, YELLOW);
            print("Date Joined: ");
            printColor(u.joinedOn.toString(), YELLOW);
            print("Full Name: ");
            printColor(u.fullName, YELLOW);
            print("Username: ");
            printColor(u.User_Name, YELLOW);
        }
        printColor("----------------------------------", CYAN);
    }
}
