package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.List;

public class BranchAndBoundAlgorithmManager extends AlgorithmManager {

	private List<Integer> sources=new ArrayList<>();
	private List<Integer> bottomUpSinks=new ArrayList<>();
	private List<Schedule> rootSchedules=new ArrayList<>();
	private int[] bottomLevels;
	private int numNodes;
	private int totalNodeWeights;
	private int upperBound;

	public BranchAndBoundAlgorithmManager(int processingCores) {
		super(processingCores);
		numNodes=_nodeWeights.length;
		bottomLevels=new int[numNodes];
	}

	@Override
	protected void execute(){
		//get sources
		for(int nodeId=0; nodeId<numNodes; nodeId++){
			//sources
			if (!hasInneighbours(nodeId)) {
				sources.add(nodeId);
				//start a schedule with this node as source on each possible processor
				for(int p=0; p<_processingCores; p++){
					Schedule newSchedule = new Schedule(null, nodeId, p); 
					rootSchedules.add(newSchedule);
				}
			}		
			//sinks
			else if(!hasOutneighbours(nodeId)){
				bottomUpSinks.add(nodeId);
				bottomLevels[nodeId]=_nodeWeights[nodeId];
			}

			totalNodeWeights+=_nodeWeights[nodeId];
		}

		upperBound=totalNodeWeights; //TODO: is this a good upper bound?
		calculateBottomLevels();

		while(!rootSchedules.isEmpty()){
			bnb(rootSchedules.remove(0));
		}
	}

	private void bnb(Schedule remove) {
		// TODO Auto-generated method stub
		
	}

	private void calculateBottomLevels() {
		// TODO Auto-generated method stub
		
	}

	private boolean hasInneighbours(int nodeId) {
		for(int node=0; node<_nodeWeights.length; node++){
			if(_arcs[node][nodeId]) return true;
		}
		return false;
	}
	
	private boolean hasOutneighbours(int nodeId) {
		for(int node=0; node<_nodeWeights.length; node++){
			if(_arcs[nodeId][node]) return true;
		}
		return false;
	}

}
