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
            Set<Integer> setToTestUniqueElemsInColumn = new HashSet<>();
            depths.add(0);
            int[] colArray = new int[_arcWeights.length];
            for (int j = 0; j < _arcWeights.length; j++) {
                setToTestUniqueElemsInColumn.add( _arcWeights[j][row]);
            }

            //if they only have a single -1 element then it will
            //be considered a sink
            if (setToTestUniqueElemsInColumn.size() == 1 && setToTestUniqueElemsInColumn.contains(-1)) {
                sinks.add(row);
                int[] thisRow = _arcWeights[row];
            }
        }

        //now we want to get ALL the nodes okay?

        for (int row = 0; row < _arcWeights.length; row++) {
            allNodes.add(row);
        }

        //and after that we will start looking for things?

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
            processorsDummy.add(1);
            currentCost += _nodeWeights[node];
        }
        System.out.println(nStarts);

        getListener().finalSchedule(
                "new graph",
                _nodeOrder,
                new ArrayList<String>(),
                getToString(allNodes),
                primToBool(_arcs),
                primToInt(_arcWeights),
                getToInteger(_nodeWeights),
                nStarts,
                processorsDummy
        );
    }

    private List<String> getToString(ArrayList<Integer> intList) {
        ArrayList<String> strList = new ArrayList<>();
        for (int i: intList) {
            strList.add(Integer.toString(i));
        }
        return strList;
    }

    private List<Integer> getToInteger(int[] primitiveInts) {
        ArrayList<Integer> objectInts = new ArrayList<>();
        for (int i: primitiveInts) {
            objectInts.add(i);
        }
        return objectInts;
    }


    private List<List<Boolean>> primToBool(boolean[][] primitiveBools) {
        List<List<Boolean>> objectBools = new ArrayList<>();

        for (int i = 0; i < primitiveBools.length; i++) {
            objectBools.add(new ArrayList<>());
            for (int j = 0; j < primitiveBools[i].length; j++) {
                objectBools.get(i).add(primitiveBools[i][j]);
            }
        }

        return objectBools;
    }

    private List<List<Integer>> primToInt(int[][] primitiveInts) {
        List<List<Integer>> objectInts = new ArrayList<>();

        for (int i = 0; i < primitiveInts.length; i++) {
            objectInts.add(new ArrayList<>());
            for (int j = 0; j < primitiveInts[i].length; j++) {
                objectInts.get(i).add(primitiveInts[i][j]);
            }
        }
        return objectInts;
    }

}
