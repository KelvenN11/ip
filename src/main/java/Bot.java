import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        ui.showWelcome();
        List<Task> tasks = loadTasks();
        runCommandLoop(tasks);
        ui.showFarewell();
        ui.close();
    }

    /**
     * Loads the saved task list from disk (see {@link Storage}), reporting
     * a load failure or any corrupted lines that had to be skipped. Starts
     * with an empty list if there's nothing to load or loading failed
     * outright.
     */
    private static List<Task> loadTasks() {
        ArrayList<Task> tasks;
        ArrayList<String> loadWarnings = new ArrayList<>();
        try {
            tasks = new ArrayList<>(Storage.load(loadWarnings));
        } catch (BotException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new ArrayList<>();
        }
        ui.showLoadWarnings(loadWarnings);
        return tasks;
    }

    /**
     * Reads one line of user input at a time and executes it as a command,
     * until the user types "bye".
     */
    private static void runCommandLoop(List<Task> tasks) {
        String input = ui.readCommand();
        while (!input.equals("bye")) {
            ui.showLine();
            executeCommand(input, tasks);
            ui.showLine();
            input = ui.readCommand();
        }
    }

    /**
     * Parses one line of user input into a command word and its argument
     * text, and carries out whichever command it names against
     * {@code tasks}. Any problem with the command (unrecognized word,
     * missing/malformed argument, bad task number) is reported as an
     * "OOPS!!!" message instead of propagating further.
     */
    private static void executeCommand(String input, List<Task> tasks) {
        String[] parts = input.split(" ", 2);
        String commandWord = parts[0];
        String rest = (parts.length > 1) ? parts[1] : "";

        try {
            switch (commandWord) {
            case "list":
                ui.showTaskList(tasks);
                break;
            case "mark": {
                int index = parseTaskIndex(rest, "mark", tasks.size());
                tasks.get(index).markAsDone();
                ui.showMarked(tasks.get(index));
                saveTasks(tasks);
                break;
            }
            case "unmark": {
                int index = parseTaskIndex(rest, "unmark", tasks.size());
                tasks.get(index).markAsNotDone();
                ui.showUnmarked(tasks.get(index));
                saveTasks(tasks);
                break;
            }
            case "delete": {
                int index = parseTaskIndex(rest, "delete", tasks.size());
                Task removed = tasks.remove(index);
                ui.showRemoved(removed, tasks.size());
                saveTasks(tasks);
                break;
            }
            case "todo": {
                tasks.add(parseTodo(rest));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                saveTasks(tasks);
                break;
            }
            case "deadline": {
                tasks.add(parseDeadline(rest));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                saveTasks(tasks);
                break;
            }
            case "event": {
                tasks.add(parseEvent(rest));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                saveTasks(tasks);
                break;
            }
            case "on": {
                LocalDate date = parseOnDate(rest);
                List<Task> matching = new ArrayList<>();
                for (Task task : tasks) {
                    if (task.occursOn(date)) {
                        matching.add(task);
                    }
                }
                ui.showTasksOnDate(date, matching);
                break;
            }
            default:
                throw new BotException(
                        "OOPS!!! I don't understand \"" + commandWord
                                + "\" - try list, todo, deadline, event, mark, unmark, delete, on, or bye.");
            }
        } catch (BotException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Persists the current task list to disk, so the change just made
     * survives a restart. Saving is best-effort: if it fails (e.g. the
     * disk is full or the data folder isn't writable), the user is told
     * but the in-memory task list is left as-is rather than crashing.
     */
    private static void saveTasks(List<Task> tasks) {
        try {
            Storage.save(tasks);
        } catch (BotException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Parses a 1-based task number as typed by the user (e.g. the "2" in
     * "mark 2") and converts it to the corresponding 0-based index into
     * {@code tasks}.
     */
    private static int parseTaskIndex(String arg, String commandWord, int taskCount) throws BotException {
        String trimmed = arg.trim();
        if (trimmed.isEmpty()) {
            throw new BotException(
                    "OOPS!!! Tell me which task number to " + commandWord + ", e.g. \"" + commandWord + " 2\".");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            throw new BotException("OOPS!!! \"" + trimmed + "\" isn't a task number - try something like \""
                    + commandWord + " 2\".");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BotException("OOPS!!! There's no task number " + taskNumber + " in your list.");
        }
        return taskNumber - 1;
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
            throw new BotException("OOPS!!! A deadline needs a \"/by\" date or time, e.g. "
                    + "\"deadline return book /by 2019-10-15\" or \"deadline return book /by 2019-10-15 1800\".");
        }
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + 4).trim();
        if (description.isEmpty()) {
            throw new BotException("OOPS!!! A deadline needs a description, e.g. "
                    + "\"deadline return book /by 2019-10-15\".");
        }
        if (by.isEmpty()) {
            throw new BotException("OOPS!!! Tell me the date or time after \"/by\", e.g. "
                    + "\"deadline return book /by 2019-10-15\".");
        }
        return new Deadline(description, TaskDateTime.parse(by));
    }

    private static Event parseEvent(String rest) throws BotException {
        int fromIndex = rest.indexOf("/from ");
        int toIndex = rest.indexOf("/to ");
        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BotException("OOPS!!! An event needs both \"/from\" and \"/to\", e.g. "
                    + "\"event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600\".");
        }
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + 6, toIndex).trim();
        String to = rest.substring(toIndex + 4).trim();
        if (description.isEmpty()) {
            throw new BotException("OOPS!!! An event needs a description, e.g. "
                    + "\"event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600\".");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BotException("OOPS!!! Tell me both a \"/from\" and \"/to\" date or time, e.g. "
                    + "\"event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600\".");
        }
        return new Event(description, TaskDateTime.parse(from), TaskDateTime.parse(to));
    }

    private static LocalDate parseOnDate(String rest) throws BotException {
        String trimmed = rest.trim();
        if (trimmed.isEmpty()) {
            throw new BotException("OOPS!!! Tell me which date, e.g. \"on 2019-10-15\".");
        }
        return TaskDateTime.parseDateOnly(trimmed);
    }
}
