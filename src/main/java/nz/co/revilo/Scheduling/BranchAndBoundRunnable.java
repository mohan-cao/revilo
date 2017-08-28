package nz.co.revilo.Scheduling;

public class BranchAndBoundRunnable extends BranchAndBoundAlgorithmManager implements Runnable{

	private BnBSchedule _startingSchedule;
	private int _threadNum;

	public BranchAndBoundRunnable(int processingCores, BnBSchedule startingSchedule, int threadNum) {
		super(processingCores);
		_startingSchedule = startingSchedule;
		_threadNum=threadNum;
	}

	@Override
	public void run() {
		System.out.println("Running: " + _threadNum);
		bnb(_startingSchedule);
	}

}
