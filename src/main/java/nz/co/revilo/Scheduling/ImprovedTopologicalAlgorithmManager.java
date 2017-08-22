package nz.co.revilo.Scheduling;

import java.util.*;

public class ImprovedTopologicalAlgorithmManager extends AlgorithmManager {

    public ImprovedTopologicalAlgorithmManager(int processingCores) {
        super(processingCores);
    }

    ArrayList<Integer> depthList = new ArrayList<>();
    ArrayList<Integer> sources = new ArrayList<>();
    HashMap<String, Integer> nameToIndex = new HashMap<>();
    ArrayList<Integer> toProcess = new ArrayList<>();
    ArrayList<Integer> visited = new ArrayList<>();
    // information we have
    // graph name, node names, node weights, arcs, arcweights
    @Override
    public int getCurrentOptimal() {
        return -1; // this doesn't actually have a current optimal because it's topological
    }

    @Override
    protected void execute() {

        //create a hashmap between names and nodeweights for clarity


        //find sources through column array
        for (int row = 0; row < _arcWeights.length; row++) {

            nameToIndex.putIfAbsent(_nodeNames[row], row);

            Set<Integer> uniqueSet = new HashSet<>();
            depthList.add(0); //everything starts from 0
            int[] columnArray = new int[_arcWeights.length];
            for (int j = 0; j <_arcWeights.length; j++) {
                uniqueSet.add(_arcWeights[j][row]);
            }

            //considers the sources
            if (uniqueSet.size() == 1 && uniqueSet.contains(-1)) {
                sources.add(row);
                int[] thisRow = _arcWeights[row];
            } 
        }

        // get all the nodes
//        System.out.println(nameToIndex);

        //put all sources in nodes to process
        toProcess.addAll(sources);
        for (Integer i: sources) {
            depthList.set(i, 0);
        }

        //start processing from source
        while (!toProcess.isEmpty()) {
            Integer lastProcessed = toProcess.get(0);
            toProcess.remove(lastProcessed);
            visited.add(lastProcessed);

            //find processed node's children
            for (int i = 0; i < _arcWeights[lastProcessed].length; i++) {
                if (_arcWeights[lastProcessed][i] != -1) {
                    toProcess.add(i);
                    depthList.set(i, Math.max(depthList.get(i), depthList.get(lastProcessed) + 1));
                }
            }
        }

        //create a depth mapping
        HashMap<Integer, ArrayList<Integer>> depthMap = new HashMap<>();
        for (int i = 0; i < depthList.size(); i++) {
            if (depthMap.containsKey(depthList.get(i))) {
                //if it's already in
                depthMap.get(depthList.get(i)).add(i);
            } else {
                depthMap.put(depthList.get(i), new ArrayList<>());
                depthMap.get(depthList.get(i)).add(i);
            }
        }

//        System.out.println(depthMap);

        ArrayList<Integer> orderedNodes = new ArrayList<>();
        ArrayList<Integer> orderedKeySet = new ArrayList<>(depthMap.keySet());
        Collections.sort(orderedKeySet); //sort it JUST IN CASE
        for (Integer k: orderedKeySet) {
            orderedNodes.addAll(depthMap.get(k));
        }

        ArrayList<Integer> nodeStartTime = new ArrayList<>(Collections.nCopies(_nodeNames.length, 0));
        ArrayList<Integer> processorName = new ArrayList<>(Collections.nCopies(_nodeNames.length, 1));
        int runningCost = 0;

        for (Integer node: orderedNodes) {
//            System.out.println("Node index " + node + " name " + _nodeNames[node]);
            nodeStartTime.set(node, runningCost);
            runningCost += _nodeWeights[node];
        }

//        System.out.println("node names: " + Arrays.toString(_nodeNames));
//        System.out.println("start times:" + nodeStartTime);
//        System.out.println("node weights: " + Arrays.toString(_nodeWeights));

        getListener().finalSchedule(
                _graphName,
                Arrays.asList(_nodeNames),
                primToBool2D(_arcs),
                primToInt2D(_arcWeights),
                primToInt(_nodeWeights),
                nodeStartTime,
                processorName
        );

    }

    /**
     * Creates an object type 2d bool list from primitive array
     * @param prim the primitive boolean 2d array
     * @return b the reference type list
     */
    private List<List<Boolean>> primToBool2D(boolean[][] prim) {
        List<List<Boolean>> b = new ArrayList<>();
        for (int i = 0; i < prim.length; i++) {
            b.add(new ArrayList<>());
            for (int j = 0; j < prim[i].length; j++) {
                b.get(i).add(prim[i][j]);
            }
        }
        return b;
    }

    /**
     * Creates an object type 2d int list from primitive array
     * @param prim the primitive int 2d array
     * @return n the reference type list
     */
    private List<List<Integer>> primToInt2D(int[][] prim) {
        List<List<Integer>> n = new ArrayList<>();
        for (int i = 0; i < prim.length; i++) {
            n.add(new ArrayList<>());
            for (int j = 0; j < prim[i].length; j++) {
                n.get(i).add(prim[i][j]);
            }
        }
        return n;
    }

    private List<Integer> primToInt(int[] prim) {
        ArrayList<Integer> n = new ArrayList<>();
        for (int i: prim) {
            n.add(i);
        }
        return n;
    }
}
