package nz.co.revilo.Input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *  DotFileReader is a class that extends DotFileParser it's purpose is to read the file and place the digraph in to a
 *  data-structure and give it to a ParseResultListener.
 *
 *  At the moment this is just a skeleton and needs to be fleshed out but a data structure needs to be determined
 *
 *  @version alpha
 *  @author Michael Kemp
 */
public class DotFileReader extends DotFileParser {

    private ParseResultListener _listener;

    public DotFileReader(String filename) {
        super(filename);
    }

    public void startParsing(ParseResultListener newListener) throws FileNotFoundException {
        _listener = newListener;
        BufferedReader reader = openFile();

        try {
            String line = reader.readLine();
            while (!line.equals("}") && !line.equals(null)) {
                //[\s]*[\p{Alpha}]*[\s]*.>[\s]*[\p{Alpha}]*[\s]*\[[\s]*[Ww]eight[\s]*[=][\s]*[\p{Digit}]*[\s]*\][\s]*;
                if (line.matches("[\\s]*[\\p{Alpha}]*[\\s]*.>[\\s]*[\\p{Alpha}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                //[\s]*[\p{Alpha}]*[\s]*\[[\s]*[Ww]eight[\s]*[=][\s]*[\p{Digit}]*[\s]*\][\s]*;
                } else if (line.matches("[\\s]*[\\p{Alpha}]*[\\s]*\\[[\\s]*[Ww]eight[\\s]*[=][\\s]*[\\p{Digit}]*[\\s]*\\][\\s]*;")) {
                //[\s]*digraph[\s]*"[\s]*[\p{Alpha}\p{Digit}]*[\s]*"[\s]*\{[\s]*
                } else if (line.matches("[\\s]*digraph[\\s]*\"[\\s]*[\\p{Alpha}\\p{Digit}]*[\\s]*\"[\\s]*\\{[\\s]*")) {

                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            //TO-DO
        }

        _listener.ParsingResults();
    }

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFilename()));
    }
}
