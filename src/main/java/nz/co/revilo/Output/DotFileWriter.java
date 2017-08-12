package nz.co.revilo.Output;

import java.io.PrintWriter;

public class DotFileWriter extends DotFileProducer {
    public DotFileWriter(String filename) {
        super(filename);
    }

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
        for (Integer from : _arcs.keySet()) {
            for (Integer to : _arcs.get(from).keySet()) {
                output.println("\t\t" + _nodeNames.get(from) + " -> " + _nodeNames.get(to) + "\t[Weight=" + _arcs.get(from).get(to) + "];");
            }
        }

        // Nodes
        for (int node = 0; node < _nodeWeights.size(); node++) {
            output.println("\t\t" + _nodeNames.get(node) + "\t\t[Weight=" + _nodeWeights.get(node) + ",Start=" + _nodeStarts.get(node) + ",Processor=" + (_nodeProcessor.get(node) + 1) + "];");
        }

        // End
        output.println("}");
    }
}
