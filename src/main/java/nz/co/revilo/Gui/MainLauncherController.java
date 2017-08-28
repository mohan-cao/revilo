package nz.co.revilo.Gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import nz.co.revilo.App;
import nz.co.revilo.Gui.GanttChart.ExtraData;
import nz.co.revilo.Output.NewOptimalResultListener;
import nz.co.revilo.Output.ScheduleResultListener;
import nz.co.revilo.Scheduling.BnBSchedule;

import java.net.URL;
import java.util.*;

public class MainLauncherController implements Initializable, ScheduleResultListener, NewOptimalResultListener {

    public final String processorTitle = "PSR ";

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
    private MainLauncher ml;


    public MainLauncherController(App app, MainLauncher ml) {
        _nodeStarts = new ArrayList<>();
        thisStage = ml.getPrimaryStage();
        this.app = app;
        this.ml = ml;

    }

    @FXML
    private Label processorLabel;

    @FXML
    private Label memoryLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label parallelLabel;

    @FXML
    private Label timeUnits;

    @FXML
    private Label branchesLabel;

    @FXML
    private Label bestLabel;

    @FXML
    private Label threadsLabel;

    @FXML
    private Label graphNameLabel;

    @FXML
    private Label systemLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private BorderPane ganttPane;

    @FXML
    private BorderPane baseBp;

    private GanttChart<Number, String> ganttChart;

    private ArrayList<String> processorCatStr;
    private ArrayList<XYChart.Series> processorCat;

    /**
     * JavaFX method to set up the GUI and listeners
      * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getAlgorithmManager().inform(this);
        App.getAlgorithmManager().optimalInform(this);

        results = new GUIScheduleResult();
        //set up change listeners
        results.isDoneProcessingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Platform.runLater(() -> {
                    updateGantt();
                });
            }
        });

        processorLabel.setText(App.getExecCores() + "");

        // start running the algorithm
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
        // update all the GUI information in a GUI thread
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        // calculate current used memory
                        long currentMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
                        double timeElapsed = App.getRunningTime();
                        memoryLabel.setText((currentMemory)  / (1024l * 1024l) + "");
                        timeLabel.setText(
                                (timeElapsed>60)?
                                String.format("%d:%02.0f", (int)timeElapsed/60, timeElapsed%60)
                                :
                                String.format("%.2f", timeElapsed)
                        );
                        timeUnits.setText((timeElapsed>=60)?"MIN":"SEC");
                        branchesLabel.setText(App.getAlgorithmManager().getBrokenTrees() + "");

                    }
                });
            }
        }, 0, 50);

        // Set up rest of GUI in a GUI thread. This will run only once
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                graphNameLabel.setText(App.getInputFileName()); //padding
                systemLabel.setText("PROCESSING");
                statusLabel.setText("Starting up...");
                parallelLabel.setText(App.getNumParallelCores() + "");
                String thr = App.getNumParallelCores() == 1 ? "THREAD" : "THREADS";
                threadsLabel.setText(thr);
                createGantt();
            }
        });

    }

    /**
     * Notifies the UI that the final schedule has been found and update the status bar
     * @author Terran Kroft
     *
     * @param graphName     Name of graph
     * @param nodeNames     Name of each task
     * @param arcs          The existence of arcs
     * @param arcWeights    How long to transfer results of task completion to another processor
     * @param nodeWeights   How long each task takes
     * @param nodeStarts    When each task starts
     * @param nodeProcessor What processor each task is on
     */
    @Override
    public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor) {
        _graphName = graphName;
        _nodeNames = nodeNames;
        _nodeWeights = nodeWeights;
        _nodeStarts = nodeStarts;
        _nodeProcessor = nodeProcessor;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                systemLabel.setText("COMPLETE");
                statusLabel.setText("Optimal length: " + App.getAlgorithmManager().getUpperBound() + " (Time taken: " + App.getRunningTime() + " seconds)");
            }
        });
        results.setIsDoneProcessing(true);
    }

    /**
     * Creates a blank Gantt Chart with all the required series and axes
     * @author Terran Kroft
     */
    public void createGantt() {
        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        ArrayList<String> processorCatStr = new ArrayList<>();
        ArrayList<XYChart.Series> processorCat = new ArrayList<>();


        for (int i = 0; i < App.getExecCores(); i++) {
            processorCatStr.add(processorTitle + i);
            processorCat.add(new XYChart.Series()); // each processor has its own series
        }

        yAxis.setCategories(FXCollections.observableArrayList(processorCatStr));
        ganttChart = new GanttChart<Number, String>(xAxis, yAxis);

        ganttPane.setCenter(ganttChart);

        //live adjust gantt chart based on window size
        ganttChart.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(ganttChart.getHeight());
                ganttChart.setBlockHeight(newValue.doubleValue()*0.70/(App.getExecCores()));

            }
        });

    }

    /**
     * Updates the Gantt chart with new found information.
     */
    public void updateGantt() {
        int processorStripLength = processorTitle.length();
        ArrayList<String> processorCatStr = new ArrayList<>();
        ArrayList<XYChart.Series> processorCat = new ArrayList<>();
        ArrayList<ArrayList<String>> pcatName = new ArrayList<>();

        long lowestNodeWeight = (long) Collections.min(_nodeWeights);
        long highestNodeWeight = (long) Collections.max(_nodeWeights);

        for (int i = 0; i < App.getExecCores(); i++) {
            processorCatStr.add(processorTitle + i);
            processorCat.add(new XYChart.Series()); // each processor has its own series
            pcatName.add(new ArrayList<>());
        }

        for (int i = 0; i < _nodeStarts.size(); i++) { //using node starts as not all nodes might be set yet
            int psr = _nodeProcessor.get(i);
            XYChart.Series psrCat = processorCat.get(psr);
            ArrayList<String> pcat = pcatName.get(psr);
            String styleclass;
            switch (psr) { // set up colors for different processors
                case 0: styleclass = "gantt0"; break;
                case 1: styleclass = "gantt1"; break;
                case 2: styleclass = "gantt2"; break;
                case 3: styleclass = "gantt3"; break;
                case 4: styleclass = "gantt4"; break;
                default: styleclass = "ganttdefault"; break;
            }
            XYChart.Data data = new XYChart.Data(_nodeStarts.get(i), (processorTitle + psr), new ExtraData(_nodeWeights.get(i), styleclass));
            String iterNodeName = _nodeNames.get(i);

            psrCat.getData().add(data);
            pcat.add(iterNodeName);
        }

        ganttChart.getData().clear();
        for (XYChart.Series ps : processorCat) {
            int iterI = processorCat.indexOf(ps);
            ganttChart.getData().add(ps);
            ObservableList<XYChart.Data> data = ps.getData();
            for (XYChart.Data d: data) {
                //set up tooltips and barchart colors
                int iterJ = data.indexOf(d);
                StackPane bar = (StackPane) d.getNode();
                String node = pcatName.get(iterI).get(iterJ);   //nodename
                int startTime = (int)d.getXValue();
                String processor = (String)d.getYValue();
                processor = processor.substring(processorStripLength);
                ExtraData nodeW = (ExtraData)d.getExtraValue();
                long nodeWeight = nodeW.getLength();
                Tooltip.install(bar, new Tooltip("Node\t" + node + "\nWeight\t" + nodeWeight + "\nStart\t" + startTime + "\nProc.\t" + processor));
                double percentage = (double)(nodeWeight - lowestNodeWeight) / (double)(highestNodeWeight - lowestNodeWeight);
                percentage = 1.0 - (0.25 + percentage * (0.75)); //scale percentages from 25% to 100%
                if (highestNodeWeight == lowestNodeWeight) {
                    percentage = 0.0; // don't want NaNs
                }
                ColorAdjust ca = new ColorAdjust();
                ca.setBrightness(percentage);
                bar.setEffect(ca);

            }
        }
    }

    /**
     * Notifies the GUI that a new optimal has been found.
     * @param optimal
     */
    @Override
    public void newOptimal(BnBSchedule optimal) {
        _graphName = App.getAlgorithmManager().getGraphName();
        _nodeNames = App.getAlgorithmManager().getNodeNames();
        _nodeWeights = App.getAlgorithmManager().getNodeWeights();
        _nodeStarts = new ArrayList<>();
        _nodeProcessor = new ArrayList<>();
        for(int nodeId=0; nodeId< _nodeWeights.size(); nodeId++){
            _nodeStarts.add(optimal.getClosedNodes().get(nodeId).getA());//start times
            _nodeProcessor.add(optimal.getClosedNodes().get(nodeId).getB());//processors scheduled on
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateGantt();
                statusLabel.setText("New optimal schedule found with length " + App.getAlgorithmManager().getAtomicBound() + ".");
                bestLabel.setText(App.getAlgorithmManager().getAtomicBound() + "");
            }
        });

    }

}
