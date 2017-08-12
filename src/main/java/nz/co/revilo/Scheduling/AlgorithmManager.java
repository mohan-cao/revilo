package nz.co.revilo.Scheduling;

import nz.co.revilo.Input.ParseResultListener;
import nz.co.revilo.Output.ScheduleResultListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AlgorithmManager implements ParseResultListener {

    private int _processingCores;
    Map<String, Integer> _nodeNums;
    String _graphName;
    List<String> _nodeNamesList;
    List<Integer> _nodeWeights;
    AtomicInteger _nodeCounter;
    Set<Integer> _startNodes;
    Set<Integer> _endNodes;
    Map<Integer, Map<Integer, Integer>> _arcs;
    private List<ScheduleResultListener> _listeners;


    public AlgorithmManager(int processingCores) {
        _processingCores = processingCores;
        _listeners = new ArrayList<>();
    }

    protected abstract void execute();//TODO

    public void inform(ScheduleResultListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void ParsingResults(String graphName, Map<String, Integer> nodeNums, List<String> nodeNames, List<Integer> nodeWeights, AtomicInteger nodeCounter, Set<Integer> startNodes, Set<Integer> endNodes, Map<Integer, Map<Integer, Integer>> arcs) {
        _graphName = graphName;
        _nodeNums = nodeNums;
        _nodeNamesList = nodeNames;
        _nodeWeights = nodeWeights;
        _nodeCounter = nodeCounter;
        _startNodes = startNodes;
        _endNodes = endNodes;
        _arcs = arcs;
    }

    protected int getProcessingCores() {
        return _processingCores;
    }

    protected List<ScheduleResultListener> getListener() {
        return _listeners;
    }
}
