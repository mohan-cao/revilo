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
    private ScheduleResultListener _listener;
    private NewOptimalResultListener optimalListener;
    private List<ScheduleResultListener> listeners = new ArrayList<>();;
    int[] _nodeWeights;
    boolean[][] _arcs;
    int[][] _arcWeights;
    String[] _nodeNames;
    String _graphName;

    long brokenTrees;
    int upperBound;
    Schedule optimalSchedule;

    /**
     * Sets the number of processing cores the tasks must be scheduled on.
     * @param processingCores number of cores specified for the final schedule
     */
    public AlgorithmManager(int processingCores) {
        _processingCores = processingCores;
        brokenTrees = 0;
    }

    public long getBrokenTrees() {
        return brokenTrees;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public String getGraphName() {
        return _graphName;
    }

    public List<String> getNodeNames(){
        return new ArrayList<>(Arrays.asList(_nodeNames));
    }

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
     * produced by the algorithm implementation (to use in output file creation).
     * @param listener
     */
//    public void inform(ScheduleResultListener listener) {
//        _listener = listener;
//    }

    public void inform(ScheduleResultListener listener) {
        listeners.add(listener);
    }
    public void optimalInform(NewOptimalResultListener listener) { optimalListener = listener; }
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
