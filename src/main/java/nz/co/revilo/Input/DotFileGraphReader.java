package nz.co.revilo.Input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

public class DotFileGraphReader extends DotFileParser {

    HashMap<String, Integer> nodeNames;
    List<Integer> nodeWeights;
    HashMap<String, HashMap<String, Integer>> arcs;

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

            fs.removeSink(g);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }
}
