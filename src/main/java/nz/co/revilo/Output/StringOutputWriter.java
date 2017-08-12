package nz.co.revilo.Output;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Writer which prints the DOT file output that is expected in the behaviour of this application as a String.
 * This will be used in testing
 * @author Aimee
 * @version alpha
 */
public class StringOutputWriter extends DotFileWriter {
    public StringOutputWriter(String filename) {
        super(filename);
    }

    @Override
    protected Writer createWriter() {
        Writer sw = null;
        sw = new StringWriter();
        return sw;
    }
}
