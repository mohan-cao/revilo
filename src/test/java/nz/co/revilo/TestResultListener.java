package nz.co.revilo;

import nz.co.revilo.Output.ScheduleResultListener;
import org.jfree.util.ArrayUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Output/final schedule listener which allows access to graph properties for testing
 *
 */
public class TestResultListener implements ScheduleResultListener {
    /**
     * Inner class used to more easily represent nodes for testing.
     * @author Aimee
     * @version pre-alpha
     */
    public class Node {
        private String _name;
        private ArrayList<Integer> _dependencies;
        private int _startTime;
        private int _weight;
        private int _core;

        public Node(String name, ArrayList<Integer> dependencies, int startTime, int weight, int core) {
            _name = name;
            _dependencies = dependencies;
            _startTime = startTime;
            _weight = weight;
            _core = core;
        }

        public ArrayList<Integer> getDependencies() {
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

    private ArrayList<String> _nodeNames;
    private ArrayList<List<Boolean>> _arcs;
    private ArrayList<List<Integer>> _arcWeights;
    private ArrayList<Integer> _nodeWeights;
    private ArrayList<Integer> _nodeStarts;
    private ArrayList<Integer> _nodeProcessor;
    private ArrayList<Node> _nodes;

    @Override
    public void finalSchedule(String graphName,
                              List<String> nodeNames,
                              List<List<Boolean>> arcs,
                              List<List<Integer>> arcWeights,
                              List<Integer> nodeWeights,
                              List<Integer> nodeStarts,
                              List<Integer> nodeProcessor) {
        _nodeNames = (ArrayList) nodeNames;
        _arcs = (ArrayList) arcs;
        _arcWeights = (ArrayList) arcWeights;
        _nodeWeights = (ArrayList) nodeWeights;
        _nodeStarts = (ArrayList) nodeStarts;
        _nodeProcessor = (ArrayList) nodeProcessor;
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
    public ArrayList<Node> getNodes() {
        return _nodes;
    }

    public List<Integer> getArcWeights(int nodeIndex) {
        return _arcWeights.get(nodeIndex);
    }
}
