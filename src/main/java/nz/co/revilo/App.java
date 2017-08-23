package nz.co.revilo;

import com.beust.jcommander.JCommander;
import nz.co.revilo.CommandLine.CLIParameters;
import nz.co.revilo.Input.DotFileReader;
import nz.co.revilo.Output.DotFileProducer;
import nz.co.revilo.Output.DotFileWriter;
import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.AstartFirstAlgorithmManager;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * App is the main class using the singleton pattern and is used to take the command line arguments and co-ordinate
 * everything. It's not a final class name nor implementation, it purely exists to be a starting point in the program.
 * We should investigate a argument input library and output library.
 *
 * @author Mohan Cao (file created by), Michael Kemp (pattern and fleshed out), Terran Kroft (Modified for CLI library), Abby Shen
 * @version alpha
 */
public class App {
    public static final String DEFAULT_FILETYPE = ".dot";
    public static final String DEFAULT_OUTPUT_FILENAME = "-output.dot";

    // Instance of the singleton pattern
    private static App _inst = null;

    // Command line interface arguments
    private String _inputFilename;
    private int _numExecutionCores;
    private int _numParallelProcessors; //for parallelisation
    private boolean _visualise;
    private String _outputFilename;

    /**
     * The one and only constructor which allows for the singleton pattern by never overriding the current instance
     *
     * @author Michael Kemp
     */
    private App() {
        // If there's no current instance then it's instantiated
        if (_inst == null) {
            _inst = this;
            // If there is then throw a warning
        } else {
            System.out.println("App was instantiated more than once");
            //TODO throw an informative exception to indicate error
        }
    }

    /**
     * The entry point for the program, the only arguments are those from the CLI
     * <p>
     * Creates an instance
     * Parses the arguments
     * Starts visualisation
     * Starts parsing the input file
     * Instantiates an algorithm manager to be informed of read graph
     * Instantiates an output file writer to be informed of schedule
     *
     * @param args CLI args
     */
    public static void main(String[] args) {
        // Creates the singleton instance
        new App();

        // Instantiates a new parameters container
        CLIParameters params = new CLIParameters();

        // Checks for an insufficient number of arguments
        if (args.length < 2) {
            throw new RuntimeException("Insufficient arguments given. Needs [input file] [# processors]");
        } else {
            // Parses the arguments
            String[] optionalArgs = Arrays.copyOfRange(args, 2, args.length);
            JCommander.newBuilder().addObject(params).build().parse(optionalArgs);

            // Set the input filename
            _inst._inputFilename = args[0];
            try {
                // Set the number of cores the problem has
                _inst._numExecutionCores = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException("Invalid number of processors");
            }
            // Sets the number of cores to do the scheduling on
            _inst._numParallelProcessors = params.getParallelCores();
            // Sets the visualisation switch
            _inst._visualise = params.getVisualise();

            // Sets the output filename if one is given, otherwise uses default
            if (params.getOutputName() == null) {
                int fileNameLocation = _inst._inputFilename.toLowerCase().lastIndexOf(DEFAULT_FILETYPE);
                String fileNameWithoutExtension = _inst._inputFilename.substring(0, fileNameLocation);
                _inst._outputFilename = fileNameWithoutExtension + DEFAULT_OUTPUT_FILENAME;
            } else {
                _inst._outputFilename = params.getOutputName();
            }
        }

        // Starts visualisation if requested
//        System.out.println("This is the schedule called " + _inst._outputFilename + " processed on " + _inst._numParallelProcessors + " core(s).");
        if (_inst._visualise) {
//            System.out.println("There is a visualisation outputted.");
        }

        // Parse file and give it algorithm manager to give results to. @Michael Kemp
        AlgorithmManager manager = new AstartFirstAlgorithmManager(_inst._numExecutionCores);
        DotFileReader reader = new DotFileReader(_inst._inputFilename);

        // Output to file @Michael Kemp
        DotFileProducer output = new DotFileWriter(_inst._outputFilename);
        manager.inform(output);
        try {
            reader.startParsing(manager);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Input file does not exist");
        }
    }
}