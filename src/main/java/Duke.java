import java.util.Scanner;

public class Duke {
    private static final String DIVIDER = "    ____________________________________________________________";

    public static void main(String[] args) {
        String name = "BOT";
        String banner = " ____   ___  _____ \n"
                + "| __ ) / _ \\|_   _|\n"
                + "|  _ \\| | | | | |  \n"
                + "| |_) | |_| | | |  \n"
                + "|____/ \\___/  |_|  \n";

        System.out.println(DIVIDER);
        System.out.print(banner);
        System.out.println("     Hello! I'm " + name + ".");
        System.out.println("     What can I do for you?");
        System.out.println(DIVIDER);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            System.out.println(DIVIDER);
            System.out.println("     " + input);
            System.out.println(DIVIDER);
            input = scanner.nextLine();
        }

        System.out.println(DIVIDER);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);

        scanner.close();
    }
}
