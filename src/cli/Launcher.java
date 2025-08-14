package cli;

import Model.User;
import services.UserServices;

import java.util.Date;
import java.util.Scanner;
import static cli.General.*;

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
                , CYAN);
        printColorf("-----------------------------------------------------------------------------------------------------------------------------",PURPLE);






        while (choice != 3) {


            System.out.println(BLUE);
            printBox("1.Login \n  2.Signup \n  3.exit",YELLOW);
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
                        printColorf("Goodbye ", CYAN);
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
        printColor("UserName:",BLUE);
        String username=sc.nextLine();
        printColor("Fullname:",BLUE);

        String fullname=sc.nextLine();
        printColor("Password:",BLUE);
        String Password=sc.nextLine();


        printColor("email:",BLUE);

        String email=sc.nextLine();
        while (!email.endsWith("@gmail.com")){
            printColor("email:",BLUE);
            email=sc.nextLine();
        }

        printColor("bio:",BLUE);
        String bio=sc.nextLine();
        java.util.Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        User u= new User(username, Password,  fullname,  email,  bio, sqlDate);
        UserServices us= new UserServices();
        us.insertNewUser(u);
    }


}
