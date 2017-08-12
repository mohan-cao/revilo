package nz.co.revilo.Output;

import java.util.List;
import java.util.Map;

public interface ScheduleResultListener {
    void finalSchedule(String graphName, List<String> nodeNames, Map<Integer, Map<Integer, Integer>> arcs, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor); //TODO determine data structure to pass
}
