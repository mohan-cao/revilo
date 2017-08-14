package nz.co.revilo.Input;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DotFileGraphReader is an adaptor pattern for using the
 *
 * Implementation of DotFileParser based on using GraphStream to represent graphs and read in input.
 */
public class DotFileGraphReader extends DotFileParser {

    HashMap<String, Integer> nodeNames;
    List<Integer> nodeWeights;
    HashMap<String, HashMap<String, Integer>> arcs;
    String graphName;
    String[] nodeNamesList;

    private ParseResultListener _listener;


    public DotFileGraphReader(String filename) {
        super(filename);
    }

    @Override
    public void startParsing(ParseResultListener newListener) throws FileNotFoundException {
        _listener = newListener;
        BufferedReader reader = openFile();

        nodeNames = new HashMap<>();
        nodeWeights = new ArrayList<>();
        arcs = new HashMap<>();
    System.out.println(getFilename());
        Graph g = new DefaultGraph("g");
        try {
            FileSource fs = FileSourceFactory.sourceFor(getFilename());
            fs.addSink(g);
            fs.readAll(getFilename());

            g.display();
            //print out a bunch of graph stuff
            for (Node n: g.getEachNode()) {
                System.out.println("Node " + n + " has weight " + n.getAttribute("Weight") + ".");
            }
            for (Edge e: g.getEachEdge()) {
                System.out.println("Edge " + e + " has weight " + e.getAttribute("Weight") + ".");
            }

            // Create structure for nodes

            int numOfNodes = g.getNodeCount();
            HashMap<Integer, Node> idToNodeMap = new HashMap<>();

            List<Node> nodes = new ArrayList<>();
            List<ArrayList<Integer>> weights = new ArrayList<>();


            graphName = g.toString();

            // initialize the arcweights

            for (Node n: g.getEachNode()) {
                nodes.add(n);
            }

            for (int i = 0; i < nodes.size(); i++) {
                nodeNamesList[i] = nodes.get(i).toString();
            }

            System.out.println("Node array " + nodes);

            for (Node n: nodes) {
                weights.add(new ArrayList<>());
                for (int i = 0; i < nodes.size(); i++) {
                    weights.get(nodes.indexOf(n)).add(-1);
                }
            }

            for (Node sourceNode: nodes) {
                for (Edge e: sourceNode.getEachLeavingEdge()) {
                    Node destinationNode = e.getTargetNode();
                    Double weight = e.getAttribute("Weight");
                    weights.get(nodes.indexOf(sourceNode)).set(nodes.indexOf(destinationNode), weight.intValue());
                }
            }

            System.out.println("Weights array " + weights);

            int[] primitiveNodeWeights = new int[numOfNodes];
            boolean[][] primitiveArcs = new boolean[numOfNodes][numOfNodes];
            int[][] primitiveArcWeights = new int[numOfNodes][numOfNodes];

            for (int i = 0; i < numOfNodes; i++) {
                Double weight = nodes.get(i).getAttribute("Weight");
                primitiveNodeWeights[i] = weight.intValue();
            }

            for (int i = 0; i < numOfNodes; i++) {
                for (int j = 0; j < numOfNodes; j++) {
                    if (weights.get(i).get(j) == -1) {
                        primitiveArcs[i][j] = false;
                        primitiveArcWeights[i][j] = -1;
                    } else {
                        primitiveArcs[i][j] = true;
                        primitiveArcWeights[i][j] = weights.get(i).get(j);
                    }

                }
            }

            _listener.ParsingResults(graphName, nodeNamesList, primitiveNodeWeights, primitiveArcs, primitiveArcWeights);


            // Create primitive structure for arcs and weights


            fs.removeSink(g);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method created a BufferedReader for the input file, to be used to read in the graph from the dot file.
     *
     * @return BufferedReader for the file needing to be parsed
     * @throws FileNotFoundException
     */
    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }
}
