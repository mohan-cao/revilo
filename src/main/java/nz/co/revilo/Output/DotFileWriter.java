package nz.co.revilo.Output;

import java.io.*;

/**
 * Creates the specified DOT file output using the PrintWriter implementation of Writer.
 * @author Michael, Terran
 */
public class DotFileWriter extends OutputProducer {
    public DotFileWriter(String filename) {
        super(filename);
    }

    /**
     * Creates a PrintWriter for use as the writer when producing output
     * @return
     */
    @Override
    protected Writer createWriter() {
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

    /**
     * Produces the output final schedule, which is written to the output Writer
     * @param output the Writer which whe output will be written to.
     */
    protected void produceOutput(Writer output) {
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

        try {
            output.write("digraph \"output" + temp + "\" {");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Arcs
        for (int from = 0; from < _arcs.size(); from++) {
            for (int to = 0; to < _arcs.get(from).size(); to++) {
                if (_arcs.get(from).get(to)) {
                    try {
                        output.write("\n\t\t" + _nodeNames.get(from) + " -> " + _nodeNames.get(to) + "\t[Weight=" + _arcWeights.get(from).get(to) + "];");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Nodes
        for (int node = 0; node < _arcs.size(); node++) {
            try {
                output.write("\n\t\t" + _nodeNames.get(node) + "\t\t[Weight=" + _nodeWeights.get(node) + ",Start=" + _nodeStarts.get(node) + ",Processor=" + _nodeProcessor.get(node) + "];");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // End
        try {
            output.write("\n}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
