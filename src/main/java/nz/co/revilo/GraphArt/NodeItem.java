package nz.co.revilo.GraphArt;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class NodeItem extends Pane {
    String nodeId;
    List<NodeItem> children = new ArrayList<>();
    List<NodeItem> parents = new ArrayList<>();

    Node view;

    public NodeItem(String nodeId) {
        this.nodeId = nodeId;

    }

    public void addNodeChild(NodeItem node) {
        children.add(node);
    }

    public List<NodeItem> getNodeChildren() {
        return children;
    }

    public void addNodeParent(NodeItem node) {
        parents.add(node);
    }

    public List<NodeItem> getNodeParents() {
        return parents;
    }

    public void removeNodeChild(NodeItem child) {
        children.remove(child);
    }

    public void setView(Node view) {
        this.view = view;
        getChildren().add(view);
    }

    public Node getView() {
        return this.view;
    }

    public String getNodeId() {
        return nodeId;
    }


}
