import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
}
