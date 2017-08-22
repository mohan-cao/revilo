package nz.co.revilo.Gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import nz.co.revilo.App;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainLauncherController implements Initializable {

    App app;

    public MainLauncherController(App app) {
        this.app = app;
    // TODO is this the best way?
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                app.startParsing();
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }
    @FXML
    private Button closeBtn;

    @FXML
    private Button hideBtn;

    @FXML
    private Label processorLabel;

    @FXML
    private Label memoryLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label branchesLabel;

    @FXML
    private Label bestLabel;

    @FXML
    private void closeRevilo(ActionEvent event) {
//        timeLabel.setText(app.getRunningTime()+ "");
        ((Button)event.getSource()).getScene().getWindow().hide(); //close machine broke
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        processorLabel.setText(app.getExecCores() + "");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        timeLabel.setText(String.format("%.1f", app.getRunningTime()));
                        bestLabel.setText(app.getAlgorithmManager().getCurrentOptimal() + "");
//                        timeLabel.setText(app.getRunningTime() + "");
                    }
                });
            }
        }, 0, 50);
    }



/*
    @FXML
    void hideRevilo(MouseEvent event) {
    }*/

}
