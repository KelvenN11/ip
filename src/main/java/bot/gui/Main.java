package bot.gui;

import java.io.IOException;

import bot.Bot;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A JavaFX GUI for Bot, backed by the same {@link Bot} used by the
 * console UI (see {@code bot.Bot#main}) and the same data file, so tasks
 * added through either interface are visible in both.
 */
public class Main extends Application {

    private final Bot bot = new Bot("data/bot.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Bot");
            fxmlLoader.<MainWindow>getController().setBot(bot);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
