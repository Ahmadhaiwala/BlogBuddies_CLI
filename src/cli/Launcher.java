package cli;

import db.DBConnection;
import services.UserAuthentication;

import java.sql.Connection;
import java.util.Scanner;
import static cli.General.*;

public class Launcher {
    static Scanner sc= new Scanner(System.in);

    public static void main(String[] args) {
        Connection con= DBConnection.getConnection();
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (choice != 3) {
            printColor("        ----------------", YELLOW);
            printColor("        | BLOG BUDDIES |", YELLOW);
            printColor("        ----------------", YELLOW);
            System.out.println(BLUE);
            print("1. Login");
            print("2. Sign Up");
            print("3. Exit");
            System.out.println(RESET);

            print("Enter choice: ");


            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        signup();
                        break;
                    case 3:
                        printColor("Goodbye 👋", CYAN);
                        break;
                    default:
                        printColor("Only choices 1, 2, or 3 are valid!", RED);
                }
            } catch (Exception e) {
                printColor("Please enter a valid number!", RED);
            }

            System.out.println();
        }

        sc.close();
    }

    public static void login() {

        printColor("Enter UserName:",BLUE);
       String username=sc.nextLine();
        printColor("Enter Password:",BLUE);
        String password=sc.nextLine();
        if(UserAuthentication.authenticateUser(username,password)){
            printColor("login Successfully",GREEN);
        }




    }

    public static void signup() {
        printColor("Signup functionality coming soon!", GREEN);
    }
    public void Home(){
        print("enter choice:");
        int choice=sc.nextInt();
        while(choice!=3){

        }
    }

    public void searchUser(){

    }
    public void ViewFriends(){
        //To chat with them see there profile see there blog
    }
    public void PublicBlog(){

    }
    public void Inbox(){

    }
    public void PostBlog(){}

}
