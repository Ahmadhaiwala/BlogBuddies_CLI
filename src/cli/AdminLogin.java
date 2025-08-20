package cli;

import services.AdminServices;
import static cli.General.*;

import java.util.Scanner;

public class AdminLogin {

    public void login(String[] args) {
        Scanner sc = new Scanner(System.in);
        printColor("--- Admin Login ---", GREEN);

        printColor("Username:", YELLOW);
        String username = sc.nextLine();

        printColor("Password:", YELLOW);
        String password = sc.nextLine();

        if (AdminServices.authenticateAdmin(username, password)) {
            printColor("Login successful! Welcome Admin.", GREEN);
            new AdminMenu().showMenu();
        } else {
            printColor("Invalid credentials! Try again.", RED);


        }
        Launcher.main(args);

    }
}
