package cli;

import Model.User;
import services.UserServices;

import java.util.Date;
import java.util.Scanner;
import static cli.General.*;
import static java.awt.Color.MAGENTA;
import static java.awt.Color.ORANGE;

public class Launcher {
    static Scanner sc= new Scanner(System.in);

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice = 0;
        printColorf("-----------------------------------------------------------------------------------------------------------------------------",PURPLE);
        printColorf( "░▒▓███████▓▒░░▒▓█▓▒░      ░▒▓██████▓▒░ ░▒▓██████▓▒░       ░▒▓███████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓███████▓▒░░▒▓███████▓▒░░▒▓█▓▒░░▒▓█▓▒░ \n" +
                        "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ \n" +
                        "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░             ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ \n" +
                        "░▒▓███████▓▒░░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒▒▓███▓▒░      ░▒▓███████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░  \n" +
                        "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░     \n" +
                        "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░     \n" +
                        "░▒▓███████▓▒░░▒▓████████▓▒░▒▓██████▓▒░ ░▒▓██████▓▒░       ░▒▓███████▓▒░ ░▒▓██████▓▒░░▒▓███████▓▒░░▒▓███████▓▒░   ░▒▓█▓▒░     "
                , BRIGHT_WHITE);
        printColorf("-----------------------------------------------------------------------------------------------------------------------------",PURPLE);






        while (choice != 4) {


            System.out.println(BLUE);
            printBox("1.Login \n  2.Signup \n  3.about \n 4.adminlogin \n 5.exit",YELLOW);
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
                        about();
                        break;
                    case 4:
                        new AdminLogin().login(args);
                        break;

                    case 5:
                        printColorf("Goodbye", CYAN);
                        return;

                    default:
                        printColor("Only choices 1-5 are valid!", RED);
                }

            } catch (Exception e) {
                printColor("Please enter a valid number!", RED);
            }

            System.out.println();
        }


    }
    public static void about() {

        General.printSection("About This Project", General.BRIGHT_BLUE);

        // Overview
        General.printColor(" Overview:", General.BRIGHT_YELLOW);
        General.print("This is a Java-based Social system integrated with MySQL.");
        General.print("It allows viewing blog ");
        General.print("Git:"+" https://github.com/Ahmadhaiwala/BlogBuddies_CLI.git");

        // Core Highlights
        General.printColor("\n Core Highlights:", General.BRIGHT_YELLOW);
        General.printColor("• Fully database-driven using JDBC.", General.BRIGHT_CYAN);
        General.printColor("• Object-Oriented design with service and model layers.", General.BRIGHT_CYAN);
        General.printColor("• Modular code ready for expansion.", General.BRIGHT_CYAN);

        // Tech Stack
        General.printColor("\n Tech Stack:", General.BRIGHT_YELLOW);
        General.printColor("• Java (Core Java, JDBC)", General.BRIGHT_PURPLE);
        General.printColor("• Postgres (users table for persistent storage)", General.BRIGHT_PURPLE);
        General.printColor("• CLI-based interface for user interaction", General.BRIGHT_PURPLE);

        // Developer
        General.printColor("\n Developer:", General.BRIGHT_YELLOW);
        General.printColor("• Ahmad", General.BRIGHT_GREEN);
        General.printColor("• Purpose: Strengthen Java + SQL skills while building a scalable application.", General.BRIGHT_GREEN);
        General.printColor("• Smit", General.BRIGHT_GREEN);
        General.printColor("• Purpose: Logic and data structure", General.BRIGHT_GREEN);
        General.printColor("• Grensy", General.BRIGHT_GREEN);
        General.printColor("• Purpose: Design", General.BRIGHT_GREEN);



        System.out.println();
        General.printBox(" End of Project Overview ", General.BRIGHT_BLUE);
    }


    public static void login() {

        printColor("Enter UserName:",BLUE);
       String username=sc.nextLine();
        printColor("Enter Password:",BLUE);
        String password=sc.nextLine();
        UserMenu a= new UserMenu();
        if(UserServices.authenticateUser(username,password,a)){
            printColor("login Successfully",GREEN);

            a.Home();
        }
        else{
            printColor("Incorrect UserName or password please try again",RED);
        }




    }

    public static void signup() {
        UserServices us= new UserServices();
        printColor("UserName:",BLUE);
        String username=sc.nextLine();
        while(us.isUserAlreadyThere(username)){
            printColor("User already exist try another username!!!",RED);
            printColor("UserName:",BLUE);
            username=sc.nextLine();
        }
        printColor("Fullname:",BLUE);

        String fullname=sc.nextLine();
        printColor("Password:",BLUE);
        String Password=sc.nextLine();
        while(us.checkStrength(Password)<3){
            printColor("please enter a password containing [A-Z],[a-z],Numbers,Symbols",RED);
            Password=sc.nextLine();
        }


        printColor("email:",BLUE);

        String email=sc.nextLine();
        while (!email.endsWith("@gmail.com") || us.isEmailDuplicate(email)){
            if(!email.endsWith("@gmail.com")){
                printColor("Email should ends with @gmail.com ",RED);
            }
            if(us.isEmailDuplicate(email)){
                printColor("Email is duplicate  ",RED);
            }

            printColor("email:",BLUE);
            email=sc.nextLine();
        }

        printColor("bio:",BLUE);
        String bio=sc.nextLine();
        java.util.Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        User u= new User(username, Password,  fullname,  email,  bio, sqlDate);
        u= new User(username, Password,  fullname,  email,  bio, sqlDate);
        us.insertNewUser(u);

    }


}
