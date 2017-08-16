/*package nz.co.revilo.Scheduling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

*//**
 * Represents a schedules
 * 
 * @author Abby S
 *//*
public class Schedule {
	int[] finishTimes;
	int idleTime=0;
	int lowerBound;
	Set<BnbNode> openSet=new HashSet<>(); //need to assign to processor
	Map<BnbNode,Tuple> closedSet=new HashMap<>(); //done nodes
	Set<BnbNode> independentSet=new HashSet<>(); //nodes it depends on are done

	*//**
	 * Create schedule
	 * @author Abby S
	 * 
	 * @param parentSchedule
	 * @param node
	 * @param processor
	 *//*
	public Schedule(Schedule parentSchedule, BnbNode node, int processor){
		int startTime=0;
		
		//scheduling on a root node
		if(parentSchedule==null){
			//initialise data structures
			finishTimes = new int[numProcessors];
			openSet.addAll(nodes);
			independentSet.addAll(sources);
			lowerBound=node.bottomLevel;
		} else { //adding to a schedule
			cloneParentSchedule(parentSchedule);
			
			//when parents are done
			for(BnbNode parent:node.inneighbours){
				Tuple parentAssignment=closedSet.get(parent);
				int dataReadyTime=parentAssignment._startTime + parent.nodeWeight;
				if(processor!=parentAssignment._processor) dataReadyTime+=parent.distTo(node);
				startTime=dataReadyTime>startTime?dataReadyTime:startTime;
			}
			//when processor is ready
			startTime=finishTimes[processor]>startTime?finishTimes[processor]:startTime;
			
			idleTime+=startTime-finishTimes[processor];//processor idle time

			int perfectLoadBalancing=(totalNodeWeights+idleTime)/numProcessors;
			lowerBound=(startTime+node.bottomLevel)>perfectLoadBalancing?(startTime+node.bottomLevel):perfectLoadBalancing;
		}
		
		//update data structures
		closedSet.put(node, new Tuple(startTime, processor));
		openSet.remove(node);		
		independentSet.remove(node);
		updateIndependentChildren(node);
		finishTimes[processor]+=startTime+node.nodeWeight;
	}

	*//**
	 * Clones the parent schedule for this next schedule
	 * @author Abby S
	 * 
	 * @param parentSchedule
	 *//*
	private void cloneParentSchedule(Schedule parentSchedule) {
		for(int i=0; i<numProcessors;i++){
			finishTimes[i]=parentSchedule.finishTimes[i];
		}
		idleTime=parentSchedule.idleTime;
		openSet=(Set<BnbNode>) bnbClone(parentSchedule.openSet);
		closedSet=(Map<BnbNode, Tuple>) bnbClone(parentSchedule.closedSet);
		independentSet=(Set<BnbNode>) bnbClone(parentSchedule.independentSet);
	}
	
	*//**
	 * Disgusting stub method so compiler doesn't screen at me
	 * Hope cloning issue to be solved by using primitives
	 * 
	 * @author Abby S
	 * 
	 * @param toCloneparentScheduleObject
	 * @return
	 *//*
	private Object bnbClone(Object toCloneparentScheduleObject) {
		// TODO Auto-generated method stub
		return null;
	}

	*//**
	 * Adds any children that don't have any other parents they're waiting on
	 * 
	 * @author Abby S
	 * 
	 * @param parent
	 *//*
	private void updateIndependentChildren(BnbNode parent) {
		for(BnbNode child:parent.outneighbours){
			for(BnbNode p:child.inneighbours){
				if(openSet.contains(p));
				break; //still waiting on a parent
			}
			independentSet.add(child); //not waiting on any parents
		}
	}

	*//**
	 * Java does not have a tuple class!! :O
	 * @author Abby S
	 *
	 *//*
	class Tuple {
		int _startTime;
		int _processor;
		Tuple(int startTime, int processor){
			_startTime=startTime;
			_processor=processor;
		}
	}
}*/