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
     */
    public static List<Task> load() throws BotException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(DATA_FILE)) {
                tasks.add(parseLine(line));
            }
        } catch (IOException e) {
            throw new BotException("OOPS!!! I couldn't read your saved tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Parses one data-file line back into the Task it represents, e.g.
     * {@code "D | 0 | return book | Sunday"} into an undone Deadline.
     */
    private static Task parseLine(String line) throws BotException {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            task = new Event(description, parts[3], parts[4]);
            break;
        default:
            task = new Todo(description);
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
