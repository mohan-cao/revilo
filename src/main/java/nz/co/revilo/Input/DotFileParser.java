package nz.co.revilo.Input;

import java.io.FileNotFoundException;

/**
 * DotFileParser is an abstract class designed to be extended to give all the required functionality to read a DOT file
 * then load the DAG into a data structure which is yet to be determined.
 *
 * @author Michael Kemp
 * @version Beta
 */
public abstract class DotFileParser {

    private String _filename;
    private ParseResultListener _listener;

    /**
     * DotFileParser is a constructor for the abstract class which currently does nothing
     */
    public DotFileParser(String filename, ParseResultListener listener) {
        _filename = filename;
        _listener = listener;
    }

    /**
     * Returns the name of the filename to be parsed
     */
    protected final String getFilename() {
        return _filename;
    }

    /**
     * Returns the name of the listener to inform
     */
    protected final String getListener() {
        return _filename;
    }

    /**
     * Initiates the file parsing on another thread and returns the result to the listener
     */
    public abstract void startParsing() throws FileNotFoundException;
}
