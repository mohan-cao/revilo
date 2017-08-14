package nz.co.revilo.Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This algorithm stores all states in memory
 * Memory issues!
 * A* explores the least number of states, but may run out of memory before it finishes
 * 
 * IDA* and BBA* have a much smaller memory footprint
 * See the relevant classes
 * 
 * If A* runs out of memory, can use those classes for algorithm with comparable performance
 * Although cost is exploring more states, so only reasonably well performance
 * 
 * A* threads have to share the priority queue, and thus requires synchronisation
 * IDA* and BBA* can also be explored for parallelisation.
 * 
 * TODO: delete!
 * 
 * @author Abby S
 *
 */
public class AStar {
	public static void aStar(Graph g, int lower){
		List<Node> priorityQueue = new ArrayList<Node>();
		Set<Node> evaluatedNodes = new HashSet<Node>();
		
		while(!priorityQueue.isEmpty()){
			Node n = priorityQueue.remove(0);
			evaluatedNodes.add(n);
			
			for(Edge e:n.edges){
				Node child=e.to;
				
				//already looked at it, skip
				if(evaluatedNodes.contains(child)){
					continue;
				}
				
				if(!priorityQueue.contains(child)){
					//found a new node
					priorityQueue.add(child);
				}
				
				//find tentative score and use that for priority assignment
				
				Collections.sort(priorityQueue);
			}
		}
	}
	
	/**
	 * Represents a node
	 * 
	 * @author Abby S
	 *
	 */
	class Node implements Comparable<Node> {
		List<Edge> edges;
		int timeScore;

		@Override
		public int compareTo(Node o) {
			if(o.timeScore < timeScore){
				return -1;
			}
			
			if(o.timeScore > timeScore) {
				return 1;
			}
			
			return 0;
		}
	}
	
	/**
	 * Represents an edge
	 * 
	 * @author Abby S
	 *
	 */
	class Edge {
		Node from;
		Node to;
	}
	
	/**
	 * TODO: use GraphStream Graph data structure
	 * 
	 * @author Abby S
	 *
	 */
	class Graph {
		int numNodes;
	}
}
