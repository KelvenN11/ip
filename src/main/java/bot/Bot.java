package bot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bot.exception.BotException;
import bot.parser.Parser;
import bot.storage.Storage;
import bot.task.Task;
import bot.task.TaskList;
import bot.ui.Ui;

/**
 * The chatbot's entry point and orchestrator. Bot itself holds no
 * command-handling logic: it wires together a {@link Ui} (console
 * interaction), a {@link Storage} (loading/saving the data file), a
 * {@link TaskList} (the tasks and operations on them), and a
 * {@link Parser} (interpreting each command line), and its
 * {@link #run()} loop just calls each of them in turn.
 */
public class Bot {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /** The saved-task load failure to report once run() starts, or null if loading didn't fail outright. */
    private final String loadErrorMessage;

    /** Any saved-task lines that couldn't be parsed, to report once run() starts; empty if none. */
    private final List<String> loadWarnings;

    /**
     * Sets up a Bot backed by the data file at {@code filePath}, loading
     * whatever tasks are already saved there (or starting with an empty
     * list if the file doesn't exist yet). Building a Bot has no visible
     * effect on the console by itself - any load failure or corrupted
     * line found here is only reported once {@link #run()} starts, so
     * construction and the order things are printed in stay independent
     * of each other.
     */
    public Bot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        List<Task> loaded;
        List<String> warnings = new ArrayList<>();
        String errorMessage = null;
        try {
            loaded = storage.load(warnings);
        } catch (BotException e) {
            errorMessage = e.getMessage();
            loaded = new ArrayList<>();
        }
        loadErrorMessage = errorMessage;
        loadWarnings = warnings;
        tasks = new TaskList(loaded);
    }

    /** Greets the user, reports any loading trouble, processes commands until "bye", then says goodbye. */
    public void run() {
        ui.showWelcome();
        if (loadErrorMessage != null) {
            ui.showLoadingError(loadErrorMessage);
        }
        ui.showLoadWarnings(loadWarnings);
        runCommandLoop();
        ui.showFarewell();
        ui.close();
    }

    /**
     * Reads one line of user input at a time and executes it as a command,
     * until the user types "bye".
     */
    private void runCommandLoop() {
        String input = ui.readCommand();
        while (!input.equals("bye")) {
            ui.showLine();
            executeCommand(input);
            ui.showLine();
            input = ui.readCommand();
        }
    }

    /**
     * Parses one line of user input into a command word and its argument
     * text (via {@link Parser}), and carries out whichever command it
     * names against the task list. Any problem with the command
     * (unrecognized word, missing/malformed argument, bad task number) is
     * reported as an "OOPS!!!" message instead of propagating further.
     */
    private void executeCommand(String input) {
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
                    saveTasks();
                    break;
                }
                case "unmark": {
                    int index = Parser.parseTaskIndex(rest, "unmark", tasks.size());
                    tasks.unmark(index);
                    ui.showUnmarked(tasks.get(index));
                    saveTasks();
                    break;
                }
                case "delete": {
                    int index = Parser.parseTaskIndex(rest, "delete", tasks.size());
                    Task removed = tasks.delete(index);
                    ui.showRemoved(removed, tasks.size());
                    saveTasks();
                    break;
                }
                case "todo": {
                    tasks.add(Parser.parseTodo(rest));
                    ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                    saveTasks();
                    break;
                }
                case "deadline": {
                    tasks.add(Parser.parseDeadline(rest));
                    ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                    saveTasks();
                    break;
                }
                case "event": {
                    tasks.add(Parser.parseEvent(rest));
                    ui.showAdded(tasks.get(tasks.size() - 1), tasks.size());
                    saveTasks();
                    break;
                }
                case "on": {
                    LocalDate date = Parser.parseOnDate(rest);
                    ui.showTasksOnDate(date, tasks.tasksOn(date));
                    break;
                }
                case "find": {
                    String keyword = Parser.parseFindKeyword(rest);
                    ui.showMatchingTasks(tasks.findByKeyword(keyword));
                    break;
                }
                default:
                    throw new BotException(
                            "OOPS!!! I don't understand \"" + commandWord
                                    + "\" - try list, todo, deadline, event, mark, unmark, delete, on, find, or bye.");
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
    private void saveTasks() {
        try {
            storage.save(tasks.asList());
        } catch (BotException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Starts Bot, backed by the data file at {@code data/bot.txt}.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Bot("data/bot.txt").run();
    }
}
