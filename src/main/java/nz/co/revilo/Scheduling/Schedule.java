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
	int _id = -1;
	BranchAndBoundAlgorithmManager bnb;
	Set<Integer> openSet=new HashSet<>(); //need to assign to processor
	Map<Integer,Tuple<Integer,Integer>> closedSet=new HashMap<>(); //done nodes
	Map<Integer,Set<Tuple<Integer,Integer>>> processorToTasks; //processor to task map
	Set<Integer> independentSet=new HashSet<>(); //nodes it depends on are done

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
			for(int node=0; node<bnb.numNodes; node++) openSet.add(node);
			independentSet.addAll(bnb.sources);
			lowerBound=bnb.bottomLevels[nodeId];
		} else { //adding to a schedule
			cloneParentSchedule(parentSchedule);

			//when parents are done
			for(int parent:NeighbourManagerHelper.getInneighbours(nodeId)){
				Tuple<Integer,Integer> parentAssignment=closedSet.get(parent);
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

			//TODO: cost function. 
			//Does this actually work as a heuristic?			
			int perfectLoadBalancing=(bnb.totalNodeWeights+totalIdleTime)/bnb._processingCores;
			lowerBound=(startTime+bnb.bottomLevels[nodeId])>perfectLoadBalancing?(startTime+bnb.bottomLevels[nodeId]):perfectLoadBalancing;

			//this will only remove the very slow ones that have already exceeded upper bound
			//lowerBound=getMaxFinishTime();
		}

		//create schedule structure map for hashcode comparison
		processorToTasks = new HashMap<>();
		for(int i=0;i<bnb._processingCores;i++){
			processorToTasks.put(i,new HashSet<>());
		}
		for(Integer node : closedSet.keySet()){
			int starttime = closedSet.get(node).getA();
			int processornum = closedSet.get(node).getB();

			Set<Tuple<Integer,Integer>> set = processorToTasks.get(processornum);
			set.add(new Tuple<>(node,starttime));
		}

		//update data structures
		closedSet.put(nodeId, new Tuple<>(startTime, processor));

		Set<Tuple<Integer,Integer>> setToAdd = processorToTasks.get(processor);
		setToAdd.add(new Tuple<>(nodeId, startTime));
		processorToTasks.put(processor,setToAdd);

		openSet.remove(nodeId);		
		independentSet.remove(nodeId);
		updateIndependentChildren(nodeId);
		finishTimes[processor]+=addedIdleTime+bnb._nodeWeights[nodeId];

		_id = generateHashCode();
	}

	private int generateHashCode(){//TODO: actually generate a hashcode

		return -1;
	}

	/**
	 * 
	 * Get finish time of slowest processor
	 * This determins the finish time of the schedule
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
		//clone parent's processor lists TODO: not yet

		//clone finishTimes
		for(int i=0; i<bnb._processingCores;i++) finishTimes[i]=parentSchedule.finishTimes[i];

		//clone idleTime
		totalIdleTime=parentSchedule.totalIdleTime;

		Integer element;
		//clone openSet
		Iterator<Integer> iterator=parentSchedule.openSet.iterator();
		for(int n=0; n<parentSchedule.openSet.size();n++){
			element=iterator.next();
			openSet.add(element);
		}

		//clone closedSet
		Integer nodeKey;
		iterator=parentSchedule.closedSet.keySet().iterator();
		for(int n=0; n<parentSchedule.closedSet.keySet().size();n++){
			nodeKey=iterator.next();
			Tuple<Integer,Integer> t = parentSchedule.closedSet.get(nodeKey);
			closedSet.put(nodeKey, new Tuple<>(t.getA(), t.getB()));
		}

		//clone openSet
		iterator=parentSchedule.independentSet.iterator();
		for(int n=0; n<parentSchedule.independentSet.size();n++){
			element=iterator.next();
			independentSet.add(element);
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
				if(openSet.contains(p)){
					waitingForParent=true; //still waiting on a parent
					break; //move to next child node
				}
			}
			if(!waitingForParent) {
				independentSet.add(child); //not waiting on any parents
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
		return "Printing schedule with " + closedSet.keySet() + " closed, and " + openSet + " open. Independent " + independentSet;
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
	public class Tuple<T,V> {
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
	}
}