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
 *  DotFileReader is a class that extends DotFileParser it's purpose is to read the file and place the digraph in to a
 *  data-structure and give it to a ParseResultListener.
 *
 *  At the moment this is just a skeleton and needs to be fleshed out but a data structure needs to be determined
 *
 *  @version Beta
 *  @author Michael Kemp
 */
public class DotFileReader extends DotFileParser {

    // Regex for string matchers that will extract the required information from the string
    public static final Pattern GRAPH_NAME_MATCH = Pattern.compile("[\\s]*digraph[\\s]*\"(.*)\"[\\s]*\\{[\\s]*");
    public static final Pattern ARC_FROM_MATCH = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern ARC_TO_MATCH = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern ARC_WEIGHT_MATCH = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");
    public static final Pattern NODE_NAME_MATCH = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern NODE_WEIGHT_MATCH = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");

    HashMap<String, Integer> _nodeNames;
    List<Integer> _nodeWeights;
    HashMap<String, HashMap<String, Integer>> _arcs;
    String[] _nodeNamesList;
    String _graphName;

    private ParseResultListener _listener;

    /**
     * Constructs a DotFileReader with the filename to read
     *
     * @param filename is the name of the file to open
     */
    public DotFileReader(String filename) {
        super(filename);
    }

    /**
     * Initiates reading the file and interpreting it as a DAG then informs the listener of the read graph data.
     *
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
         Reads the file using regex by checking the line is of an expected format then extracts information from the lines by matching with regex
          */
        try {
            //TODO Handle an empty file
            //Note: regex might not work
            String line = reader.readLine();
            // Continue reading the file if there's a next line and the current line isn't a closing line
            while ((line != null) && !line.contains("}")) {
                // Arcs
                if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    // Extracts information about the arc (from, to and weight)
                    // Extracts which node the arc is from
                    Matcher m = ARC_FROM_MATCH.matcher(line);
                    m.find();
                    String from = m.group(1);
                    // Extracts which node the arc is to
                    m = ARC_TO_MATCH.matcher(line);
                    m.find();
                    String to = m.group(1);
                    // Extracts the weight of the arc
                    m = ARC_WEIGHT_MATCH.matcher(line);
                    m.find();
                    int weight = Integer.parseInt(m.group(1));

                    // Places information about the arc into data structures
                    // If the from node isn't defined yet then it is temporarily created with a negative weight
                    if (!_nodeNames.containsKey(from)) {
                        _nodeWeights.set(_nodeNames.get(from), -1);
                    }
                    // If the to node isn't defined yet then it is temporarily created with a negative weight
                    if (!_nodeNames.containsKey(to)) {
                        _nodeWeights.set(_nodeNames.get(to), -1);
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

                    // Nodes
                } else if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    // Extracts the node name from the line
                    Matcher m = NODE_NAME_MATCH.matcher(line);
                    m.find();
                    String name = m.group(1);
                    // Extracts the node weight from the line
                    m = NODE_WEIGHT_MATCH.matcher(line);
                    m.find();
                    int weight = Integer.parseInt(m.group(1));
                    // If the node exists the weight is updated else it is created and weight is recorded
                    if (_nodeNames.containsKey(name)) {
                        _nodeWeights.set(_nodeNames.get(name), weight);
                    } else {
                        _nodeNames.put(name, _nodeWeights.size());
                        _nodeWeights.add(weight);
                    }

                    // Graph name
                } else if (line.matches("[\\s]*digraph[\\s]*\".*\"[\\s]*\\{[\\s]*")) {
                    // Extracts the graph name from the line
                    Matcher m = GRAPH_NAME_MATCH.matcher(line);
                    m.find();
                    // Sets the graph name
                    _graphName = m.group(1);
                }

                // Reads the next line in the file
                line = reader.readLine();
            }
        } catch (IOException e) {
            //TODO Error handling
        }

        String[] nodeNamesPrimitive = new String[_nodeWeights.size()];
        int[] nodeWeightsPrimitive = new int[_nodeWeights.size()];
        List<String> tempNames = new ArrayList<>(_nodeNames.keySet());
        _nodeNamesList = new String[_nodeWeights.size()];
        for (int i = 0; i < _nodeWeights.size(); i++) {
            String tempName = tempNames.get(i);
            int location = _nodeNames.get(tempName);
            _nodeNamesList[location] = tempName;
            nodeWeightsPrimitive[location] = _nodeWeights.get(location);
            nodeNamesPrimitive[location] = tempName;
        }

        boolean[][] arcsPrimitive = new boolean[_nodeWeights.size()][_nodeWeights.size()];
        int[][] arcWeightsPrimitive = new int[_nodeWeights.size()][_nodeWeights.size()];
        for (int j = 0; j < nodeNamesPrimitive.length; j++) {
            Arrays.fill(arcWeightsPrimitive[j], -1);
            for (int k = 0; k < nodeNamesPrimitive.length; k++) {
                if (_arcs.containsKey(nodeNamesPrimitive[j])) {
                    if (_arcs.get(nodeNamesPrimitive[j]).containsKey(nodeNamesPrimitive[k])) {
                        arcsPrimitive[j][k] = true;
                        arcWeightsPrimitive[j][k] = _arcs.get(nodeNamesPrimitive[j]).get(nodeNamesPrimitive[k]);
                    } else {
                        arcWeightsPrimitive[j][k] = -1;
                    }
                }
            }
        }

        _listener.ParsingResults(_graphName, _nodeNamesList, nodeWeightsPrimitive, arcsPrimitive, arcWeightsPrimitive);
    }

    /**
     * Opens the file given in the filename
     *
     * @return BufferedReader of the file
     * @throws FileNotFoundException
     */
    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }
}
