package nz.co.revilo.Algorithms;

import java.util.Map;
import java.util.Set;

import nz.co.revilo.Algorithms.BranchAndBound.BnbNode;
import nz.co.revilo.Algorithms.BranchAndBound.Schedule;
import nz.co.revilo.Algorithms.BranchAndBound.Schedule.Tuple;

public class AddToScheduleHelper {
	int _np;

	public AddToScheduleHelper(int numProcessors){
		_np=numProcessors;
	}

	public void addToSchedule(Schedule newSchedule, Schedule baseSchedule){
		for(int i=0; i<_np;i++){
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
