package nz.co.revilo.GraphArt;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group{
    protected NodeItem source;
    protected NodeItem target;

    Line line;

    public Edge(NodeItem source, NodeItem target) {
        this.source = source;
        this.target = target;
        source.addNodeChild(target);
        target.addNodeParent(source);

        line = new Line();

        line.startXProperty().bind(source.layoutXProperty().add(source.getBoundsInParent().getWidth()/2.0));
        line.startYProperty().bind(source.layoutYProperty().add(source.getBoundsInParent().getHeight()/2.0));
        line.endXProperty().bind(target.layoutXProperty().add(target.getBoundsInParent().getWidth()/2.0));
        line.endYProperty().bind(target.layoutYProperty().add(target.getBoundsInParent().getHeight()/2.0));

        getChildren().add(line);
    }

    public NodeItem getSource() {
        return source;
    }

    public NodeItem getTarget() {
        return target;
    }

}
