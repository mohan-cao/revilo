package nz.co.revilo.Output;

import java.util.List;

/**
 * Interface implemented by classes which need to make use of scheduling results. It allows those classes to receive
 * key information about the schedule, particularly those in relation to producing output.
 *
 * @author Michael Kemp
 * @version 1.0
 */
public interface ScheduleResultListener {
    /**
     * Method which allows graph information about the final schedule to be passed to listeners.
     *
     * @param graphName     Name of graph
     * @param nodeNames     Name of each task
     * @param arcs          The existence of arcs
     * @param arcWeights    How long to transfer results of task completion to another processor
     * @param nodeWeights   How long each task takes
     * @param nodeStarts    When each task starts
     * @param nodeProcessor What processor each task is on
     */
    void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor); //TODO determine data structure to pass
}
