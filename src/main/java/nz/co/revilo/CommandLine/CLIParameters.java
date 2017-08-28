package nz.co.revilo.CommandLine;

import com.beust.jcommander.Parameter;

/**
 * Holds the parameters used for CLI parsing as well as their getters. The instance variables
 * are the default values but there may be better ways to do them in the future. Further investigation
 * must be done for this.
 *
 * @author Terran Kroft
 * @version 1.0
 */
public class CLIParameters {

    @Parameter(names = {"--parallel", "-p"})
    private int _parallelCores = 1; //default one core
    @Parameter(names = {"--visualise", "--visualize", "-v"})
    private boolean _visualise = false;
    @Parameter(names = {"--output", "-o"})
    private String _outputName = null; //should get input name actually
    @Parameter(names={"--help", "-h"}, help = true)
    private boolean help;

    /**
     * Gets the amount of cores to paralelise processing on
     *
     * @return the amount of cores
     */
    public int getParallelCores() {
        return _parallelCores;
    }

    /**
     * Whether or not to visualise the process
     *
     * @return visualisation switch
     */
    public boolean getVisualise() {
        return _visualise;
    }

    /**
     * The desired file output name
     * Default file name is "input-name-OUTPUT.dot"
     *
     * @return filename for output filename
     */
    public String getOutputName() {
        return _outputName;
    }

    /**
     * Checks if help flag is present
     * @return true if flag is present
     */
    public boolean getHelp() {
        return help;
    }

    /**
     * Sets the desired file output name
     *
     * @param outputFileName the name of the output file
     */
    public void setDefaultOutputName(String outputFileName) {
        _outputName = outputFileName + "-output.dot";
    }
}
