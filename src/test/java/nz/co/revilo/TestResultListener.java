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

        @Override
        public String toString() {
            return _name;
        }
    }

    private List<String> _nodeNames;
    private List<List<Boolean>> _arcs;
    private List<List<Integer>> _arcWeights;
    private List<Integer> _nodeWeights;
    private List<Integer> _nodeStarts;
    private List<Integer> _nodeProcessor;
    private List<Node> _nodes;

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
        _nodeWeights = nodeWeights;
        _nodeStarts = nodeStarts;
        _nodeProcessor = nodeProcessor;
        processNodes();
    }
    private void processNodes() {
        _nodes =  new ArrayList<>(_nodeWeights.size());
        for (int i = 0; i < _nodeWeights.size(); i++) {
            ArrayList<Integer> dependencies =  new ArrayList<>(_nodeWeights.size());
            for(int j = 0; j < _nodeWeights.size(); j++) {
                if(_arcs.get(i).get(j)) {
                    dependencies.add(j);
                }
            }
            _nodes.add(new Node(_nodeNames.get(i), dependencies, _nodeStarts.get(i),_nodeWeights.get(i), _nodeProcessor.get(i)));
        }
    }
    public List<Node> getNodes() {
        return _nodes;
    }

    public List<Integer> getArcWeights(int nodeIndex) {
        return _arcWeights.get(nodeIndex);
    }
}
