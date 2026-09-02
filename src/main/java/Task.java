import java.time.LocalDate;

/**
 * Represents a task with a description and a done/not-done status.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** The task's description text, e.g. "return book". */
    protected String getDescription() {
        return description;
    }

    /** Whether the task is currently marked done, e.g. for a subclass building its save-file line. */
    protected boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns this task's representation for the data file, as a single
     * pipe-separated line (e.g. {@code "T | 1 | read book"}). Each
     * subclass supplies its own type letter and any extra fields.
     */
    public abstract String toSaveFormat();

    /**
     * Returns whether this task falls on the given calendar date, used by
     * the {@code on} command. A task with no date attached (a Todo) never
     * matches; Deadline and Event override this with their own dates.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }
}
