package nz.co.revilo.Scheduling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nz.co.revilo.Scheduling.BranchAndBoundAlgorithmManager;

/**
 * Represents a schedule
 * 
 * @author Abby S
 */
public class Schedule {
	int[] finishTimes;
	int idleTime=0;
	int lowerBound;
	BranchAndBoundAlgorithmManager bnb;
	Set<Integer> openSet=new HashSet<>(); //need to assign to processor
	Map<Integer,Tuple> closedSet=new HashMap<>(); //done nodes
	Set<Integer> independentSet=new HashSet<>(); //nodes it depends on are done

	/**
	 * Create new schedule object
	 * 
	 * @author Abby S
	 * 
	 * @param bnb
	 * @param parentSchedule
	 * @param nodeId
	 * @param processor
	 */
	public Schedule(BranchAndBoundAlgorithmManager bnb, Schedule parentSchedule, int nodeId, int processor){
		int startTime=0;
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
				Tuple parentAssignment=closedSet.get(parent);
				int dataReadyTime=parentAssignment._startTime + bnb._nodeWeights[parent];
				if(processor!=parentAssignment._processor) {
					dataReadyTime+=bnb._arcWeights[parent][nodeId];
				}
				startTime=dataReadyTime>startTime?dataReadyTime:startTime;
			}
			//when processor is ready
			startTime=finishTimes[processor]>startTime?finishTimes[processor]:startTime;

			idleTime+=startTime-finishTimes[processor];//processor idle time

			//TODO: cost function (with perfect load balancing?), because this doesn't actually work
			//totalNodeWeights should be lower bound, why is perfectLoadBalancing sometimes giving greater number?
			/*
			int perfectLoadBalancing=(bnb.totalNodeWeights+idleTime)/bnb._processingCores;
			lowerBound=(startTime+bnb.bottomLevels[nodeId])>perfectLoadBalancing?(startTime+bnb.bottomLevels[nodeId]):perfectLoadBalancing;
			*/
			
			 //TODO: this will only remove the very slow ones that have already exceeded upper bound
			lowerBound=getMaxFinishTime();
		}

		//update data structures
		closedSet.put(nodeId, new Tuple(startTime, processor));
		openSet.remove(nodeId);		
		independentSet.remove(nodeId);
		updateIndependentChildren(nodeId);
		finishTimes[processor]+=idleTime+bnb._nodeWeights[nodeId];
	}

	/**
	 * TODO: testing purposes only!
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
		idleTime=parentSchedule.idleTime;

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
			Tuple t = parentSchedule.closedSet.get(nodeKey);
			closedSet.put(nodeKey, new Tuple(t._startTime, t._processor));
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
				System.out.println("Adding "+child);
			}
		}
	}
	
	@Override
	public String toString(){
		return "Printing schedule with " + closedSet.keySet() + " closed, and " + openSet + " open. Independent " + independentSet;
	}

	/**
	 * Tuple class to represent a start time and processor tuple
	 * Used in scheduling on a node
	 * 
	 * @author Abby S
	 *
	 */
	class Tuple {
		int _startTime;
		int _processor;
		
		Tuple(int startTime, int processor){
			_startTime=startTime;
			_processor=processor;
		}
	}
}