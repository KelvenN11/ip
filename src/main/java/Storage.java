import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves the task list to a fixed location on disk so tasks survive between
 * runs of the program.
 *
 * The data file lives at {@code ./data/bot.txt}, a path relative to
 * wherever the program is run from (never a hard-coded absolute path such
 * as {@code C:\data}) and built with {@link Paths#get}, so it uses the
 * right separator on every operating system.
 */
public class Storage {
    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static final Path DATA_FILE = DATA_DIRECTORY.resolve("bot.txt");

    /**
     * Writes every task to the data file, one per line, overwriting
     * whatever was there before. Creates the {@code ./data} directory
     * first if it does not already exist.
     */
    public static void save(List<Task> tasks) throws BotException {
        try {
            Files.createDirectories(DATA_DIRECTORY);
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(DATA_FILE))) {
                for (Task task : tasks) {
                    writer.println(task.toSaveFormat());
                }
            }
        } catch (IOException e) {
            throw new BotException("OOPS!!! I couldn't save your tasks to disk: " + e.getMessage());
        }
    }

    /**
     * Loads the task list from the data file. If the file (or its
     * containing ./data folder) doesn't exist yet, returns an empty list
     * instead of failing, since that's the normal state the first time
     * the program runs on a new machine.
     *
     * <p>The file may have been hand-edited or corrupted since it was last
     * written, so each line is parsed independently: a malformed line is
     * skipped rather than aborting the whole load, and a human-readable
     * description of what was wrong with it (including its 1-based line
     * number) is appended to {@code warnings} so the caller can tell the
     * user. Well-formed lines are unaffected by a bad line elsewhere in
     * the file.
     *
     * @throws BotException if the file exists but can't be read at all,
     *         e.g. due to a permissions problem
     */
    public static List<Task> load(List<String> warnings) throws BotException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(DATA_FILE);
        } catch (IOException e) {
            throw new BotException("OOPS!!! I couldn't read your saved tasks: " + e.getMessage());
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseLine(line));
            } catch (BotException e) {
                warnings.add("line " + (i + 1) + ": " + e.getMessage());
            }
        }
        return tasks;
    }

    /**
     * Parses one data-file line back into the Task it represents, e.g.
     * {@code "D | 0 | return book | 2019-10-15"} into an undone Deadline.
     * Rejects anything that doesn't match the expected
     * {@code type | done-flag | description [| ...]} shape instead of
     * guessing, so a corrupted line is reported rather than silently
     * misread.
     */
    private static Task parseLine(String line) throws BotException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new BotException(
                    "expected at least 3 fields separated by \" | \" (type, done flag, description), found "
                            + parts.length);
        }
        String type = parts[0];
        String doneFlag = parts[1];
        String description = parts[2];
        if (!doneFlag.equals("0") && !doneFlag.equals("1")) {
            throw new BotException("done flag must be \"0\" or \"1\", found \"" + doneFlag + "\"");
        }
        boolean isDone = doneFlag.equals("1");

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new BotException("a deadline (\"D\") line needs a 4th field for its \"by\" date/time");
            }
            task = new Deadline(description, TaskDateTime.parse(parts[3]));
            break;
        case "E":
            if (parts.length < 5) {
                throw new BotException(
                        "an event (\"E\") line needs 4th and 5th fields for its \"from\" and \"to\" date/time");
            }
            task = new Event(description, TaskDateTime.parse(parts[3]), TaskDateTime.parse(parts[4]));
            break;
        default:
            throw new BotException("unknown task type \"" + type + "\" (expected \"T\", \"D\", or \"E\")");
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
