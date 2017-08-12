package nz.co.revilo.Scheduling;

import nz.co.revilo.Output.ScheduleResultListener;

import java.util.*;

public class HybridAlgorithmManager extends AlgorithmManager {

    private int upperBound = 0;
    private long[][] schedule;

    public HybridAlgorithmManager(int processingCores) {
        super(processingCores);
    }

    protected void execute() {
        for (Integer weight : _nodeWeights) {
            upperBound += weight;
        }

        schedule = new long[upperBound][getProcessingCores()];
        for (long[] time : schedule) {
            Arrays.fill(time, -1);
        }

        for (Integer weight : _nodeNums.values()) {
            if (_nodeWeights.get(weight) == -1) {
                //TODO weights shouldn't be 0
                weight = 0;
            }
        }

        if (getProcessingCores() < 1) {
            //TODO throw exception
        } else if (getProcessingCores() == 1) {
            int cost = 0;

            List<Integer> nodeStarts = new ArrayList<>(_nodeCounter.get());
            List<Integer> processor = new ArrayList<>(Collections.nCopies(_nodeCounter.get(), 0));

            Set<Integer> doneNodes = new HashSet<>();
            Set<Integer> readyNodes = new HashSet<>();
            Set<Integer> rest = new HashSet<>();

            rest.addAll(_nodeNums.values());
            rest.removeAll(_startNodes);
            readyNodes.addAll(_startNodes);

            do {
                for (Integer node : readyNodes) {
                    nodeStarts.set(node, cost);
                    cost += _nodeWeights.get(node);
                }

                doneNodes.addAll(readyNodes);
                readyNodes.clear();

                for (Integer to : rest) {
                    boolean ready = true;
                    for (Integer from : rest) {
                        if (_arcs.containsKey(from) && _arcs.get(from).containsKey(to)) {
                            ready = false;
                            break;
                        }
                    }
                    if (ready) {
                        readyNodes.add(to);
                    }
                }

                rest.removeAll(readyNodes);
            } while (!rest.isEmpty() || !readyNodes.isEmpty());

            //TODO Cycle detection error

            for (ScheduleResultListener listener : getListener()) {
                listener.finalSchedule(_graphName, _nodeNames, _arcs, _nodeWeights, nodeStarts, processor);
            }
        } else if (getProcessingCores() >= _nodeCounter.get()) {
            //page 90 of textbook
            //TODO Cycle detection error
            //TODO Inform output about schedule
        } else {
            int[] scheduleCost = new int[getProcessingCores()];

            //TODO Greedy run straight down tree for upper-bound

            List<List<List<Integer>>> options = new ArrayList<>(); //decision, option, node/processor/starttime/totaltime
            List<List<Integer>> choices = new ArrayList<>();

            options.add(new ArrayList<>());
            for (Integer startNode : _startNodes) {
                List<Integer> temp = new ArrayList<>(3);
                temp.add(startNode);
                temp.add(0);
                temp.add(0);
                temp.add(_nodeWeights.get(startNode));
                options.get(0).add(temp);
            }

            //TODO In-order tree traversal of "options" tree which uses "choices" to keep track of where in the tree you are, need to break out as it will be in a forever loop to traverse the tree
            for (List<Integer> option : options.get(0)) { //every start option
                while (true) {
                    break;
                }

            }
        }

    }
}