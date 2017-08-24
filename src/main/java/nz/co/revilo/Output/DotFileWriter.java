package nz.co.revilo.Output;

import java.io.PrintWriter;

/**
 * Implementation of DotFileProducer which uses produced output by iterating through the nodes and edges of the graph,
 * and printing details in the required format.
 *
 * @author Michael Kemp
 * @version 1.0
 */
public class DotFileWriter extends DotFileProducer {
    public DotFileWriter(String filename) {
        super(filename);
    }

    /**
     * Iterates through all the nodes and edges to create a scheduling results DOT file
     *
     * @author Michael Kemp
     * @param output PrintWriter set up and used to create the new DOT file, and print output to it
     */
    protected void produceOutput(PrintWriter output) {
        // Prints graph name
        String temp;
        // If there's a name
        if (_graphName != null) {
            // And it's longer than 1 character
            if (_graphName.length() > 1) {
                // Capitalise the first character
                temp = _graphName.substring(0, 1).toUpperCase() + _graphName.substring(1);
            } else {
                // otherwise just capitalise the whole thing
                temp = _graphName.toUpperCase();
            }
        } else {
            // and if there isn't a name then it's empty
            temp = "";
        }
        // Prints the graph name
        output.println("digraph \"output" + temp + "\" {");

        // Prints nodes with their weights
        for (int node = 0; node < _arcs.size(); node++) {
            output.println("\t\t" + _nodeNames.get(node) + "\t\t[Weight=" + _nodeWeights.get(node) + ",Start=" +
                    _nodeStarts.get(node) + ",Processor=" + _nodeProcessor.get(node) + "];");
        }

        // Prints arcs with their weights
        for (int from = 0; from < _arcs.size(); from++) {
            for (int to = 0; to < _arcs.get(from).size(); to++) {
                if (_arcs.get(from).get(to)) {
                    output.println("\t\t" + _nodeNames.get(from) + " -> " + _nodeNames.get(to) + "\t[Weight=" +
                            _arcWeights.get(from).get(to) + "];");
                }
            }
        }

        // Print the closing line
        output.println("}");
    }
}
