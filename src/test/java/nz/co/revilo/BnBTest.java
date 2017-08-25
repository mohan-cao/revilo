package nz.co.revilo;

import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.BranchAndBoundAlgorithmManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for BnB
 * 
 * @author Abby S
 * Based on class written by Aimee
 *
 */
public class BnBTest extends ValidityTest {

    class MockManager extends AlgorithmManager {
        /**
         * Sets the number of processing cores the tasks must be scheduled on.
         *
         * @param processingCores number of cores specified for the final schedule
         */
        public MockManager(int processingCores) {
            super(processingCores);
        }

        @Override
        protected void execute() {
            fail("Forgot to implement a real manager.");
        }
    }
    /**
     * Sets up a new AlgorithmManager before each test case. AlgorithmManagerImplementations can be switched out
     */
    @Before
    public void setUp() {
        _algorithmManager = new MockManager(1);
    }


    /**
     * Tests against the simple diamond DAG
     * @author Mohan Cao
     */
    @Test
    public void testSimpleDiamondDAG(){
        _algorithmManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "input.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),10);
        _algorithmManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t2 = scheduleBnB(AppTest.TEST_PATH + "input.dot");
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(_algorithmManager.getUpperBound(),10);
    }


    /**
     * Tests against a linearly dependent DAG
     */
    @Test
    public void testLinearDAG(){
        _algorithmManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "input1.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),25);
    }

    /**
     * Tests against a linearly dependent (with some branching) DAG
     */
    @Test
    public void testLinearDAG2(){
        _algorithmManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "input2.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),25);
    }

    /**
     * Tests against a simple fanning DAG
     */
    @Test
    public void testSimpleFanningDAG(){
        _algorithmManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "input4.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),25);
    }

    /**
     * Tests if the 10-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test10NodesRandom() {
        _algorithmManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "Nodes_10_Random.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),50);
        _algorithmManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = scheduleBnB(AppTest.TEST_PATH + "Nodes_10_Random.dot");
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(_algorithmManager.getUpperBound(),50);
    }

    /**
     * Tests if the 7-node out tree input schedule satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test7NodeOutTree() {
        _algorithmManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "Nodes_7_OutTree.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),28);
        _algorithmManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = scheduleBnB(AppTest.TEST_PATH + "Nodes_7_OutTree.dot");
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(_algorithmManager.getUpperBound(),22);
    }

    /**
     * Tests if the 8-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test8NodeRandom() {
        _algorithmManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "Nodes_8_Random.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),581);
        _algorithmManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = scheduleBnB(AppTest.TEST_PATH + "Nodes_8_Random.dot");
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(_algorithmManager.getUpperBound(),581);
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test9NodeSeriesParallel() {
        _algorithmManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "Nodes_9_SeriesParallel.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),55);
        _algorithmManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = scheduleBnB(AppTest.TEST_PATH + "Nodes_9_SeriesParallel.dot");
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(_algorithmManager.getUpperBound(),55);
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    @Category(SlowTest.class)
    public void test11NodeSeriesParallel() {
        _algorithmManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = scheduleBnB(AppTest.TEST_PATH + "Nodes_11_OutTree.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(_algorithmManager.getUpperBound(),350);
        _algorithmManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = scheduleBnB(AppTest.TEST_PATH + "Nodes_11_OutTree.dot");
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(_algorithmManager.getUpperBound(),227);
    }
}
