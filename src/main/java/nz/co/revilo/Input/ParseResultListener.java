package nz.co.revilo.Input;

/**
 * ParseResultListener is the interface which allows for getting updated with results from a DotFileParser object when
 * parsing a DOT file is complete.
 *
 * @author Michael Kemp (mkem114)
 * @version Beta
 */
public interface ParseResultListener {

    /**
     * ParsingResults is called when a DotFileParser that the implementor is subscribed to has finished. The parameter
     * will be the results but is yet to be determined.
     */
    void ParsingResults(String graphName, String[] nodeNames, int[] nodeWeights, boolean[][] arcs, int[][] arcWeights);

}
