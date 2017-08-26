package nz.co.revilo.Gui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;

/**
 * @author Terran Kroft
 */
public class GUIScheduleResult {
    private BooleanProperty isDoneProcessing = new SimpleBooleanProperty();
    private StringProperty graphName = new SimpleStringProperty();
    private ListProperty<String> nodeNames = new SimpleListProperty<>();
    private ListProperty<Integer> nodeWeights = new SimpleListProperty<>();
    private ListProperty<Integer> nodeStarts = new SimpleListProperty<>();
    private ListProperty<Integer> nodeProcessor = new SimpleListProperty<>();


    public final boolean getIsDoneProcessing() {
        return isDoneProcessing.get();
    }

    public final void setIsDoneProcessing(boolean isDoneProcessing) {
        this.isDoneProcessing.set(isDoneProcessing);
    }

    public BooleanProperty isDoneProcessingProperty() {
        return isDoneProcessing;
    }

    public final String getGraphName() {
        return graphName.get();
    }

    public final void setGraphName(String graphName) {
        this.graphName.set(graphName);
    }

    public StringProperty graphNameProperty() {
        return graphName;
    }


    public final List<String> getNodeNames() {
        return nodeNames.get();
    }

    public final void setNodeNames(List<String> nodeNames) {
        this.nodeNames.set(FXCollections.observableArrayList(nodeNames));
    }

    public ListProperty<String> nodeNamesProperty() {
        return nodeNames;
    }

    public final List<Integer> getNodeWeights() {
        return nodeWeights.get();
    }

    public final void setNodeWeights(List<Integer> nodeWeights) {
        this.nodeWeights.set(FXCollections.observableArrayList(nodeWeights));
    }

    public ListProperty<Integer> nodeWeightsProperty() {
        return nodeWeights;
    }


    public final List<Integer> getNodeStarts() {
        return nodeStarts.get();
    }

    public final void setNodeStarts(List<Integer> nodeStarts) {
        this.nodeStarts.set(FXCollections.observableArrayList(nodeStarts));
    }

    public ListProperty<Integer> nodeStartsProperty() {
        return nodeStarts;
    }
//nodeprocessors

    public final List<Integer> getNodeProcessor() {
        return nodeProcessor.get();
    }

    public final void setNodeProcessor(List<Integer> nodeProcessor) {

        this.nodeProcessor.set(FXCollections.observableArrayList(nodeProcessor));
    }

    public ListProperty<Integer> nodeProcessorProperty() {
        return nodeProcessor;
    }
}
