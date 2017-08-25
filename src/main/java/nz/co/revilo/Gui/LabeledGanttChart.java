package nz.co.revilo.Gui;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.Axis;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

public class LabeledGanttChart<X,Y> extends GanttChart<X, Y>{
    public LabeledGanttChart(Axis axis, Axis axis2) {
        super(axis, axis2);
    }

    @Override
    public void layoutPlotChildren() {
        super.layoutPlotChildren();
        for (Series<X, Y> series: getData()) {
            for (Data<X, Y> data: series.getData()) {
                Tooltip.install(data.getNode(), new Tooltip(
                        "topl"
                ));
                data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-background-color: orange"));

                StackPane bar = (StackPane) data.getNode();
                System.out.println(bar.getLayoutX());
                System.out.println(bar.getWidth());
                System.out.println(bar.getLayoutY());
                System.out.println(bar.getHeight());

                Text l = new Text("hey");
                l.setFont(Font.font(17));
                l.setFontSmoothingType(FontSmoothingType.LCD);
//                l.setRotate(90.0);
                bar.getChildren().add(l);
                l.setTranslateX(-(bar.getLayoutX() + bar.getWidth())/2);
//                bar.setAlignment(l, Pos.CENTER);
            }
        }
    }
}
