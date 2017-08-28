package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import nz.co.revilo.Output.NewOptimalResultListener;

public class PtBnb {
		private ArrayList<BnBSchedule> _schedulesToComplete;
		private int _threads;
		public AtomicInteger upperBound;
		public Collection<? extends Integer> sources;
		public Collection<? extends Integer> bottomUpSinks;
		public Object bottomLevels;
		public int numNodes;
		public int totalNodeWeights;
		public Collection<? extends Integer> nodeStartTimes;
		public Collection<? extends Integer> nodeProcessors;
		public AtomicLong brokenTrees;
		public AtomicInteger atomicBound;
		public AtomicReference<NewOptimalResultListener> atomicListener;
		public Object _nodeWeights;
		public Object _arcs;
		public Object _arcWeights;
		
		/**
		 * Constructor which sets the number of processing cores we are scheduling for
		 * @author Aimee T
		 */
		public PtBnb(int processingCores, int threads) {
			_schedulesToComplete = new ArrayList<BnBSchedule>(threads);
			_threads = threads;
		}

		/**
		 * Starts the branch and bound algorithm on a thread, and then waits for the 
		 * tasks occurring in parallel to finish
		 * 
		 * @author Aimee T.
		 */
		protected void startBnb() {
	    	ArrayList<BnBSchedule> rootSchedules = null;
			while(!rootSchedules.isEmpty()){
	    		//bnb(rootSchedules.remove(0));
	        }
	    		//_schedulesToComplete.waitTillFinished();
	    	
	    }

		/**
		 * Creates the ParallelTask task which needs to be scheduled for execution
		 * 
		 * @author Aimee T
		 */
		private void createTask(BnBSchedule schedule) {
			//Object partial = startNewBnbThread(schedule);
			//_schedulesToComplete.add((BnBSchedule) partial);
		}
		
		/*
		private Object startNewBnbThread(BnBSchedule schedule) {
			int _processingCores;
			WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);
			worker.execute();
			AtomicInteger upperBound;
			// If the worker found the last optimal length schedule, set that
			// to be the optimal schedule
			if(worker.getLocalOptimalLength() == upperBound.get()) {
				BnBSchedule optimalSchedule = worker.optimalSchedule;
			}
			worker = null; // null it for hopeful garbage collection
		}
		*/
		
	    /**
	     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
	     * recursion depth is reached. This method implements the depth check.
	     * 
	     * @author Aimee T
	     */
		protected boolean isParallel(int closedSet) {
			// Start parallelising when deeper than 3 nodes into the branching
			if(_threads == 1) {
				return false;
			} else {
				return closedSet > 2;
			}
	    }
	    
	    /**
	     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
	     * recursion depth is reached. This method implements the behaviour required.
	     * @author Aimee T
	     */
		protected void doParallel(BnBSchedule s) {
			createTask(s);
	    }
		
		/**
		 * Class responsible for expanding partial schedules, which are the subtasks the branch and bound
		 * algorithm is split into.
		 */
		private class WorkerBnb extends BranchAndBoundAlgorithmManager{
			private BnBSchedule _initialPartialSchedule;
			private int _localOptimalLength = 0;

			public WorkerBnb(PtBnb m, BnBSchedule currentPartialSchedule, int processingCores) {
				super(processingCores);
				_initialPartialSchedule = currentPartialSchedule;
				upperBound = m.upperBound;
				sources = new ArrayList<Integer>(m.sources);
				bottomUpSinks = new ArrayList<Integer>(m.bottomUpSinks);
				//bottomLevels = m.bottomLevels.clone();
				numNodes = m.numNodes;
				totalNodeWeights = m.totalNodeWeights;
				nodeStartTimes = new ArrayList<Integer>(m.nodeStartTimes);
				nodeProcessors = new ArrayList<Integer>(m.nodeProcessors);
				brokenTrees = m.brokenTrees;
				atomicBound = m.atomicBound;
				atomicListener = m.atomicListener;

				//_nodeWeights = m._nodeWeights.clone();
				//_arcs = m._arcs.clone();
				//_arcWeights = m._arcWeights.clone();
			}
			
			/**
			 * Starts running the branch and bound algorithm (without taking input or generating
			 * and output file)
			 * 
			 * @author Aimee T
			 */
	        @Override
			public void execute() {
				bnb(_initialPartialSchedule);
			}
	        
			/**
			 * Sets the value of the length of this worker's optimal schedule
			 * 
			 * @author Aimee T
			 */
			@Override
			protected void setOptimalSchedule(BnBSchedule schedule) {
				super.setOptimalSchedule(schedule);
				_localOptimalLength = schedule.getMaxFinishTime();
			}

			/**
			 * Sets the value of the length of this worker's optimal schedule
			 * 
			 * @author Aimee T
			 */
			protected int getLocalOptimalLength() {
				return _localOptimalLength;
			}
		}
	}

