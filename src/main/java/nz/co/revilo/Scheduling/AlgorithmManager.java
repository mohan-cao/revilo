package nz.co.revilo.Scheduling;

import nz.co.revilo.Input.ParseResultListener;
import nz.co.revilo.Output.ScheduleResultListener;

/**
 * Abstract class defining the data structures, and information to be used when implementing any algorithms as part of a
 * scheduling solution.
 */
public abstract class AlgorithmManager implements ParseResultListener {

    private int _processingCores;
    private ScheduleResultListener _listener;
    int[] _nodeWeights;
    boolean[][] _arcs;
    int[][] _arcWeights;
    String[] _nodeNames;
    String _graphName;

    /**
     * Sets the number of processing cores the tasks must be scheduled on.
     * @param processingCores number of cores specified for the final schedule
     */
    public AlgorithmManager(int processingCores) {
        _processingCores = processingCores;
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
    public void inform(ScheduleResultListener listener) {
        _listener = listener;
    }

    public void ParsingResults(String graphName, String[] nodeNames, int[] nodeWeights, boolean[][] arcs, int[][] arcWeights) {


    /**
     * Template method for reading in graph information required to process a schedule, and executes the schedule (using
     * the execute() hook method.
     * @param nodeWeights
     * @param arcs
     * @param arcWeights
     */
    public void ParsingResults(int[] nodeWeights, boolean[][] arcs, int[][] arcWeights) {
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
    protected ScheduleResultListener getListener() {
        return _listener;
    }
}
