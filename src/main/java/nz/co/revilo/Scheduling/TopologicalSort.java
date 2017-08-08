package nz.co.revilo.Scheduling;

import org.graphstream.graph.Graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TopologicalSort extends AlgorithmManager {

    public TopologicalSort(int processingCores) {
        super(processingCores);
    }

    @Override
    protected void execute() {
        Queue<Integer> sources = new LinkedList<>();
        int size = _arcs.length;
        for(int column=0;column<size;column++){
            boolean isSource = true;
            for(int row=0;row<size;row++){
                if(_arcs[row][column]){
                    isSource = false;
                    break;
                }
            }
            if(isSource) sources.add(column);
        }
        // now have queue of sources
        Integer node;
        List<Integer> output = new LinkedList<>();
        boolean[][] trackedEdges = new boolean[size][size];

        while((node = sources.poll())!=null){
            //set outgoing arc weights to -1 and also false
            for(int column=0;column<size;column++){
                if(_arcs[node][column]){
                    _arcs[node][column] = false;
                    trackedEdges[node][column] = true;
                    boolean dependent = false;
                    for(int recheck=0;recheck<size;recheck++){
                        if(_arcs[column][recheck] && recheck!=column) {
                            dependent = true;
                            break;
                        }
                    }
                    if(!dependent) sources.add(column);
                }

            }
            output.add(node);
        }
        _arcs = trackedEdges;
        getListener().finalSchedule(
                "output",
                IntStream.range(0, size).boxed().map(c->String.valueOf(c)).collect(Collectors.toList()),
                arcsToBoolList(_arcs),
                arcsToIntList(_arcWeights),
                arrayToWeights(_nodeWeights),
                IntStream.range(0, size).boxed().collect(Collectors.toList()),
                new ArrayList<>(Collections.nCopies(size, 0))
        );
    }
    private List<List<Boolean>> arcsToBoolList(boolean[][] b){
        List<List<Boolean>> output = new ArrayList<>();
        for(int i=0;i<b.length;i++){
            List<Boolean> row = new ArrayList<>();
            for(int j=0;j<b[i].length;j++){
                row.add(b[i][j]);
                System.out.print(b[i][j]+" ");
            }
            System.out.print("\n");
            output.add(row);
        }
        return output;
    }
    private List<List<Integer>> arcsToIntList(int[][] b){
        List<List<Integer>> output = new ArrayList<>();
        for(int i=0;i<b.length;i++){
            List<Integer> row = new ArrayList<>();
            for(int j=0;j<b[i].length;j++){
                row.add(b[i][j]);
                System.out.print(b[i][j]+" ");
            }
            System.out.print("\n");
            output.add(row);
        }
        return output;
    }
    private List<Integer> arrayToWeights(int[] weights){
        List<Integer> arr = new ArrayList<Integer>();
        for(int i=0;i<weights.length;i++){
            arr.add(weights[i]);
        }
        return arr;
    }
}
