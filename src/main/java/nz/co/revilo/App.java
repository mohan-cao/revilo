package nz.co.revilo;

import com.beust.jcommander.JCommander;
import nz.co.revilo.CommandLine.Parameters;
import nz.co.revilo.Input.DotFileGraphReader;
import nz.co.revilo.Input.DotFileReader;
import nz.co.revilo.Output.DotFileProducer;
import nz.co.revilo.Output.DotFileWriter;
import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.VeryBasicAlgorithmManager;

import java.io.*;
import java.util.Arrays;

/**
 * App is the main class using the singleton pattern and is used to take the command line arguments and co-ordinate
 * everything. It's not a final class name nor implementation, it purely exists to be a starting point in the program.
 * We should investigate a argument input library and output library.
 *
 * @author Mohan Cao (original), Michael Kemp, Terran Kroft
 * @version alpha
 */
public class App {

    private static App _inst = null;

    private String _inputFilename;
    private int _numExecutionCores;
    private int _numParallelProcessors;
    private boolean _visualise;
    private String _outputFilename;
    private static String DEFAULT_FILETYPE = ".dot";
    private static String DEFAULT_OUTPUT_FILENAME = "-output.dot";




    private App () {
        if (_inst == null) {
            _inst = this;
        } else {
            System.out.println("App was instantiated more than once");
            //TODO
        }
    }

    public static void main( String[] args ) {
        new App();
        Parameters params = new Parameters();
        JCommander jc = new JCommander();

        //Section to display the consumed args
        int i = 0;
        for (String s: args) {
            System.out.println(i + ": " + s);
            i++;

        }

        if (args.length < 2) {
            //insufficient arguments
            throw new RuntimeException("Insufficient arguments given. Needs [input file] [# processors]");
        } else {
            String[] optionalArgs = Arrays.copyOfRange(args, 2, args.length);
            JCommander.newBuilder().addObject(params).build().parse(optionalArgs);

            //get file name first


            _inst._inputFilename = args[0];

            if(params.getHelp()) {
               showHelp();
            }

            try {
                _inst._numExecutionCores = Integer.parseInt(args[1]);
            }catch(NumberFormatException nfe){
                throw new RuntimeException("Invalid number of processors");
            }
            _inst._numParallelProcessors = params.getParallelCores();
            _inst._visualise = params.getVisualise();

            if (params.getOutputName() == null) {
                int fileNameLocation = _inst._inputFilename.toLowerCase().lastIndexOf(DEFAULT_FILETYPE);
                String fileNameWithoutExtension = _inst._inputFilename.substring(0, fileNameLocation);
                _inst._outputFilename = fileNameWithoutExtension + DEFAULT_OUTPUT_FILENAME;
            } else {
                _inst._outputFilename = params.getOutputName();
            }
        }

        //here we get the actual input name (.dot)


        System.out.println("This is the schedule called " + _inst._outputFilename + " processed on " + _inst._numParallelProcessors + " core(s).");
        if (_inst._visualise) {
            System.out.println("There is a visualisation outputted.");
        }

        // Parse file and give it algorithm manager to give results to. @Michael Kemp

        AlgorithmManager manager = new VeryBasicAlgorithmManager(_inst._numExecutionCores);
        //AlgorithmManager manager = new SchedulingAlgorithmManager(_inst._numExecutionCores);
        DotFileReader reader = new DotFileReader(_inst._inputFilename);
        // Output to file @Michael Kemp
        DotFileProducer output = new DotFileWriter(_inst._outputFilename);
        manager.inform(output);
        try {
            reader.startParsing(manager);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Input file does not exist");
        }




        //Mohan's stuff
        //        Graph graph = new SingleGraph("Tutorial 1");
        //        graph.addNode("A" );
        //        graph.addNode("B" );
        //        graph.addNode("C" );
        //        graph.addNode("D" );
        //        graph.addNode("E" );
        //        graph.addEdge("AB", "A", "B");
        //        graph.addEdge("BC", "B", "C");
        //        graph.addEdge("CD", "C", "D");
        //        graph.addEdge("DE", "D", "E");
        //        graph.addEdge("EA", "E", "A");
        //        graph.addEdge("AC", "A", "C");
        //        graph.addEdge("AD", "A", "D");
        //        graph.addEdge("BD", "B", "D");
        //        graph.addEdge("BE", "B", "E");
        //        graph.addEdge("CE", "C", "E");
        //        graph.display();
    }

    private static void showHelp(){
        System.out.println(
                "\n Welcome to Revilo Task Scheduler. " +
                        "\n\n To use our scheduler, please provide the arguments [input file] [# processors to " +
                        "schedule on] " +
                        "\n \n Optional Flags: " +
                        "\n\t\"--parallel\", \"-p\" allows the specification of the number of processors " +
                        "to use to when running our scheduler " +
                        "\n\t \"--visualise\", \"--visualize\", \"-v\" specifies if visualisation of current " +
                        "progress should be shown" +
                        "\n\t \"--output\", \"-o\" allows for the specification of a custom name for the output " +
                        "file" +
                        "\n\t \"--help\", \"-h\" specifies that this help message should be displayed." +
                        "\n"
        );
    }
}