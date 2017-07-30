package nz.co.revilo.Output;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class DotFileWriter extends DotFileProducer {

    public DotFileWriter(String filename) {
        super(filename);
    }

    protected void produceOutput(BufferedOutputStream output) {
        try {
            //TODO
            output.write(0);
            output.flush();
        } catch (IOException e) {
            //TODO
        }
    }
}
