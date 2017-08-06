package nz.co.revilo.Output;

import nz.co.revilo.Scheduling.AlgorithmManager;

import java.io.BufferedOutputStream;
import java.util.List;

public abstract class DotFileProducer implements ScheduleResultListener {

    private String _outputFilename;
    private AlgorithmManager _manager;

    @Override
    public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor) {
        produceOutput(new BufferedOutputStream(System.out));
    }

    public DotFileProducer(String outputFilename) {
        _outputFilename = outputFilename;
    }

    protected String getOutputFilename() {
        return _outputFilename;
    }

    protected abstract void produceOutput(BufferedOutputStream output);
}
