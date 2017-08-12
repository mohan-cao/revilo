package nz.co.revilo.Input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
 *  @version alpha
 *  @author Michael Kemp
 */
public class DotFileReader extends DotFileParser {

    public static final Pattern graphNameMatch = Pattern.compile("[\\s]*digraph[\\s]*\"(.*)\"[\\s]*\\{[\\s]*");
    public static final Pattern arcFrom = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern arcTo =  Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern arcWeight = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");
    public static final Pattern nodeName = Pattern.compile("[\\s]*([\\p{Alnum}]*)[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;");
    public static final Pattern nodeWeight = Pattern.compile("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*([\\p{Digit}]*)[\\s]*\\][\\s]*;");

    HashMap<String, Integer> nodeNames;
    List<Integer> nodeWeights;
    HashMap<String, HashMap<String, Integer>> arcs;
    ArrayList<String> nodeNamesList;
    String graphName;

    private ParseResultListener _listener;

    public DotFileReader(String filename) {
        super(filename);
    }

    public void startParsing(ParseResultListener newListener) throws FileNotFoundException {
        _listener = newListener;
        BufferedReader reader = openFile();

        nodeNames = new HashMap<>();
        nodeWeights = new ArrayList<>();
        arcs = new HashMap<>();

        try {
        	//TODO Empty file
        	//Note: regex might not work
            String line = reader.readLine();
            while (!line.contains("}") && (line != null)) {

                // Arc
                if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*.>[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    Matcher m = arcFrom.matcher(line);
                    m.find();
                    String from = m.group(1);
                    m = arcTo.matcher(line);
                    m.find();
                    String to = m.group(1);
                    m = arcWeight.matcher(line);
                    m.find();
                    int weight = Integer.parseInt(m.group(1));

                    if (!nodeNames.containsKey(from)) {
                        nodeWeights.set(nodeNames.get(from), -1);
                    } if (!nodeNames.containsKey(to)) {
                        nodeWeights.set(nodeNames.get(to), -1);
                    }
                    if (!arcs.containsKey(from)) {
                        arcs.put(from, new HashMap<>());
                    }
                    if (!arcs.get(from).containsKey(to)) {
                        arcs.get(from).put(to, weight);
                    } else {
                        arcs.get(from).replace(to, weight);
                    }

                    // Node
                } else if (line.matches("[\\s]*[\\p{Alnum}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                    Matcher m = nodeName.matcher(line);
                    m.find();
                    String name = m.group(1);
                    m = nodeWeight.matcher(line);
                    m.find();
                    int weight = Integer.parseInt(m.group(1));
                    if (nodeNames.containsKey(name)) {
                        nodeWeights.set(nodeNames.get(name), weight);
                    } else {
                        nodeNames.put(name, nodeWeights.size());
                        nodeWeights.add(weight);
                    }

                    // Graph Name
                } else if (line.matches("[\\s]*digraph[\\s]*\".*\"[\\s]*\\{[\\s]*")) {
                    Matcher m = graphNameMatch.matcher(line);
                    m.find();
                    graphName = m.group(1);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            //TODO
        }

        _listener.ParsingResults(graphName, nodeNamesList, nodeWeights, arcs, arcWeights);
    }

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }
}
