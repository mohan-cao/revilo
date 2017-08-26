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
//####[11]####
    private int _threads;//####[11]####
//####[17]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 * @author Aimee T
	 *///####[17]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores, int threads) {//####[17]####
        super(processingCores);//####[18]####
        _schedulesToComplete = new TaskIDGroup<BnBSchedule>(threads);//####[19]####
        _threads = threads;//####[20]####
    }//####[21]####
//####[24]####
    @Override//####[24]####
    protected void startBnb() {//####[24]####
        while (!rootSchedules.isEmpty()) //####[25]####
        {//####[25]####
            bnb(rootSchedules.remove(0));//####[26]####
        }//####[27]####
        try {//####[28]####
            _schedulesToComplete.waitTillFinished();//####[29]####
        } catch (InterruptedException e) {//####[30]####
            e.printStackTrace();//####[31]####
        } catch (ExecutionException e) {//####[32]####
            e.printStackTrace();//####[33]####
        }//####[34]####
    }//####[35]####
//####[42]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 * 
	 * @author Aimee T
	 *///####[42]####
    private void createTask(BnBSchedule schedule) {//####[42]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[44]####
        _schedulesToComplete.add(partial);//####[45]####
    }//####[46]####
//####[54]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[54]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[54]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[54]####
            try {//####[54]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[54]####
                    BnBSchedule.class//####[54]####
                });//####[54]####
            } catch (Exception e) {//####[54]####
                e.printStackTrace();//####[54]####
            }//####[54]####
        }//####[54]####
    }//####[54]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[54]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[54]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[54]####
        return startNewBnbThread(schedule, new TaskInfo());//####[54]####
    }//####[54]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[54]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[54]####
        // ensure Method variable is set//####[54]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[54]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[54]####
        }//####[54]####
        taskinfo.setParameters(schedule);//####[54]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[54]####
        taskinfo.setInstance(this);//####[54]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[54]####
    }//####[54]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[54]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[54]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[54]####
        return startNewBnbThread(schedule, new TaskInfo());//####[54]####
    }//####[54]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[54]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[54]####
        // ensure Method variable is set//####[54]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[54]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[54]####
        }//####[54]####
        taskinfo.setTaskIdArgIndexes(0);//####[54]####
        taskinfo.addDependsOn(schedule);//####[54]####
        taskinfo.setParameters(schedule);//####[54]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[54]####
        taskinfo.setInstance(this);//####[54]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[54]####
    }//####[54]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[54]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[54]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[54]####
        return startNewBnbThread(schedule, new TaskInfo());//####[54]####
    }//####[54]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[54]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[54]####
        // ensure Method variable is set//####[54]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[54]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[54]####
        }//####[54]####
        taskinfo.setQueueArgIndexes(0);//####[54]####
        taskinfo.setIsPipeline(true);//####[54]####
        taskinfo.setParameters(schedule);//####[54]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[54]####
        taskinfo.setInstance(this);//####[54]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[54]####
    }//####[54]####
    /**
	 * Creates and executes a new WorkerBnB object to continue expanding the input partial
	 * schedule
	 * 
	 * @author Aimee T
	 *///####[54]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[54]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[56]####
        worker.execute();//####[58]####
        if (worker.getLocalOptimalLength() == upperBound.get()) //####[59]####
        {//####[59]####
            optimalSchedule = worker.optimalSchedule;//####[60]####
        }//####[61]####
        worker = null;//####[62]####
    }//####[63]####
//####[63]####
//####[71]####
    /**
	 * Hook method to be implemented by subclasses which need specific behaviour when a particular 
	 * recursion depth is reached. This method implements the depth check.
	 * @author Aimee T
	 *///####[71]####
    @Override//####[71]####
    protected boolean isParallel(int closedSet) {//####[71]####
        if (_threads == 1) //####[73]####
        {//####[73]####
            return false;//####[74]####
        } else {//####[75]####
            return closedSet > 2;//####[76]####
        }//####[77]####
    }//####[78]####
//####[86]####
    /**
	 * Hook method to be implemented by subclasses which need specific behaviour when a particular 
	 * recursion depth is reached. This method implements the behaviour required.
	 * @author Aimee T
	 *///####[86]####
    @Override//####[86]####
    protected void doParallel(BnBSchedule s) {//####[86]####
        createTask(s);//####[87]####
    }//####[88]####
//####[94]####
    /**
	 * Class responsible for expanding partial schedules, which are the subtasks the branch and bound
	 * algorithm is split into.
	 *///####[94]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[94]####
//####[94]####
        /*  ParaTask helper method to access private/protected slots *///####[94]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[94]####
            if (m.getParameterTypes().length == 0)//####[94]####
                m.invoke(instance);//####[94]####
            else if ((m.getParameterTypes().length == 1))//####[94]####
                m.invoke(instance, arg);//####[94]####
            else //####[94]####
                m.invoke(instance, arg, interResult);//####[94]####
        }//####[94]####
//####[95]####
        private BnBSchedule _initialPartialSchedule;//####[95]####
//####[96]####
        private int _localOptimalLength;//####[96]####
//####[104]####
        /**
		 * Constructor sets all the necessary fields, cloning values from the input 
		 * ParallelBranchAndBoundAlgorithmManager when necessasry.
		 * 
		 * @author Aimee
		 *///####[104]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[104]####
            super(processingCores);//####[105]####
            _initialPartialSchedule = currentPartialSchedule;//####[106]####
            upperBound = m.upperBound;//####[107]####
            bottomLevels = m.bottomLevels.clone();//####[108]####
            numNodes = m.numNodes;//####[109]####
            totalNodeWeights = m.totalNodeWeights;//####[110]####
            brokenTrees = m.brokenTrees;//####[111]####
            atomicBound = m.atomicBound;//####[112]####
            atomicListener = m.atomicListener;//####[113]####
            _nodeWeights = m._nodeWeights.clone();//####[115]####
            _arcs = m._arcs.clone();//####[116]####
            _arcWeights = m._arcWeights.clone();//####[117]####
        }//####[118]####
//####[127]####
        /**
		 * Starts running the branch and bound algorithm (without taking input or generating
		 * and output file)
		 * 
		 * @author Aimee T
		 *///####[127]####
        @Override//####[127]####
        public void execute() {//####[127]####
            bnb(_initialPartialSchedule);//####[128]####
        }//####[129]####
//####[137]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[137]####
        @Override//####[137]####
        protected void setOptimalSchedule(BnBSchedule schedule) {//####[137]####
            super.setOptimalSchedule(schedule);//####[138]####
            _localOptimalLength = schedule.getMaxFinishTime();//####[139]####
        }//####[140]####
//####[147]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[147]####
        protected int getLocalOptimalLength() {//####[147]####
            return _localOptimalLength;//####[148]####
        }//####[149]####
    }//####[149]####
}//####[149]####
