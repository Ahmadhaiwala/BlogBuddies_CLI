package cli;

public class General {
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String PURPLE = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";





    public static void print(String s) {
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
            try {
                Thread.sleep(50);
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }


    public static void printColor(String s, String color) {
        System.out.print(color);
        System.out.flush();
        print(s);
        System.out.print(RESET);
        System.out.flush();
    }

    // === Clear screen ===
    public static void clear() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // fallback
        }

        for (int i = 0; i < 100; i++) System.out.println();
    }



    }



