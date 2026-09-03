package bot.exception;

/**
 * Signals a problem with a user command that Bot can explain in plain
 * language, e.g. a missing description or an unrecognized command word.
 */
public class BotException extends Exception {
    public BotException(String message) {
        super(message);
    }
}
