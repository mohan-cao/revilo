package nz.co.revilo.Scheduling;

import nz.co.revilo.Input.ParseResultListener;
import nz.co.revilo.Output.ScheduleResultListener;

public abstract class AlgorithmManager implements ParseResultListener {

    private int _processingCores;
    private ScheduleResultListener _listener;
    int[] _nodeWeights;
    boolean[][] _arcs;
    int[][] _arcWeights;


    public AlgorithmManager(int processingCores) {
        _processingCores = processingCores;
    }

    protected abstract void execute();//TODO

    public void inform(ScheduleResultListener listener) {
        _listener = listener;
    }
    
    public void ParsingResults(int[] nodeWeights, boolean[][] arcs, int[][] arcWeights) {
        _arcWeights = arcWeights;
        _arcs = arcs;
        _nodeWeights = nodeWeights;
        execute();
    }

    protected int getProcessingCores() {
        return _processingCores;
    }

    protected ScheduleResultListener getListener() {
        return _listener;
    }
}
