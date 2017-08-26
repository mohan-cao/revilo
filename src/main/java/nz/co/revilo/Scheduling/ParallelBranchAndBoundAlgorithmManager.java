package nz.co.revilo.Scheduling;//####[1]####
//####[1]####
import java.util.concurrent.ExecutionException;//####[3]####
//####[3]####
//-- ParaTask related imports//####[3]####
import pt.runtime.*;//####[3]####
import java.util.concurrent.ExecutionException;//####[3]####
import java.util.concurrent.locks.*;//####[3]####
import java.lang.reflect.*;//####[3]####
import pt.runtime.GuiThread;//####[3]####
import java.util.concurrent.BlockingQueue;//####[3]####
import java.util.ArrayList;//####[3]####
import java.util.List;//####[3]####
//####[3]####
/**
 * Algorithm manager for the parallelised implementation of branch and bound, which makes use
 * pf ParallelTask
 * 
 * @author Aimee T
 *///####[10]####
public class ParallelBranchAndBoundAlgorithmManager extends BranchAndBoundAlgorithmManager {//####[11]####
    static{ParaTask.init();}//####[11]####
    /*  ParaTask helper method to access private/protected slots *///####[11]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[11]####
        if (m.getParameterTypes().length == 0)//####[11]####
            m.invoke(instance);//####[11]####
        else if ((m.getParameterTypes().length == 1))//####[11]####
            m.invoke(instance, arg);//####[11]####
        else //####[11]####
            m.invoke(instance, arg, interResult);//####[11]####
    }//####[11]####
//####[12]####
    private TaskIDGroup<BnBSchedule> _schedulesToComplete;//####[12]####
//####[13]####
    private int _threads;//####[13]####
//####[19]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 * @author Aimee T
	 *///####[19]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores, int threads) {//####[19]####
        super(processingCores);//####[20]####
        _schedulesToComplete = new TaskIDGroup<BnBSchedule>(threads);//####[21]####
        _threads = threads;//####[22]####
    }//####[23]####
//####[26]####
    @Override//####[26]####
    protected void startBnb() {//####[26]####
        while (!rootSchedules.isEmpty()) //####[27]####
        {//####[27]####
            bnb(rootSchedules.remove(0));//####[28]####
        }//####[29]####
        try {//####[30]####
            _schedulesToComplete.waitTillFinished();//####[31]####
        } catch (InterruptedException e) {//####[32]####
            e.printStackTrace();//####[33]####
        } catch (ExecutionException e) {//####[34]####
            e.printStackTrace();//####[35]####
        }//####[36]####
    }//####[37]####
//####[42]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 *///####[42]####
    private void createTask(BnBSchedule schedule) {//####[42]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[43]####
        _schedulesToComplete.add(partial);//####[44]####
    }//####[45]####
//####[47]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[47]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[47]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[47]####
            try {//####[47]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[47]####
                    BnBSchedule.class//####[47]####
                });//####[47]####
            } catch (Exception e) {//####[47]####
                e.printStackTrace();//####[47]####
            }//####[47]####
        }//####[47]####
    }//####[47]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[47]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[47]####
        return startNewBnbThread(schedule, new TaskInfo());//####[47]####
    }//####[47]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[47]####
        // ensure Method variable is set//####[47]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[47]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[47]####
        }//####[47]####
        taskinfo.setParameters(schedule);//####[47]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[47]####
        taskinfo.setInstance(this);//####[47]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[47]####
    }//####[47]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[47]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[47]####
        return startNewBnbThread(schedule, new TaskInfo());//####[47]####
    }//####[47]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[47]####
        // ensure Method variable is set//####[47]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[47]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[47]####
        }//####[47]####
        taskinfo.setTaskIdArgIndexes(0);//####[47]####
        taskinfo.addDependsOn(schedule);//####[47]####
        taskinfo.setParameters(schedule);//####[47]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[47]####
        taskinfo.setInstance(this);//####[47]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[47]####
    }//####[47]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[47]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[47]####
        return startNewBnbThread(schedule, new TaskInfo());//####[47]####
    }//####[47]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[47]####
        // ensure Method variable is set//####[47]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[47]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[47]####
        }//####[47]####
        taskinfo.setQueueArgIndexes(0);//####[47]####
        taskinfo.setIsPipeline(true);//####[47]####
        taskinfo.setParameters(schedule);//####[47]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[47]####
        taskinfo.setInstance(this);//####[47]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[47]####
    }//####[47]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[47]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[48]####
        worker.execute();//####[49]####
        if (worker.getLocalOptimalLength() == upperBound.get()) //####[50]####
        {//####[50]####
            optimalSchedule = worker.optimalSchedule;//####[51]####
        }//####[52]####
        worker = null;//####[53]####
    }//####[54]####
//####[54]####
//####[62]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * @author Aimee T
     *///####[62]####
    @Override//####[62]####
    protected boolean isParallel(int closedSet) {//####[62]####
        if (_threads == 1) //####[64]####
        {//####[64]####
            return false;//####[65]####
        } else {//####[66]####
            return closedSet > 2;//####[67]####
        }//####[68]####
    }//####[69]####
//####[77]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the behaviour required.
     * @author Aimee T
     *///####[77]####
    @Override//####[77]####
    protected void doParallel(BnBSchedule s) {//####[77]####
        createTask(s);//####[78]####
    }//####[79]####
//####[81]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[81]####
//####[81]####
        /*  ParaTask helper method to access private/protected slots *///####[81]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[81]####
            if (m.getParameterTypes().length == 0)//####[81]####
                m.invoke(instance);//####[81]####
            else if ((m.getParameterTypes().length == 1))//####[81]####
                m.invoke(instance, arg);//####[81]####
            else //####[81]####
                m.invoke(instance, arg, interResult);//####[81]####
        }//####[81]####
//####[82]####
        private BnBSchedule _initialPartialSchedule;//####[82]####
//####[83]####
        private int _localOptimalLength = 0;//####[83]####
//####[85]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[85]####
            super(processingCores);//####[86]####
            _initialPartialSchedule = currentPartialSchedule;//####[87]####
            upperBound = m.upperBound;//####[88]####
            sources = new ArrayList<Integer>(m.sources);//####[89]####
            bottomUpSinks = new ArrayList<Integer>(m.bottomUpSinks);//####[90]####
            bottomLevels = m.bottomLevels.clone();//####[91]####
            numNodes = m.numNodes;//####[92]####
            totalNodeWeights = m.totalNodeWeights;//####[93]####
            nodeStartTimes = new ArrayList<Integer>(m.nodeStartTimes);//####[94]####
            nodeProcessors = new ArrayList<Integer>(m.nodeProcessors);//####[95]####
            brokenTrees = m.brokenTrees;//####[96]####
            atomicBound = m.atomicBound;//####[97]####
            atomicListener = m.atomicListener;//####[98]####
            _nodeWeights = m._nodeWeights.clone();//####[100]####
            _arcs = m._arcs.clone();//####[101]####
            _arcWeights = m._arcWeights.clone();//####[102]####
        }//####[103]####
//####[106]####
        @Override//####[106]####
        public void execute() {//####[106]####
            bnb(_initialPartialSchedule);//####[107]####
        }//####[108]####
//####[116]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[116]####
        @Override//####[116]####
        protected void setOptimalSchedule(BnBSchedule schedule) {//####[116]####
            super.setOptimalSchedule(schedule);//####[117]####
            _localOptimalLength = schedule.getMaxFinishTime();//####[118]####
        }//####[119]####
//####[126]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[126]####
        protected int getLocalOptimalLength() {//####[126]####
            return _localOptimalLength;//####[127]####
        }//####[128]####
    }//####[128]####
}//####[128]####
