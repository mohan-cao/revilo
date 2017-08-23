package nz.co.revilo.Scheduling;

import nz.co.revilo.Scheduling.Astar.Schedule;
import nz.co.revilo.Scheduling.Astar.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AstartFirstAlgorithmManager extends AlgorithmManager {

    int numTasks;
    private List<Set<Integer>> completeDependencies;

    public AstartFirstAlgorithmManager(int processingCores) {
        super(processingCores);
    }

    @Override
    protected void execute() {
        //Number of Tasks
        numTasks = _nodeWeights.length;

        //Sort Tasks into start, middle and end sets
        Set<Integer> startTasks = new HashSet<>();
        Set<Integer> endTasks = new HashSet<>();
        for (int task = 0; task < numTasks; task++) {
            startTasks.add(task);
            endTasks.add(task);
        }
        for (int fromTask = 0; fromTask < numTasks; fromTask++) {
            for (int toTask = 0; toTask < numTasks; toTask++) {
                if (_arcs[fromTask][toTask]) {
                    startTasks.remove(toTask);
                    endTasks.remove(fromTask);
                }
            }
        }
        Set<Integer> middleTasks = new HashSet<>();
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
        Schedule root = new Schedule();
        for (int unschedulableTask : middleTasks) {
            root._unschedulable.add(new Task(unschedulableTask));
        }
        for (int unschedulableTask : endTasks) {
            root._unschedulable.add(new Task(unschedulableTask));
        }
        for (int schedulableTask : startTasks) {
            root._schedulable.add(new Task(schedulableTask));
        }
        //Sets when the processors were last used
        root._processorLastUsed = new ArrayList<>(getProcessingCores());
        for (int processor = 0; processor < _processingCores; processor++) {
            root._processorLastUsed.set(processor, 0);
        }

        //Iterative BFS
        //Initialise each level
        List<List<Schedule>> levels = new ArrayList<>(numTasks + 1);
        for (int level = 0; level <= numTasks; level++) {
            levels.set(level, new ArrayList<>());
        }
        //Calculate
        for (int level = 0; level < numTasks; level++) {
            //Load current and next level
            List<Schedule> currentLevel = levels.get(level);
            List<Schedule> nextLevel = levels.get(level + 1);
            //Special case for first level
            if (level == 0) {
                levels.get(0).add(root);
            }
            //For every schedule in the level
            for (int child = 0; child < currentLevel.size(); child++) {
                //Load it
                Schedule childSchedule = currentLevel.get(child);
                //Then for every processor
                for (int processorNum = 0; processorNum < _processingCores; processorNum++) {
                    //And for every task that can be scheduled
                    for (Task task : root._schedulable) {
                        //Clone
                        Schedule newSchedule = childSchedule.clone();
                        Task newTask = task.clone();

                        //Set processor
                        newTask._processor = processorNum;

                        //Determine data ready time
                        int drt = childSchedule._processorLastUsed.get(processorNum);
                        for (Integer dependency : partialDependencies.get(newTask._taskNum)) {
                            //TODO
                        }

                        //TODO Update processor last used, update task start
                        newSchedule._processorLastUsed.set(processorNum, childSchedule._processorLastUsed.get(processorNum) + _nodeWeights[task._taskNum]);

                        //Move task to scheduled from schedulable
                        newSchedule._schedulable.remove(newTask);
                        newSchedule._scheduled.add(newTask);

                        //Check if unschedulables are schedulable
                        for (Task unshedulable : newSchedule._unschedulable) {
                            //Assume it is schedulable
                            boolean schedulable = true;
                            //Until a counter case is found
                            for (Integer dependency : partialDependencies.get(unshedulable._taskNum)) {
                                if (!newSchedule._scheduled.contains(dependency)) {
                                    schedulable = false;
                                    break; //If a counter case is found then we can move on
                                }
                            }
                            if (schedulable) {
                                newSchedule._unschedulable.remove(unshedulable);
                                newSchedule._schedulable.add(unshedulable);
                            }
                        }

                        //Put new sub schedule in parent and the level below
                        childSchedule._subSchedules.add(newSchedule);
                        nextLevel.add(newSchedule);
                    }
                }
            }
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
