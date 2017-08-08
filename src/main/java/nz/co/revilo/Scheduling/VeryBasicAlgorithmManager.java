package nz.co.revilo.Scheduling;

import java.util.*;
import java.util.stream.Collectors;


public class VeryBasicAlgorithmManager extends AlgorithmManager {

    Set<Integer> sinks = new HashSet<>();
    ArrayList<Integer> allNodes = new ArrayList<>();
    ArrayList<Integer> visited = new ArrayList<>();
    ArrayList<Integer> depths = new ArrayList<>();
    ArrayList<Integer> toProcess = new ArrayList<>();

    public VeryBasicAlgorithmManager(int processingCores) {
        super(processingCores);
    }


    // we have arcs arcweights and nodeweights :O
    @Override
    protected void execute() {
        // find the sources
        // create column arrays first
        for (int row = 0; row < _arcWeights.length; row++) {
            Set<Integer> smolSet = new HashSet<>();
            depths.add(0);
            int[] colArray = new int[_arcWeights.length];
            for (int j = 0; j < _arcWeights.length; j++) {
                smolSet.add( _arcWeights[j][row]);
            }
            boolean isAllEqual = smolSet.size() == 1 && smolSet.contains(-1);
            if (isAllEqual) {
                sinks.add(row);
                int[] thisRow = _arcWeights[row];
            }
        }
        System.out.println("The sinks are: " + sinks);

        //now we want to get ALL the nodes okay?

        for (int row = 0; row < _arcWeights.length; row++) {
            allNodes.add(row);
        }

        //and after that we will start looking for things?
        System.out.println("All nodes: " + allNodes);

        toProcess.addAll(sinks); //
        for (Integer i: sinks) {
            depths.set(i, 0);
        }

        while (!toProcess.isEmpty()) {
            Integer latest = toProcess.get(0);
            toProcess.remove(latest); //take out first thing and add that into visited
            visited.add(latest);

            //find its children
            for (int i = 0; i < _arcWeights[latest].length; i++) {
                if (_arcWeights[latest][i] != -1) {
                    toProcess.add(i);
                    depths.set(i, Math.max(depths.get(i), depths.get(latest) + 1));
                }
            }

            System.out.println("now it's: " + toProcess);
            System.out.println("our depths: " + depths);
        }

        HashMap<Integer, ArrayList<Integer>> depthMap = new HashMap<>();
        for (int i = 0; i < depths.size(); i++) {
            if (depthMap.containsKey(depths.get(i))) {
                //if it's already in
                depthMap.get(depths.get(i)).add(i);
            } else {
                depthMap.put(depths.get(i), new ArrayList<>());
                depthMap.get(depths.get(i)).add(i);
            }
        }
        System.out.println(depthMap);

        ArrayList<Integer> nodeInOrder = new ArrayList<>();

        for (Integer k: depthMap.keySet()) {
            nodeInOrder.addAll(depthMap.get(k));
        }

        ArrayList<Integer> nStarts = new ArrayList<>();
        ArrayList<Integer> processorsDummy = new ArrayList<>();
        int currentCost = 0;
        for (Integer node: nodeInOrder) {
            nStarts.add(currentCost);
            processorsDummy.add(0);
            currentCost += _nodeWeights[node];
        }
        System.out.println(nStarts);

//    public void finalSchedule(String graphName, List<String> nodeNames, List<List<Boolean>> arcs, List<List<Integer>> arcWeights, List<Integer> nodeWeights, List<Integer> nodeStarts, List<Integer> nodeProcessor); //TODO determine data structure to pass

        getListener().finalSchedule(
                "new graph",
                allNodes.stream().map(Object::toString).collect(Collectors.toList()),
                primToBool(_arcs),
                primToInt(_arcWeights),
                getToInteger(_nodeWeights),
                nStarts,
                processorsDummy
        );


    }



    private List<Integer> getToInteger(int[] prim) {
        ArrayList<Integer> oop = new ArrayList<>();
        for (int i: prim) {
            oop.add(i);
        }
        return oop;
    }

    private List<List<Boolean>> primToBool(boolean[][] b) {
        List<List<Boolean>> bb = new ArrayList<>();

        for (int i = 0; i < b.length; i++) {
            bb.add(new ArrayList<>());
            for (int j = 0; j < b[i].length; j++) {
                bb.get(i).add(b[i][j]);
            }
        }

        return bb;
    }

    private List<List<Integer>> primToInt(int[][] iii) {
        List<List<Integer>> ii = new ArrayList<>();

        for (int i = 0; i < iii.length; i++) {
            ii.add(new ArrayList<>());
            for (int j = 0; j < iii[i].length; j++) {
                ii.get(i).add(iii[i][j]);
            }
        }

        return ii;
    }

}
