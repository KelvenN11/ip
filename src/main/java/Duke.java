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

            String[] parts = input.split(" ", 2);
            String commandWord = parts[0];
            String rest = (parts.length > 1) ? parts[1] : "";

            try {
                switch (commandWord) {
                case "list":
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("     " + (i + 1) + "." + tasks[i]);
                    }
                    break;
                case "mark": {
                    int index = parseTaskIndex(rest, "mark", taskCount);
                    tasks[index].markAsDone();
                    System.out.println("     Nice! I've marked this task as done:");
                    System.out.println("       " + tasks[index]);
                    break;
                }
                case "unmark": {
                    int index = parseTaskIndex(rest, "unmark", taskCount);
                    tasks[index].markAsNotDone();
                    System.out.println("     OK, I've marked this task as not done yet:");
                    System.out.println("       " + tasks[index]);
                    break;
                }
                case "todo": {
                    tasks[taskCount] = parseTodo(rest);
                    taskCount++;
                    printAdded(tasks[taskCount - 1], taskCount);
                    break;
                }
                case "deadline": {
                    tasks[taskCount] = parseDeadline(rest);
                    taskCount++;
                    printAdded(tasks[taskCount - 1], taskCount);
                    break;
                }
                case "event": {
                    tasks[taskCount] = parseEvent(rest);
                    taskCount++;
                    printAdded(tasks[taskCount - 1], taskCount);
                    break;
                }
                default:
                    throw new BotException(
                            "OOPS!!! I don't understand \"" + commandWord
                                    + "\" - try list, todo, deadline, event, mark, unmark, or bye.");
                }
            } catch (BotException e) {
                System.out.println("     " + e.getMessage());
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

    private static int parseTaskIndex(String arg, String commandWord, int taskCount) throws BotException {
        String trimmed = arg.trim();
        if (trimmed.isEmpty()) {
            throw new BotException(
                    "OOPS!!! Tell me which task number to " + commandWord + ", e.g. \"" + commandWord + " 2\".");
        }
        int index;
        try {
            index = Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            throw new BotException("OOPS!!! \"" + trimmed + "\" isn't a task number - try something like \""
                    + commandWord + " 2\".");
        }
        if (index < 1 || index > taskCount) {
            throw new BotException("OOPS!!! There's no task number " + index + " in your list.");
        }
        return index - 1;
    }

    private static Todo parseTodo(String rest) throws BotException {
        String description = rest.trim();
        if (description.isEmpty()) {
            throw new BotException("OOPS!!! A todo needs a description, e.g. \"todo borrow book\".");
        }
        return new Todo(description);
    }

    private static Deadline parseDeadline(String rest) throws BotException {
        int byIndex = rest.indexOf("/by ");
        if (byIndex == -1) {
            throw new BotException(
                    "OOPS!!! A deadline needs a \"/by\" date or time, e.g. \"deadline return book /by Sunday\".");
        }
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + 4).trim();
        if (description.isEmpty()) {
            throw new BotException("OOPS!!! A deadline needs a description, e.g. \"deadline return book /by Sunday\".");
        }
        if (by.isEmpty()) {
            throw new BotException("OOPS!!! Tell me the date or time after \"/by\", e.g. \"deadline return book /by Sunday\".");
        }
        return new Deadline(description, by);
    }

    private static Event parseEvent(String rest) throws BotException {
        int fromIndex = rest.indexOf("/from ");
        int toIndex = rest.indexOf("/to ");
        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BotException("OOPS!!! An event needs both \"/from\" and \"/to\", e.g. "
                    + "\"event project meeting /from Mon 2pm /to 4pm\".");
        }
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + 6, toIndex).trim();
        String to = rest.substring(toIndex + 4).trim();
        if (description.isEmpty()) {
            throw new BotException("OOPS!!! An event needs a description, e.g. "
                    + "\"event project meeting /from Mon 2pm /to 4pm\".");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BotException("OOPS!!! Tell me both a \"/from\" and \"/to\" date or time, e.g. "
                    + "\"event project meeting /from Mon 2pm /to 4pm\".");
        }
        return new Event(description, from, to);
    }
}
