package nz.co.revilo.Scheduling;

import java.util.*;

/**
 * Represents a schedule.
 *
 * ClosedSet tuple:
 * Tuple A - Node start time
 * Tuple B - Processor number
 * --
 * ProcessorLists tuple:
 * Tuple A - Node ID
 * Tuple B - Start time
 * 
 * @author Abby S
 * @author Mohan Cao
 */
public class Schedule {
	int[] finishTimes;
	int totalIdleTime=0;
	int lowerBound;
	int scheduledWeight = 0;
	String _scheduleStructureId = "";
	BranchAndBoundAlgorithmManager bnb;
	Set<Integer> openNodes=new HashSet<>(); //need to assign to processor
	Map<Integer,Tuple<Integer,Integer>> closedNodes=new HashMap<>(); //done nodes
	Set<Integer> independentNodes=new HashSet<>(); //nodes it depends on are done
	Map<Integer,List<Tuple<Integer,Integer>>> scheduleStructure; //map of processor to tasks assigned on each processor

	/**
	 * Create new schedule object
	 * 
	 * @author Abby S
	 * 
	 * @param bnb the branch and bound algorithm manager that is being used
	 * @param parentSchedule the parent schedule that is being used
	 * @param nodeId the node id added to the current schedule
	 * @param processor the processor that the node is being added on
	 */
	public Schedule(BranchAndBoundAlgorithmManager bnb, Schedule parentSchedule, int nodeId, int processor){
		int startTime=0;
		int addedIdleTime=0;
		finishTimes = new int[bnb._processingCores];
		this.bnb=bnb;

		//scheduling on a root node
		if(parentSchedule==null){
			//initialise data structures
			for(int node=0; node<bnb.numNodes; node++) openNodes.add(node);
			independentNodes.addAll(bnb.sources);
			lowerBound=bnb.bottomLevels[nodeId];
		} else { //adding to a schedule
			cloneParentSchedule(parentSchedule);

			//when parents are done
			for(int parent:NeighbourManagerHelper.getInneighbours(nodeId)){
				Tuple<Integer,Integer> parentAssignment=closedNodes.get(parent);
				int dataReadyTime=parentAssignment.getA() + bnb._nodeWeights[parent];
				if(processor!=parentAssignment.getB()) {
					dataReadyTime+=bnb._arcWeights[parent][nodeId];
				}
				startTime=dataReadyTime>startTime?dataReadyTime:startTime;
			}
			//when processor is ready
			startTime=finishTimes[processor]>startTime?finishTimes[processor]:startTime;

			addedIdleTime=startTime-finishTimes[processor]; //added by this node
			totalIdleTime+=addedIdleTime;//total processor idle time

			//TODO: cost function. Does this actually work as a heuristic?			
			int perfectLoadBalancing=(bnb.totalNodeWeights+totalIdleTime)/bnb._processingCores;
			lowerBound=(startTime+bnb.bottomLevels[nodeId])>perfectLoadBalancing?(startTime+bnb.bottomLevels[nodeId]):perfectLoadBalancing;
		}

		//create schedule structure map for structure id comparison
		createScheduleStructureMap(bnb);

		//update data structures
		closedNodes.put(nodeId, new Tuple<>(startTime, processor));
		openNodes.remove(nodeId);		
		independentNodes.remove(nodeId);
		updateIndependentChildren(nodeId);
		finishTimes[processor]+=addedIdleTime+bnb._nodeWeights[nodeId];

		//update schedule structure
		List<Tuple<Integer,Integer>> listToAdd = scheduleStructure.get(processor);
		listToAdd.add(new Tuple<>(nodeId, startTime));
		scheduleStructure.put(processor,listToAdd);
		_scheduleStructureId = generateScheduleStructureId();
	}

	/**
	 * create schedule structure map for id comparison
	 * @param bnb
	 * 
	 * @author Abby S
	 * @author Mohan Cao
	 */
	private void createScheduleStructureMap(BranchAndBoundAlgorithmManager bnb) {
		scheduleStructure = new HashMap<>();
		for(int i=0;i<bnb._processingCores;i++){
			//list of tuples assigned on each processor
			scheduleStructure.put(i,new ArrayList<>());
		}

		//Add all assigned nodes to schedule structure map
		for(Integer assignedNode : closedNodes.keySet()){
			int assignedStartTime = closedNodes.get(assignedNode).getA();
			int assignedProcessor = closedNodes.get(assignedNode).getB();
			scheduleStructure.get(assignedProcessor).add(new Tuple<>(assignedNode,assignedStartTime));
		}
	}

	/**
	 * Generates id for identifying the structure of this schedule
	 * To be compared to existing structures for mirrors
	 * 
	 * @return
	 */
	private String generateScheduleStructureId(){
		String[] ids = new String[bnb._processingCores];
		Arrays.fill(ids, " ");
		for(int p=0; p<bnb._processingCores; p++){
			List<Tuple<Integer,Integer>> list = scheduleStructure.get(p);
			Collections.sort(list);
			for(Tuple<Integer,Integer> t:list){
				ids[p]+=t.hashCode();
			}
		}
		Arrays.sort(ids);
		return Arrays.toString(ids);
	}

	/**
	 * 
	 * Get finish time of slowest processor
	 * This determines the finish time of the schedule
	 * 
	 * @author Abby S
	 * 
	 */
	public int getMaxFinishTime() {
		int max=finishTimes[0];
		for(int i=1; i<bnb._processingCores;i++){
			if(finishTimes[i]>max) max=finishTimes[i];
		}
		return max;
	}

	//TODO: remove if unused
	public boolean isBounded(int maxFinishTime) {
		if(this.lowerBound >= maxFinishTime){
			return true;
		}
		return false;
	}

	/**
	 * Clones the parent schedule for this next schedule
	 * As child schedules are based on the partial schedule set out by parent
	 * 
	 * @author Abby S
	 * 
	 * @param parentSchedule
	 */
	private void cloneParentSchedule(Schedule parentSchedule) {
		//clone finishTimes
		for(int i=0; i<bnb._processingCores;i++) finishTimes[i]=parentSchedule.finishTimes[i];

		//clone idleTime
		totalIdleTime=parentSchedule.totalIdleTime;

		Integer element;
		//clone openSet
		Iterator<Integer> iterator=parentSchedule.openNodes.iterator();
		for(int n=0; n<parentSchedule.openNodes.size();n++){
			element=iterator.next();
			openNodes.add(element);
		}

		//clone closedSet
		Integer nodeKey;
		iterator=parentSchedule.closedNodes.keySet().iterator();
		for(int n=0; n<parentSchedule.closedNodes.keySet().size();n++){
			nodeKey=iterator.next();
			Tuple<Integer,Integer> t = parentSchedule.closedNodes.get(nodeKey);
			closedNodes.put(nodeKey, new Tuple<>(t.getA(), t.getB()));
		}

		//clone openSet
		iterator=parentSchedule.independentNodes.iterator();
		for(int n=0; n<parentSchedule.independentNodes.size();n++){
			element=iterator.next();
			independentNodes.add(element);
		}
	}

	/**
	 * Adds any children that don't have any other parents they're waiting on
	 * 
	 * @author Abby S
	 * 
	 * @param parent
	 */
	private void updateIndependentChildren(int parent) {
		for(int child:NeighbourManagerHelper.getOutneighbours(parent)){
			boolean waitingForParent=false;
			for(int p:NeighbourManagerHelper.getInneighbours(child)){
				if(openNodes.contains(p)){
					waitingForParent=true; //still waiting on a parent
					break; //move to next child node
				}
			}
			if(!waitingForParent) {
				independentNodes.add(child); //not waiting on any parents
			}
		}
	}

	// TODO: design decision to not compare Schedule objects

	/**
	 * Method to show current schedule in a string form
	 * 
	 * TODO: delete if unused
	 * 
	 * @author Abby S
	 * 
	 */
	@Override
	public String toString(){
		return "Printing schedule with " + closedNodes.keySet() + " closed, and " + openNodes + " open. Independent " + independentNodes;
	}

	/**
	 * Tuple class to represent a start time and processor tuple
	 * Used in scheduling on a node.
	 *
	 * Genericized.
	 * 
	 * @author Abby S
	 * @author Mohan Cao
	 *
	 */
	public class Tuple<T,V> implements Comparable<Tuple<T, V>>{
		T _a;
		V _b;

		public Tuple(T a, V b){
			_a = a;
			_b = b;
		}

		public T getA(){
			return _a;
		}
		public V getB(){
			return _b;
		}

		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Tuple)) {
				return false;
			}

			Tuple t = (Tuple) o;

			return (t._a == this._a) && (t._b == this._b);
		}

		@Override
		public int hashCode(){
			return Objects.hash(_a,_b);
		}

		@Override
		public int compareTo(Tuple<T, V> o) {
			Tuple<T, V> tuple = (Tuple<T, V>)o;
			if((Integer)(this.getA())<(Integer)(tuple.getA())){
				return -1;
			} else if((int)this.getA()>(int)tuple.getA()){
				return 1;
			}
			return 0;
		}
	}
}