package nz.co.revilo.Scheduling;

import java.util.*;

public class SchedulingAlgorithmManager extends AlgorithmManager {

    long upperBound = 0;

    public SchedulingAlgorithmManager(int processingCores) {
        super(processingCores);
    }

    protected void execute() {
        int numNodes = _nodeWeights.length;
        int[] startNodes;
        boolean[] unvisitedNodes = new boolean[numNodes];
        long minCost = 0;
        int[][] schedule;

        // Determine starting nodes
        // Can run in n time by determining during input
        Set<Integer> startNodesTemp;
        startNodesTemp = new TreeSet<>();
        // Populates set
        for (int to = 0; to < numNodes; to++) {
            boolean node = true;
            for (int from = 0; from < numNodes; from++) {
                if (_arcs[from][to]) {
                    node = false;
                    break;
                }
            }
            if (node) {
                startNodesTemp.add(to);
            }
        }
        //https://stackoverflow.com/questions/31394715/convert-integer-to-int-array
        //https://stackoverflow.com/questions/2451184/how-can-i-convert-a-java-hashsetinteger-to-a-primitive-int-array
        startNodes = startNodesTemp.stream().mapToInt(Number::intValue).toArray();

        // All nodes are unvisited
        Arrays.fill(unvisitedNodes, true);

        // Find worst case
        for (int weight : _nodeWeights) {
            upperBound += weight;
        }

        // Initialise schedule
        schedule = new int[getProcessingCores()][(int) upperBound * 10];
        Arrays.fill(schedule, -1);

        // For every start node do a search and return the best schedule
        for (int start : startNodes) {
            boolean[] unvisitedNodesClone = unvisitedNodes.clone();
            unvisitedNodesClone[start] = false;
            int[][] scheduleClone = schedule.clone();
            for (int time = 0; time < _nodeWeights[start]; time++) {
                scheduleClone[0][time] = start;
            }
            int[][] resultSchedule = recursiveMemeHere(scheduleClone, unvisitedNodesClone, _nodeWeights[start]);
            long costOfResult = scheduleCost(resultSchedule);
            if (costOfResult < upperBound) {
                schedule = resultSchedule;
                upperBound = costOfResult;
            }
        }

        //getListener().finalSchedule();
    }

    private int[][] recursiveMemeHere(int[][] currentSchedule, boolean[] unvisitedNodes, long currentCost) {
        int[][] bestschedule = new int[0][0];
        for (int unvisited = 0; unvisited < unvisitedNodes.length; unvisited++) {
            if (unvisitedNodes[unvisited] == true) {
                int[] dependencyList = dependencies(unvisited);

                boolean allDependciesVisited = true;
                for (int dependency : dependencyList) {
                    if (unvisitedNodes[dependency] && _arcs[dependency][unvisited]) {
                        allDependciesVisited = false;
                        break;
                    }
                }
                //If the node is not yet visited and all it's dependencies have been satisfied
                if (allDependciesVisited) {
                    //assume processor 0
                    //if (currentCost + _nodeWeights[unvisited] < worstCase) {
                    int[][] currentScheduleClone = currentSchedule.clone();
                    for (int time = 0; time < _nodeWeights[unvisited]; time++) {
                        currentScheduleClone[0][time + (int) currentCost] = unvisited;
                    }
                    boolean[] unvisitedNodesClone = unvisitedNodes.clone();
                    unvisitedNodesClone[unvisited] = false;
                    long currentCostClone = currentCost + _nodeWeights[unvisited];
                    int[][] resultSchedule = recursiveMemeHere(currentScheduleClone, unvisitedNodesClone, currentCostClone);
                    long resultCost = scheduleCost(resultSchedule);
                    if (resultCost <= upperBound) {
                        currentCost = resultCost;
                        bestschedule = resultSchedule;
                    }
                    //}
                }
            }
        }
        return bestschedule;
    }

    private int[] dependencies(int target) {
        List<Integer> dependenciesList = new ArrayList<>();
        for (int node = 0; node < _arcs.length; node++) {
            if (_arcs[node][target]) {
                dependenciesList.add(node);
            }
        }
        return Arrays.stream((Integer[]) dependenciesList.toArray()).mapToInt(Integer::intValue).toArray();
    }

    private long scheduleCost(int[][] schedule) {
        long cost = 0;
        for (int time = 0; time < schedule[0].length; time++) {
            boolean workFlag = false;
            for (int cores = 0; cores < schedule.length; cores++) {
                if (schedule[cores][time] != -1) {
                    workFlag = true;
                    break;
                }
            }
            if (workFlag) {
                cost++;
            }
        }
        return cost;
    }
}
