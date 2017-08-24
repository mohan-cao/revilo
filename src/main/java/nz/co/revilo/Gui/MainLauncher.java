package nz.co.revilo.Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import nz.co.revilo.App;
import nz.co.revilo.GraphArt.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLauncher extends Application{

    Graph graph = new Graph();

    private Stage primaryStage;
    private BorderPane rootLayout;
    private double xOffset = 0;
    private double yOffset = 0;
    App app = App.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.initStyle(StageStyle.UNDECORATED);
        this.primaryStage.setTitle("Revilo");
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Main.fxml"));
            MainLauncherController mlc = new MainLauncherController(app);
            loader.setController(mlc);
            rootLayout = loader.load();

            rootLayout.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            rootLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            // Show the scene containing the root layout.

            /* SECTION FOR GRAPH TEST*/
            BorderPane root = new BorderPane();
            graph = new Graph();
            root.setCenter(graph.getScrollPane());
            Scene scene = new Scene(root, 1024, 768);
            /*END*/
//            Scene scene = new Scene(rootLayout);
//            scene.getStylesheets().add("/main.css");
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                    System.out.println("stop!");
                }
            });
            primaryStage.show();
            addGraphComponents();
            LayoutManager layout = new RandomLayout(graph);
            layout.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addGraphComponents() {
        GraphModel model = graph.getModel();
        graph.beginUpdate();
        List<String> names = App.getAlgorithmManager().getNodeNames();
        boolean[][] arcs = App.getAlgorithmManager().getArcs();
        for (String node: names) {
            model.addNodeItem(node, NodeType.CIRCLE);
        }

        for (int i = 0; i < names.size(); i++) {
            boolean[] row = arcs[i];
            for (int j = 0; j < names.size(); j++) {
                if (arcs[i][j] == true) {
                    model.addEdge(names.get(i), names.get(j));
                }
            }
        }
//        model.addNodeItem("01", NodeType.CIRCLE);
        graph.endUpdate();
    }
}
