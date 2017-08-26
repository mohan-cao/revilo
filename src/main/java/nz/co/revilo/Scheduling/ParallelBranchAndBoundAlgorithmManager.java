package nz.co.revilo.Scheduling;//####[1]####
//####[1]####
//-- ParaTask related imports//####[1]####
import pt.runtime.*;//####[1]####
import java.util.concurrent.ExecutionException;//####[1]####
import java.util.concurrent.locks.*;//####[1]####
import java.lang.reflect.*;//####[1]####
import pt.runtime.GuiThread;//####[1]####
import java.util.concurrent.BlockingQueue;//####[1]####
import java.util.ArrayList;//####[1]####
import java.util.List;//####[1]####
//####[1]####
/**
 * Algorithm manager for the parallelised implementation of branch and bound, which makes use
 * pf ParallelTask
 * 
 * @author Aimee T
 *///####[8]####
public class ParallelBranchAndBoundAlgorithmManager extends BranchAndBoundAlgorithmManager {//####[9]####
    static{ParaTask.init();}//####[9]####
    /*  ParaTask helper method to access private/protected slots *///####[9]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[9]####
        if (m.getParameterTypes().length == 0)//####[9]####
            m.invoke(instance);//####[9]####
        else if ((m.getParameterTypes().length == 1))//####[9]####
            m.invoke(instance, arg);//####[9]####
        else //####[9]####
            m.invoke(instance, arg, interResult);//####[9]####
    }//####[9]####
//####[10]####
    private TaskIDGroup<BnBSchedule> _schedulesToComplete;//####[10]####
//####[16]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 * @author Aimee T
	 *///####[16]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores, int threads) {//####[16]####
        super(processingCores);//####[17]####
        _schedulesToComplete = new TaskIDGroup<BnBSchedule>(threads);//####[18]####
    }//####[19]####
//####[22]####
    @Override//####[22]####
    protected void startBnb() {//####[22]####
        while (!rootSchedules.isEmpty()) //####[23]####
        {//####[23]####
            bnb(rootSchedules.remove(0));//####[24]####
        }//####[25]####
        try {//####[26]####
            _schedulesToComplete.waitTillFinished();//####[27]####
        } catch (InterruptedException e) {//####[28]####
            e.printStackTrace();//####[29]####
        } catch (ExecutionException e) {//####[30]####
            e.printStackTrace();//####[31]####
        }//####[32]####
    }//####[33]####
//####[40]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 * 
	 * @author Aimee T
	 *///####[40]####
    private void createTask(BnBSchedule schedule) {//####[40]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[41]####
        _schedulesToComplete.add(partial);//####[42]####
    }//####[43]####
//####[51]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[51]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[51]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[51]####
            try {//####[51]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[51]####
                    BnBSchedule.class//####[51]####
                });//####[51]####
            } catch (Exception e) {//####[51]####
                e.printStackTrace();//####[51]####
            }//####[51]####
        }//####[51]####
    }//####[51]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[51]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return startNewBnbThread(schedule, new TaskInfo());//####[51]####
    }//####[51]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[51]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[51]####
        // ensure Method variable is set//####[51]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[51]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[51]####
        }//####[51]####
        taskinfo.setParameters(schedule);//####[51]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[51]####
        taskinfo.setInstance(this);//####[51]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[51]####
    }//####[51]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[51]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return startNewBnbThread(schedule, new TaskInfo());//####[51]####
    }//####[51]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[51]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[51]####
        // ensure Method variable is set//####[51]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[51]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[51]####
        }//####[51]####
        taskinfo.setTaskIdArgIndexes(0);//####[51]####
        taskinfo.addDependsOn(schedule);//####[51]####
        taskinfo.setParameters(schedule);//####[51]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[51]####
        taskinfo.setInstance(this);//####[51]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[51]####
    }//####[51]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[51]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return startNewBnbThread(schedule, new TaskInfo());//####[51]####
    }//####[51]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[51]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[51]####
        // ensure Method variable is set//####[51]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[51]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[51]####
        }//####[51]####
        taskinfo.setQueueArgIndexes(0);//####[51]####
        taskinfo.setIsPipeline(true);//####[51]####
        taskinfo.setParameters(schedule);//####[51]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[51]####
        taskinfo.setInstance(this);//####[51]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[51]####
    }//####[51]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[51]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[51]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[52]####
        worker.execute();//####[53]####
        if (worker.getLocalOptimalLength() == upperBound.get()) //####[54]####
        {//####[54]####
            optimalSchedule = worker.optimalSchedule;//####[55]####
        }//####[56]####
        worker = null;//####[57]####
    }//####[58]####
//####[58]####
//####[66]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * @author Aimee T
     *///####[66]####
    @Override//####[66]####
    protected boolean isParallel(int closedSet) {//####[66]####
        return closedSet > 3;//####[67]####
    }//####[68]####
//####[76]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the behaviour required.
     * @author Aimee T
     *///####[76]####
    @Override//####[76]####
    protected void doParallel(BnBSchedule s) {//####[76]####
        createTask(s);//####[77]####
    }//####[78]####
//####[84]####
    /**
	 * Class responsible for expanding partial schedules, which are the subtasks the branch and bound
	 * algorithm is split into.
	 *///####[84]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[84]####
//####[84]####
        /*  ParaTask helper method to access private/protected slots *///####[84]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[84]####
            if (m.getParameterTypes().length == 0)//####[84]####
                m.invoke(instance);//####[84]####
            else if ((m.getParameterTypes().length == 1))//####[84]####
                m.invoke(instance, arg);//####[84]####
            else //####[84]####
                m.invoke(instance, arg, interResult);//####[84]####
        }//####[84]####
//####[85]####
        private BnBSchedule _initialPartialSchedule;//####[85]####
//####[86]####
        private int _localOptimalLength;//####[86]####
//####[94]####
        /**
		 * Constructor sets all the necessary fields, cloning values from the input 
		 * ParallelBranchAndBoundAlgorithmManager when necessasry.
		 * 
		 * @author Aimee
		 *///####[94]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[94]####
            super(processingCores);//####[95]####
            _initialPartialSchedule = currentPartialSchedule;//####[96]####
            upperBound = m.upperBound;//####[97]####
            sources = new ArrayList<Integer>(m.sources);//####[98]####
            bottomLevels = m.bottomLevels.clone();//####[99]####
            numNodes = m.numNodes;//####[100]####
            totalNodeWeights = m.totalNodeWeights;//####[101]####
            brokenTrees = m.brokenTrees;//####[102]####
            _nodeWeights = m._nodeWeights.clone();//####[104]####
            _arcs = m._arcs.clone();//####[105]####
            _arcWeights = m._arcWeights.clone();//####[106]####
        }//####[107]####
//####[116]####
        /**
		 * Starts running the branch and bound algorithm (without taking input or generating
		 * and output file)
		 * 
		 * @author Aimee T
		 *///####[116]####
        @Override//####[116]####
        public void execute() {//####[116]####
            bnb(_initialPartialSchedule);//####[117]####
        }//####[118]####
//####[126]####
        /**
         * Sets the value of the length of this worker's optimal schedule
         * 
         * @author Aimee T
         *///####[126]####
        @Override//####[126]####
        protected void setOptimalSchedule(BnBSchedule schedule) {//####[126]####
            super.setOptimalSchedule(schedule);//####[127]####
            _localOptimalLength = schedule.getMaxFinishTime();//####[128]####
        }//####[129]####
//####[136]####
        /**
         * Sets the value of the length of this worker's optimal schedule
         * 
         * @author Aimee T
         *///####[136]####
        protected int getLocalOptimalLength() {//####[136]####
            return _localOptimalLength;//####[137]####
        }//####[138]####
    }//####[138]####
}//####[138]####
