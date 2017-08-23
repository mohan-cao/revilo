package nz.co.revilo.Gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nz.co.revilo.App;
import nz.co.revilo.Output.ScheduleResultListener;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;

public class MainLauncherController implements Initializable, ScheduleResultListener {

    App app;
    protected String _graphName;
    protected List<String> _nodeNames;
    protected List<List<Boolean>> _arcs;
    protected List<List<Integer>> _arcWeights;
    protected List<Integer> _nodeWeights;
    protected List<Integer> _nodeStarts;
    protected List<Integer> _nodeProcessor;

    private Stage thisStage;
    private GUIScheduleResult results;


    public MainLauncherController(App app) {
        _nodeStarts = new ArrayList<>();
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

    @FXML
    private BorderPane mainPane;

    @FXML
    private BorderPane baseBp;

    private StackedBarChart<Number, String> ganttChart;


    @FXML
    private void closeRevilo(ActionEvent event) {
//        timeLabel.setText(app.getRunningTime()+ "");
//        ((Button)event.getSource()).getScene().getWindow().hide(); //close machine broke
//        thisStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        app.getAlgorithmManager().inform(this);
        results = new GUIScheduleResult();

        //set up change listeners
        results.isDoneProcessingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Platform.runLater(() -> {
                    makeGantt();
                });
            }
        });



        processorLabel.setText(App.getExecCores() + "");
//        app.startParsing();
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                App.startParsing();
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        memoryLabel.setText((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024+"");
                        timeLabel.setText(String.format("%.2f", App.getRunningTime()));
                        bestLabel.setText(App.getAlgorithmManager().getUpperBound() + "");
                        branchesLabel.setText(App.getAlgorithmManager().getBrokenTrees() + "");
//                        timeLabel.setText(app.getRunningTime() + "");
                    }
                });
            }
        }, 0, 50);
    }

    @Override
    public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor) {
        _graphName = graphName;
        _nodeNames = nodeNames;
        _nodeWeights = nodeWeights;
        _nodeStarts = nodeStarts;
        _nodeProcessor = nodeProcessor;

        results.setIsDoneProcessing(true);
    }

    public void makeGantt() {
        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        ArrayList<String> processorCat = new ArrayList<>();
        for (int i = 1; i < App.getExecCores() + 1; i++) {
            processorCat.add("Processor " + i);
        }
        yAxis.setCategories(FXCollections.observableArrayList(processorCat));
        ganttChart = new StackedBarChart<Number, String>(xAxis, yAxis);

        XYChart.Series<Number, String> idleSeries = new XYChart.Series<>();
        idleSeries.setName("Idle time");






        ganttChart.setTitle(_graphName);
        mainPane.setCenter(ganttChart);
        processorLabel.setText("test");
    }


/*
    @FXML
    void hideRevilo(MouseEvent event) {
    }*/

}
