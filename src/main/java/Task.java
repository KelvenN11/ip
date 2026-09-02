/**
 * Represents a task with a description and a done/not-done status.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
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
}
