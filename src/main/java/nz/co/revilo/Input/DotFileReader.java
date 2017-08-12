package nz.co.revilo.Input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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

    public static final Pattern GRAPH_NAME_MATCH = Pattern.compile("[\\s]*digraph[\\s]*\"(.*)\"[\\s]*\\{[\\s]*");
    public static final Pattern ARC_FROM = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern ARC_TO = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern ARC_WEIGHT = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");
    public static final Pattern NODE_NAME = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern NODE_WEIGHT = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");

    public static final int

    private String _graphName;
    private Map<String, Integer> _nodeNums;
    private List<String> _nodeNamesList;
    private List<Integer> _nodeWeights;
    private AtomicInteger _nodeCounter;
    private Set<Integer> _startNodes;
    private Set<Integer> _endNodes;
    private Map<Integer, Map<Integer, Integer>> _arcs;

    public DotFileReader(String filename, ParseResultListener listener) {
        super(filename, listener);
    }

    public void startParsing() throws FileNotFoundException {
        BufferedReader reader = openFile();

        _graphName = null;
        _nodeNums = new ConcurrentHashMap<>();
        _nodeNamesList = new ArrayList<>();
        _nodeWeights = new ArrayList<>();
        _nodeCounter = new AtomicInteger();
        _startNodes = new HashSet<>();
        _endNodes = new HashSet<>();
        _arcs = new ConcurrentHashMap<>();

        // Starts at -1 because array indexing begins at 0 and the method incrementAndGet increments before getting
        //_nodeCounter.set(-1);

        try {
        	//TODO Empty file
            String line = reader.readLine();
            while (!line.contains("}") && (line != null)) {

                // Arc
                if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    addArc(line);

                    // Node
                } else if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    Matcher m = NODE_NAME.matcher(line);
                    m.find();
                    String name = m.group(1);
                    m = NODE_WEIGHT.matcher(line);
                    m.find();
                    Integer weight = Integer.parseInt(m.group(1));
                    addNode(name, weight);

                    // Graph Name
                } else if (line.matches("[\\s]*digraph[\\s]*\".*\"[\\s]*\\{[\\s]*")) {
                    setGraphName(line);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            //TODO Exceptions when reading lines
        }

        getListener().ParsingResults(_graphName, _nodeNums, _nodeNamesList, _nodeWeights, _nodeCounter, _startNodes, _endNodes, _arcs);
    }

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }

    private void addArc(String line) {
        //TODO Refactor such that addNode is used
        Matcher m = ARC_FROM.matcher(line);
        m.find();
        String from = m.group(1);
        m = ARC_TO.matcher(line);
        m.find();
        String to = m.group(1);
        m = ARC_WEIGHT.matcher(line);
        m.find();
        int weight = Integer.parseInt(m.group(1));

        if (!_nodeNums.containsKey(from)) {
            _nodeWeights.set(_nodeNums.get(from), -1);
        }
        if (!_nodeNums.containsKey(to)) {
            _nodeWeights.set(_nodeNums.get(to), -1);
        }
        if (!_arcs.containsKey(from)) {
            _arcs.put(_nodeNums.get(from), new ConcurrentHashMap<>());
        }
        if (!_arcs.get(_nodeNums.get(from)).containsKey(_nodeNums.get(to))) {
            _arcs.get(_nodeNums.get(from)).put(_nodeNums.get(to), weight);
        } else {
            //TODO Should have an error message if arc already has weight
            _arcs.get(_nodeNums.get(from)).replace(_nodeNums.get(to), weight);
        }

    }

    private void addNode(String name, Integer weight) {
        if (_nodeNums.containsKey(name)) {
            //TODO Should have an error message if node already has weight other than -1
            _nodeWeights.set(_nodeNums.get(name), weight);
        } else {
            _nodeNums.put(name, _nodeCounter.getAndIncrement());
            _nodeWeights.add(weight);
        }
    }

    private void setGraphName(String line) {
        Matcher m = GRAPH_NAME_MATCH.matcher(line);
        m.find();
        _graphName = m.group(1);
    }
}
