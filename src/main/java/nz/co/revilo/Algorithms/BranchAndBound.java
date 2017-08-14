package nz.co.revilo.Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Branch and Bound solution using objects
 * DFS based
 * 
 * TODO: write to latest interface, use primitives?
 * 
 * @author Abby S
 *
 */
public class BranchAndBound {
	private List<BnbNode> nodes;
	private int numProcessors;
	private List<Schedule> rootSchedules=new ArrayList<>();
	private List<BnbNode> sources=new ArrayList<>();
	private List<BnbNode> bottomUpSinks=new ArrayList<>();
	private int upperBound;
	private int totalNodeWeights=0; //time if it was topological sort
	private Schedule optimalSchedule;

	/**
	 * To use:
	 * - Create BranchAndBound object
	 * - Get BranchAndBound.optimalSchedule
	 * 
	 * @author Abby S
	 * 
	 * @param graphPrimitives should actually be primitives
	 * @param np
	 */
	public BranchAndBound(List<BnbNode> graphPrimitives, int np){
		nodes=graphPrimitives;
		numProcessors=np;

		//get sources
		for(BnbNode node:nodes){
			//sources
			if (node.inneighboursClone.size()==0) {
				sources.add(node);
				//start a schedule with this node as source on each possible processor
				for(int p=0; p<numProcessors; p++){
					Schedule newSchedule = new Schedule(null, node, p); 
					rootSchedules.add(newSchedule);
				}
			}		
			//sinks
			else if(node.outneighboursClone.size()==0){
				bottomUpSinks.add(node);
				node.bottomLevel=node.nodeWeight;
			}
			
			totalNodeWeights+=node.nodeWeight;
		}
		
		upperBound=totalNodeWeights; //TODO: is this a good upper bound?
		calculateBottomLevels();
		
		while(!rootSchedules.isEmpty()){
			bnb(rootSchedules.remove(0));
		}
		
		//by here optimalSchedule is the optimal schedule
	}

	/**
	 * bnb based on the current schedule s
	 * @param s
	 * @author Abby S
	 */
	private void bnb(Schedule s) {
		//not good enough
		if(s.lowerBound>upperBound){
			return; //break tree at this point
		}
		
		//found optimal for the root started with
		if(s.openSet.isEmpty()){
			//reached end of a valid schedule. Never broke off, so is optimal
			optimalSchedule=s;
			upperBound=s.finishTimes[0];
			for(int i=1; i<numProcessors;i++){
				if(s.finishTimes[i]>upperBound) upperBound=s.finishTimes[1];
			}
			return;
		}
		
		//continue DFS
		List<Schedule> nextSchedules = new ArrayList<>();
		for(BnbNode n:s.independentSet){
			for(int p=0; p<numProcessors; p++){
				nextSchedules.add(new Schedule(s, n, p));
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
			BnbNode node = bottomUpSinks.remove(0);
			
			for(BnbNode inneighbour:node.inneighboursClone){
				inneighbour.bottomLevel=(inneighbour.bottomLevel>(node.bottomLevel+node.distTo(inneighbour)))?inneighbour.bottomLevel:(node.bottomLevel+node.distTo(inneighbour));
				node.inneighboursClone.remove(inneighbour);
				inneighbour.outneighboursClone.remove(node);
				if(inneighbour.outneighboursClone.isEmpty()){
					bottomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}
	}

	/**
	 * Represents a node
	 * Will be represented in a matrix by the i,j position
	 * 
	 * @author Abby S
	 *
	 */
	class BnbNode {
		List<BnbNode> outneighbours; //nodes with 1 on this node's row
		List<BnbNode> inneighbours; //nodes with 1 on this node's column
		List<BnbNode> outneighboursClone; //TODO: do something about cloning
		List<BnbNode> inneighboursClone;
		int bottomLevel;
		int nodeWeight;

		//TODO: to return distance based on weight matrix
		public int distTo(BnbNode neighbour){
			return -1; //currently returns value for if no edge
		}
	}

	/**
	 * Represents a schedules
	 * 
	 * @author Abby S
	 */
	class Schedule {
		int[] finishTimes;
		int idleTime=0;
		int lowerBound;
		Set<BnbNode> openSet=new HashSet<>(); //need to assign to processor
		Map<BnbNode,Tuple> closedSet=new HashMap<>(); //done nodes
		Set<BnbNode> independentSet=new HashSet<>(); //nodes it depends on are done

		/**
		 * Create schedule
		 * @author Abby S
		 * 
		 * @param parentSchedule
		 * @param node
		 * @param processor
		 */
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

		/**
		 * Clones the parent schedule for this next schedule
		 * @author Abby S
		 * 
		 * @param parentSchedule
		 */
		private void cloneParentSchedule(Schedule parentSchedule) {
			for(int i=0; i<numProcessors;i++){
				finishTimes[i]=parentSchedule.finishTimes[i];
			}
			idleTime=parentSchedule.idleTime;
			openSet=(Set<BnbNode>) bnbClone(parentSchedule.openSet);
			closedSet=(Map<BnbNode, Tuple>) bnbClone(parentSchedule.closedSet);
			independentSet=(Set<BnbNode>) bnbClone(parentSchedule.independentSet);
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
		private void updateIndependentChildren(BnbNode parent) {
			for(BnbNode child:parent.outneighbours){
				for(BnbNode p:child.inneighbours){
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
}