package nz.co.revilo.Scheduling;

import nz.co.revilo.Input.DotFileParser;
import nz.co.revilo.Input.ParseResultListener;
import nz.co.revilo.Output.ScheduleResultListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AlgorithmManager implements ParseResultListener {

    private int _processingCores;
    private ScheduleResultListener _listener;
    int[] _nodeWeights;
    boolean[][] _arcs;
    int[][] _arcWeights;


    public AlgorithmManager(int processingCores) {
        _processingCores = processingCores;
    }

    protected abstract void execute();//TODO

    public void inform(ScheduleResultListener listener) {
        _listener = listener;
    }
    
       public void ParsingResults(int[] nodeWeights, boolean[][] arcs, int[][] arcWeights) {

        _arcWeights = arcWeights;
        _arcs = arcs;
        _nodeWeights = nodeWeights;

        execute();
    }

    protected List<List<Boolean>> arcsToBoolList(boolean[][] b){
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
    protected List<List<Integer>> arcsToIntList(int[][] b){
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
    protected List<Integer> arrayToWeights(int[] weights){
        List<Integer> arr = new ArrayList<Integer>();
        for(int i=0;i<weights.length;i++){
            arr.add(weights[i]);
        }
        return arr;
    }

    protected int getProcessingCores() {
        return _processingCores;
    }

    protected ScheduleResultListener getListener() {
        return _listener;
    }
}
