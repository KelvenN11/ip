import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the current list of tasks and every operation that reads or
 * changes it (adding, removing, marking, and querying by date). Callers
 * work with task numbers and dates; they never touch the underlying
 * {@code List<Task>} directly.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Starts with an empty task list, e.g. when there was nothing to load from disk. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Starts with the given tasks already in the list, e.g. ones just loaded from disk. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the given 0-based index. */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public void mark(int index) {
        tasks.get(index).markAsDone();
    }

    public void unmark(int index) {
        tasks.get(index).markAsNotDone();
    }

    /** The tasks occurring on the given date, in list order, for the {@code on} command. */
    public List<Task> tasksOn(LocalDate date) {
        List<Task> matching = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matching.add(task);
            }
        }
        return matching;
    }

    /** A read-only view of every task in the list, in order - for {@code list} and for saving to disk. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
