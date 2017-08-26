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
//####[38]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 *///####[38]####
    private void createTask(BnBSchedule schedule) {//####[38]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[39]####
        _schedulesToComplete.add(partial);//####[40]####
    }//####[41]####
//####[43]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[43]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[43]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[43]####
            try {//####[43]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[43]####
                    BnBSchedule.class//####[43]####
                });//####[43]####
            } catch (Exception e) {//####[43]####
                e.printStackTrace();//####[43]####
            }//####[43]####
        }//####[43]####
    }//####[43]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[43]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[43]####
        return startNewBnbThread(schedule, new TaskInfo());//####[43]####
    }//####[43]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[43]####
        // ensure Method variable is set//####[43]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[43]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[43]####
        }//####[43]####
        taskinfo.setParameters(schedule);//####[43]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[43]####
        taskinfo.setInstance(this);//####[43]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[43]####
    }//####[43]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[43]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[43]####
        return startNewBnbThread(schedule, new TaskInfo());//####[43]####
    }//####[43]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[43]####
        // ensure Method variable is set//####[43]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[43]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[43]####
        }//####[43]####
        taskinfo.setTaskIdArgIndexes(0);//####[43]####
        taskinfo.addDependsOn(schedule);//####[43]####
        taskinfo.setParameters(schedule);//####[43]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[43]####
        taskinfo.setInstance(this);//####[43]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[43]####
    }//####[43]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[43]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[43]####
        return startNewBnbThread(schedule, new TaskInfo());//####[43]####
    }//####[43]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[43]####
        // ensure Method variable is set//####[43]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[43]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[43]####
        }//####[43]####
        taskinfo.setQueueArgIndexes(0);//####[43]####
        taskinfo.setIsPipeline(true);//####[43]####
        taskinfo.setParameters(schedule);//####[43]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[43]####
        taskinfo.setInstance(this);//####[43]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[43]####
    }//####[43]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[43]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[44]####
        worker.execute();//####[45]####
        if (worker.getLocalOptimalLength() == upperBound.get()) //####[46]####
        {//####[46]####
            optimalSchedule = worker.optimalSchedule;//####[47]####
        }//####[48]####
        worker = null;//####[49]####
    }//####[50]####
//####[50]####
//####[58]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * @author Aimee T
     *///####[58]####
    @Override//####[58]####
    protected boolean isParallel(int closedSet) {//####[58]####
        return closedSet > 2;//####[59]####
    }//####[60]####
//####[68]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the behaviour required.
     * @author Aimee T
     *///####[68]####
    @Override//####[68]####
    protected void doParallel(BnBSchedule s) {//####[68]####
        createTask(s);//####[69]####
    }//####[70]####
//####[72]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[72]####
//####[72]####
        /*  ParaTask helper method to access private/protected slots *///####[72]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[72]####
            if (m.getParameterTypes().length == 0)//####[72]####
                m.invoke(instance);//####[72]####
            else if ((m.getParameterTypes().length == 1))//####[72]####
                m.invoke(instance, arg);//####[72]####
            else //####[72]####
                m.invoke(instance, arg, interResult);//####[72]####
        }//####[72]####
//####[73]####
        private BnBSchedule _initialPartialSchedule;//####[73]####
//####[74]####
        private int _localOptimalLength;//####[74]####
//####[76]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[76]####
            super(processingCores);//####[77]####
            _initialPartialSchedule = currentPartialSchedule;//####[78]####
            upperBound = m.upperBound;//####[79]####
            sources = new ArrayList<Integer>(m.sources);//####[80]####
            bottomUpSinks = new ArrayList<Integer>(m.bottomUpSinks);//####[81]####
            bottomLevels = m.bottomLevels.clone();//####[82]####
            numNodes = m.numNodes;//####[83]####
            totalNodeWeights = m.totalNodeWeights;//####[84]####
            nodeStartTimes = new ArrayList<Integer>(m.nodeStartTimes);//####[85]####
            nodeProcessors = new ArrayList<Integer>(m.nodeProcessors);//####[86]####
            brokenTrees = m.brokenTrees;//####[87]####
            _nodeWeights = m._nodeWeights.clone();//####[89]####
            _arcs = m._arcs.clone();//####[90]####
            _arcWeights = m._arcWeights.clone();//####[91]####
        }//####[92]####
//####[95]####
        @Override//####[95]####
        public void execute() {//####[95]####
            bnb(_initialPartialSchedule);//####[96]####
        }//####[97]####
//####[100]####
        @Override//####[100]####
        protected void setOptimalSchedule(BnBSchedule schedule) {//####[100]####
            super.setOptimalSchedule(schedule);//####[101]####
            _localOptimalLength = schedule.getMaxFinishTime();//####[102]####
        }//####[103]####
//####[105]####
        protected int getLocalOptimalLength() {//####[105]####
            return _localOptimalLength;//####[106]####
        }//####[107]####
    }//####[107]####
}//####[107]####
