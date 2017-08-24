package nz.co.revilo.Output;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Abstract class for reading in information from the final schedule which is required in producing an output file. The
 * actual creation of the output is left to child classes.
 *
 * @author Michael Kemp
 * @version 1.0
 */
public abstract class DotFileProducer implements ScheduleResultListener {

    // Character set to write the file in
    public static final String CHAR_SET = "UTF-8";

    // Data structure to accept and write from scheduling manager
    protected String _outputFilename;
    protected String _graphName;
    protected List<String> _nodeNames;
    protected List<List<Boolean>> _arcs;
    protected List<List<Integer>> _arcWeights;
    protected List<Integer> _nodeWeights;
    protected List<Integer> _nodeStarts;
    protected List<Integer> _nodeProcessor;

    /**
     * Accepts data about how tasks are going to be run on each processor and at what time then calls produceOutput()
     *
     * @param graphName     Name of graph
     * @param nodeNames     Name of each task
     * @param arcs          The existence of arcs
     * @param arcWeights    How long to transfer results of task completion to another processor
     * @param nodeWeights   How long each task takes
     * @param nodeStarts    When each task starts
     * @param nodeProcessor What processor each task is on
     */
    @Override
    final public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor) {
        // Sets data structure
        _graphName = graphName;
        _nodeNames = nodeNames;
        _arcs = arcs;
        _arcWeights = arcWeights;
        _nodeWeights = nodeWeights;
        _nodeStarts = nodeStarts;
        _nodeProcessor = nodeProcessor;
        System.out.println("This is notified!");
        // Attempts to print the graph to a file
        try {
            PrintWriter pw = new PrintWriter(_outputFilename, CHAR_SET);
            produceOutput(pw);
            pw.flush();
            pw.close();
        } catch (UnsupportedEncodingException e) {
            //TODO Error handling
        } catch (FileNotFoundException e) {
            //TODO Error handling
        }
    }

    /**
     * DotFileProducer constructor which sets the file name of the output schedule DOT file.
     *
     * @param outputFilename
     */
    public DotFileProducer(String outputFilename) {
        _outputFilename = outputFilename;
        if (_outputFilename.toUpperCase().endsWith(".DOT")) {
            _outputFilename = outputFilename + ".dot";
        }
    }

    /**
     * Abstract method for producing an output file in DOT file format (to be implemented by child classes).
     *
     * @param output PrintWriter set up and used to create the new DOT file, and print output to it
     */
    protected abstract void produceOutput(PrintWriter output);
}
