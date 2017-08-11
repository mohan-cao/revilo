package nz.co.revilo.Input;

import java.io.FileNotFoundException;

/**
 * DotFileParser is an abstract class designed to be extended to give all the required functionality to read a DOT file
 * then load the DAG into a data structure which is yet to be determined.
 *
 * @author Michael Kemp
 * @version 1.0
 */
public abstract class DotFileParser {


    private String _filename;

    /**
     * DotFileParser is a constructor for the abstract class which currently does nothing
     */
     public DotFileParser(String filename) {
        _filename = filename;
    }

    /**
     * Returns the name of the filename to be parsed
     */
    protected String getFilename() {
        return _filename;
    }

    /**
     * Initiates the file parsing on another thread and returns the result to the listener
     */
    public abstract void startParsing(ParseResultListener newListener) throws FileNotFoundException;
}
