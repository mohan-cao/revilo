package nz.co.revilo.CommandLine;

import com.beust.jcommander.Parameter;

/**
 * Holds the parameters used for CLI parsing as well as their getters. The instance variables
 * are the default values but there may be better ways to do them in the future. Further investigation
 * must be done for this.
 *
 * @author Terran Kroft
 * @version pre-alpha
 */
public class CLIParameters {

    @Parameter(names={"--parallel", "-p"})
    private int _parallelCores = 1; //default one core
    @Parameter(names={"--visualise", "--visualize", "-v"})
    private boolean _visualise = false;
    @Parameter(names={"--output", "-o"})
    private String _outputName = null; //should get input name actually

    public int getParallelCores() {
        return _parallelCores;
    }

    public boolean getVisualise() {
        return _visualise;
    }

    public String getOutputName() {
        return _outputName;
    }

    public void setDefaultOutputName(String outputFileName) {
        _outputName = outputFileName + "-output.dot";
    }
}