package nz.co.revilo.Input;

import java.io.FileNotFoundException;

/**
 * FileParser is an abstract class designed to be extended to give all the required functionality to read a DOT file
 * then load the DAG into a data structure which is yet to be determined.
 *
 * @author Michael Kemp
 * @version 1.0
 */
public abstract class FileParser {


    private String _filename;

    /**
     * Constructor for the class which sets the file name
     *
     * @param filename
     */
    public FileParser(String filename) {
        _filename = filename;
    }

    /**
     * @return the filename of the file the FileParser is to parse/has parsed, as set in the constructor
     */
    protected String getFilename() {
        return _filename;
    }

    /**
     * Initiates the file parsing on another thread and returns the result to the listener
     *
     * @param newListener
     * @throws FileNotFoundException
     */
    public abstract void startParsing(ParseResultListener newListener) throws FileNotFoundException;
}
