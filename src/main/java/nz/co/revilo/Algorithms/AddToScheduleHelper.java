package nz.co.revilo.Algorithms;

import java.util.Map;
import java.util.Set;

import nz.co.revilo.Algorithms.BranchAndBound.BnbNode;
import nz.co.revilo.Algorithms.BranchAndBound.Schedule;
import nz.co.revilo.Algorithms.BranchAndBound.Schedule.Tuple;

/**
 * Helper class to create a new schedule by adding to a current schedule
 * 
 * Currently unused, as included in BranchAndBound class
 * 
 * @author Abby S
 *
 */
public class AddToScheduleHelper {
	int _np;

	public AddToScheduleHelper(int numProcessors){
		_np=numProcessors;
	}

	/**
	 * Currently only clones the base schedule
	 * 
	 * @author Abby S
	 * 
	 * @param newSchedule
	 * @param baseSchedule
	 */
	public void addToSchedule(Schedule newSchedule, Schedule baseSchedule){
		for(int i=0; i<_np;i++){
			newSchedule.finishTimes[i]=baseSchedule.finishTimes[i];
		}
		newSchedule.idleTime=baseSchedule.idleTime;
		newSchedule.openSet=(Set<BnbNode>) bnbClone(baseSchedule.openSet);
		newSchedule.closedSet=(Map<BnbNode, Tuple>) bnbClone(baseSchedule.closedSet);
		newSchedule.independentSet=(Set<BnbNode>) bnbClone(baseSchedule.independentSet);
		
		//to manipulate data structures for this new schedule
	}


	/**
	 * Dummy method for cloning
	 * 
	 * Yet to be implementated.
	 * 
	 * @param toCloneBaseScheduleObject
	 * @return
	 */
	private Object bnbClone(Object toCloneBaseScheduleObject) {
		// TODO Auto-generated method stub
		return null;
	}
}
