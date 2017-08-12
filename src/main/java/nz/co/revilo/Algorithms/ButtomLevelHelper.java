package nz.co.revilo.Algorithms;

import nz.co.revilo.Algorithms.BranchAndBound.BnbNode;

public class ButtomLevelHelper {
	BranchAndBound _bnb;
	
	public ButtomLevelHelper(BranchAndBound bnb){
		_bnb=bnb;
	}
	
	private void calculateBottomLevels() {
		while(!_bnb.buttomUpSinks.isEmpty()){
			BnbNode node = _bnb.buttomUpSinks.remove(0);
			
			for(BnbNode inneighbour:node.inneighboursClone){
				inneighbour.bottomLevel=Math.max(inneighbour.bottomLevel, node.bottomLevel+node.distTo(inneighbour));
				node.inneighboursClone.remove(inneighbour);
				inneighbour.outneighboursClone.remove(node);
				if(inneighbour.outneighboursClone.isEmpty()){
					_bnb.buttomUpSinks.add(inneighbour);//become a sink now that child is removed
				}
			}
		}
	}
}
