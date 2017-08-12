package nz.co.revilo.Output;


import nz.co.revilo.Input.DotFileParser;

import java.io.*;
import java.util.List;

/**
 * Abstract class where child classes will implement some form of output createion based on the final schedule
 * received from an AlgorithmManager.
 * @author Michael
 * @version alpha
 */
public abstract class OutputProducer implements ScheduleResultListener {

    public static final String CHAR_SET = "UTF-8";

    protected String _outputFilename;
    protected String _graphName;
    protected List<String> _nodeNames;
    protected List<List<Boolean>> _arcs;
    protected List<List<Integer>> _arcWeights;
    protected List<Integer> _nodeWeights;
    protected List<Integer> _nodeStarts;
    protected List<Integer> _nodeProcessor;

    @Override
    final public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor) {
        _graphName = graphName;
        _nodeNames = nodeNames;
        _arcs = arcs;
        _arcWeights = arcWeights;
        _nodeWeights = nodeWeights;
        _nodeStarts = nodeStarts;
        _nodeProcessor = nodeProcessor;

        Writer pw = createWriter();
        produceOutput(pw);
        try {
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputProducer(String outputFilename) {
        _outputFilename = outputFilename;
    }

    protected abstract Writer createWriter();

    protected abstract void produceOutput(Writer output);
}
