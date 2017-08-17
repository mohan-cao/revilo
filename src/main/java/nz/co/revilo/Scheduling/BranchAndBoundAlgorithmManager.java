package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BranchAndBoundAlgorithmManager extends AlgorithmManager {

	List<Integer> sources=new ArrayList<>();
	private List<Integer> bottomUpSinks=new ArrayList<>();
	private List<Schedule> rootSchedules=new ArrayList<>();
	int[] bottomLevels;
	int numNodes;
	int totalNodeWeights;
	private int upperBound;
	private Schedule optimalSchedule;
	private List<Integer> nodeStartTimes=new ArrayList<>();;
	private List<Integer> nodeProcessors=new ArrayList<>();;

	public BranchAndBoundAlgorithmManager(int processingCores) {
		super(processingCores);
	}

	@Override
	protected void execute(){
		numNodes=_nodeWeights.length;
		bottomLevels=new int[numNodes];
		NeighbourManagerHelper.setUpHelper(numNodes, _arcs);
		
		//get sources
		for(int nodeId=0; nodeId<numNodes; nodeId++){
			//sources
			if (!NeighbourManagerHelper.hasInneighbours(nodeId)) {
				sources.add(nodeId);
				//start a schedule with this node as source on each possible processor
				for(int p=0; p<_processingCores; p++){
					Schedule newSchedule = new Schedule(this, null, nodeId, p); 
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
			System.out.println("Next root");
			bnb(rootSchedules.remove(0));
		}
		
		passResults();
	}

	private void passResults() {
		for(int nodeId=0; nodeId<numNodes; nodeId++){
			nodeStartTimes.add(optimalSchedule.closedSet.get(nodeId)._startTime);
			nodeProcessors.add(optimalSchedule.closedSet.get(nodeId)._processor);
		}	
		
		getListener().finalSchedule(
                _graphName,
                Arrays.asList(_nodeNames),
                primToBool2D(_arcs),
                primToInt2D(_arcWeights),
                primToInt(_nodeWeights),
                nodeStartTimes,
                nodeProcessors
        );
	}

	/**
	 * bnb based on the current schedule s
	 * @param s
	 * @author Abby S
	 */
	private void bnb(Schedule s) {
		System.out.println(s.toString());
		
		//TODO: not good enough?
		if(s.lowerBound>upperBound){
			return; //break tree at this point
		}

		//found optimal for the root started with
		if(s.openSet.isEmpty()){
			//reached end of a valid schedule. Never broke off, so is optimal
			
			//TODO: doing this to avoid unoptimal schedules getting through
			if(s.getMaxFinishTime()<=upperBound){
				optimalSchedule=s;
				upperBound=s.getMaxFinishTime();
			}
				
			return;
		}

		//continue DFS
		List<Schedule> nextSchedules = new ArrayList<>();
		for(int n:s.independentSet){
			for(int p=0; p<_processingCores; p++){
				nextSchedules.add(new Schedule(this, s, n, p));
			}
		}
		for(Schedule nextSchedule:nextSchedules){
			bnb(nextSchedule);
		}
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
				List<Integer> inneighboursChildren=NeighbourManagerHelper.getOutneighbours(inneighbour); //nodes with 1 on the node's row

				int fromGivenNode=bottomLevels[nodeId]+_nodeWeights[inneighbour];
				bottomLevels[inneighbour]=bottomLevels[inneighbour]>fromGivenNode?bottomLevels[inneighbour]:fromGivenNode;
				//inneighbours.remove(inneighbour);
				inneighboursChildren.remove(Integer.valueOf(nodeId));
				if(inneighboursChildren.isEmpty()){
					bottomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}	
	}
	
	
    /**
     * Creates an object type 2d bool list from primitive array
     * @param prim the primitive boolean 2d array
     * @return b the reference type list
     */
    private List<List<Boolean>> primToBool2D(boolean[][] prim) {
        List<List<Boolean>> b = new ArrayList<>();
        for (int i = 0; i < prim.length; i++) {
            b.add(new ArrayList<>());
            for (int j = 0; j < prim[i].length; j++) {
                b.get(i).add(prim[i][j]);
            }
        }
        return b;
    }

    /**
     * Creates an object type 2d int list from primitive array
     * @param prim the primitive int 2d array
     * @return n the reference type list
     */
    private List<List<Integer>> primToInt2D(int[][] prim) {
        List<List<Integer>> n = new ArrayList<>();
        for (int i = 0; i < prim.length; i++) {
            n.add(new ArrayList<>());
            for (int j = 0; j < prim[i].length; j++) {
                n.get(i).add(prim[i][j]);
            }
        }
        return n;
    }

    private List<Integer> primToInt(int[] prim) {
        ArrayList<Integer> n = new ArrayList<>();
        for (int i: prim) {
            n.add(i);
        }
        return n;
    }
}
