package nz.co.revilo;

import nz.co.revilo.Output.ScheduleResultListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Output/final ScheduleResultListener which allows access to graph properties for testing
 * @author Aimee
 * @version alpha
 */
public class TestResultListener implements ScheduleResultListener {
	/*
	 * @Abby S
	 */
	boolean _isBnb=false;
	
	/**
	 * Default constructor for testing non-BnB algorithms
	 * Currently this is for Topo sort
	 * 
	 * @author Abby S
	 * 
	 */
	public TestResultListener(){
	}
	
	/**
	 * Constructor for specifying the algorithm is BnB
	 * This boolean will be used to determine what to use when referencing the cores
	 * 
	 * @author Abby S
	 * 
	 * @param isBnB
	 */
	public TestResultListener(boolean isBnB){
		_isBnb=isBnB;
	}
	
	
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
    private List<List<Node>> _cores;
    private int _nCores;

    @Override
    public void finalSchedule(String graphName,
                              List<String> nodeNames,
                              List<List<Boolean>> arcs,
                              List<List<Integer>> arcWeights,
                              List<Integer> nodeWeights,
                              List<Integer> nodeStarts,
                              List<Integer> nodeCore) {
        _nodeNames = nodeNames;
        _arcs = arcs;
        _arcWeights = arcWeights;
        processNodes(nodeNames, nodeStarts, nodeWeights, nodeCore);
    }
    
    private void processNodes(List<String> nodeNames, List <Integer> nodeStarts, List<Integer> nodeWeights
            , List<Integer> nodeCore) {
        _nCores = 1;
        _nodes =  new ArrayList<>(nodeWeights.size());
        for (int i = 0; i < nodeWeights.size(); i++) {
            ArrayList<Integer> dependencies =  new ArrayList<>(nodeWeights.size());
            for(int j = 0; j < nodeWeights.size(); j++) {
                if(_arcs.get(i).get(j)) {
                    dependencies.add(j);
                }
            }
            _nodes.add(new Node(nodeNames.get(i), dependencies, nodeStarts.get(i),nodeWeights.get(i)
                    , nodeCore.get(i)));
            if (nodeCore.get(i) >  _nCores) {
                _nCores = nodeCore.get(i);
            }
        }

        // Initialise the ArrayList to store the nodes in cores
        _cores = new ArrayList<>(_nCores);
        for (int i = 0; i < _nCores; i++) {
            _cores.add(new ArrayList<Node>());
        }

        for(Node node : _nodes) {
        	//Changed by @Abby S slightly to accommodate for BnB
        	if(_isBnb){
        		_cores.get(node.getCore()).add(node);
        	} else {
        		_cores.get(node.getCore() - 1).add(node);
        	}
        }
    }
    
    public List<Node> getNodes() {
        return _nodes;
    }

    public List<Integer> getArcWeights(int nodeIndex) {
        return _arcWeights.get(nodeIndex);
    }

    public List<List<Node>> getCores() { return _cores; }
}
