package nz.co.revilo.Output;


import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class DotFileProducer implements ScheduleResultListener {

    public static final String CHAR_SET = "UTF-8";

    private String _outputFilename;
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

        try {
            produceOutput(new PrintWriter(_outputFilename, CHAR_SET));
        } catch (UnsupportedEncodingException e) {
            //TODO
        } catch (FileNotFoundException e) {
            //TODO
        }
    }

    public DotFileProducer(String outputFilename) {
        _outputFilename = outputFilename;
    }

    protected abstract void produceOutput(PrintWriter output);
}
