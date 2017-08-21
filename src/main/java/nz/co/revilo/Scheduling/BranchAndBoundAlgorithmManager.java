package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Finds optimal schedule using DFS Branch and Bound
 * 
 * @author Abby S
 *
 */
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
			//check that sources have no parents
			if (!NeighbourManagerHelper.hasInneighbours(nodeId)) {
				//if they don't have parents, then add it to a sources queue
				sources.add(nodeId);
				//start a schedule with this node as source on each possible processor
			}
			//sinks
			else if(!NeighbourManagerHelper.hasOutneighbours(nodeId)){
				bottomUpSinks.add(nodeId);
				bottomLevels[nodeId]=_nodeWeights[nodeId];
			}

			totalNodeWeights+=_nodeWeights[nodeId];
		}

		for(int nodeId : sources) {
			for(int p=0; p<_processingCores; p++){
				Schedule newSchedule = new Schedule(this, null, nodeId, p);
				rootSchedules.add(newSchedule);
			}
		}

		upperBound=totalNodeWeights + 1; //TODO: is this a good upper bound?
		calculateBottomLevels();

		while(!rootSchedules.isEmpty()){
			bnb(rootSchedules.remove(0));
		}

		returnResults();
	}

	/**
	 * Return the optimal schedule found and it's information
	 * 
	 * @author Abby S
	 * 
	 */
	private void returnResults() {
		for(int nodeId=0; nodeId<numNodes; nodeId++){
			nodeStartTimes.add(optimalSchedule.closedSet.get(nodeId)._startTime);//start times
			nodeProcessors.add(optimalSchedule.closedSet.get(nodeId)._processor);//processors scheduled on
		}	
		System.out.println("Optimal length found: "+optimalSchedule.getMaxFinishTime());

		//pass to output
		getListener().finalSchedule(
				_graphName,
				Arrays.asList(_nodeNames),
				primToBoolean2D(_arcs),
				primToInteger2D(_arcWeights),
				primToInteger1D(_nodeWeights),
				nodeStartTimes,
				nodeProcessors
				);
	}

	/**
	 * bnb based on the current schedule s
	 * 
	 * @param s
	 * 
	 * @author Abby S
	 */
	private void bnb(Schedule s) {
		//TODO: not strict enough?
		if(s.lowerBound>=upperBound){
			s=null; //garbage collect that schedule
			return; //break tree at this point
		}

		//found optimal for the root started with
		//reached end of a valid schedule. Never broke off, so is optimal
		if(s.openSet.isEmpty()){			
			//TODO: doing this to make sure only optimal schedules get through
			if(s.getMaxFinishTime()<=upperBound){
				optimalSchedule=s;
				upperBound=s.getMaxFinishTime();
				return;
			}
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
	 * Using bottom-up approach
	 * 
	 * @author Abby S
	 * 
	 */
	private void calculateBottomLevels() {
		while(!bottomUpSinks.isEmpty()){
			int nodeId = bottomUpSinks.remove(0);
			List<Integer> inneighbours=NeighbourManagerHelper.getInneighbours(nodeId);

			for(int inneighbour:inneighbours){
				//bottom up add it's weight to child's
				int fromGivenNode=bottomLevels[nodeId]+_nodeWeights[inneighbour];
				//Farthest distance needed from bottom
				bottomLevels[inneighbour]=bottomLevels[inneighbour]>fromGivenNode?bottomLevels[inneighbour]:fromGivenNode;

				//inneighbours.remove(inneighbour); //ordered access so don't actually need to remove
				List<Integer> inneighboursChildren=NeighbourManagerHelper.getOutneighbours(inneighbour); //nodes with 1 on the node's row
				inneighboursChildren.remove(Integer.valueOf(nodeId)); //Integer or will treat the int as index
				if(inneighboursChildren.isEmpty()){
					bottomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}	
	}


	/**
	 * Creates an object type 2d Boolean list from primitive matrix
	 * @param primMatrix the primitive boolean 2d matrix
	 * @return b the reference type list
	 */
	private List<List<Boolean>> primToBoolean2D(boolean[][] primMatrix) {
		List<List<Boolean>> booleanMatrix = new ArrayList<>();
		for (int row = 0; row < primMatrix.length; row++) {
			booleanMatrix.add(new ArrayList<>());
			for (int col = 0; col < primMatrix[row].length; col++) {
				booleanMatrix.get(row).add(primMatrix[row][col]);
			}
		}
		return booleanMatrix;
	}

	/**
	 * Creates an object type 2d Integer list from primitive matrix
	 * @param primMatrix the primitive int 2d matrix
	 * @return n the reference type list
	 */
	private List<List<Integer>> primToInteger2D(int[][] primMatrix) {
		List<List<Integer>> integerMatrix = new ArrayList<>();
		for (int row = 0; row < primMatrix.length; row++) {
			integerMatrix.add(new ArrayList<>());
			for (int col = 0; col < primMatrix[row].length; col++) {
				integerMatrix.get(row).add(primMatrix[row][col]);
			}
		}
		return integerMatrix;
	}

	/**
	 * Creates an object type Integer list from primitive int array
	 * 
	 * @param primArray
	 * @return
	 */
	private List<Integer> primToInteger1D(int[] primArray) {
		ArrayList<Integer> integerList = new ArrayList<>();
		for (int i: primArray) {
			integerList.add(i);
		}
		return integerList;
	}
}