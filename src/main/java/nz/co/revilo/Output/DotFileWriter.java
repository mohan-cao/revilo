package nz.co.revilo.Output;

import java.io.PrintWriter;

public class DotFileWriter extends DotFileProducer {
    public DotFileWriter(String filename) {
        super(filename);
    }

    protected void produceOutput(PrintWriter output) {
        // Name and start
        output.println("digraph \"" + _outputFilename + "\" {");

        // Arcs
//        for (int from = 0; from < _arcs.size(); from++) {
//            for (int to = 0; to < _arcs.get(from).size(); to++) {
//                if (_arcs.get(from).get(to)) {
//                    output.println("\t\t" + _nodeNames.get(from) + " -> " + _nodeNames.get(to) + "\t[Weight=" + _arcWeights.get(from).get(to) + "];");
//                }
//            }
//        }


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
