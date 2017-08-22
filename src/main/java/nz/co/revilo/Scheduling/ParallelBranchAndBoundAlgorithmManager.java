package nz.co.revilo.Scheduling;//####[1]####
//####[1]####
import java.util.ArrayList;//####[3]####
import java.util.List;//####[4]####
import java.util.concurrent.CopyOnWriteArrayList;//####[6]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[7]####
//####[7]####
//-- ParaTask related imports//####[7]####
import pt.runtime.*;//####[7]####
import java.util.concurrent.ExecutionException;//####[7]####
import java.util.concurrent.locks.*;//####[7]####
import java.lang.reflect.*;//####[7]####
import pt.runtime.GuiThread;//####[7]####
import java.util.concurrent.BlockingQueue;//####[7]####
import java.util.ArrayList;//####[7]####
import java.util.List;//####[7]####
//####[7]####
/**
 * Finds optimal schedule using a parallel DFS Branch and Bound
 * 
 * @author Aimee T
 *
 *///####[14]####
public class ParallelBranchAndBoundAlgorithmManager extends BranchAndBoundAlgorithmManager {//####[15]####
    static{ParaTask.init();}//####[15]####
    /*  ParaTask helper method to access private/protected slots *///####[15]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[15]####
        if (m.getParameterTypes().length == 0)//####[15]####
            m.invoke(instance);//####[15]####
        else if ((m.getParameterTypes().length == 1))//####[15]####
            m.invoke(instance, arg);//####[15]####
        else //####[15]####
            m.invoke(instance, arg, interResult);//####[15]####
    }//####[15]####
//####[17]####
    private TaskIDGroup _schedulesToComplete;//####[17]####
//####[18]####
    private int _initialRecursionDepth;//####[18]####
//####[20]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores) {//####[20]####
        super(processingCores);//####[21]####
        rootSchedules = new CopyOnWriteArrayList<Schedule>();//####[22]####
    }//####[23]####
//####[26]####
    @Override//####[26]####
    protected final void setUpExecute() {//####[26]####
        _initialRecursionDepth = numNodes / 2;//####[27]####
    }//####[28]####
//####[38]####
    /**
	 * Method to call each of the initial and worker bnb methods
	 * 
	 * @param s
	 * 
	 * @author Aimee T
	 *///####[38]####
    @Override//####[38]####
    protected void bnb(Schedule s) {//####[38]####
        initialBnb(s, 0);//####[39]####
    }//####[40]####
//####[42]####
    private void initialBnb(Schedule s, int depth) {//####[42]####
        if (isBounded(s)) //####[43]####
        {//####[43]####
            return;//####[44]####
        }//####[45]####
        if (depth == _initialRecursionDepth) //####[47]####
        {//####[47]####
            completeSchedule(s);//####[48]####
            return;//####[49]####
        }//####[50]####
        List<Schedule> nextSchedules = new ArrayList<Schedule>();//####[53]####
        for (int n : s.independentSet) //####[54]####
        {//####[54]####
            for (int p = 0; p < _processingCores; p++) //####[55]####
            {//####[55]####
                nextSchedules.add(new Schedule(this, s, n, p));//####[56]####
            }//####[57]####
        }//####[58]####
        depth++;//####[59]####
        for (Schedule nextSchedule : nextSchedules) //####[60]####
        {//####[60]####
            initialBnb(nextSchedule, depth);//####[61]####
        }//####[62]####
    }//####[63]####
//####[69]####
    /**
	 * Creates the ParaTask tasks which complete partial schedules generated by initialBnb
	 * 
	 *///####[69]####
    private void completeSchedule(Schedule s) {//####[69]####
        TaskID<Void> partial = completeScheduleTask(s);//####[70]####
    }//####[71]####
//####[77]####
    private static volatile Method __pt__completeScheduleTask_Schedule_method = null;//####[77]####
    private synchronized static void __pt__completeScheduleTask_Schedule_ensureMethodVarSet() {//####[77]####
        if (__pt__completeScheduleTask_Schedule_method == null) {//####[77]####
            try {//####[77]####
                __pt__completeScheduleTask_Schedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__completeScheduleTask", new Class[] {//####[77]####
                    Schedule.class//####[77]####
                });//####[77]####
            } catch (Exception e) {//####[77]####
                e.printStackTrace();//####[77]####
            }//####[77]####
        }//####[77]####
    }//####[77]####
    /**
	 * ParaTask task which completes the partial schedules generated by initialBnb
	 * 
	 *///####[77]####
    private TaskID<Void> completeScheduleTask(Schedule s) {//####[77]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[77]####
        return completeScheduleTask(s, new TaskInfo());//####[77]####
    }//####[77]####
    /**
	 * ParaTask task which completes the partial schedules generated by initialBnb
	 * 
	 *///####[77]####
    private TaskID<Void> completeScheduleTask(Schedule s, TaskInfo taskinfo) {//####[77]####
        // ensure Method variable is set//####[77]####
        if (__pt__completeScheduleTask_Schedule_method == null) {//####[77]####
            __pt__completeScheduleTask_Schedule_ensureMethodVarSet();//####[77]####
        }//####[77]####
        taskinfo.setParameters(s);//####[77]####
        taskinfo.setMethod(__pt__completeScheduleTask_Schedule_method);//####[77]####
        taskinfo.setInstance(this);//####[77]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[77]####
    }//####[77]####
    /**
	 * ParaTask task which completes the partial schedules generated by initialBnb
	 * 
	 *///####[77]####
    private TaskID<Void> completeScheduleTask(TaskID<Schedule> s) {//####[77]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[77]####
        return completeScheduleTask(s, new TaskInfo());//####[77]####
    }//####[77]####
    /**
	 * ParaTask task which completes the partial schedules generated by initialBnb
	 * 
	 *///####[77]####
    private TaskID<Void> completeScheduleTask(TaskID<Schedule> s, TaskInfo taskinfo) {//####[77]####
        // ensure Method variable is set//####[77]####
        if (__pt__completeScheduleTask_Schedule_method == null) {//####[77]####
            __pt__completeScheduleTask_Schedule_ensureMethodVarSet();//####[77]####
        }//####[77]####
        taskinfo.setTaskIdArgIndexes(0);//####[77]####
        taskinfo.addDependsOn(s);//####[77]####
        taskinfo.setParameters(s);//####[77]####
        taskinfo.setMethod(__pt__completeScheduleTask_Schedule_method);//####[77]####
        taskinfo.setInstance(this);//####[77]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[77]####
    }//####[77]####
    /**
	 * ParaTask task which completes the partial schedules generated by initialBnb
	 * 
	 *///####[77]####
    private TaskID<Void> completeScheduleTask(BlockingQueue<Schedule> s) {//####[77]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[77]####
        return completeScheduleTask(s, new TaskInfo());//####[77]####
    }//####[77]####
    /**
	 * ParaTask task which completes the partial schedules generated by initialBnb
	 * 
	 *///####[77]####
    private TaskID<Void> completeScheduleTask(BlockingQueue<Schedule> s, TaskInfo taskinfo) {//####[77]####
        // ensure Method variable is set//####[77]####
        if (__pt__completeScheduleTask_Schedule_method == null) {//####[77]####
            __pt__completeScheduleTask_Schedule_ensureMethodVarSet();//####[77]####
        }//####[77]####
        taskinfo.setQueueArgIndexes(0);//####[77]####
        taskinfo.setIsPipeline(true);//####[77]####
        taskinfo.setParameters(s);//####[77]####
        taskinfo.setMethod(__pt__completeScheduleTask_Schedule_method);//####[77]####
        taskinfo.setInstance(this);//####[77]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[77]####
    }//####[77]####
    /**
	 * ParaTask task which completes the partial schedules generated by initialBnb
	 * 
	 *///####[77]####
    public void __pt__completeScheduleTask(Schedule s) {//####[77]####
        WorkerBnb worker = new WorkerBnb(s, upperBound, _processingCores, sources, bottomUpSinks, rootSchedules, bottomLevels, numNodes, totalNodeWeights, nodeStartTimes, nodeProcessors, _nodeWeights, _arcs, _arcWeights);//####[78]####
        worker.execute();//####[81]####
        if (upperBound > worker.getOptimalLength()) //####[82]####
        {//####[82]####
            upperBound = worker.getOptimalLength();//####[83]####
            optimalSchedule = worker.getOptimal();//####[84]####
        }//####[85]####
        worker = null;//####[86]####
    }//####[87]####
//####[87]####
//####[89]####
    private class WorkerBnb extends BranchAndBoundAlgorithmManager {//####[89]####
//####[89]####
        /*  ParaTask helper method to access private/protected slots *///####[89]####
        public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[89]####
            if (m.getParameterTypes().length == 0)//####[89]####
                m.invoke(instance);//####[89]####
            else if ((m.getParameterTypes().length == 1))//####[89]####
                m.invoke(instance, arg);//####[89]####
            else //####[89]####
                m.invoke(instance, arg, interResult);//####[89]####
        }//####[89]####
//####[90]####
        private Schedule _schedule;//####[90]####
//####[91]####
        private Schedule _optimalSchedule;//####[91]####
//####[93]####
        public WorkerBnb(Schedule schedule, int upperBound, int processingCores, List<Integer> sources, List<Integer> bottomUpSinks, List<Schedule> rootSchedules, int[] bottomLevels, int numNodes, int totalNodeWeights, List<Integer> nodeStartTimes, List<Integer> nodeProcessors, int[] nodeWeights, boolean[][] arcs, int[][] arcWeights) {//####[97]####
            super(processingCores);//####[98]####
            _schedule = schedule;//####[99]####
            this.upperBound = upperBound;//####[100]####
            this.sources = sources;//####[101]####
            this.bottomUpSinks = bottomUpSinks;//####[102]####
            this.rootSchedules = rootSchedules;//####[103]####
            this.bottomLevels = bottomLevels;//####[104]####
            this.numNodes = numNodes;//####[105]####
            this.totalNodeWeights = totalNodeWeights;//####[106]####
            this.nodeStartTimes = nodeStartTimes;//####[107]####
            this.nodeProcessors = nodeProcessors;//####[108]####
            _nodeWeights = nodeWeights;//####[110]####
            _arcs = arcs;//####[111]####
            _arcWeights = arcWeights;//####[112]####
        }//####[113]####
//####[115]####
        public void execute() {//####[115]####
            workerBnb(_schedule);//####[116]####
        }//####[117]####
//####[119]####
        private void workerBnb(Schedule s) {//####[119]####
            if (s.lowerBound >= upperBound) //####[120]####
            {//####[120]####
                return;//####[121]####
            }//####[122]####
            if (s.openSet.isEmpty()) //####[126]####
            {//####[126]####
                if (s.getMaxFinishTime() <= upperBound) //####[128]####
                {//####[128]####
                    _optimalSchedule = s;//####[129]####
                    upperBound = s.getMaxFinishTime();//####[130]####
                    upperBound = s.getMaxFinishTime();//####[131]####
                    return;//####[132]####
                }//####[133]####
            }//####[134]####
            List<Schedule> nextSchedules = new ArrayList<Schedule>();//####[137]####
            for (int n : s.independentSet) //####[138]####
            {//####[138]####
                for (int p = 0; p < _processingCores; p++) //####[139]####
                {//####[139]####
                    nextSchedules.add(new Schedule(this, s, n, p));//####[140]####
                }//####[141]####
            }//####[142]####
            for (Schedule nextSchedule : nextSchedules) //####[144]####
            {//####[144]####
                workerBnb(nextSchedule);//####[145]####
            }//####[146]####
        }//####[147]####
//####[152]####
        /**
		 * Returns the optimal schedule found by this worker.
		 *///####[152]####
        public Schedule getOptimal() {//####[152]####
            return _optimalSchedule;//####[153]####
        }//####[154]####
//####[159]####
        /**
		 * Returns the optimal schedule's length
		 *///####[159]####
        public int getOptimalLength() {//####[159]####
            return upperBound;//####[160]####
        }//####[161]####
    }//####[161]####
}//####[161]####