package nz.co.revilo;

import nz.co.revilo.Output.ScheduleResultListener;
import org.jfree.util.ArrayUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Output/final ScheduleResultListener which allows access to graph properties for testing
 * @author Aimee
 * @version alpha
 */
public class TestResultListener implements ScheduleResultListener {
    /**
     * Inner class used to more easily represent nodes for testing.
     * @author Aimee
     * @version pre-alpha
     */
    public class Node {
        private String _name;
        private List<Integer> _dependencies;
        private int _startTime;
        private int _weight;
        private int _core;

        public Node(String name, List<Integer> dependencies, int startTime, int weight, int core) {
            _name = name;
            _dependencies = dependencies;
            _startTime = startTime;
            _weight = weight;
            _core = core;
        }

        public List<Integer> getDependencies() {
            return _dependencies;
        }

        public int getStartTime() {
            return _startTime;
        }

        public int getWeight() {
            return _weight;
        }

        public boolean sameCore(Node n) {
            return (n._core == this. _core);
        }

        public int getCore() { return _core; }

        @Override
        public String toString() {
            return _name;
        }
    }

    private List<String> _nodeNames;
    private List<List<Boolean>> _arcs;
    private List<List<Integer>> _arcWeights;
    private List<Node> _nodes;
    private List<List<Node>> _processors;
    private int _nProcessors;

    @Override
    public void finalSchedule(String graphName,
                              List<String> nodeNames,
                              List<List<Boolean>> arcs,
                              List<List<Integer>> arcWeights,
                              List<Integer> nodeWeights,
                              List<Integer> nodeStarts,
                              List<Integer> nodeProcessor) {
        _nodeNames = nodeNames;
        _arcs = arcs;
        _arcWeights = arcWeights;
        processNodes(nodeNames, nodeStarts, nodeWeights, nodeProcessor);
    }
    private void processNodes(List<String> nodeNames, List <Integer> nodeStarts, List<Integer> nodeWeights
            , List<Integer> nodeProcessor) {
        _nProcessors = 1;
        _nodes =  new ArrayList<>(nodeWeights.size());
        for (int i = 0; i < nodeWeights.size(); i++) {
            ArrayList<Integer> dependencies =  new ArrayList<>(nodeWeights.size());
            for(int j = 0; j < nodeWeights.size(); j++) {
                if(_arcs.get(i).get(j)) {
                    dependencies.add(j);
                }
            }
            _nodes.add(new Node(_nodeNames.get(i), dependencies, nodeStarts.get(i),nodeWeights.get(i)
                    , nodeProcessor.get(i)));
            if (nodeProcessor.get(i) >  _nProcessors) {
                _nProcessors = nodeProcessor.get(i);
            }
        }

        // Initialise the ArrayList to store the nodes in processors
        _processors = new ArrayList<>(_nProcessors);
        for (int i = 0; i < _nProcessors; i++) {
            _processors.add(new ArrayList<Node>());
        }

        for(Node node : _nodes) {
            _processors.get(node.getCore()).add(node);
        }
    }
    public List<Node> getNodes() {
        return _nodes;
    }

    public List<Integer> getArcWeights(int nodeIndex) {
        return _arcWeights.get(nodeIndex);
    }

    public List<List<Node>> getProcessors() { return _processors; }
}
