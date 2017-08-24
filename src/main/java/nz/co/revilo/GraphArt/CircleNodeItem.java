package nz.co.revilo.GraphArt;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class CircleNodeItem extends NodeItem {
    public CircleNodeItem(String id) {
        super(id);
        double width = 20;
        double height = 20;
        Rectangle view = new Rectangle(25, 25);
        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.BISQUE);
        setView(view);
    }

}
