package nz.co.revilo;

import nz.co.revilo.Input.DotFileReader;
import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.ImprovedTopologicalAlgorithmManager;
import nz.co.revilo.Scheduling.VeryBasicAlgorithmManager;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases designed for use with the topological sort implementation(s) of AlgorithmManager. These tests only ensure
 * that schedules are valid, NOT optimal. This is done by ensuring all dependencies are satisfied (i.e. arcs correctly
 * acknowledged) in scheduling, and that is no more than one process running on a processor at once.
 * @author Aimee
 * @version alpha
 */
public class TopologicalTest {
    private AlgorithmManager _algorithmManager;

    class Pair<K,V>{
        private K _a;
        private V _b;
        public Pair(K a, V b){
            _a = a;
            _b = b;
        }
        @Override
        public boolean equals(Object o){
            if(o instanceof Pair){
                return (this._a == ((Pair)o)._a && this._b == ((Pair)o)._b);
            }
            return false;
        }
    }

    /**
     * Sets up a new AlgorithmManager before each test case. AlgorithmManagerImplementations can be switched out
     */
    @Before
    public void setUp() {
        _algorithmManager = new ImprovedTopologicalAlgorithmManager(1);
    }

    /**
     * Tests if the 10-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test10NodesRandom() {
       // Test is dependencies are all satisfied
        TestResultListener t = schedule("input.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }

    /**
     * Reads in the graph's DOT file, and finds the schedule based on the supplied AlgorithmManager. The final schedule
     * is provided to the output TestResultListener
     * @param filename name of the file containing the graph we want schedule, and then test the schedule of
     * @return a TestResultListener containing the information about the final schedule
     */
    public TestResultListener schedule(String filename) {
        DotFileReader reader = new DotFileReader(filename);
        TestResultListener listener = new TestResultListener();
        _algorithmManager.inform(listener);
        try {
            reader.startParsing(_algorithmManager);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File " + filename+ " does not exist");
        }
        return listener;
    }

    /**
     * Check that dependencies (i.e. ordering of tasks based on what tasks must be completed for others to start) are
     * correctly being set.
     * @param listener the test result listener containing the information from
     * @return boolean representing if dependencies are satisfied
     */
    public boolean satisfiesDependencies(TestResultListener listener) {
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
    public boolean validStartTimeForTasks(TestResultListener listener){
        List<TestResultListener.Node> nodes = listener.getNodes();
        Map<Pair<Integer,Integer>,TestResultListener.Node> nodeProcessMap = new HashMap<>();
        int nNodes = nodes.size();
        // Iterate through all nodes
        for(TestResultListener.Node n : nodes){
            // Check for pairs of start times + processor cores for a node, and put them in a map.
            Pair<Integer,Integer> startAndCore = new Pair<>(n.getStartTime(),n.getCore());
            if(nodeProcessMap.get(startAndCore)!=null){
                // Found a node in the map that was already starting at that time + processor number
                return false;
            }
            nodeProcessMap.put(new Pair<>(n.getStartTime(),n.getCore()),n);
        }
        return true;
    }

}
