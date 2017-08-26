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
    private int _initialRecursionDepth;//####[13]####
//####[14]####
    private int _currentThreadCount;//####[14]####
//####[20]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 * @author Aimee T
	 *///####[20]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores) {//####[20]####
        super(processingCores);//####[21]####
        _schedulesToComplete = new TaskIDGroup(4);//####[22]####
        _initialRecursionDepth = numNodes / 2;//####[23]####
        _currentThreadCount = 0;//####[24]####
    }//####[25]####
//####[28]####
    @Override//####[28]####
    protected void startBnb() {//####[28]####
        while (!rootSchedules.isEmpty()) //####[29]####
        {//####[29]####
            bnb(rootSchedules.remove(0));//####[30]####
        }//####[31]####
        try {//####[32]####
            _schedulesToComplete.waitTillFinished();//####[33]####
        } catch (InterruptedException e) {//####[34]####
            e.printStackTrace();//####[35]####
        } catch (ExecutionException e) {//####[36]####
            e.printStackTrace();//####[37]####
        } finally {//####[38]####
            System.out.println("Exectuion interrupted");//####[39]####
        }//####[40]####
    }//####[41]####
//####[46]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 *///####[46]####
    private void createTask(BnBSchedule schedule) {//####[46]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[47]####
        _schedulesToComplete.add(partial);//####[48]####
    }//####[49]####
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
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return startNewBnbThread(schedule, new TaskInfo());//####[51]####
    }//####[51]####
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
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return startNewBnbThread(schedule, new TaskInfo());//####[51]####
    }//####[51]####
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
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return startNewBnbThread(schedule, new TaskInfo());//####[51]####
    }//####[51]####
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
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[51]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[52]####
        _currentThreadCount++;//####[53]####
        worker.execute();//####[54]####
        if (upperBound > worker.upperBound) //####[55]####
        {//####[55]####
            upperBound = worker.upperBound;//####[56]####
            optimalSchedule = worker.optimalSchedule;//####[57]####
        }//####[58]####
        worker = null;//####[59]####
        _currentThreadCount--;//####[60]####
    }//####[61]####
//####[61]####
//####[69]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * @author Aimee T
     *///####[69]####
    @Override//####[69]####
    protected boolean isAtExitDepth(BnBSchedule s) {//####[69]####
        return 3 * numNodes / 4 <= s.getClosedNodes().size();//####[70]####
    }//####[71]####
//####[79]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the behaviour required.
     * @author Aimee T
     *///####[79]####
    @Override//####[79]####
    protected void exitDepthAction(BnBSchedule s) {//####[79]####
        createTask(s);//####[80]####
    }//####[81]####
//####[83]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[83]####
//####[83]####
        /*  ParaTask helper method to access private/protected slots *///####[83]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[83]####
            if (m.getParameterTypes().length == 0)//####[83]####
                m.invoke(instance);//####[83]####
            else if ((m.getParameterTypes().length == 1))//####[83]####
                m.invoke(instance, arg);//####[83]####
            else //####[83]####
                m.invoke(instance, arg, interResult);//####[83]####
        }//####[83]####
//####[84]####
        private BnBSchedule _initialPartialSchedule;//####[84]####
//####[86]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[86]####
            super(processingCores);//####[87]####
            _initialPartialSchedule = currentPartialSchedule;//####[88]####
            upperBound = m.upperBound;//####[89]####
            sources = new ArrayList<Integer>(m.sources);//####[90]####
            bottomUpSinks = new ArrayList<Integer>(m.bottomUpSinks);//####[91]####
            bottomLevels = m.bottomLevels.clone();//####[92]####
            numNodes = m.numNodes;//####[93]####
            totalNodeWeights = m.totalNodeWeights;//####[94]####
            nodeStartTimes = new ArrayList<Integer>(m.nodeStartTimes);//####[95]####
            nodeProcessors = new ArrayList<Integer>(m.nodeProcessors);//####[96]####
            _nodeWeights = m._nodeWeights.clone();//####[98]####
            _arcs = m._arcs.clone();//####[99]####
            _arcWeights = m._arcWeights.clone();//####[100]####
        }//####[101]####
//####[104]####
        @Override//####[104]####
        public void execute() {//####[104]####
            bnb(_initialPartialSchedule);//####[105]####
        }//####[106]####
    }//####[106]####
}//####[106]####
