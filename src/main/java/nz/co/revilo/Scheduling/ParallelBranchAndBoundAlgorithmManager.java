package nz.co.revilo.Scheduling;//####[1]####
//####[1]####
//-- ParaTask related imports//####[1]####

import pt.runtime.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
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
//####[30]####
    /**
	 * Starts the branch and bound algorithm on a thread, and then waits for the 
	 * tasks occurring in parallel to finish
	 * 
	 * @author Aimee T.
	 *///####[30]####
    @Override//####[30]####
    protected void startBnb() {//####[30]####
        while (!rootSchedules.isEmpty()) //####[31]####
        {//####[31]####
            bnb(rootSchedules.remove(0));//####[32]####
        }//####[33]####
        try {//####[34]####
            _schedulesToComplete.waitTillFinished();//####[35]####
        } catch (InterruptedException e) {//####[36]####
            e.printStackTrace();//####[37]####
        } catch (ExecutionException e) {//####[38]####
            e.printStackTrace();//####[39]####
        }//####[40]####
    }//####[41]####
//####[48]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 * 
	 * @author Aimee T
	 *///####[48]####
    private void createTask(BnBSchedule schedule) {//####[48]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[49]####
        _schedulesToComplete.add(partial);//####[50]####
    }//####[51]####
//####[53]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[53]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[53]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[53]####
            try {//####[53]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[53]####
                    BnBSchedule.class//####[53]####
                });//####[53]####
            } catch (Exception e) {//####[53]####
                e.printStackTrace();//####[53]####
            }//####[53]####
        }//####[53]####
    }//####[53]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return startNewBnbThread(schedule, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[53]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setParameters(schedule);//####[53]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return startNewBnbThread(schedule, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[53]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setTaskIdArgIndexes(0);//####[53]####
        taskinfo.addDependsOn(schedule);//####[53]####
        taskinfo.setParameters(schedule);//####[53]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return startNewBnbThread(schedule, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[53]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setQueueArgIndexes(0);//####[53]####
        taskinfo.setIsPipeline(true);//####[53]####
        taskinfo.setParameters(schedule);//####[53]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[53]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[54]####
        worker.execute();//####[55]####
        if (worker.getLocalOptimalLength() == upperBound.get()) //####[58]####
        {//####[58]####
            optimalSchedule = worker.optimalSchedule;//####[59]####
        }//####[60]####
        worker = null;//####[61]####
    }//####[62]####
//####[62]####
//####[71]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * 
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
        private int _localOptimalLength = 0;//####[96]####
//####[98]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[98]####
            super(processingCores);//####[99]####
            _initialPartialSchedule = currentPartialSchedule;//####[100]####
            upperBound = m.upperBound;//####[101]####
            sources = new ArrayList<Integer>(m.sources);//####[102]####
            bottomUpSinks = new ArrayList<Integer>(m.bottomUpSinks);//####[103]####
            bottomLevels = m.bottomLevels.clone();//####[104]####
            numNodes = m.numNodes;//####[105]####
            totalNodeWeights = m.totalNodeWeights;//####[106]####
            nodeStartTimes = new ArrayList<Integer>(m.nodeStartTimes);//####[107]####
            nodeProcessors = new ArrayList<Integer>(m.nodeProcessors);//####[108]####
            brokenTrees = m.brokenTrees;//####[109]####
            atomicBound = m.atomicBound;//####[110]####
            atomicListener = m.atomicListener;//####[111]####
            _nodeWeights = m._nodeWeights.clone();//####[113]####
            _arcs = m._arcs.clone();//####[114]####
            _arcWeights = m._arcWeights.clone();//####[115]####
        }//####[116]####
//####[125]####
        /**
		 * Starts running the branch and bound algorithm (without taking input or generating
		 * and output file)
		 * 
		 * @author Aimee T
		 *///####[125]####
        @Override//####[125]####
        public void execute() {//####[125]####
            bnb(_initialPartialSchedule);//####[126]####
        }//####[127]####
//####[135]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[135]####
        @Override//####[135]####
        protected void setOptimalSchedule(BnBSchedule schedule) {//####[135]####
            super.setOptimalSchedule(schedule);//####[136]####
            _localOptimalLength = schedule.getMaxFinishTime();//####[137]####
        }//####[138]####
//####[145]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[145]####
        protected int getLocalOptimalLength() {//####[145]####
            return _localOptimalLength;//####[146]####
        }//####[147]####
    }//####[147]####
}//####[147]####
