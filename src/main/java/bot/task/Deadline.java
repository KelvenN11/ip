package bot.task;

import java.time.LocalDate;

import bot.parser.TaskDateTime;

/**
 * A task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {

    protected TaskDateTime by;

    /**
     * Creates a new, not-done deadline with the given description and due date/time.
     *
     * @param description The deadline's description text.
     * @param by The date (and optionally time) the task is due by.
     */
    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toSaveFormat() {
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + by.toSaveFormat();
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.toLocalDate().equals(date);
    }
}
