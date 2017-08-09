package nz.co.revilo.Output;

import nz.co.revilo.Input.DotFileParser;

import java.util.List;

public interface ScheduleResultListener {
    public void finalSchedule(String graphName, List<DotFileParser.GraphObject> inputOrder, List<String> edgeStrings, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor);
}
