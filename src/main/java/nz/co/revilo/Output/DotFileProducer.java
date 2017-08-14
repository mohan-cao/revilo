package nz.co.revilo.Output;


import nz.co.revilo.Input.DotFileParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Abstract class for reading in information from the final schedule which is required in producing an output file. The
 * actual creation of the output is left to child classes.
 */
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

    /**
     * DotFileProducer constructor which sets the file name of the output schedule DOT file.
     * @param outputFilename
     */
    public DotFileProducer(String outputFilename) {
        _outputFilename = outputFilename;
    }

    /**
     *  Abstract method for producing an output file in DOT file format (to be implemented by child classes).
     * @param output PrintWriter set up and used to create the new DOT file, and print output to it
     */
    protected abstract void produceOutput(PrintWriter output);
}
