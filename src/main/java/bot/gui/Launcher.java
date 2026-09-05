package bot.gui;

import javafx.application.Application;

/**
 * A launcher class to work around a classpath issue where JavaFX fails to
 * find the application class if it's launched directly.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
