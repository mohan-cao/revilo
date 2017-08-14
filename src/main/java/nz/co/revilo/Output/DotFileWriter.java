package nz.co.revilo.Output;

import java.io.PrintWriter;

/**
 * Implementation of DotFileProducer which uses produced output by iterating through the nodes and edges of the graph,
 * and printing details in the required format.
 * @version Beta
 * @author Michael Kemp
 */
public class DotFileWriter extends DotFileProducer {
    public DotFileWriter(String filename) {
        super(filename);
    }

    /**
     * Iterates through all the nodes and edges to create a scheduling results DOT file
     * @param output PrintWriter set up and used to create the new DOT file, and print output to it
     */
    protected void produceOutput(PrintWriter output) {
        // Name and start
        String temp;
        if (_graphName != null) {
            if (_graphName.length() > 1) {
                temp = _graphName.substring(0, 1).toUpperCase() + _graphName.substring(1);
            } else {
                temp = _graphName.toUpperCase();
            }
        } else {
            temp = "";
        }

        output.println("digraph \"output" + temp + "\" {");

        // Arcs
        for (int from = 0; from < _arcs.size(); from++) {
            for (int to = 0; to < _arcs.get(from).size(); to++) {
                if (_arcs.get(from).get(to)) {
                    output.println("\t\t" + _nodeNames.get(from) + " -> " + _nodeNames.get(to) + "\t[Weight=" + _arcWeights.get(from).get(to) + "];");
                }
            }
        }

        // Nodes
        for (int node = 0; node < _arcs.size(); node++) {
            output.println("\t\t" + _nodeNames.get(node) + "\t\t[Weight=" + _nodeWeights.get(node) + ",Start=" + _nodeStarts.get(node) + ",Processor=" + _nodeProcessor.get(node) + "];");
        }

        // End
        output.println("}");
    }
}
