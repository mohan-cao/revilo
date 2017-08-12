package nz.co.revilo.Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Branch and Bound solution using objects
 * 
 * @author Abby S
 *
 */
public class BranchAndBound {
	private List<BnbNode> nodes;
	private int numProcessors;
	private List<Schedule> schedules=new ArrayList<>();
	private List<BnbNode> sources=new ArrayList<>();
	private List<BnbNode> buttomUpSinks=new ArrayList<>();
	private int upperBound; //TODO: find one to use
	private Schedule optimalSchedule;

	/**
	 * To use:
	 * - Create BranchAndBound object
	 * - Get BranchAndBound.optimalSchedule
	 * 
	 * @author Abby S
	 * 
	 * @param graphPrimitives
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
					schedules.add(newSchedule);
				}
			}		
			//sinks
			else if(node.outneighboursClone.size()==0){
				buttomUpSinks.add(node);
				node.bottomLevel=node.nodeWeight;
			}
		}
		
		calculateBottomLevels();
		
		while(!schedules.isEmpty()){
			bnb(schedules.remove(0));
		}
		
		//by here optimalSchedule is the optimal schedule
	}

	/**
	 * Calculates the buttom levels for all nodes in graph
	 * @author Abby S
	 * 
	 */
	private void calculateBottomLevels() {
		while(!buttomUpSinks.isEmpty()){
			BnbNode node = buttomUpSinks.remove(0);
			
			for(BnbNode inneighbour:node.inneighboursClone){
				inneighbour.bottomLevel=Math.max(inneighbour.bottomLevel, node.bottomLevel+node.distTo(inneighbour));
				node.inneighboursClone.remove(inneighbour);
				inneighbour.outneighboursClone.remove(node);
				if(inneighbour.outneighboursClone.isEmpty()){
					buttomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}
	}

	/**
	 * @author Abby S
	 */
	private void bnb(Schedule s) {
		
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
		List<BnbNode> outneighboursClone;
		List<BnbNode> inneighboursClone;
		int bottomLevel;
		int nodeWeight;
//		int rowColNumber; //represents the row/col in matrix this node is on

		//to return distance based on weight matrix
		public int distTo(BnbNode neighbour){
			return -1; //currently returns value for if no edge
		}
	}

	/**
	 * @author Abby S
	 */
	class Schedule {
		int[] finishTimes;
		int idleTime=0;
		int bestFinishTime;
		Set<BnbNode> openSet=new HashSet<>(); //need to assign to processor
		Map<BnbNode,Tuple> closedSet=new HashMap<>(); //done
		Set<BnbNode> independentSet=new HashSet<>(); //nodes it depends on are done

		
		/**
		 * Create new schedule
		 * @author Abby S
		 * @param node
		 * @param processor
		 */
		public Schedule(Schedule baseSchedule, BnbNode node, int processor){
			
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