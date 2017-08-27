package nz.co.revilo;

import nz.co.revilo.Input.DotFileReader;
import nz.co.revilo.Input.FileParser;
import nz.co.revilo.Input.GxlFileReader;
import nz.co.revilo.Scheduling.AlgorithmManager;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Redone as a utility class.
 */
public class ValidityTest {
    static class Tuple<T,V>{
        T a; V b;
        Tuple(T x, V y){
            a = x; b = y;
        }

        public T getA() {
            return a;
        }

        public V getB() {
            return b;
        }
    }
    /**
     * Reads in the graph's DOT file, and finds the schedule based on the supplied AlgorithmManager. The final schedule
     * is provided to the output TestResultListener
     *
     * @author Mohan Cao
     *
     * @param filename name of the file containing the graph we want schedule, and then test the schedule of
     * @return a TestResultListener containing the information about the final schedule
     */
    public synchronized static Tuple<TestResultListener,FileParser> schedule(final AlgorithmManager aManager, final String filename, final boolean isBnB) {
        FileParser reader;
        if (filename.toUpperCase().contains("GXL")) {
            reader = new GxlFileReader(filename);
        } else {
            reader = new DotFileReader(filename);
        }

        TestResultListener listener;
        listener = (isBnB)?new TestResultListener(true):new TestResultListener();
        aManager.inform(listener);
        return new Tuple<>(listener,reader);
    }

    /**
     * Check that dependencies (i.e. ordering of tasks based on what tasks must be completed for others to start) are
     * correctly being set.
     * @param listener the test result listener containing the information from
     * @return boolean representing if dependencies are satisfied
     */
    public synchronized static boolean satisfiesDependencies(final TestResultListener listener) {
        List<TestResultListener.Node> nodes = listener.getNodes();
        int nNodes = nodes.size();

        // Iterate through all nodes checking their dependencies start at least as late as necessary by node and arc
        // weight constraints.
        for(int i = 0; i < nNodes; i++) {
            TestResultListener.Node currentNode = nodes.get(i);
            List<Integer> dependencies = currentNode.getDependencies();
            List<Integer> arcs = listener.getArcWeights(i);
            int earliestDependencyStart = currentNode.getStartTime() + currentNode.getWeight();
            // Interage through all dependencies, checking if each start time is greater than or equal to the earlier
            // possible
            for(int dependency : dependencies) {
                TestResultListener.Node currentDependency = nodes.get(dependency);
                // If the nodes are on different cores, include processing time
                int earliestCurrentDependencyStart = earliestDependencyStart
                        + (currentDependency.sameCore(currentNode)
                        ? 0 /* if on same core, do not add communication*/
                        : arcs.get(dependency)/* if on different cores, add communication*/ );
                if(currentDependency.getStartTime() < earliestCurrentDependencyStart) {
                    System.out.println("Error: Node " + currentDependency + " starts at time "
                            + currentDependency.getStartTime() + " while Node " + currentNode
                            + " it is dependent on starts at time " + currentNode.getStartTime());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check that no two tasks start at the same time.
     * @param listener the test result listener containing the information from
     * @return boolean true if it satisfies that no processor has two tasks starting at the same time
     */
    public synchronized static boolean validStartTimeForTasks(final TestResultListener listener){
        // Iterate through all processors
        List<TestResultListener.Node> nodes = listener.getNodes();
        for(List<TestResultListener.Node> processor : listener.getCores()) {
            // Iterate through tasks on the current processor
            for (int i = 0; i < processor.size() - 1; i++) {
                int start = nodes.get(i).getStartTime();
                int end = nodes.get(i).getStartTime() + nodes.get(i).getWeight();
                // Iterate through all tasks listed after that task in the processor to avoid redundancy
                for (int j = i + 1; j < processor.size(); j++) {
                    // If the node at index j has a start time between the start and end times of the task at index j,
                    // there is overlap, and the start time is not valid i.e. the start time of the task at index j is
                    // in the range [start, end)
                    if ((nodes.get(j).getStartTime() >= start) && (nodes.get(j).getStartTime() < end) && i == j) {
                        System.out.print("Error: Tasks " + nodes.get(j) + " and " + nodes.get(i) + "overlap in the " +
                                "schedule on core " + nodes.get(i).getCore());
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
