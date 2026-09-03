package bot.task;

import java.time.LocalDate;

import bot.parser.TaskDateTime;

/**
 * A task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {

    /** The date (and optionally time) this task is due by. */
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

    /**
     * Returns this deadline's status icon, description, and due date/time,
     * prefixed with the {@code [D]} type icon.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns this deadline's data-file line, e.g.
     * {@code "D | 1 | return book | 2019-10-15"}.
     */
    @Override
    public String toSaveFormat() {
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + by.toSaveFormat();
    }

    /**
     * Returns whether the given date matches this deadline's due date exactly.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return by.toLocalDate().equals(date);
    }
}
