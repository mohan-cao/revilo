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

    public static final int FILE_CANT_BE_READ_EXIT_STATUS = 1;

    private String _filename;

    /**
     * Constructor for the class which sets the file name
     * @author Michael Kemp
     * @param filename
     */
    public FileParser(String filename) {
        _filename = filename;
    }

    /**
     * @author Michael Kemp
     * @return the filename of the file the FileParser is to parse/has parsed, as set in the constructor
     */
    protected final String getFilename() {
        return _filename;
    }

    /**
     * If an error occurs during file reading and there is no obvious way to recover from it, gracefully quite while
     * giving diagnostic information from the exception
     *
     * @param e The exception that occurred during file reading
     * @author Michael
     */
    protected final void fileReadingError(Exception e) {
        System.out.println("File could not be read");
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
        System.exit(FILE_CANT_BE_READ_EXIT_STATUS);
    }

    /**
     * Initiates the file parsing on another thread and returns the result to the listener
     * @author Michael Kemp
     * @param newListener
     * @throws FileNotFoundException
     */
    public abstract void startParsing(ParseResultListener newListener) throws FileNotFoundException;
}
