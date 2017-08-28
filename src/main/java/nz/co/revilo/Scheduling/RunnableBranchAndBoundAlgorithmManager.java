package nz.co.revilo.Scheduling;

import java.util.List;

public class RunnableBranchAndBoundAlgorithmManager extends BranchAndBoundAlgorithmManager {
	private int _threads;
	private int _threadsCreated = 0;

	public RunnableBranchAndBoundAlgorithmManager(int processingCores, int numThreads) {
		super(processingCores);
		_threads=numThreads;
	}
	
	@Override
	protected void bnbRecursive(List<BnBSchedule> nextSchedules) {    	
        for (BnBSchedule nextSchedule : nextSchedules) {
        	if(_threadsCreated < _threads){
        		_threadsCreated++;
        		BranchAndBoundRunnable runnable = new BranchAndBoundRunnable(_processingCores, nextSchedule, _threadsCreated);
        		Thread thread = new Thread(runnable);
        		System.out.println(nextSchedule.toString());
        		thread.start();
        	} else {
                bnb(nextSchedule);
        	}
        }
    }
}