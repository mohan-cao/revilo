package nz.co.revilo.Input;

import java.util.List;

/**
 * ParseResultListener is the interface which allows for getting updated with results from a DotFileParser object when
 * parsing a DOT file is complete.
 *
 * @author Michael Kemp (mkem114)
 * @version 1.0
 * @since Alpha 1.0
 */
public interface ParseResultListener {

    /**
     * ParsingResults is called when a DotFileParser that the implementor is subscribed to has finished. The parameter
     * will be the results but is yet to be determined.
     */
    void ParsingResults(int[] nodeWeights, boolean[][] arcs, int[][] arcWeights);

}
