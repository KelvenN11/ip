import java.util.Scanner;

public class Duke {
    private static final String DIVIDER = "    ____________________________________________________________";
    private static final int MAX_TASKS = 100;

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

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            System.out.println(DIVIDER);
            if (input.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasks[index].markAsDone();
                System.out.println("     Nice! I've marked this task as done:");
                System.out.println("       " + tasks[index]);
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks[index].markAsNotDone();
                System.out.println("     OK, I've marked this task as not done yet:");
                System.out.println("       " + tasks[index]);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("     added: " + input);
            }
            System.out.println(DIVIDER);
            input = scanner.nextLine();
        }

        System.out.println(DIVIDER);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);

        scanner.close();
    }
}
