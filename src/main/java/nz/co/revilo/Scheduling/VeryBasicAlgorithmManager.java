package nz.co.revilo.Scheduling;

import java.util.*;

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


    }

}
