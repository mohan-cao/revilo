package nz.co.revilo.GraphArt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphModel {
    NodeItem graphParent;
    List<NodeItem> allNodes;
    List<NodeItem> addedNodes;
    List<NodeItem> removedNodes;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map<String, NodeItem> nodeMap;

    public GraphModel() {
        graphParent = new NodeItem("__ROOT__");
        clear();
    }

    public void clear() {
        allNodes = new ArrayList<>();
        addedNodes = new ArrayList<>();
        removedNodes = new ArrayList<>();
        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();
        nodeMap = new HashMap<>();
    }

    public void clearAddedLists() {
        addedNodes.clear();
        addedEdges.clear();
    }

    public List<NodeItem> getAddedNodes() {
        return addedNodes;
    }

    public List<NodeItem> getRemovedNodes() {
        return removedNodes;
    }

    public List<NodeItem> getAllNodes() {
        return allNodes;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void addNodeItem(String id, NodeType type) {
        switch(type) {
            case CIRCLE:
                CircleNodeItem circleNode = new CircleNodeItem(id);
                addNodeItem(circleNode);
                break;
        }
    }

    private void addNodeItem(NodeItem node) {
        addedNodes.add(node);
        nodeMap.put(node.getNodeId(), node);
    }

    public void addEdge(String sourceId, String targetId) {
        NodeItem sourceCell = nodeMap.get(sourceId);
        NodeItem targetCell = nodeMap.get(targetId);
        Edge edge = new Edge(sourceCell, targetCell);
        addedEdges.add(edge);
    }

    public void attachOrphansToGraphParent(List<NodeItem> nodeList) {
        for (NodeItem node: nodeList) {
            if (node.getNodeParents().size() == 0) {
                graphParent.addNodeChild(node);
            }
        }
    }

    public void disconnectFromGraphParent(List<NodeItem> nodeList) {
        for (NodeItem node: nodeList) {
            graphParent.removeNodeChild(node);
        }
    }

    public void merge() {
        allNodes.addAll(addedNodes);
        allNodes.removeAll(removedNodes);

        addedNodes.clear();
        removedNodes.clear();

        allEdges.addAll(addedEdges);
        allEdges.removeAll(removedEdges);

        addedEdges.clear();
        removedEdges.clear();
    }

}
