package nz.co.revilo.GraphArt;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class Graph {
    private GraphModel model;
    private Group canvas;
    private ScrollPane sp;
    NodeLayer nodeLayer;

    public Graph() {
        this.model = new GraphModel();
        canvas = new Group();
        nodeLayer = new NodeLayer();

        canvas.getChildren().add(nodeLayer);
        sp = new ScrollPane(canvas);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
    }

    public ScrollPane getScrollPane() {
        return this.sp;
    }

    public Pane getNodeLayer() {
        return this.nodeLayer;
    }

    public GraphModel getModel() {
        return model;
    }

    public void beginUpdate() {

    }

    public void endUpdate() {
        getNodeLayer().getChildren().addAll(model.getAddedEdges());
        getNodeLayer().getChildren().addAll(model.getAddedNodes());
        getNodeLayer().getChildren().removeAll(model.getRemovedNodes());
        getNodeLayer().getChildren().removeAll(model.getRemovedEdges());

        getModel().attachOrphansToGraphParent(model.getAddedNodes());
        getModel().disconnectFromGraphParent(model.getRemovedNodes());
        getModel().merge();
    }


}
