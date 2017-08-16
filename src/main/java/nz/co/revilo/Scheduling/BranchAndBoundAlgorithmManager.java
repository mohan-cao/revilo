package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.List;

public class BranchAndBoundAlgorithmManager extends AlgorithmManager {

	static List<Integer> sources=new ArrayList<>();
	private List<Integer> bottomUpSinks=new ArrayList<>();
	private List<Schedule> rootSchedules=new ArrayList<>();
	static int[] bottomLevels;
	static int numNodes;
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
			if (!NeighbourManagerHelper.hasInneighbours(nodeId)) {
				sources.add(nodeId);
				//start a schedule with this node as source on each possible processor
				for(int p=0; p<_processingCores; p++){
					Schedule newSchedule = new Schedule(null, nodeId, p, _processingCores); 
					rootSchedules.add(newSchedule);
				}
			}		
			//sinks
			else if(!NeighbourManagerHelper.hasOutneighbours(nodeId)){
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

	/**
	 * bnb based on the current schedule s
	 * @param s
	 * @author Abby S
	 */
	private void bnb(Schedule s) {
		
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
			List<Integer> inneighbours=NeighbourManagerHelper.getInneighbours(nodeId);

			for(int inneighbour:inneighbours){
				List<Integer> inneighboursParents=NeighbourManagerHelper.getOutneighbours(inneighbour); //nodes with 1 on the node's row

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
}
