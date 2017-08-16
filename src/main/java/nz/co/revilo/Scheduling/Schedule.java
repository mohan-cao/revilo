package nz.co.revilo.Scheduling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.revilo.Scheduling.BranchAndBoundAlgorithmManager;

/**
 * Represents a schedules
 * 
 * @author Abby S
 */
public class Schedule {
	int[] finishTimes;
	int idleTime=0;
	int lowerBound;
	Set<Integer> openSet=new HashSet<>(); //need to assign to processor
	Map<Integer,Tuple> closedSet=new HashMap<>(); //done nodes
	Set<Integer> independentSet=new HashSet<>(); //nodes it depends on are done

	/**
	 * Create schedule
	 * @author Abby S
	 * 
	 * @param parentSchedule
	 * @param node
	 * @param processor
	 */
	public Schedule(BranchAndBoundAlgorithmManager bnb, Schedule parentSchedule, int nodeId, int processor){
		int startTime=0;
		
		//scheduling on a root node
		if(parentSchedule==null){
			//initialise data structures
			finishTimes = new int[bnb._processingCores];
			for(int node=0; node<bnb.numNodes; node++){
				openSet.add(node);
			}
			independentSet.addAll(bnb.sources);
			lowerBound=bnb.bottomLevels[nodeId];
			
		} else { //adding to a schedule
			cloneParentSchedule(parentSchedule);
			
			//when parents are done
			for(int parent:NeighbourManagerHelper.getInneighbours(nodeId)){
				Tuple parentAssignment=closedSet.get(parent);
				int dataReadyTime=parentAssignment._startTime + bnb._nodeWeights[parent];
				if(processor!=parentAssignment._processor) dataReadyTime+=bnb._arcWeights[parent][nodeId];
				startTime=dataReadyTime>startTime?dataReadyTime:startTime;
			}
			//when processor is ready
			startTime=finishTimes[processor]>startTime?finishTimes[processor]:startTime;
			
			idleTime+=startTime-finishTimes[processor];//processor idle time

			int perfectLoadBalancing=(bnb.totalNodeWeights+idleTime)/bnb._processingCores;
			lowerBound=(startTime+bnb.bottomLevels[nodeId])>perfectLoadBalancing?(startTime+bnb.bottomLevels[nodeId]):perfectLoadBalancing;
		}
		
		//update data structures
		closedSet.put(nodeId, new Tuple(startTime, processor));
		openSet.remove(nodeId);		
		independentSet.remove(nodeId);
		//updateIndependentChildren(nodeId);
		finishTimes[processor]+=startTime+bnb._nodeWeights[nodeId];
	}

	/**
	 * Clones the parent schedule for this next schedule
	 * @author Abby S
	 * 
	 * @param parentSchedule
	 */
	private void cloneParentSchedule(Schedule parentSchedule) {
		/*for(int i=0; i<numProcessors;i++){
			finishTimes[i]=parentSchedule.finishTimes[i];
		}
		idleTime=parentSchedule.idleTime;
		openSet=(Set<BnbNode>) bnbClone(parentSchedule.openSet);
		closedSet=(Map<BnbNode, Tuple>) bnbClone(parentSchedule.closedSet);
		independentSet=(Set<BnbNode>) bnbClone(parentSchedule.independentSet);*/
	}
	
	/**
	 * Disgusting stub method so compiler doesn't screen at me
	 * Hope cloning issue to be solved by using primitives
	 * 
	 * @author Abby S
	 * 
	 * @param toCloneparentScheduleObject
	 * @return
	 */
	private Object bnbClone(Object toCloneparentScheduleObject) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds any children that don't have any other parents they're waiting on
	 * 
	 * @author Abby S
	 * 
	 * @param parent
	 */
	private void updateIndependentChildren(int parent) {
		/*for(BnbNode child:parent.outneighbours){
			for(BnbNode p:child.inneighbours){
				if(openSet.contains(p));
				break; //still waiting on a parent
			}
			independentSet.add(child); //not waiting on any parents
		}*/
	}

	/**
	 * Java does not have a tuple class!! :O
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