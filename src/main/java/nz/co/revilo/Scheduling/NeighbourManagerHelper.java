package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.List;

/**
 * Static helper class to manage in and out neighbours
 * Uses the primitive interface and loops through matrices
 * 
 * @author Abby S
 *
 */
public class NeighbourManagerHelper {
	static int numNodes;
	static boolean[][] arcs;

	public static void setUpHelper(int numNodes, boolean[][] arcs) {
		NeighbourManagerHelper.numNodes=numNodes;
		NeighbourManagerHelper.arcs=arcs;
	}

	static List<Integer> getOutneighbours(int nodeId) {
		List<Integer> outneighbours=new ArrayList<>();

		for(int node=0; node<numNodes; node++){
			if(arcs[nodeId][node]){
				outneighbours.add(node);
			}
		}
		return outneighbours;
	}

	static List<Integer> getInneighbours(int nodeId) {
		List<Integer> inneighbours=new ArrayList<>();

		for(int node=0; node<numNodes; node++){
			if(arcs[node][nodeId]){
				inneighbours.add(node);
			}
		}
		return inneighbours;
	}

	static boolean hasInneighbours(int nodeId) {
		for(int node=0; node<numNodes; node++){
			if(arcs[node][nodeId]) return true;
		}
		return false;
	}

	static boolean hasOutneighbours(int nodeId) {
		for(int node=0; node<numNodes; node++){
			if(arcs[nodeId][node]) return true;
		}
		return false;
	}

}
