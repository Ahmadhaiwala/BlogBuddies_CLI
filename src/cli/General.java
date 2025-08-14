package cli;

public class General {

    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";

    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String PURPLE = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";

    public static final String BRIGHT_RED = "\033[91m";
    public static final String BRIGHT_GREEN = "\033[92m";
    public static final String BRIGHT_YELLOW = "\033[93m";
    public static final String BRIGHT_BLUE = "\033[94m";
    public static final String BRIGHT_PURPLE = "\033[95m";
    public static final String BRIGHT_CYAN = "\033[96m";
    public static final String BRIGHT_WHITE = "\033[97m";

    // === Slow Print ===
    public static void print(String s) {
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }


    public static void printfast(String s) {
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
            try {
                Thread.sleep(5);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }


    public static void printColor(String s, String color) {
        System.out.print(color + BOLD);
        print(s);
        System.out.print(RESET);
    }


    public static void printColorf(String s, String color) {
        System.out.print(color + BOLD);
        printfast(s);
        System.out.print(RESET);
    }


    public static void printSection(String title, String color) {
        printColor("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━", color);
        printColor("   " + title.toUpperCase(), color);
        printColor("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n", color);
    }


    public static void printBox(String message, String color) {
        int width = message.length() + 4;
        String border = "┌" + "─".repeat(width) + "┐";
        String middle = "  " + message + "        ";
        String bottom = "└" + "─".repeat(width) + "┘";

        printColor(border, color);
        printColor(middle, color);
        printColor(bottom, color);
    }







}
