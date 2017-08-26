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
        TaskID<Void> partial = startNewBnbThread(schedule);//####[42]####
        _schedulesToComplete.add(partial);//####[43]####
    }//####[44]####
//####[52]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[52]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[52]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[52]####
            try {//####[52]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[52]####
                    BnBSchedule.class//####[52]####
                });//####[52]####
            } catch (Exception e) {//####[52]####
                e.printStackTrace();//####[52]####
            }//####[52]####
        }//####[52]####
    }//####[52]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[52]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return startNewBnbThread(schedule, new TaskInfo());//####[52]####
    }//####[52]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[52]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[52]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setParameters(schedule);//####[52]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[52]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return startNewBnbThread(schedule, new TaskInfo());//####[52]####
    }//####[52]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[52]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[52]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setTaskIdArgIndexes(0);//####[52]####
        taskinfo.addDependsOn(schedule);//####[52]####
        taskinfo.setParameters(schedule);//####[52]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[52]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return startNewBnbThread(schedule, new TaskInfo());//####[52]####
    }//####[52]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[52]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[52]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setQueueArgIndexes(0);//####[52]####
        taskinfo.setIsPipeline(true);//####[52]####
        taskinfo.setParameters(schedule);//####[52]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[52]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[52]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[54]####
        worker.execute();//####[56]####
        if (worker.getLocalOptimalLength() == upperBound.get()) //####[57]####
        {//####[57]####
            optimalSchedule = worker.optimalSchedule;//####[58]####
        }//####[59]####
        worker = null;//####[60]####
    }//####[61]####
//####[61]####
//####[69]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * @author Aimee T
     *///####[69]####
    @Override//####[69]####
    protected boolean isParallel(int closedSet) {//####[69]####
        return closedSet > 3;//####[71]####
    }//####[72]####
//####[80]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the behaviour required.
     * @author Aimee T
     *///####[80]####
    @Override//####[80]####
    protected void doParallel(BnBSchedule s) {//####[80]####
        createTask(s);//####[81]####
    }//####[82]####
//####[88]####
    /**
	 * Class responsible for expanding partial schedules, which are the subtasks the branch and bound
	 * algorithm is split into.
	 *///####[88]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[88]####
//####[88]####
        /*  ParaTask helper method to access private/protected slots *///####[88]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[88]####
            if (m.getParameterTypes().length == 0)//####[88]####
                m.invoke(instance);//####[88]####
            else if ((m.getParameterTypes().length == 1))//####[88]####
                m.invoke(instance, arg);//####[88]####
            else //####[88]####
                m.invoke(instance, arg, interResult);//####[88]####
        }//####[88]####
//####[89]####
        private BnBSchedule _initialPartialSchedule;//####[89]####
//####[90]####
        private int _localOptimalLength;//####[90]####
//####[98]####
        /**
		 * Constructor sets all the necessary fields, cloning values from the input 
		 * ParallelBranchAndBoundAlgorithmManager when necessasry.
		 * 
		 * @author Aimee
		 *///####[98]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[98]####
            super(processingCores);//####[99]####
            _initialPartialSchedule = currentPartialSchedule;//####[100]####
            upperBound = m.upperBound;//####[101]####
            sources = new ArrayList<Integer>(m.sources);//####[102]####
            bottomLevels = m.bottomLevels.clone();//####[103]####
            numNodes = m.numNodes;//####[104]####
            totalNodeWeights = m.totalNodeWeights;//####[105]####
            brokenTrees = m.brokenTrees;//####[106]####
            _nodeWeights = m._nodeWeights.clone();//####[108]####
            _arcs = m._arcs.clone();//####[109]####
            _arcWeights = m._arcWeights.clone();//####[110]####
        }//####[111]####
//####[120]####
        /**
		 * Starts running the branch and bound algorithm (without taking input or generating
		 * and output file)
		 * 
		 * @author Aimee T
		 *///####[120]####
        @Override//####[120]####
        public void execute() {//####[120]####
            bnb(_initialPartialSchedule);//####[121]####
        }//####[122]####
//####[130]####
        /**
         * Sets the value of the length of this worker's optimal schedule
         * 
         * @author Aimee T
         *///####[130]####
        @Override//####[130]####
        protected void setOptimalSchedule(BnBSchedule schedule) {//####[130]####
            super.setOptimalSchedule(schedule);//####[131]####
            _localOptimalLength = schedule.getMaxFinishTime();//####[132]####
        }//####[133]####
//####[140]####
        /**
         * Sets the value of the length of this worker's optimal schedule
         * 
         * @author Aimee T
         *///####[140]####
        protected int getLocalOptimalLength() {//####[140]####
            return _localOptimalLength;//####[141]####
        }//####[142]####
    }//####[142]####
}//####[142]####
