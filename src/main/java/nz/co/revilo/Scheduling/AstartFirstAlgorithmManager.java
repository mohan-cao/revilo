package nz.co.revilo.Scheduling;

import nz.co.revilo.Scheduling.Astar.Schedule;
import nz.co.revilo.Scheduling.Astar.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AstartFirstAlgorithmManager extends AlgorithmManager {

    int numTasks;
    private List<Set<Integer>> dependencies;

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

        //Determine dependencies for
        dependencies = new ArrayList<>(numTasks);
        for (int task = 0; task < numTasks; task++) {
            dependencies.add(new HashSet<Integer>());
        }
        for (Integer startTask : startTasks) {
            updateDependencies(new ArrayList<>(), startTask);
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
        List<List<Schedule>> levels = new ArrayList<>(numTasks);
        for (int level = 0; level < numTasks; level++) {
            levels.set(level, new ArrayList<>());
        }
        Schedule blah = root;
        for (int level = 0; level < numTasks; level++) {
            List<Schedule> currentLevel = levels.get(level);
            if (level == 0) {
                levels.get(0).add(root);
            }
            for (int child = 0; child < currentLevel.size(); child++) {
                Schedule childSchedule = currentLevel.get(child);
                for (int processorNum = 0; processorNum < _processingCores; processorNum++) {
                    for (Task task : root._schedulable) {
                        Schedule newSchedule = childSchedule.clone();
                        Task newTask = task.clone();

                        newTask._processor = processorNum;
                        newSchedule._processorLastUsed.set(processorNum, childSchedule._processorLastUsed.get(processorNum) + _nodeWeights[task._taskNum]);

                        newSchedule._schedulable.remove(newTask);
                        newSchedule._scheduled.add(newTask);

                        //Check if unschedulables are schedulable

                        blah._subSchedules.add(newSchedule);
                        currentLevel.add(newSchedule);//TODO next level idiot
                    }
                }
            }
        }
    }

    private void updateDependencies(List<Integer> parentDependcies, int target) {
        dependencies.get(target).addAll(parentDependcies);
        List<Integer> newParentDependencies = new ArrayList<>(parentDependcies);
        newParentDependencies.add(target);
        for (int toTask = 0; toTask < numTasks; toTask++) {
            if (_arcs[target][target]) {
                updateDependencies(newParentDependencies, toTask);
            }
        }
    }
}
