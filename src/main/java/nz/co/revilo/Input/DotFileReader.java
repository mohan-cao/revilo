package nz.co.revilo.Input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DotFileReader is a class that extends FileParser it's purpose is to read the file and place the digraph in to a
 * data-structure and give it to a ParseResultListener.
 *
 * @author Michael Kemp
 * @version 1.0
 */
public class DotFileReader extends FileParser {

    // Regex for string matchers that will extract the required information from the string
    public static final Pattern GRAPH_NAME_MATCH = Pattern.compile("[\\s]*digraph[\\s]*\"(.*)\"[\\s]*\\{[\\s]*");
    public static final Pattern ARC_FROM_MATCH = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*][\\s]*;");
    public static final Pattern ARC_TO_MATCH = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*][\\s]*;");
    public static final Pattern ARC_WEIGHT_MATCH = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*][\\s]*;");
    public static final Pattern NODE_NAME_MATCH = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*][\\s]*;");
    public static final Pattern NODE_WEIGHT_MATCH = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*][\\s]*;");

    // Regex for string matchers of line types
    public static final String ARC_LINE_MATCH = "[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*][\\s]*;";
    public static final String NODE_LINE_MATCH = "[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*][\\s]*;";
    public static final String GRAPH_NAME_LINE_MATCH = "[\\s]*digraph[\\s]*\".*\"[\\s]*\\{[\\s]*";
    public static final String END_OF_DOT_FILE_MATCH = "[\\s]*}[\\s]*";

    // Default weight for arcs and nodes if not defined
    public static final int DEFAULT_WEIGHT = -1;

    // First match found by matcher
    public static final int FIRST_MATCH = 1;
    public static final int FILE_CANT_BE_READ_EXIT_STATUS = 1;

    private HashMap<String, Integer> _nodeNames;
    private List<Integer> _nodeWeights;
    private HashMap<String, HashMap<String, Integer>> _arcs;
    private String[] _nodeNamesList;
    private String _graphName;

    private ParseResultListener _listener;

    /**
     * Constructs a DotFileReader with the filename to read
     *
     * @author Michael Kemp
     * @param filename is the name of the file to open
     */
    public DotFileReader(String filename) {
        super(filename);
    }

    /**
     * Initiates reading the file and interpreting it as a DAG then informs the listener of the read graph data.
     *
     * @author Michael Kemp
     * @param newListener To inform of parsing results
     * @throws FileNotFoundException Thrown if the file name given doesn't exist
     */
    public void startParsing(ParseResultListener newListener) throws FileNotFoundException {
        // Sets fields
        _listener = newListener;
        BufferedReader reader = openFile();

        // Initialises the data structures
        _nodeNames = new HashMap<>();
        _nodeWeights = new ArrayList<>();
        _arcs = new HashMap<>();

        /*
         Reads the file using regex by checking the line is of an expected format then extracts information from the
         lines by matching with regex
          */
        try {
            // Read the first line to get started
            String line = reader.readLine();

            // Continue reading the file if there's a next line and the current line isn't a closing line
            while ((line != null) && !line.contains(END_OF_DOT_FILE_MATCH)) {
                // Arcs
                if (line.matches(ARC_LINE_MATCH)) {
                    createArc(line);

                    // Nodes
                } else if (line.matches(NODE_LINE_MATCH)) {
                    createNode(line);

                    // Graph name
                } else if (line.matches(GRAPH_NAME_LINE_MATCH)) {
                    graphName(line);
                }

                // Reads the next line in the file
                line = reader.readLine();
            }

            // Problems reading the file
        } catch (IOException e) {
            System.out.println("File could not be read");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.exit(FILE_CANT_BE_READ_EXIT_STATUS);
        }

        // Converts nodes from the generic type data structures to a primitive form
        String[] nodeNamesPrimitive = new String[_nodeWeights.size()];
        int[] nodeWeightsPrimitive = new int[_nodeWeights.size()];
        List<String> tempNames = new ArrayList<>(_nodeNames.keySet());
        _nodeNamesList = new String[_nodeWeights.size()];
        // For every node copies information into new structure
        for (int i = 0; i < _nodeWeights.size(); i++) {
            String tempName = tempNames.get(i);
            int location = _nodeNames.get(tempName);
            _nodeNamesList[location] = tempName;
            nodeWeightsPrimitive[location] = _nodeWeights.get(location);
            nodeNamesPrimitive[location] = tempName;
        }

        // Converts arcs from the generic type data structures to a primitive form
        boolean[][] arcsPrimitive = new boolean[_nodeWeights.size()][_nodeWeights.size()];
        int[][] arcWeightsPrimitive = new int[_nodeWeights.size()][_nodeWeights.size()];
        // From every node to every node (self included) copies the weights and if it doesn't exist DEFAULT_WEIGHT is used
        for (int from = 0; from < nodeNamesPrimitive.length; from++) {
            // Any arc that doesn't exist has a DEFAULT_WEIGHT default weight
            Arrays.fill(arcWeightsPrimitive[from], DEFAULT_WEIGHT);
            for (int to = 0; to < nodeNamesPrimitive.length; to++) {
                // If the from node
                if (_arcs.containsKey(nodeNamesPrimitive[from])) {
                    // Goes to the to node
                    if (_arcs.get(nodeNamesPrimitive[from]).containsKey(nodeNamesPrimitive[to])) {
                        // Copy the weight
                        arcsPrimitive[from][to] = true;
                        arcWeightsPrimitive[from][to] = _arcs.get(nodeNamesPrimitive[from]).get(nodeNamesPrimitive[to]);
                    }
                }
            }
        }

        // Informs the listener about the freshly read graph
        _listener.ParsingResults(_graphName, _nodeNamesList, nodeWeightsPrimitive, arcsPrimitive, arcWeightsPrimitive);
    }

    /**
     * Gets the graph name from the current line
     *
     * @param line
     * @author Michael Kemp
     */
    private void graphName(String line) {
        // Extracts the graph name from the line
        Matcher m = GRAPH_NAME_MATCH.matcher(line);
        m.find();
        // Sets the graph name
        _graphName = m.group(FIRST_MATCH);
    }

    /**
     * Creates a node with a weight based on the line
     *
     * @param line
     * @autho Michael Kemp
     */
    private void createNode(String line) {
        // Extracts the node name from the line
        Matcher m = NODE_NAME_MATCH.matcher(line);
        m.find();
        String name = m.group(FIRST_MATCH);
        // Extracts the node weight from the line
        m = NODE_WEIGHT_MATCH.matcher(line);
        m.find();
        int weight = Integer.parseInt(m.group(FIRST_MATCH));
        // If the node exists the weight is updated else it is created and weight is recorded
        if (_nodeNames.containsKey(name)) {
            _nodeWeights.set(_nodeNames.get(name), weight);
        } else {
            _nodeNames.put(name, _nodeWeights.size());
            _nodeWeights.add(weight);
        }
    }

    /**
     * Creates an arc and if need be the nodes in the arc too (with invalid weights) based on the line
     *
     * @param line
     * @author Michael Kemp
     */
    private void createArc(String line) {
        // Extracts information about the arc (from, to and weight)
        // Extracts which node the arc is from
        Matcher m = ARC_FROM_MATCH.matcher(line);
        m.find();
        String from = m.group(FIRST_MATCH);
        // Extracts which node the arc is to
        m = ARC_TO_MATCH.matcher(line);
        m.find();
        String to = m.group(FIRST_MATCH);
        // Extracts the weight of the arc
        m = ARC_WEIGHT_MATCH.matcher(line);
        m.find();
        int weight = Integer.parseInt(m.group(FIRST_MATCH));

        // Places information about the arc into data structures
        // If the from node isn't defined yet then it is temporarily created with a negative weight
        if (!_nodeNames.containsKey(from)) {
            _nodeWeights.set(_nodeNames.get(from), DEFAULT_WEIGHT);
        }
        // If the to node isn't defined yet then it is temporarily created with a negative weight
        if (!_nodeNames.containsKey(to)) {
            _nodeWeights.set(_nodeNames.get(to), DEFAULT_WEIGHT);
        }
        // If the from node doesn't have an arc list then one is made
        if (!_arcs.containsKey(from)) {
            _arcs.put(from, new HashMap<>());
        }
        // Replaces the weight information in the arc if it already exists otherwise creates the arc
        if (!_arcs.get(from).containsKey(to)) {
            _arcs.get(from).put(to, weight);
        } else {
            _arcs.get(from).replace(to, weight);
        }
    }

    /**
     * Opens the file given in the filename
     *
     * @author Michael Kemp
     * @return BufferedReader of the file
     * @throws FileNotFoundException
     */
    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }
}
