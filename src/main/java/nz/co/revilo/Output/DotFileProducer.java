package nz.co.revilo.Output;


import nz.co.revilo.Input.DotFileParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class DotFileProducer implements ScheduleResultListener {

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

        PrintWriter pw = createPrintWriter();
        produceOutput(pw);
        pw.flush();
        pw.close();
    }

    protected DotFileProducer(String outputFilename) {
        _outputFilename = outputFilename;
    }

    public abstract PrintWriter createPrintWriter();

    protected abstract void produceOutput(PrintWriter output);
}
