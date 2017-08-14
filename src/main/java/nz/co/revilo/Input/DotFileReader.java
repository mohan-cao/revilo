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
    public static final Pattern ARC_WEIGHT = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");
    public static final Pattern NODE_NAME = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern NODE_WEIGHT = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");

    //
    HashMap<String, Integer> _nodeNames;
    List<Integer> _nodeWeights;
    HashMap<String, HashMap<String, Integer>> _arcs;
    String[] _nodeNamesList;
    String _graphName;

    private ParseResultListener _listener;

    public DotFileReader(String filename) {
        super(filename);
    }

    public void startParsing(ParseResultListener newListener) throws FileNotFoundException {
        _listener = newListener;
        BufferedReader reader = openFile();

        _nodeNames = new HashMap<>();
        _nodeWeights = new ArrayList<>();
        _arcs = new HashMap<>();

        try {
        	//TODO Empty file
        	//Note: regex might not work
            String line = reader.readLine();
            while (!line.contains("}") && (line != null)) {
                //[\s]*[\p{Alnum}]*[\s]*.>[\s]*[\p{Alnum}]*[\s]*\[[\s]*[Ww]eight[\s]*[=][\s]*[\p{Digit}]*[\s]*\][\s]*;
                // arc
                if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    Matcher m = ARC_FROM_MATCH.matcher(line);
                    m.find();
                    String from = m.group(1);
                    m = ARC_TO_MATCH.matcher(line);
                    m.find();
                    String to = m.group(1);
                    m = ARC_WEIGHT.matcher(line);
                    m.find();
                    int weight = Integer.parseInt(m.group(1));

                    if (!_nodeNames.containsKey(from)) {
                        _nodeWeights.set(_nodeNames.get(from), -1);
                    }
                    if (!_nodeNames.containsKey(to)) {
                        _nodeWeights.set(_nodeNames.get(to), -1);
                    }
                    if (!_arcs.containsKey(from)) {
                        _arcs.put(from, new HashMap<>());
                    }
                    if (!_arcs.get(from).containsKey(to)) {
                        _arcs.get(from).put(to, weight);
                    } else {
                        _arcs.get(from).replace(to, weight);
                    }

                //[\s]*[\p{Alnum}]*[\s]*\[[\s]*[Ww]eight[\s]*[=][\s]*[\p{Digit}]*[\s]*\][\s]*;
                // node
                } else if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    Matcher m = NODE_NAME.matcher(line);
                    m.find();
                    String name = m.group(1);
                    m = NODE_WEIGHT.matcher(line);
                    m.find();
                    int weight = Integer.parseInt(m.group(1));
                    if (_nodeNames.containsKey(name)) {
                        _nodeWeights.set(_nodeNames.get(name), weight);
                    } else {
                        _nodeNames.put(name, _nodeWeights.size());
                        _nodeWeights.add(weight);
                    }

                //[\s]*digraph[\s]*".*"[\s]*\{[\s]*
                // graph name
                } else if (line.matches("[\\s]*digraph[\\s]*\".*\"[\\s]*\\{[\\s]*")) {
                    Matcher m = GRAPH_NAME_MATCH.matcher(line);
                    m.find();
                    _graphName = m.group(1);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            //TODO
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

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }
}
