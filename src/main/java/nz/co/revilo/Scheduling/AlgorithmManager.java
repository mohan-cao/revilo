package nz.co.revilo.Scheduling;

import nz.co.revilo.Input.ParseResultListener;
import nz.co.revilo.Output.NewOptimalResultListener;
import nz.co.revilo.Output.ScheduleResultListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Abstract class defining the data structures, and information to be used when implementing any algorithms as part of a
 * scheduling solution.
 */
public abstract class AlgorithmManager extends Observable implements ParseResultListener {

    int _processingCores;
    private NewOptimalResultListener optimalListener;
    private List<ScheduleResultListener> listeners = new ArrayList<>();;
    int[] _nodeWeights;
    boolean[][] _arcs;
    int[][] _arcWeights;
    String[] _nodeNames;
    String _graphName;

    long brokenTrees;
    int upperBound; // used in subclasses

    /**
     * Sets the number of processing cores the tasks must be scheduled on.
     * @param processingCores number of cores specified for the final schedule
     */
    public AlgorithmManager(int processingCores) {
        _processingCores = processingCores;
        brokenTrees = 0;
    }

    /**
     * Get the number of branches broken (i.e. branches that have been deemed not as good)
     * @return
     */
    public long getBrokenTrees() {
        return brokenTrees;
    }

    /**
     * Gets the upper bound value (current best)
     * @return current best
     */
    public int getUpperBound() {
        return upperBound;
    }

    /**
     * Gets graph name.
     * @return the name of the graph
     */
    public String getGraphName() {
        return _graphName;
    }

    /**
     * Getter for list of node names for easy Gantt setup
     * @return the list of node names
     */
    public List<String> getNodeNames(){
        return new ArrayList<>(Arrays.asList(_nodeNames));
    }

    /**
     * Gets a tentative list of nodes for preliminary setup of Gantt Chart
     * @return
     */
    public List<Integer> getNodeWeights() {
        return PrimitiveInterfaceHelper.primToInteger1D(_nodeWeights);
    }

    /**
     * Executes the scheduling algorithm associated with the specific implementation of AlgorithmManager. This is the
     * hook method associated with the template method inform()
     */
    protected abstract void execute();//TODO

    /**
     * Associates a ScheduleResultListener implementation to the AlgorithmManager, which will receive the final result as
     * produced by the algorithm implementation (to use in output file creation or visualization).
     * @param listener
     */
    public void inform(ScheduleResultListener listener) {
        listeners.add(listener);
    }
    public void optimalInform(NewOptimalResultListener listener) { optimalListener = listener; }

    /**
     *Retrieves NewOptimalResultListener whenever an algorithm (BnB) finds a graph length that is
     * better than the current one.
     * @return
     */
    public NewOptimalResultListener getOptimalListener() { return optimalListener; }

    /**
     * Template method for reading in graph information required to process a schedule, and executes the schedule (using
     * the execute() hook method.
     * @param graphName
     * @param nodeNames
     * @param nodeWeights
     * @param arcs
     * @param arcWeights
     */
    public void ParsingResults(String graphName, String[] nodeNames, int[] nodeWeights, boolean[][] arcs, int[][] arcWeights) {
        _arcWeights = arcWeights;
        _arcs = arcs;
        _nodeWeights = nodeWeights;
        _nodeNames = nodeNames;
        _graphName = graphName;

        execute();
    }

    /**
     * Getter for the number of processing cores used for scheduling
     * @return number of processing cores used
     */
    protected int getProcessingCores() {
        return _processingCores;
    }

    /**
     * Getter for the associated ScheduleResultListener
     * @return listener associated with this algorithm manager
     */
    protected List<ScheduleResultListener> getListeners() {
        return listeners;
    }
}
