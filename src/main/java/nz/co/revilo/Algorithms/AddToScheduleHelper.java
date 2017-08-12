package nz.co.revilo.Algorithms;

import java.util.Map;
import java.util.Set;

import nz.co.revilo.Algorithms.BranchAndBound.BnbNode;
import nz.co.revilo.Algorithms.BranchAndBound.Schedule;
import nz.co.revilo.Algorithms.BranchAndBound.Schedule.Tuple;

public class AddToScheduleHelper {
	BranchAndBound _bnb;

	public AddToScheduleHelper(BranchAndBound bnb){
		_bnb=bnb;
	}

	public void addToSchedule(Schedule newSchedule, Schedule baseSchedule){
		for(int i=0; i<_bnb.numProcessors;i++){
			newSchedule.finishTimes[i]=baseSchedule.finishTimes[i];
		}
		newSchedule.idleTime=baseSchedule.idleTime;
		newSchedule.openSet=(Set<BnbNode>) bnbClone(baseSchedule.openSet);
		newSchedule.closedSet=(Map<BnbNode, Tuple>) bnbClone(baseSchedule.closedSet);
		newSchedule.independentSet=(Set<BnbNode>) bnbClone(baseSchedule.independentSet);
	}


	private Object bnbClone(Object toCloneBaseScheduleObject) {
		// TODO Auto-generated method stub
		return null;
	}
}
