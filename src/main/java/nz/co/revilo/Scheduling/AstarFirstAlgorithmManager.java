package nz.co.revilo.Scheduling;

import nz.co.revilo.Output.ScheduleResultListener;
import nz.co.revilo.Scheduling.Astar.AstarSchedule;
import nz.co.revilo.Scheduling.Astar.AstarTask;

import java.util.*;

import static nz.co.revilo.Scheduling.PrimitiveInterfaceHelper.*;

public class AstarFirstAlgorithmManager extends AlgorithmManager {

    int numTasks;
    private List<Set<Integer>> completeDependencies;

    public AstarFirstAlgorithmManager(int processingCores) {
        super(processingCores);
    }

    @Override
    protected void execute() {
        //Number of Tasks
        numTasks = _nodeWeights.length;

        //Sort Tasks into start, middle and end sets
        Set<Integer> startTasks = new HashSet<>();
        Set<Integer> endTasks = new HashSet<>();
        Set<Integer> middleTasks = new HashSet<>();
        for (int task = 0; task < numTasks; task++) {
            startTasks.add(task);
            endTasks.add(task);
            middleTasks.add(task);
        }
        for (int fromTask = 0; fromTask < numTasks; fromTask++) {
            for (int toTask = 0; toTask < numTasks; toTask++) {
                if (_arcs[fromTask][toTask]) {
                    startTasks.remove(toTask);
                    endTasks.remove(fromTask);
                }
            }
        }
        middleTasks.removeAll(endTasks);
        middleTasks.removeAll(startTasks);

        //Determine total completeDependencies for every task
        completeDependencies = new ArrayList<>(numTasks);
        for (int task = 0; task < numTasks; task++) {
            completeDependencies.add(new HashSet<>());
        }
        for (Integer startTask : startTasks) {
            updateDependencies(new ArrayList<>(), startTask);
        }

        //Determine partial dependencies for every task
        List<List<Integer>> partialDependencies = new ArrayList<>();
        for (int toTask = 0; toTask < numTasks; toTask++) {
            List<Integer> subList = new ArrayList<>();
            for (int fromTask = 0; fromTask < numTasks; fromTask++) {
                if (_arcs[fromTask][toTask]) {
                    subList.add(fromTask);
                }
            }
            partialDependencies.add(subList);
        }

        //Initialises root schedule
        //Adds tasks
        AstarSchedule root = new AstarSchedule();
        for (int unschedulableTask : middleTasks) {
            root._unschedulable.add(new AstarTask(unschedulableTask));
        }
        for (int unschedulableTask : endTasks) {
            root._unschedulable.add(new AstarTask(unschedulableTask));
        }
        for (int schedulableTask : startTasks) {
            root._schedulable.add(new AstarTask(schedulableTask));
        }
        //Sets when the processors were last used
        root._processorLastUsed = new ArrayList<>(_processingCores);
        for (int processor = 0; processor < _processingCores; processor++) {
            root._processorLastUsed.add(0);
        }

        //Iterative BFS
        //Initialise each level
        List<List<AstarSchedule>> levels = new ArrayList<>(numTasks + 1);
        for (int level = 0; level <= numTasks; level++) {
            levels.add(new ArrayList<>());
        }
        //Calculate
        for (int level = 0; level < numTasks; level++) {
            //Load current and next level
            List<AstarSchedule> currentLevel = levels.get(level);
            List<AstarSchedule> nextLevel = levels.get(level + 1);
            //Special case for first level
            if (level == 0) {
                levels.get(0).add(root);
            }
            //For every schedule in the level
            for (int child = 0; child < currentLevel.size(); child++) {
                //Load it
                AstarSchedule childSchedule = currentLevel.get(child);
                //Then for every processor
                for (int processorNum = 0; processorNum < _processingCores; processorNum++) {
                    //And for every task that can be scheduled
                    for (AstarTask task : childSchedule._schedulable) {
                        //Clone
                        AstarSchedule newSchedule = childSchedule.clone();
                        AstarTask newTask = task.clone();

                        //Set processor
                        newTask._processor = processorNum;

                        //Determine data ready time
                        int drt = childSchedule._processorLastUsed.get(processorNum);
                        for (AstarTask t : childSchedule._scheduled) {
                            if (_arcs[t._taskNum][newTask._taskNum]) {
                                int temp = t._start + _nodeWeights[t._taskNum];
                                if (t._processor != processorNum) {
                                    temp += _arcWeights[t._taskNum][newTask._taskNum];
                                }
                                if (drt < temp) {
                                    drt = temp;
                                }
                            }
                        }

                        newTask._start = drt;

                        newSchedule._processorLastUsed.set(processorNum, (drt + _nodeWeights[newTask._taskNum]));

                        //Move task to scheduled from schedulable
                        newSchedule._schedulable.remove(newTask);
                        newSchedule._scheduled.add(newTask);

                        //Check if unschedulables are schedulable
                        for (AstarTask unshedulable : newSchedule._unschedulable) {
                            boolean schedulable = true;
                            int taskNum = unshedulable._taskNum;
                            for (Integer dependency : partialDependencies.get(taskNum)) {
                                if (!newSchedule._scheduled.contains(new AstarTask(dependency))) {
                                   schedulable = false;
                                   break;
                               }
                            }
                            if (schedulable) {
                                newSchedule._schedulable.add(unshedulable);
                            }
                        }
                        newSchedule._unschedulable.removeAll(newSchedule._schedulable);

                        //Put new sub schedule in parent and the level below
                        childSchedule._subSchedules.add(newSchedule);
                        nextLevel.add(newSchedule);
                    }
                }
            }
        }

        //Find the lowest cost schedule
        int temp = levels.get(levels.size() - 1).get(0).cost();
        AstarSchedule best = levels.get(levels.size() - 1).get(0);
        for (AstarSchedule s : levels.get(levels.size() - 1)) {
            if (s.cost() < temp) {
                temp = s.cost();
                best = s;
            }
        }

        //Convert to output format
        List<Integer> starts = new ArrayList<>();
        List<Integer> processors = new ArrayList<>();
        List<AstarTask> tasks = new ArrayList<>(best._scheduled);
        Collections.sort(tasks);
        for (AstarTask scheduled : tasks) {
            starts.add(scheduled._start);
            processors.add(scheduled._processor);
        }

        //Hand output schedule to output listener
        for (ScheduleResultListener l : getListeners()) {
            l.finalSchedule(_graphName, Arrays.asList(_nodeNames), primToBoolean2D(_arcs), primToInteger2D(_arcWeights), primToInteger1D(_nodeWeights), starts, processors);
        }
    }

    private void updateDependencies(List<Integer> parentDependcies, int target) {
        completeDependencies.get(target).addAll(parentDependcies);
        List<Integer> newParentDependencies = new ArrayList<>(parentDependcies);
        newParentDependencies.add(target);
        for (int toTask = 0; toTask < numTasks; toTask++) {
            if (_arcs[target][target]) {
                updateDependencies(newParentDependencies, toTask);
            }
        }
    }
}
