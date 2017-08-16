package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.List;

public class NeighbourManagerHelper {
	static int numNodes;
	static boolean[][] arcs;
	
	public static void setUpHelper(int numNodes, boolean[][] arcs) {
		NeighbourManagerHelper.numNodes=numNodes;
		NeighbourManagerHelper.arcs=arcs;
	}

	/*
	 * TODO: which of these methods are used?
	 * Use getNeighbours().size!=0 instead of hasNeighbour()?
	 */
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

	static int numInneighbours(int nodeId) {
		int count=0;

		for(int node=0; node<numNodes; node++){
			if(arcs[node][nodeId]) count++;
		}
		return count;
	}

	static int numOutneighbours(int nodeId) {
		int count=0;

		for(int node=0; node<numNodes; node++){
			if(arcs[nodeId][node]) count++;
		}
		return count;
	}
}
