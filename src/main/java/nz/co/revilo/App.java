package nz.co.revilo;

import com.beust.jcommander.JCommander;
import javafx.application.Application;
import nz.co.revilo.CommandLine.CLIParameters;
import nz.co.revilo.Gui.MainLauncher;
import nz.co.revilo.Input.DotFileReader;
import nz.co.revilo.Input.FileParser;
import nz.co.revilo.Input.GxlFileReader;
import nz.co.revilo.Output.DotFileProducer;
import nz.co.revilo.Output.DotFileWriter;
import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.BranchAndBoundAlgorithmManager;
import nz.co.revilo.Scheduling.ParallelBranchAndBoundAlgorithmManager;
import pt.runtime.ParaTask;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * App is the main class using the singleton pattern and is used to take the command line arguments and co-ordinate
 * everything. It's not a final class name nor implementation, it purely exists to be a starting point in the program.
 * We should investigate a argument input library and output library.
 *
 * @author Mohan Cao (file created by), Michael Kemp (pattern and fleshed out), Terran Kroft (CLI library, visualization integration), Abby Shen
 * @version 1.0
 */
public class App {
    // Constants
    public static final String DEFAULT_FILE_EXTENSION = ".dot";
    public static final String DEFAULT_OUTPUT_FILENAME = "-output.dot";
    public static final double MILLISECONDS_PER_SECOND = 1000.0;
    public static final int MINIMUM_EXPECTED_ARGUMENTS = 2;
    public static final int FILENAME_ARGUMENT_PLACEMENT = 0;
    public static final int LENGTH_OF_DOT_FILE_EXTENSION = 4;
    public static final int MIN_NUM_OF_PROBLEM_PROCESSORS = 1;

    // Instance of the singleton pattern
    private static App _inst = null;

    // Command line interface arguments
    private String _inputFilename;
    private int _numExecutionCores;
    private int _numParallelProcessors; //for parallelisation
    private boolean _visualise;
    private String _outputFilename;

    private static long _startingTime;
    private static long _endingTime;
    private static boolean _isDone;

    // Fields
    private static AlgorithmManager _manager;
    private static FileParser _reader;
    private static DotFileProducer _output;


    /**
     * The one and only constructor which allows for the singleton pattern by never overriding the current instance
     *
     * @author Michael Kemp
     */
    private App() {
        // If there's no current instance then it's instantiated
        if (_inst == null) {
            _inst = this;
        }
    }

    /**
     * Gets the algorithm manager we are using for visualization purposes.
     * @author Terran Kroft
     * @return current algorithm manager
     */
    public static AlgorithmManager getAlgorithmManager() {
        return _manager;
    }

    /**
     * Get the number of processors used
     * @author Terran Kroft
     * @return num cores to execute on
     */
    public static int getExecCores() {
        return _inst._numExecutionCores;
    }

    /**
     * Get the file name of the graph dot/gxl file
     * @author Terran Kroft
     * @return input file name
     */
    public static String getInputFileName() {
        return _inst._inputFilename;
    }

    public static int getNumParallelCores() {
        return _inst._numParallelProcessors;
    }

    /**
     * Get the length that the algorithm has been running for, unless it is
     * already done, then get the final time
     * @author Terran Kroft
     * @return Time spent on solving
     */
    public static double getRunningTime() {
        double elapsed;
        if (_isDone) {
            elapsed = ((_endingTime - _startingTime) / MILLISECONDS_PER_SECOND); // incl. tenths of second
        } else {
            long now = System.currentTimeMillis();
            elapsed = ((now - _startingTime) / MILLISECONDS_PER_SECOND); // incl. tenths of second
        }
        return elapsed;
    }

    /**
     * Gets the current running instance for visualization purposes
     * @author Terran Kroft
     * @return App instance
     */
    public static App getInstance() {
        return _inst;
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
     * @author Michael Kemp, Terran Kroft (visualisation and later implementation of CLI)
     *
     * @param args CLI args
     */
    public static void main(String[] args) {
        // Creates the singleton instance
        new App();

        // Process arguments given by the user
        processArguments(args);
        
        // Start an AlgorithmManager
        if(_inst._numParallelProcessors > 2) { // Don't want PT on two threads, since all it's doing is making a redundant thread
            // Subtract one from the input number of processors to account for the master in the
            // parallelisation methods used in ParallelBranchAndBoundAlgorithmManager, where
            // there will always be one thread allocating to other threads
            ParaTask.setThreadPoolSize(ParaTask.ThreadPoolType.ALL, _inst._numParallelProcessors - 1);
            ParaTask.init();
        	_manager = new ParallelBranchAndBoundAlgorithmManager(_inst._numExecutionCores,  _inst._numParallelProcessors - 1);
        } else {
        	_manager = new BranchAndBoundAlgorithmManager(_inst._numExecutionCores);
        }

        // Parse file and give it algorithm manager to give results to depending on the file extension
        if (_inst._inputFilename.toUpperCase().matches(".*GXL")) {
            _reader = new GxlFileReader(_inst._inputFilename);
        } else {
            _reader = new DotFileReader(_inst._inputFilename);
        }

        // Output to file by letting the manager know of the output generator
        _output = new DotFileWriter(_inst._outputFilename);
        _manager.inform(_output);

        //Launch GUI if visualization is desired, otherwise just start parsing.
        if (_inst._visualise) {
            Application.launch(MainLauncher.class);

        } else {
            startParsing();
        }
    }

    /**
     * Takes the input arguments given by the user and processes them: input filename, processors for problem,
     * visualisation, threads to execute on and name of the output file
     *
     * @param args from CLI
     * @author Terran Kroft, Michael Kemp
     */
    private static void processArguments(String[] args) {
        // Instantiates a new parameters container
        CLIParameters params = new CLIParameters();

        // Checks for an insufficient number of arguments
        if (args.length < MINIMUM_EXPECTED_ARGUMENTS) {
            throw new RuntimeException("Insufficient arguments given. Needs [input file] [# processors]");
        } else {

            // Parses the arguments
            String[] optionalArgs = Arrays.copyOfRange(args, MINIMUM_EXPECTED_ARGUMENTS, args.length);
            JCommander.newBuilder().addObject(params).build().parse(optionalArgs);

            // Set the input filename
            _inst._inputFilename = args[FILENAME_ARGUMENT_PLACEMENT];

            // Set the number of cores the problem has
            try {
                _inst._numExecutionCores = Integer.parseInt(args[1]);
                if (_inst._numExecutionCores < MIN_NUM_OF_PROBLEM_PROCESSORS) {
                    throw new RuntimeException("Insufficient processors to solve problem");
                }
            } catch (NumberFormatException nfe) {
                throw new RuntimeException("Invalid number of processors");
            }

            // Sets the number of cores to do the scheduling on
            _inst._numParallelProcessors = params.getParallelCores();
            if (params.getParallelCores() < MIN_NUM_OF_PROBLEM_PROCESSORS) {
                throw new RuntimeException("Need to allocate more threads for program to be able to run");
            }

            // Sets the visualisation switch
            _inst._visualise = params.getVisualise();

            // Sets the output filename if one is given, otherwise uses default
            if (params.getOutputName() == null) {
                String workingInputFilename = _inst._inputFilename;

                // If it's a GXL input file then the output filename will need to remain a .dot file
                if (workingInputFilename.toUpperCase().endsWith(".GXL")) {
                    workingInputFilename = _inst._inputFilename.substring(0, _inst._inputFilename.length() - LENGTH_OF_DOT_FILE_EXTENSION) + ".dot";
                }

                // Append input file name with default suffix
                int fileNameLocation = workingInputFilename.toLowerCase().lastIndexOf(DEFAULT_FILE_EXTENSION);
                String fileNameWithoutExtension = workingInputFilename.substring(0, fileNameLocation);
                _inst._outputFilename = fileNameWithoutExtension + DEFAULT_OUTPUT_FILENAME;

                // Use given filename if available
            } else {
                _inst._outputFilename = params.getOutputName();
            }
        }
    }

    /**
     * Algorithm to set up parsing of scheduling
     * @author Terran Kroft
     */
    public static void startParsing() {
        try {
            _isDone = false;
            _startingTime = System.currentTimeMillis();
            _reader.startParsing(_manager);
            _endingTime = System.currentTimeMillis();
            _isDone = true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Input file does not exist");
        }
    }
}