package nz.co.revilo.Scheduling;//####[1]####
//####[1]####
import java.util.ArrayList;//####[3]####
import java.util.List;//####[4]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[5]####
//####[5]####
//-- ParaTask related imports//####[5]####
import pt.runtime.*;//####[5]####
import java.util.concurrent.ExecutionException;//####[5]####
import java.util.concurrent.locks.*;//####[5]####
import java.lang.reflect.*;//####[5]####
import pt.runtime.GuiThread;//####[5]####
import java.util.concurrent.BlockingQueue;//####[5]####
import java.util.ArrayList;//####[5]####
import java.util.List;//####[5]####
//####[5]####
/**
 * Algorithm manager for the parallelised implementation of branch and bound, which makes use
 * pf ParallelTask
 * 
 * @author Aimee T
 *///####[12]####
public class ParallelBranchAndBoundAlgorithmManager extends BranchAndBoundAlgorithmManager {//####[13]####
    static{ParaTask.init();}//####[13]####
    /*  ParaTask helper method to access private/protected slots *///####[13]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[13]####
        if (m.getParameterTypes().length == 0)//####[13]####
            m.invoke(instance);//####[13]####
        else if ((m.getParameterTypes().length == 1))//####[13]####
            m.invoke(instance, arg);//####[13]####
        else //####[13]####
            m.invoke(instance, arg, interResult);//####[13]####
    }//####[13]####
//####[14]####
    private TaskIDGroup<BnBSchedule> _schedulesToComplete;//####[14]####
//####[15]####
    private int _threads;//####[15]####
//####[21]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 * @author Aimee T
	 *///####[21]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores, int threads) {//####[21]####
        super(processingCores);//####[22]####
        _schedulesToComplete = new TaskIDGroup<BnBSchedule>(threads);//####[23]####
        _threads = threads;//####[24]####
    }//####[25]####
//####[34]####
    /**
	 * Starts the branch and bound algorithm on a thread, and then waits for the 
	 * tasks occurring in parallel to finish
	 * 
	 * @author Aimee T.
	 *///####[34]####
    @Override//####[34]####
    protected void startBnb() {//####[34]####
        TaskIDGroup tasks = this.removeAndExecute(new ConcurrentLinkedQueue<BnBSchedule>(rootSchedules));//####[35]####
        try {//####[36]####
            tasks.waitTillFinished();//####[37]####
        } catch (InterruptedException e) {//####[38]####
            e.printStackTrace();//####[39]####
        } catch (ExecutionException e) {//####[40]####
            e.printStackTrace();//####[41]####
        }//####[42]####
    }//####[43]####
//####[45]####
    private static volatile Method __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method = null;//####[45]####
    private synchronized static void __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet() {//####[45]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[45]####
            try {//####[45]####
                __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__removeAndExecute", new Class[] {//####[45]####
                    ConcurrentLinkedQueue.class//####[45]####
                });//####[45]####
            } catch (Exception e) {//####[45]####
                e.printStackTrace();//####[45]####
            }//####[45]####
        }//####[45]####
    }//####[45]####
    public TaskIDGroup<Void> removeAndExecute(ConcurrentLinkedQueue<BnBSchedule> rootSchedules) {//####[45]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[45]####
        return removeAndExecute(rootSchedules, new TaskInfo());//####[45]####
    }//####[45]####
    public TaskIDGroup<Void> removeAndExecute(ConcurrentLinkedQueue<BnBSchedule> rootSchedules, TaskInfo taskinfo) {//####[45]####
        // ensure Method variable is set//####[45]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[45]####
            __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet();//####[45]####
        }//####[45]####
        taskinfo.setParameters(rootSchedules);//####[45]####
        taskinfo.setMethod(__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method);//####[45]####
        taskinfo.setInstance(this);//####[45]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[45]####
    }//####[45]####
    public TaskIDGroup<Void> removeAndExecute(TaskID<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules) {//####[45]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[45]####
        return removeAndExecute(rootSchedules, new TaskInfo());//####[45]####
    }//####[45]####
    public TaskIDGroup<Void> removeAndExecute(TaskID<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules, TaskInfo taskinfo) {//####[45]####
        // ensure Method variable is set//####[45]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[45]####
            __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet();//####[45]####
        }//####[45]####
        taskinfo.setTaskIdArgIndexes(0);//####[45]####
        taskinfo.addDependsOn(rootSchedules);//####[45]####
        taskinfo.setParameters(rootSchedules);//####[45]####
        taskinfo.setMethod(__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method);//####[45]####
        taskinfo.setInstance(this);//####[45]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[45]####
    }//####[45]####
    public TaskIDGroup<Void> removeAndExecute(BlockingQueue<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules) {//####[45]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[45]####
        return removeAndExecute(rootSchedules, new TaskInfo());//####[45]####
    }//####[45]####
    public TaskIDGroup<Void> removeAndExecute(BlockingQueue<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules, TaskInfo taskinfo) {//####[45]####
        // ensure Method variable is set//####[45]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[45]####
            __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet();//####[45]####
        }//####[45]####
        taskinfo.setQueueArgIndexes(0);//####[45]####
        taskinfo.setIsPipeline(true);//####[45]####
        taskinfo.setParameters(rootSchedules);//####[45]####
        taskinfo.setMethod(__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method);//####[45]####
        taskinfo.setInstance(this);//####[45]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[45]####
    }//####[45]####
    public void __pt__removeAndExecute(ConcurrentLinkedQueue<BnBSchedule> rootSchedules) {//####[45]####
        BnBSchedule b = null;//####[46]####
        while ((b = rootSchedules.poll()) != null) //####[47]####
        {//####[47]####
            bnb(b);//####[48]####
        }//####[49]####
    }//####[50]####
//####[50]####
//####[53]####
    @Override//####[53]####
    protected void bnb(BnBSchedule schedule) {//####[53]####
        if (schedule.lowerBound >= upperBound.get()) //####[54]####
        {//####[54]####
            schedule = null;//####[55]####
            brokenTrees.incrementAndGet();//####[56]####
            return;//####[58]####
        }//####[59]####
        if (existingScheduleStructures.containsKey(schedule._scheduleStructureId)) //####[62]####
        {//####[62]####
            schedule = null;//####[63]####
            brokenTrees.incrementAndGet();//####[64]####
            return;//####[66]####
        } else {//####[67]####
            existingScheduleStructures.put(schedule._scheduleStructureId, null);//####[68]####
        }//####[69]####
        if (isParallel(schedule.getClosedNodes().size())) //####[71]####
        {//####[71]####
            doParallel(schedule);//####[72]####
            return;//####[73]####
        }//####[74]####
        if (schedule.openNodes.isEmpty()) //####[78]####
        {//####[78]####
            if (schedule.getMaxFinishTime() < upperBound.get()) //####[80]####
            {//####[80]####
                setOptimalSchedule(schedule);//####[81]####
                return;//####[82]####
            }//####[83]####
        }//####[84]####
        List<BnBSchedule> nextSchedules = new ArrayList<BnBSchedule>();//####[87]####
        for (int node : schedule.independentNodes) //####[88]####
        {//####[88]####
            for (int processor = 0; processor < _processingCores; processor++) //####[89]####
            {//####[89]####
                nextSchedules.add(new BnBSchedule(this, schedule, node, processor));//####[90]####
            }//####[91]####
        }//####[92]####
        for (BnBSchedule nextSchedule : nextSchedules) //####[93]####
        {//####[93]####
            bnb(nextSchedule);//####[94]####
        }//####[95]####
    }//####[96]####
//####[149]####
    /**
	 * Creates the ParallelTask task which needs to be scheduled for execution
	 * 
	 * @author Aimee T
	 *///####[149]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[149]####
//####[149]####
        /*  ParaTask helper method to access private/protected slots *///####[149]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[149]####
            if (m.getParameterTypes().length == 0)//####[149]####
                m.invoke(instance);//####[149]####
            else if ((m.getParameterTypes().length == 1))//####[149]####
                m.invoke(instance, arg);//####[149]####
            else //####[149]####
                m.invoke(instance, arg, interResult);//####[149]####
        }//####[149]####
//####[150]####
        private BnBSchedule _initialPartialSchedule;//####[150]####
//####[151]####
        private int _localOptimalLength = 0;//####[151]####
//####[153]####
        public WorkerBnb(ParallelBranchAndBoundAlgorithmManager m, BnBSchedule currentPartialSchedule, int processingCores) {//####[153]####
            super(processingCores);//####[154]####
            _initialPartialSchedule = currentPartialSchedule;//####[155]####
            upperBound = m.upperBound;//####[156]####
            sources = new ArrayList<Integer>(m.sources);//####[157]####
            bottomUpSinks = new ArrayList<Integer>(m.bottomUpSinks);//####[158]####
            bottomLevels = m.bottomLevels.clone();//####[159]####
            numNodes = m.numNodes;//####[160]####
            totalNodeWeights = m.totalNodeWeights;//####[161]####
            nodeStartTimes = new ArrayList<Integer>(m.nodeStartTimes);//####[162]####
            nodeProcessors = new ArrayList<Integer>(m.nodeProcessors);//####[163]####
            brokenTrees = m.brokenTrees;//####[164]####
            atomicBound = m.atomicBound;//####[165]####
            atomicListener = m.atomicListener;//####[166]####
            _nodeWeights = m._nodeWeights.clone();//####[168]####
            _arcs = m._arcs.clone();//####[169]####
            _arcWeights = m._arcWeights.clone();//####[170]####
        }//####[171]####
//####[180]####
        /**
		 * Starts running the branch and bound algorithm (without taking input or generating
		 * and output file)
		 * 
		 * @author Aimee T
		 *///####[180]####
        @Override//####[180]####
        public void execute() {//####[180]####
            bnb(_initialPartialSchedule);//####[181]####
        }//####[182]####
//####[190]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[190]####
        @Override//####[190]####
        protected void setOptimalSchedule(BnBSchedule schedule) {//####[190]####
            super.setOptimalSchedule(schedule);//####[191]####
            _localOptimalLength = schedule.getMaxFinishTime();//####[192]####
        }//####[193]####
//####[200]####
        /**
		 * Sets the value of the length of this worker's optimal schedule
		 * 
		 * @author Aimee T
		 *///####[200]####
        protected int getLocalOptimalLength() {//####[200]####
            return _localOptimalLength;//####[201]####
        }//####[202]####
    }//####[202]####
}//####[202]####
