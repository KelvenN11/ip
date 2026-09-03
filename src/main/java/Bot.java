import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage("data/bot.txt");

    public static void main(String[] args) {
        ui.showWelcome();
        TaskList tasks = loadTasks();
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
    private static TaskList loadTasks() {
        List<Task> loaded;
        ArrayList<String> loadWarnings = new ArrayList<>();
        try {
            loaded = storage.load(loadWarnings);
        } catch (BotException e) {
            ui.showLoadingError(e.getMessage());
            loaded = new ArrayList<>();
        }
        ui.showLoadWarnings(loadWarnings);
        return new TaskList(loaded);
    }

    /**
     * Reads one line of user input at a time and executes it as a command,
     * until the user types "bye".
     */
    private static void runCommandLoop(TaskList tasks) {
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
    private static void executeCommand(String input, TaskList tasks) {
        Parser.ParsedCommand command = Parser.parseCommand(input);
        String commandWord = command.commandWord();
        String rest = command.arguments();

        try {
            switch (commandWord) {
            case "list":
                ui.showTaskList(tasks.asList());
                break;
            case "mark": {
                int index = Parser.parseTaskIndex(rest, "mark", tasks.size());
                tasks.mark(index);
                ui.showMarked(tasks.get(index));
                saveTasks(tasks);
                break;
            }
            case "unmark": {
                int index = Parser.parseTaskIndex(rest, "unmark", tasks.size());
                tasks.unmark(index);
                ui.showUnmarked(tasks.get(index));
                saveTasks(tasks);
                break;
            }
            case "delete": {
                int index = Parser.parseTaskIndex(rest, "delete", tasks.size());
                Task removed = tasks.delete(index);
                ui.showRemoved(removed, tasks.size());
                saveTasks(tasks);
                break;
            }
            case "todo": {
                tasks.add(Parser.parseTodo(rest));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                saveTasks(tasks);
                break;
            }
            case "deadline": {
                tasks.add(Parser.parseDeadline(rest));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                saveTasks(tasks);
                break;
            }
            case "event": {
                tasks.add(Parser.parseEvent(rest));
                ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                saveTasks(tasks);
                break;
            }
            case "on": {
                LocalDate date = Parser.parseOnDate(rest);
                ui.showTasksOnDate(date, tasks.tasksOn(date));
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
    private static void saveTasks(TaskList tasks) {
        try {
            storage.save(tasks.asList());
        } catch (BotException e) {
            ui.showError(e.getMessage());
        }
    }
}
