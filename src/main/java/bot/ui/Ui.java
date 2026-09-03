package bot.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import bot.parser.TaskDateTime;
import bot.task.Task;

/**
 * Handles all interaction with the user: everything printed to the
 * console, and reading each line of input. No other class prints to
 * {@code System.out} or reads from {@code System.in} directly, so the
 * console format can be changed (or swapped for another interface
 * entirely) by editing only this class.
 */
public class Ui {
    private static final String DIVIDER = "    ____________________________________________________________";
    private static final String NAME = "Bot";
    private static final String BANNER = " ____   ___  _____ \n"
            + "| __ ) / _ \\|_   _|\n"
            + "|  _ \\| | | | | |  \n"
            + "| |_) | |_| | | |  \n"
            + "|____/ \\___/  |_|  \n";

    private final Scanner scanner = new Scanner(System.in);

    /** Prints the startup banner and greeting. */
    public void showWelcome() {
        showLine();
        System.out.print(BANNER);
        System.out.println("     Hello! I'm " + NAME + ".");
        System.out.println("     What can I do for you?");
        showLine();
    }

    /** Prints the closing message shown just before the program exits. */
    public void showFarewell() {
        showLine();
        System.out.println("     Bye. Hope to see you again soon!");
        showLine();
    }

    /** Prints a bare divider line, used to separate one command's output from the next. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Reads one line of user input. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Releases the console input resource; call once, when the program is exiting. */
    public void close() {
        scanner.close();
    }

    /**
     * Prints an "OOPS!!!" style message describing a problem with the
     * user's command or a failed save. Prints no divider of its own: the
     * caller's command loop already prints one before and after every
     * command, success or failure.
     */
    public void showError(String message) {
        System.out.println("     " + message);
    }

    /** Prints the message for when the saved task file couldn't be loaded at startup. */
    public void showLoadingError(String message) {
        System.out.println("     " + message);
        showLine();
    }

    /**
     * Prints a warning for each saved-task line that couldn't be parsed at startup, if any.
     * Prints nothing at all when {@code warnings} is empty.
     */
    public void showLoadWarnings(List<String> warnings) {
        if (warnings.isEmpty()) {
            return;
        }
        System.out.println("     Note: some saved tasks were skipped because the data file looks corrupted:");
        for (String warning : warnings) {
            System.out.println("       - " + warning);
        }
        showLine();
    }

    /** Prints the full numbered task list for the {@code list} command. */
    public void showTaskList(List<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /** Prints the numbered tasks matching a keyword search, for the {@code find} command. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println("     Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /** Prints the numbered tasks occurring on a date, for the {@code on} command. */
    public void showTasksOnDate(LocalDate date, List<Task> matchingTasks) {
        System.out.println("     Here are the tasks on " + TaskDateTime.formatDateOnly(date) + ":");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /** Prints the confirmation shown after a task is added. */
    public void showAdded(Task task, int taskCount) {
        showTaskCountUpdate("Got it. I've added this task:", task, taskCount);
    }

    /** Prints the confirmation shown after a task is deleted. */
    public void showRemoved(Task task, int taskCount) {
        showTaskCountUpdate("Noted. I've removed this task:", task, taskCount);
    }

    /** Prints the confirmation shown after a task is marked done. */
    public void showMarked(Task task) {
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + task);
    }

    /** Prints the confirmation shown after a task is marked not done. */
    public void showUnmarked(Task task) {
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + task);
    }

    /**
     * Prints the shared "here's what changed, here's the task, here's the
     * new count" structure used by both {@link #showAdded} and
     * {@link #showRemoved}.
     */
    private void showTaskCountUpdate(String leadIn, Task task, int taskCount) {
        String taskWord = (taskCount == 1) ? "task" : "tasks";
        System.out.println("     " + leadIn);
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
