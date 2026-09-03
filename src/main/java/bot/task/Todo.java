package bot.task;

/**
 * A task with no date/time attached to it.
 */
public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toSaveFormat() {
        return "T | " + (isDone() ? "1" : "0") + " | " + getDescription();
    }
}
