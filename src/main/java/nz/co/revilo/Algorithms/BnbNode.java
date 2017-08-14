package nz.co.revilo.Algorithms;

import java.util.List;

/**
 * Represents a node
 * Will be represented in a matrix by the i,j position
 * 
 * Currently unused, as copied into BranchAndBound class
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
//	int rowColNumber; //represents the row/col in matrix this node is on

	//to return distance based on weight matrix
	public int distTo(BnbNode neighbour){
		return -1; //currently returns value for if no edge
	}
}
