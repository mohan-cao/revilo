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

    public static final Integer DEFAULT_NODE_WEIGHT = -1;

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
        _nodeCounter.set(-1);

        try {
        	//TODO Empty file
            String line = reader.readLine();
            while ((line != null) && !line.contains("}")) {

                // Arc
                if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    addArc(line);

                    // Node
                } else if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    addNode(line);

                    // Graph Name
                } else if (line.matches("[\\s]*digraph[\\s]*\".*\"[\\s]*\\{[\\s]*")) {
                    setGraphName(line);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            //TODO Exceptions when reading lines
        }

        determineStartAndEndNodes();

        getListener().ParsingResults(_graphName, _nodeNums, _nodeNamesList, _nodeWeights, _nodeCounter, _startNodes, _endNodes, _arcs);
    }

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }

    private void addArc(String line) {
        //TODO Refactor such that addNode is used
        Matcher m = ARC_FROM.matcher(line);
        m.find();
        String fromName = m.group(1);
        m = ARC_TO.matcher(line);
        m.find();
        String toName = m.group(1);
        m = ARC_WEIGHT.matcher(line);
        m.find();
        Integer weight = Integer.parseInt(m.group(1));

        Integer fromNum;
        Integer toNum;

        if (!_nodeNums.containsKey(fromName)) {
            fromNum = addNode(fromName, DEFAULT_NODE_WEIGHT);
        } else {
            fromNum = _nodeNums.get(fromName);
        }
        if (!_nodeNums.containsKey(toName)) {
            toNum = addNode(toName, DEFAULT_NODE_WEIGHT);
        } else {
            toNum = _nodeNums.get(toName);
        }

        if (!_arcs.containsKey(fromNum)) {
            _arcs.put(fromNum, new ConcurrentHashMap<>());
        }

        if (!_arcs.get(fromNum).containsKey(_nodeNums.get(toName))) {
            _arcs.get(fromNum).put(toNum, weight);
        } else {
            //TODO Should have an error message if arc already has weight
            _arcs.get(fromNum).replace(toNum, weight);
        }

    }

    private void addNode(String line) {
        Matcher m = NODE_NAME.matcher(line);
        m.find();
        String name = m.group(1);
        m = NODE_WEIGHT.matcher(line);
        m.find();
        Integer weight = Integer.parseInt(m.group(1));
        addNode(name, weight);
    }

    private Integer addNode(String name, Integer weight) {
        if (_nodeNums.containsKey(name)) {
            //TODO Should have an error message if node already has weight other than -1
            _nodeWeights.set(_nodeNums.get(name), weight);
        } else {
            _nodeNums.put(name, _nodeCounter.getAndIncrement());
            _nodeWeights.add(weight);
        }
        return _nodeNums.get(name);
    }

    private void setGraphName(String line) {
        Matcher m = GRAPH_NAME_MATCH.matcher(line);
        m.find();
        _graphName = m.group(1);
    }

    private void determineStartAndEndNodes() {
        _startNodes.addAll(_arcs.keySet());

        for (Map<Integer, Integer> nodeList : _arcs.values()) {
            for (Integer node : nodeList.values()) {
                _endNodes.add(node);
                _startNodes.remove(node);
            }
        }

        for (Integer node : _arcs.keySet()) {
            if (!_arcs.get(node).isEmpty()) {
                _endNodes.remove(node);
            }
        }
    }
}
