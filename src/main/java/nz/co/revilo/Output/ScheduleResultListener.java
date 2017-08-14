package nz.co.revilo.Output;

import nz.co.revilo.Input.DotFileParser;

import java.util.List;

/**
 * Interface implemented by classes which need to make use of scheduling results. It allows those classes to receive
 * key information about the schedule, particularly those in relation to producing output.
 */
public interface ScheduleResultListener {
    /**
     * Method which allows graph information abotu the final schedule to be passed to listeners.
     *
     * @param graphName
     * @param nodeNames
     * @param arcs
     * @param arcWeights
     * @param nodeWeights
     * @param nodeStarts
     * @param nodeProcessor
     */
    public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor); //TODO determine data structure to pass
}
