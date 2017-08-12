package nz.co.revilo.Algorithms;

import java.util.List;

import nz.co.revilo.Algorithms.BranchAndBound.BnbNode;

public class ButtomLevelHelper {
	BranchAndBound _bnb;
	private List<BnbNode> _buttomUpSinks;
	
	public ButtomLevelHelper(BranchAndBound bnb, List<BnbNode> buttomUpSinks){
		_bnb=bnb;
		_buttomUpSinks=buttomUpSinks;
	}
	
	public void calculateBottomLevels() {
		while(!_buttomUpSinks.isEmpty()){
			BnbNode node = _buttomUpSinks.remove(0);
			
			for(BnbNode inneighbour:node.inneighboursClone){
				inneighbour.bottomLevel=Math.max(inneighbour.bottomLevel, node.bottomLevel+node.distTo(inneighbour));
				node.inneighboursClone.remove(inneighbour);
				inneighbour.outneighboursClone.remove(node);
				if(inneighbour.outneighboursClone.isEmpty()){
					_buttomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}
	}
}
