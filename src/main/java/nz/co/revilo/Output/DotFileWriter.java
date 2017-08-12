package nz.co.revilo.Output;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class DotFileWriter extends DotFileProducer {
    public DotFileWriter(String filename) {
        super(filename);
    }

    @Override
    public PrintWriter createPrintWriter() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(_outputFilename, CHAR_SET);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return pw;
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
