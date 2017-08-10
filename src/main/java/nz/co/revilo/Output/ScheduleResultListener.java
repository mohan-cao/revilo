package nz.co.revilo.Output;

import nz.co.revilo.Input.DotFileParser;

import java.util.List;

public interface ScheduleResultListener {
    public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor); //TODO determine data structure to pass
}
