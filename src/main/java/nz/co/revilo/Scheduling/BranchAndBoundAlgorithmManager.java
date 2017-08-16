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

	/**
	 * Calculates bottom level of each node in the graph
	 * 
	 * @author Abby S
	 * 
	 */
	private void calculateBottomLevels() {
		while(!bottomUpSinks.isEmpty()){
			int nodeId = bottomUpSinks.remove(0);
			List<Integer> inneighbours=getInneighbours(nodeId);

			for(int inneighbour:inneighbours){
				List<Integer> inneighboursParents=getOutneighbours(inneighbour); //nodes with 1 on the node's row

				int fromGivenNode=bottomLevels[nodeId]+_arcWeights[inneighbour][nodeId];
				bottomLevels[inneighbour]=bottomLevels[inneighbour]>fromGivenNode?bottomLevels[inneighbour]:fromGivenNode;
				inneighbours.remove(inneighbour);
				inneighboursParents.remove(nodeId);
				if(inneighboursParents.isEmpty()){
					bottomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}	
	}


	/*
	 * TODO: which of these methods are used?
	 * Use getNeighbours().size!=0 instead of hasNeighbour()?
	 */
	private List<Integer> getOutneighbours(int nodeId) {
		List<Integer> outneighbours=new ArrayList<>();

		for(int node=0; node<numNodes; node++){
			if(_arcs[nodeId][node]){
				outneighbours.add(node);
			}
		}
		return outneighbours;
	}

	private List<Integer> getInneighbours(int nodeId) {
		List<Integer> inneighbours=new ArrayList<>();

		for(int node=0; node<numNodes; node++){
			if(_arcs[node][nodeId]){
				inneighbours.add(node);
			}
		}
		return inneighbours;
	}

	private boolean hasInneighbours(int nodeId) {
		for(int node=0; node<numNodes; node++){
			if(_arcs[node][nodeId]) return true;
		}
		return false;
	}

	private boolean hasOutneighbours(int nodeId) {
		for(int node=0; node<numNodes; node++){
			if(_arcs[nodeId][node]) return true;
		}
		return false;
	}

	private int numInneighbours(int nodeId) {
		int count=0;

		for(int node=0; node<numNodes; node++){
			if(_arcs[node][nodeId]) count++;
		}
		return count;
	}

	private int numOutneighbours(int nodeId) {
		int count=0;

		for(int node=0; node<numNodes; node++){
			if(_arcs[nodeId][node]) count++;
		}
		return count;
	}
}
