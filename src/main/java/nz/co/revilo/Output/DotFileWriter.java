package nz.co.revilo.Output;

import nz.co.revilo.Input.DotFileParser;
import org.graphstream.graph.Graph;

import java.io.PrintWriter;

public class DotFileWriter extends DotFileProducer {

    public DotFileWriter(String filename) {
        super(filename);
    }

    protected void produceOutput(PrintWriter output) {
        // Name and start
        output.println("digraph \"" + _graphName + "\" {");

        int currentNode = 0;
        int currentEdge = 0;
        for(DotFileParser.GraphObject o : _inputOrder) {
            if (o == DotFileParser.GraphObject.NODE) {
                output.println("\t\t" + _nodeNames.get(currentNode) + "\t\t[Weight=" + _nodeWeights.get(currentNode)
                        + ",Start=" + _nodeStarts.get(currentNode) + ",Processor=" + _nodeProcessor.get(currentNode) + "];");
                currentNode++;
            } else {
//                output.println(_edgeLines.get(currentEdge));
//                currentEdge++;
            }
        }


//        // Arcs
//        for (int from = 0; from < _arcs.size(); from++) {
//            for (int to = 0; to < _arcs.get(from).size(); to++) {
//                if (_arcs.get(from).get(to)) {
//                    output.println("\t\t" + _nodeNames.get(from) + " -> " + _nodeNames.get(to) + "\t[Weight=" + _arcWeights.get(from).get(to) + "];");
//                }
//            }
//        }
//
//        // Nodes
//        for (int arc = 0; arc < _arcs.size(); arc++) {
//            output.println("\t\t" + _nodeNames.get(arc) + "\t\t[Weight=" + _nodeWeights.get(arc) + ",Start=" + _nodeStarts.get(arc) + ",Processor=" + _nodeProcessor.get(arc) + "];");
//        }

        // End
        output.println("}");
    }
}
