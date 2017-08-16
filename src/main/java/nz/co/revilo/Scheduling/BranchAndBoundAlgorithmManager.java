package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.List;

public class BranchAndBoundAlgorithmManager extends AlgorithmManager {

	private List<Integer> sources=new ArrayList<>();
	private List<Integer> bottomUpSinks=new ArrayList<>();

	public BranchAndBoundAlgorithmManager(int processingCores) {
		super(processingCores);
	}

	@Override
	protected void execute(){
		//get sources
		for(int nodeId=0; nodeId<_nodeWeights.length; nodeId++){
			//sources
			if (!hasInneighbours(nodeId)) {
				sources.add(nodeId);
			}}
				//start a schedule with this node as source on each possible processor
				/*for(int p=0; p<_processingCores; p++){
					Schedule newSchedule = new Schedule(null, nodeId, p); 
					rootSchedules.add(newSchedule);
				}
			}		
			//sinks
			else if(nodeId.outneighboursClone.size()==0){
				bottomUpSinks.add(nodeId);
				nodeId.bottomLevel=nodeId.nodeWeight;
			}

			totalNodeWeights+=nodeId.nodeWeight;
		}

		upperBound=totalNodeWeights; //TODO: is this a good upper bound?
		calculateBottomLevels();

		while(!rootSchedules.isEmpty()){
			bnb(rootSchedules.remove(0));
		}*/
	}

	private boolean hasInneighbours(int nodeId) {
		for(int node=0; node<_nodeWeights.length; node++){
			if(_arcs[node][nodeId]) return true;
		}
		return false;
	}

}
