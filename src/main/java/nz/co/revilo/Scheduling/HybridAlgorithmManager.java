package nz.co.revilo.Scheduling;

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

            //TODO Inform output about schedule
        } else {

        }
    }
}