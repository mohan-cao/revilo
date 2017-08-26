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
    private int _currentThreadCount;//####[13]####
//####[19]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 * @author Aimee T
	 *///####[19]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores) {//####[19]####
        super(processingCores);//####[20]####
        _schedulesToComplete = new TaskIDGroup(4);//####[21]####
        _currentThreadCount = 0;//####[22]####
    }//####[23]####
//####[33]####
    /**
    * Implementation of hook method to start the branch and bound algorithm. In this
    * parallel algorithm, it effectively allocates all the tasks to be done, taking
    * on a master-like role.
    *
    * @author Aimee T
     *///####[33]####
    @Override//####[33]####
    protected void startBnb() {//####[33]####
        while (!rootSchedules.isEmpty()) //####[34]####
        {//####[34]####
            bnb(rootSchedules.remove(0));//####[35]####
        }//####[36]####
        try {//####[37]####
            _schedulesToComplete.waitTillFinished();//####[38]####
        } catch (InterruptedException e) {//####[39]####
            e.printStackTrace();//####[40]####
        } catch (ExecutionException e) {//####[41]####
            e.printStackTrace();//####[42]####
        } finally {//####[43]####
            System.out.println("Exectuion interrupted");//####[44]####
        }//####[45]####
    }//####[46]####
//####[51]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 *///####[51]####
    private void createTask(BnBSchedule schedule) {//####[51]####
        TaskID<Void> partial = startNewBnbThread(schedule);//####[52]####
        _schedulesToComplete.add(partial);//####[53]####
    }//####[54]####
//####[56]####
    private static volatile Method __pt__startNewBnbThread_BnBSchedule_method = null;//####[56]####
    private synchronized static void __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet() {//####[56]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[56]####
            try {//####[56]####
                __pt__startNewBnbThread_BnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startNewBnbThread", new Class[] {//####[56]####
                    BnBSchedule.class//####[56]####
                });//####[56]####
            } catch (Exception e) {//####[56]####
                e.printStackTrace();//####[56]####
            }//####[56]####
        }//####[56]####
    }//####[56]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return startNewBnbThread(schedule, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<Void> startNewBnbThread(BnBSchedule schedule, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[56]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setParameters(schedule);//####[56]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return startNewBnbThread(schedule, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<Void> startNewBnbThread(TaskID<BnBSchedule> schedule, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[56]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setTaskIdArgIndexes(0);//####[56]####
        taskinfo.addDependsOn(schedule);//####[56]####
        taskinfo.setParameters(schedule);//####[56]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return startNewBnbThread(schedule, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<Void> startNewBnbThread(BlockingQueue<BnBSchedule> schedule, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__startNewBnbThread_BnBSchedule_method == null) {//####[56]####
            __pt__startNewBnbThread_BnBSchedule_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setQueueArgIndexes(0);//####[56]####
        taskinfo.setIsPipeline(true);//####[56]####
        taskinfo.setParameters(schedule);//####[56]####
        taskinfo.setMethod(__pt__startNewBnbThread_BnBSchedule_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    public void __pt__startNewBnbThread(BnBSchedule schedule) {//####[56]####
        WorkerBnb worker = new WorkerBnb(this, schedule, _processingCores);//####[57]####
        _currentThreadCount++;//####[58]####
        worker.execute();//####[59]####
        if (upperBound > worker.upperBound) //####[60]####
        {//####[60]####
            upperBound = worker.upperBound;//####[61]####
            optimalSchedule = worker.optimalSchedule;//####[62]####
        }//####[63]####
        worker = null;//####[64]####
        _currentThreadCount--;//####[65]####
    }//####[66]####
//####[66]####
//####[74]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the depth check.
     * @author Aimee T
     *///####[74]####
    @Override//####[74]####
    protected boolean isAtExitDepth(BnBSchedule s) {//####[74]####
        return 3 * numNodes / 4 <= s.getClosedNodes().size();//####[75]####
    }//####[76]####
//####[84]####
    /**
     * Hook method to be implemented by subclasses which need specific behaviour when a particular 
     * recursion depth is reached. This method implements the behaviour required.
     * @author Aimee T
     *///####[84]####
    @Override//####[84]####
    protected void exitDepthAction(BnBSchedule s) {//####[84]####
        createTask(s);//####[85]####
    }//####[86]####
//####[95]####
    /**
	 * Algorithm manager used in multithreading, with each worker running on a different thread. It
	 * creates copies of all the fields in the original algorithm manager (which created the task
	 * that creates the required worker).
	 *
	 * @author Aimee T
	 *///####[95]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[95]####
//####[95]####
        /*  ParaTask helper method to access private/protected slots *///####[95]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[95]####
            if (m.getParameterTypes().length == 0)//####[95]####
                m.invoke(instance);//####[95]####
            else if ((m.getParameterTypes().length == 1))//####[95]####
                m.invoke(instance, arg);//####[95]####
            else //####[95]####
                m.invoke(instance, arg, interResult);//####[95]####
        }//####[95]####
//####[96]####
        private BnBSchedule _initialPartialSchedule;//####[96]####
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
            _nodeWeights = m._nodeWeights.clone();//####[110]####
            _arcs = m._arcs.clone();//####[111]####
            _arcWeights = m._arcWeights.clone();//####[112]####
        }//####[113]####
//####[116]####
        @Override//####[116]####
        public void execute() {//####[116]####
            bnb(_initialPartialSchedule);//####[117]####
        }//####[118]####
    }//####[118]####
}//####[118]####
