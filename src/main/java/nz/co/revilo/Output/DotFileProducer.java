package nz.co.revilo.Output;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public abstract class DotFileProducer implements ScheduleResultListener {

    public static final String CHAR_SET = "UTF-8";

    protected String _outputFilename;
    protected String _graphName;
    protected List<String> _nodeNames;
    protected Map<Integer, Map<Integer, Integer>> _arcs;
    protected List<Integer> _nodeWeights;
    protected List<Integer> _nodeStarts;
    protected List<Integer> _nodeProcessor;

    @Override
    public final void finalSchedule(String graphName, List<String> nodeNames, Map<Integer, Map<Integer, Integer>> arcs, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor) {
        _graphName = graphName;
        _nodeNames = nodeNames;
        _arcs = arcs;
        _nodeWeights = nodeWeights;
        _nodeStarts = nodeStarts;
        _nodeProcessor = nodeProcessor;

        try {
            PrintWriter pw = new PrintWriter(_outputFilename, CHAR_SET);
            produceOutput(pw);
            pw.flush();
            pw.close();
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
