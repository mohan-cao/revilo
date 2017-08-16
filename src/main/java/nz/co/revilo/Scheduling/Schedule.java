package nz.co.revilo.Scheduling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;

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
	BranchAndBoundAlgorithmManager bnb;
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
		finishTimes = new int[bnb._processingCores];
		this.bnb=bnb;
		
		//scheduling on a root node
		if(parentSchedule==null){
			//initialise data structures
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
		updateIndependentChildren(nodeId);
		finishTimes[processor]+=startTime+bnb._nodeWeights[nodeId];
	}

	/**
	 * Clones the parent schedule for this next schedule
	 * @author Abby S
	 * 
	 * @param parentSchedule
	 */
	private void cloneParentSchedule(Schedule parentSchedule) {
		for(int i=0; i<bnb._processingCores;i++) finishTimes[i]=parentSchedule.finishTimes[i];
		idleTime=parentSchedule.idleTime;
		Integer element;
		java.util.Iterator<Integer> iterator=parentSchedule.openSet.iterator();
		for(int n=0; n<parentSchedule.openSet.size();n++){
			element=iterator.next();
			openSet.add(element);
		}
		//TODO: Fix for rest
		Integer nodeKey;
		while((nodeKey=parentSchedule.closedSet.keySet().iterator().next()) != null){
			Tuple t = parentSchedule.closedSet.get(nodeKey);
			closedSet.put(nodeKey, new Tuple(t._startTime, t._processor));
		}
		while((element=parentSchedule.independentSet.iterator().next()) != null){
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
			for(int p:NeighbourManagerHelper.getInneighbours(child)){
				if(openSet.contains(p));
				break; //still waiting on a parent
			}
			independentSet.add(child); //not waiting on any parents
		}
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