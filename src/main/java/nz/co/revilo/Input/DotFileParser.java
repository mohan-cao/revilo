package nz.co.revilo.Input;

/**
 * DotFileParser is an abstract class designed to be extended to give all the required functionality to read a DOT file
 * then load the DAG into a data structure which is yet to be determined.
 *
 * @author Michael Kemp
 * @version 1.0
 * @since alpha
 */
public abstract class DotFileParser {

    boolean _finishedParsing = false;
    String _filename;

    /**
     * DotFileParser is a constructor for the abstract class which currently does nothing
     */
    DotFileParser() {
    }

    /**
     * Changes the filename of the file to be parsed
     */
    void changeFilename(String filename) {
        _filename = filename;
        _finishedParsing = false;
    }

    /**
     * Initiates the file parsing on another thread
     */
    abstract void startParsing();

    /**
     * Returns true if the file has already been parsed
     */
    boolean finishedParsing() {
        return _finishedParsing;
    }

    /**
     * Add a listener (to the list) to be sent the results upon parsing finish
     */
    abstract void addParseResultListener();
}
