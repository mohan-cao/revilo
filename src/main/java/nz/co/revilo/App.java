package nz.co.revilo;

import nz.co.revilo.Input.DotFileReader;
import nz.co.revilo.Output.DotFileProducer;
import nz.co.revilo.Output.DotFileWriter;
import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.SchedulingAlgorithmManager;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.FileNotFoundException;

/**
 * App is the main class using the singleton pattern and is used to take the command line arguments and co-ordinate
 * everything. It's not a final class name nor implementation, it purely exists to be a starting point in the program.
 * We should investigate a argument input library and output library.
 *
 * @author Mohan Cao (original), Michael Kemp
 * @version alpha
 */
public class App {

    private static App _inst = null;

    public static final String REGEX_VISUALISATION = "-[vV]*";
    public static final String REGEX_OUTPUT_FILENAME = "-[oO]*";
    public static final String REGEX_EXFECUTION_CORES = "-[pP]*";
    public static final boolean DEFAULT_VISUALISATION = false;
    public static final String DEFAULT_OUTPUT_FILENAME_EXTENSION = "−output.dot";
    public static final int DEFAULT_EXECUTION_CORES = 1;

    private int _numExecutionCores;
    private int _numParrallelProcessors;
    private boolean _visualise;
    private String _outputFilename;
    private String _inputFilename;

    private App () {
        if (_inst.equals(null)) {
            _inst = this;
        } else {
            System.out.println("App was instantiated more than one");
            //TODO
        }
    }

    public static void main( String[] args ) {
        new App();

        //Input arguments @Michael Kemp
        if (args.length >= 2) {
            //Input filename and number of processors for the algorithm
            _inst._inputFilename = args[0];
            _inst._numParrallelProcessors = Integer.parseInt(args[1]);
            _inst._outputFilename = _inst._inputFilename + DEFAULT_OUTPUT_FILENAME_EXTENSION;

            //Visualisation
            _inst._visualise = DEFAULT_VISUALISATION; //Default setting
            for (int i = 2; i < args.length; i++) {
                if (args[i].matches(REGEX_VISUALISATION)) {
                    _inst._visualise = true;
                }
            }

            //Number of execution cores
            _inst._numExecutionCores = DEFAULT_EXECUTION_CORES; //Default setting
            for (int j = 2; j < args.length - 1; j++) {
                if (args[j].matches(REGEX_EXFECUTION_CORES)) {
                    try {
                        _inst._numExecutionCores = Integer.parseInt(args[j + 1]);
                    } catch (NumberFormatException e) {
                        //TODO
                        System.out.println("Please only use digits and a value less than 2 billion for the number of cores argument");
                    }
                }
            }

            //Output Filename
            for (int k = 2; k < args.length - 1; k++) {
                if (args[k].matches(REGEX_OUTPUT_FILENAME)) {
                    _inst._outputFilename = args[k+1];
                }
            }
        } else {
            System.out.println("Please check argument usage, a filename then number of processors is required");
        }

        // Parse file and give it algorithm manager to give results to. @Michael Kemp
        AlgorithmManager manager = new SchedulingAlgorithmManager(_inst._numExecutionCores);
        DotFileReader reader = new DotFileReader(_inst._inputFilename);
        try {
            reader.startParsing(manager);
        } catch (FileNotFoundException e) {
            //TODO
            System.out.println("INPUT FILE NOT FOUND");
        }

        // Output to file @Michael Kemp
        DotFileProducer output = new DotFileWriter(_inst._outputFilename);
        manager.inform(output);


        //Mohan's stuff
        Graph graph = new SingleGraph("Tutorial 1");
        graph.addNode("A" );
        graph.addNode("B" );
        graph.addNode("C" );
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.display();
    }
}
