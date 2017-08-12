package nz.co.revilo;

import nz.co.revilo.Scheduling.ImprovedTopologicalAlgorithmManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test cases designed for use with the topological sort implementation(s) of AlgorithmManager. These tests only ensure
 * that schedules are valid, NOT optimal. This is done by ensuring all dependencies are satisfied (i.e. arcs correctly
 * acknowledged) in scheduling, and that is no more than one process running on a processor at once.
 * @author Aimee
 * @version alpha
 */
public class TopologicalTest extends ValidityTest {

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
        TestResultListener t = schedule(AppTest.TEST_PATH + "input.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }

    /**
     * Tests if the 7-node out tree input schedule satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test7NodeOutTree() {
        TestResultListener t = schedule(AppTest.TEST_PATH + "Nodes_7_OutTree.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }

    /**
     * Tests if the 8-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test8NodeRandom() {
        TestResultListener t = schedule(AppTest.TEST_PATH + "Nodes_7_OutTree.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test9NodeSeriesParallel() {
        TestResultListener t = schedule(AppTest.TEST_PATH + "Nodes_7_OutTree.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }
}
