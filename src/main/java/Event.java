import java.time.LocalDate;

/**
 * A task that starts at a specific date/time and ends at a specific date/time.
 */
public class Event extends Task {

    protected TaskDateTime from;
    protected TaskDateTime to;

    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toSaveFormat() {
        return "E | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + from.toSaveFormat() + " | "
                + to.toSaveFormat();
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(from.toLocalDate()) && !date.isAfter(to.toLocalDate());
    }
}
