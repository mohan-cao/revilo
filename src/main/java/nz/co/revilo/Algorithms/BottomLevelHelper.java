package nz.co.revilo.Algorithms;

import java.util.List;

import nz.co.revilo.Algorithms.BranchAndBound.BnbNode;

/**
 * Helper class for calculating bottom level for nodes
 * 
 * Currently unused, as copied into BranchAndBound class
 * 
 * @author Abby S
 *
 */
public class BottomLevelHelper {
	
	/**
	 * Set the bottom level for each node
	 * Starts from sinks then bottom up
	 * Eventually covers all nodes in graph
	 * 
	 * @author Abby S
	 * 
	 * @param bottomUpSinks
	 */
	public void calculateBottomLevels(List<BnbNode> bottomUpSinks) {
		while(!bottomUpSinks.isEmpty()){
			BnbNode node = bottomUpSinks.remove(0);
			
			for(BnbNode inneighbour:node.inneighboursClone){
				inneighbour.bottomLevel=Math.max(inneighbour.bottomLevel, node.bottomLevel+node.distTo(inneighbour));
				node.inneighboursClone.remove(inneighbour);
				inneighbour.outneighboursClone.remove(node);
				if(inneighbour.outneighboursClone.isEmpty()){
					bottomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}
	}
}
