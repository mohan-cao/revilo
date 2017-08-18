package nz.co.revilo.Gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import nz.co.revilo.App;

import java.net.URL;
import java.util.ResourceBundle;

public class MainLauncherController implements Initializable {

    App app;

    public MainLauncherController(App app) {
        this.app = app;
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

//    @FXML
//    void closeRevilo(MouseEvent event) {
//
//    }

    @FXML
    private void closeRevilo(ActionEvent event) {
        ((Button)event.getSource()).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        processorLabel.setText(app.getExecCores() + "");
    }
/*
    @FXML
    void hideRevilo(MouseEvent event) {
    }*/

}
