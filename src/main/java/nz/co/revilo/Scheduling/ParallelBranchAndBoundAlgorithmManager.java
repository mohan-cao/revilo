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
//####[18]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 * @author Aimee T
	 *///####[18]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores, int threads) {//####[18]####
        super(processingCores);//####[19]####
        _schedulesToComplete = new TaskIDGroup<BnBSchedule>(threads);//####[20]####
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
//####[40]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 *///####[40]####
    private void createTask(BnBSchedule schedule) {//####[40]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[41]####
        _schedulesToComplete.add(partial);//####[42]####
    }//####[43]####
//####[45]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[45]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[45]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[45]####
            try {//####[45]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[45]####
                    BnBSchedule.class//####[45]####
                });//####[45]####
            } catch (Exception e) {//####[45]####
                e.printStackTrace();//####[45]####
            }//####[45]####
        }//####[45]####
    }//####[45]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[45]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[45]####
        return startNewBnbThread(schedule, new TaskInfo());//####[45]####
    }//####[45]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[45]####
        // ensure Method variable is set//####[45]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[45]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[45]####
        }//####[45]####
        taskinfo.setParameters(schedule);//####[45]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[45]####
        taskinfo.setInstance(this);//####[45]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[45]####
    }//####[45]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[45]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[45]####
        return startNewBnbThread(schedule, new TaskInfo());//####[45]####
    }//####[45]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[45]####
        // ensure Method variable is set//####[45]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[45]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[45]####
        }//####[45]####
        taskinfo.setTaskIdArgIndexes(0);//####[45]####
        taskinfo.addDependsOn(schedule);//####[45]####
        taskinfo.setParameters(schedule);//####[45]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[45]####
        taskinfo.setInstance(this);//####[45]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[45]####
    }//####[45]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[45]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[45]####
        return startNewBnbThread(schedule, new TaskInfo());//####[45]####
    }//####[45]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[45]####
        // ensure Method variable is set//####[45]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[45]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[45]####
        }//####[45]####
        taskinfo.setQueueArgIndexes(0);//####[45]####
        taskinfo.setIsPipeline(true);//####[45]####
        taskinfo.setParameters(schedule);//####[45]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[45]####
        taskinfo.setInstance(this);//####[45]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[45]####
    }//####[45]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[45]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[46]####
        worker.execute();//####[47]####
        if (worker.upperBound < upperBound) //####[48]####
        {//####[48]####
            optimalSchedule = worker.optimalSchedule;//####[49]####
        }//####[50]####
        worker = null;//####[51]####
    }//####[52]####
//####[52]####
//####[60]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * @author Aimee T
     *///####[60]####
    @Override//####[60]####
    protected boolean isParallel() {//####[60]####
        return true;//####[61]####
    }//####[62]####
//####[70]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the behaviour required.
     * @author Aimee T
     *///####[70]####
    @Override//####[70]####
    protected void doParallel(BnBSchedule s) {//####[70]####
        createTask(s);//####[71]####
    }//####[72]####
//####[74]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[74]####
//####[74]####
        /*  ParaTask helper method to access private/protected slots *///####[74]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[74]####
            if (m.getParameterTypes().length == 0)//####[74]####
                m.invoke(instance);//####[74]####
            else if ((m.getParameterTypes().length == 1))//####[74]####
                m.invoke(instance, arg);//####[74]####
            else //####[74]####
                m.invoke(instance, arg, interResult);//####[74]####
        }//####[74]####
//####[75]####
        private BnBSchedule _initialPartialSchedule;//####[75]####
//####[77]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[77]####
            super(processingCores);//####[78]####
            _initialPartialSchedule = currentPartialSchedule;//####[79]####
            upperBound = m.upperBound;//####[80]####
            sources = new ArrayList<Integer>(m.sources);//####[81]####
            bottomUpSinks = new ArrayList<Integer>(m.bottomUpSinks);//####[82]####
            bottomLevels = m.bottomLevels.clone();//####[83]####
            numNodes = m.numNodes;//####[84]####
            totalNodeWeights = m.totalNodeWeights;//####[85]####
            nodeStartTimes = new ArrayList<Integer>(m.nodeStartTimes);//####[86]####
            nodeProcessors = new ArrayList<Integer>(m.nodeProcessors);//####[87]####
            brokenTrees = m.brokenTrees;//####[88]####
            atomicBound = m.atomicBound;//####[89]####
            atomicListener = m.atomicListener;//####[90]####
            _nodeWeights = m._nodeWeights.clone();//####[92]####
            _arcs = m._arcs.clone();//####[93]####
            _arcWeights = m._arcWeights.clone();//####[94]####
        }//####[95]####
//####[98]####
        @Override//####[98]####
        public void execute() {//####[98]####
            bnb(_initialPartialSchedule);//####[99]####
        }//####[100]####
    }//####[100]####
}//####[100]####
