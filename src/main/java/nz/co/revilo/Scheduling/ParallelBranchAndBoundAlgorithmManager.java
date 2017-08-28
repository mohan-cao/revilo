package nz.co.revilo.Scheduling;//####[1]####
//####[1]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[3]####
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
//####[17]####
    /**
	 * Constructor which sets the number of processing cores we are scheduling for
	 *
	 * @author Aimee T
	 *///####[17]####
    public ParallelBranchAndBoundAlgorithmManager(int processingCores, int threads) {//####[17]####
        super(processingCores);//####[18]####
    }//####[19]####
//####[28]####
    /**
	 * Starts the branch and bound algorithm on a thread, and then waits for the
	 * tasks occurring in parallel to finish
	 *
	 * @author Aimee T.
	 *///####[28]####
    @Override//####[28]####
    protected void startBnb() {//####[28]####
        TaskIDGroup tasks = this.removeAndExecute(new ConcurrentLinkedQueue<BnBSchedule>(rootSchedules));//####[29]####
        try {//####[30]####
            tasks.waitTillFinished();//####[31]####
        } catch (InterruptedException e) {//####[32]####
            e.printStackTrace();//####[33]####
        } catch (ExecutionException e) {//####[34]####
            e.printStackTrace();//####[35]####
        }//####[36]####
    }//####[37]####
//####[43]####
    private static volatile Method __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method = null;//####[43]####
    private synchronized static void __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet() {//####[43]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[43]####
            try {//####[43]####
                __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__removeAndExecute", new Class[] {//####[43]####
                    ConcurrentLinkedQueue.class//####[43]####
                });//####[43]####
            } catch (Exception e) {//####[43]####
                e.printStackTrace();//####[43]####
            }//####[43]####
        }//####[43]####
    }//####[43]####
    /**
	 * Multi task dequeueing of the root schedules
	 * Makes a new threads
	 *///####[43]####
    public TaskIDGroup<Void> removeAndExecute(ConcurrentLinkedQueue<BnBSchedule> rootSchedules) {//####[43]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[43]####
        return removeAndExecute(rootSchedules, new TaskInfo());//####[43]####
    }//####[43]####
    /**
	 * Multi task dequeueing of the root schedules
	 * Makes a new threads
	 *///####[43]####
    public TaskIDGroup<Void> removeAndExecute(ConcurrentLinkedQueue<BnBSchedule> rootSchedules, TaskInfo taskinfo) {//####[43]####
        // ensure Method variable is set//####[43]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[43]####
            __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet();//####[43]####
        }//####[43]####
        taskinfo.setParameters(rootSchedules);//####[43]####
        taskinfo.setMethod(__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method);//####[43]####
        taskinfo.setInstance(this);//####[43]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[43]####
    }//####[43]####
    /**
	 * Multi task dequeueing of the root schedules
	 * Makes a new threads
	 *///####[43]####
    public TaskIDGroup<Void> removeAndExecute(TaskID<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules) {//####[43]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[43]####
        return removeAndExecute(rootSchedules, new TaskInfo());//####[43]####
    }//####[43]####
    /**
	 * Multi task dequeueing of the root schedules
	 * Makes a new threads
	 *///####[43]####
    public TaskIDGroup<Void> removeAndExecute(TaskID<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules, TaskInfo taskinfo) {//####[43]####
        // ensure Method variable is set//####[43]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[43]####
            __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet();//####[43]####
        }//####[43]####
        taskinfo.setTaskIdArgIndexes(0);//####[43]####
        taskinfo.addDependsOn(rootSchedules);//####[43]####
        taskinfo.setParameters(rootSchedules);//####[43]####
        taskinfo.setMethod(__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method);//####[43]####
        taskinfo.setInstance(this);//####[43]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[43]####
    }//####[43]####
    /**
	 * Multi task dequeueing of the root schedules
	 * Makes a new threads
	 *///####[43]####
    public TaskIDGroup<Void> removeAndExecute(BlockingQueue<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules) {//####[43]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[43]####
        return removeAndExecute(rootSchedules, new TaskInfo());//####[43]####
    }//####[43]####
    /**
	 * Multi task dequeueing of the root schedules
	 * Makes a new threads
	 *///####[43]####
    public TaskIDGroup<Void> removeAndExecute(BlockingQueue<ConcurrentLinkedQueue<BnBSchedule>> rootSchedules, TaskInfo taskinfo) {//####[43]####
        // ensure Method variable is set//####[43]####
        if (__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method == null) {//####[43]####
            __pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_ensureMethodVarSet();//####[43]####
        }//####[43]####
        taskinfo.setQueueArgIndexes(0);//####[43]####
        taskinfo.setIsPipeline(true);//####[43]####
        taskinfo.setParameters(rootSchedules);//####[43]####
        taskinfo.setMethod(__pt__removeAndExecute_ConcurrentLinkedQueueBnBSchedule_method);//####[43]####
        taskinfo.setInstance(this);//####[43]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[43]####
    }//####[43]####
    /**
	 * Multi task dequeueing of the root schedules
	 * Makes a new threads
	 *///####[43]####
    public void __pt__removeAndExecute(ConcurrentLinkedQueue<BnBSchedule> rootSchedules) {//####[43]####
        BnBSchedule b = null;//####[44]####
        while ((b = rootSchedules.poll()) != null) //####[45]####
        {//####[45]####
            bnb(b);//####[46]####
        }//####[47]####
    }//####[48]####
//####[48]####
}//####[48]####
