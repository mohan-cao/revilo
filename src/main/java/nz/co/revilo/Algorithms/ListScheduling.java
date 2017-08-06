package nz.co.revilo.Algorithms;

import java.util.List;

/**
 * Heuristic many exhaustive solution searches are based on
 * 
 * Java generics List used, but change to primitive for performance
 * 
 * @author Abby S
 *
 */
public class ListScheduling {
	static List<Node> list;
	
	public static void listSchedule(){
		int readyTime=0;
		
		for (Node n: list){			
			for(Node pred:n.predecessors){
				readyTime=Math.max(n.startTime, pred.finishTime);
			}
			
			n.startTime=readyTime;
			n.finishTime=n.startTime+n.nodeWeight;
			process(n);
		}
	}
	
	/**
	 * Visualise and print, etc for whatever needs to be done to node
	 * 
	 * @param n
	 */
	public static void process(Node n){
		//do the processing
	}
}



/**
 * Represents a node
 * @author Abby S
 *
 */
class Node {
	List<Node> predecessors;
	int startTime;
	int finishTime;
	int nodeWeight;
}