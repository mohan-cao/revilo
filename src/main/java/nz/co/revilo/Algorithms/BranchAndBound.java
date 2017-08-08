package nz.co.revilo.Algorithms;

import java.util.List;

/**
 * Depth First Search!!
 * 
 * @author Abby S
 *
 */
public class BranchAndBound {

	public static void branchAndBound(List<Node> priorityQueue, int[][] costMatrix, int numProcessors){
		//PQ for nodes		
		Node top = priorityQueue.remove(0);
		
		//somehow get i, j of this node in matrix
		
		//children nodes
		//child.cost = top.cost + costMatrix[i][j];
		
	}
	
	/**
	 * Represents a node
	 * @author Abby S
	 *
	 */
	class Node {
		
	}
}