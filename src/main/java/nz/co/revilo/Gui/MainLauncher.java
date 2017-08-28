package nz.co.revilo.Gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import nz.co.revilo.App;

import java.io.IOException;

/**
 * Launches GUI
 *
 * @author Terran Kroft
 */
public class MainLauncher extends Application {
    App app = App.getInstance();
    private Stage primaryStage;
    private BorderPane rootLayout;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Entry point for the GUI, called by the main app entry point
     * @param primaryStage stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.initStyle(StageStyle.DECORATED);
        this.primaryStage.setTitle("Revilo");
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Main.fxml"));
            MainLauncherController mlc = new MainLauncherController(app, this);
            loader.setController(mlc);
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add("/main.css");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(400);
            // Prevent algorithm from running once we close the window (i.e. force)
            primaryStage.setOnCloseRequest(event -> System.exit(0));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
