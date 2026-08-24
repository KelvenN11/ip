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
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                tasks[taskCount] = new Todo(description);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("deadline ")) {
                String rest = input.substring(9);
                int byIndex = rest.indexOf("/by ");
                String description = rest.substring(0, byIndex).trim();
                String by = rest.substring(byIndex + 4).trim();
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("event ")) {
                String rest = input.substring(6);
                int fromIndex = rest.indexOf("/from ");
                int toIndex = rest.indexOf("/to ");
                String description = rest.substring(0, fromIndex).trim();
                String from = rest.substring(fromIndex + 6, toIndex).trim();
                String to = rest.substring(toIndex + 4).trim();
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);
            }
            System.out.println(DIVIDER);
            input = scanner.nextLine();
        }

        System.out.println(DIVIDER);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);

        scanner.close();
    }

    private static void printAdded(Task task, int taskCount) {
        String taskWord = (taskCount == 1) ? "task" : "tasks";
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
