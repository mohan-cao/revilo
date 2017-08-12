package nz.co.revilo.Scheduling;

import nz.co.revilo.Scheduling.AlgorithmManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 *
 * @author Aimee
 * @version pre-alpha
 */
public class AimeeBranchAndBound extends AlgorithmManager {
    private ArrayList<ArrayList<Integer>> _bestSolution = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> _currentSolution = new ArrayList<>();
    private ArrayList<Integer> _bestStartTimes = new ArrayList<>();
    private ArrayList<Integer> _currentStartTimes = new ArrayList<>();
    private ArrayList<Integer> _availableTasks = new ArrayList<>();
    private ArrayList<Integer> _tasksInCurrentSolution = new ArrayList<>();
    private int _bestScheduleLength = Integer.MAX_VALUE;
    private int _processingCores;
    private int _taskCount;

    /**
     * Creates an AlgorithmManager with a branch and bound scheduling solution
     *
     * @param processingCores
     */
    public AimeeBranchAndBound(int processingCores) {
        super(processingCores);
        _processingCores = processingCores;
    }

    /**
     * Executes the branch and bound implementation
     */
    @Override
    protected void execute() {
        _taskCount = _nodeWeights.length;
        prepare();
        branchAndBound(0);

        ArrayList<Integer> nodeProcessors = new ArrayList<>();

        for(int i = 0; i < _taskCount; i++) {
            nodeProcessors.add(-1);
        }

        for(int core = 0; core < _processingCores; core++) {
            for (int task : _bestSolution.get(core)) {
                nodeProcessors.set(task, core);
                System.out.println(core);
            }
        }

        getListener().finalSchedule(
                "output",
                IntStream.range(0, _taskCount).boxed().map(c->String.valueOf(c)).collect(Collectors.toList()),
                arcsToBoolList(_arcs),
                arcsToIntList(_arcWeights),
                arrayToWeights(_nodeWeights),
                _bestStartTimes,
                nodeProcessors
        );
    }

    /**
     * Recursive method which branches
     */
    private void branchAndBound(int currentLength) {
        boolean isLeaf = true;
        for (int i = 0; i < _availableTasks.size(); i++) {
            if (_availableTasks.get(i) != -1) {
                isLeaf = false;
                break;
            }
        }

        if (isLeaf) {
            System.out.println(_currentSolution);
            if (currentLength < _bestScheduleLength) {
                _bestScheduleLength = currentLength;
                _bestSolution = (ArrayList<ArrayList<Integer>>) _currentSolution.clone();
                _bestStartTimes = (ArrayList<Integer>) _currentStartTimes.clone();
            }
            return;
        }

        // Bound results
        boolean isTooLong = false;
        int minLengthBound = Integer.MAX_VALUE;
        if(minLengthBound > _bestScheduleLength) {
            isTooLong = true;
        }
        if(isTooLong) {
            return;
        }

        // Iterate through all available tasks
        for (int k = 0; k < _availableTasks.size(); k++) {
            int task = _availableTasks.get(k);
            if (task == -1) {
                continue;
            }

            // Set task as no longer available
            _availableTasks.set(k, -1);
            // Puts the currently selected task on each processor (i.e. create branches)
            for (int core = 0; core < _processingCores; core++) {
                int availableTasksInitialLength = _availableTasks.size();
                _currentSolution.get(core).add(task);
                int taskStartTime = 0;
                // Finds the time at which task starts
                for (int i = 0; i < _taskCount; i++) {
                    if (!_arcs[i][task]) {
                        continue;
                    }
                    int startTimeAfterJ = _currentStartTimes.get(i) + _nodeWeights[i]
                            + (_currentSolution.get(core).contains(i) ? 0 : _arcWeights[i][task]);
                    // Checks if there is
                    if (taskStartTime < startTimeAfterJ) {
                        taskStartTime = startTimeAfterJ;
                    }
                }

                ArrayList<Integer> currentCoreSolution = _currentSolution.get(core);
                if(taskStartTime < (_currentStartTimes.get(currentCoreSolution.get(currentCoreSolution.size() - 1))
                    + _nodeWeights[currentCoreSolution.get(currentCoreSolution.size() - 1)])) {
                    taskStartTime = _currentStartTimes.get(currentCoreSolution.get(currentCoreSolution.size() - 1))
                            + _nodeWeights[currentCoreSolution.get(currentCoreSolution.size() - 1)];
                }
                _currentStartTimes.set(task, taskStartTime);
                _tasksInCurrentSolution.add(task);
                for (int i = 0; i < _taskCount; i++) {
                    if (_arcs[task][i]) {
                        boolean hasAllDependencies = true;
                        for (int j = 0; j < _taskCount; j++) {
                            if (_arcs[j][i]) {
                                if (!(_tasksInCurrentSolution.contains(j))) {
                                    hasAllDependencies = false;
                                }
                            }
                        }

                        if (hasAllDependencies) {
                            _availableTasks.add(i);
                        }
                    }
                }

                // Recurse
                branchAndBound(((_currentStartTimes.get(task) + _nodeWeights[task]) > currentLength)
                        ? (_currentStartTimes.get(task) + _nodeWeights[task]) : currentLength);

                // Undo setup recursion
                // Removes last added available tasks
                for (int i = _availableTasks.size(); i > availableTasksInitialLength; i--) {
                    _availableTasks.remove(i - 1);
                }
                _currentSolution.get(core).remove(_currentSolution.get(core).size() - 1);

                // Resets the current start time of the task to unset
                _currentStartTimes.set(task, 0);
                _availableTasks.set(k,task);
            }
        }
    }

    /**
     * Prepares fields for scheduling by determining what nodes are sources, and storing them in _availableTasks.
     */
    private void prepare() {
        for (int i = 0; i < _taskCount; i++) {
            boolean isEmpty = true;
            for (int j = 0; j < _taskCount; j++) {
                isEmpty = isEmpty && !(_arcs[j][i]);
            }
            if (isEmpty) {
                _availableTasks.add(i);
            }
        }

        // Set up cores ArrayList
        for (int i = 0; i < _processingCores; i++) {
            _currentSolution.add(new ArrayList<Integer>());
        }

        // Set up start times ArrayList
        for (int i = 0; i < _taskCount; i++) {
            _currentStartTimes.add(0);
        }
    }
}
