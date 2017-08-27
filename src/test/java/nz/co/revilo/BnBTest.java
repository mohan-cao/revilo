package nz.co.revilo;

import nz.co.revilo.Input.FileParser;
import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.BranchAndBoundAlgorithmManager;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static nz.co.revilo.ValidityTest.satisfiesDependencies;
import static nz.co.revilo.ValidityTest.schedule;
import static nz.co.revilo.ValidityTest.validStartTimeForTasks;
import static org.junit.Assert.*;
import static nz.co.revilo.ValidityTest.Tuple;

/**
 * Test class for BnB
 * 
 * @author Abby S
 * Based on class written by Aimee
 *
 */
public class BnBTest {

    /**
     * Tests against the simple diamond DAG
     * @author Mohan Cao
     */
    @Test
    public synchronized void testSimpleDiamondDAG() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "input.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),10);
        aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t2 = schedule(aManager,AppTest.TEST_PATH + "input.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t2.getA()));
        assertTrue(validStartTimeForTasks(t2.getA()));
        assertEquals(aManager.getUpperBound(),10);
    }


    /**
     * Tests against a linearly dependent DAG
     */
    @Test
    public synchronized void testLinearDAG() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "input1.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),25);
    }

    /**
     * Tests against a linearly dependent (with some branching) DAG
     */
    @Test
    public synchronized void testLinearDAG2() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "input2.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),25);
    }

    /**
     * Tests against a simple fanning DAG
     */
    @Test
    public synchronized void testSimpleFanningDAG() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "input4.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),25);
    }

    /**
     * Tests if the 10-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public synchronized void test10NodesRandom() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "Nodes_10_Random.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),50);
        AlgorithmManager aManager2 = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t2 = schedule(aManager2,AppTest.TEST_PATH + "Nodes_10_Random.dot",true);
        t.getB().startParsing(aManager2);
        assertTrue(satisfiesDependencies(t2.getA()));
        assertTrue(validStartTimeForTasks(t2.getA()));
        assertEquals(aManager.getUpperBound(),50);
    }

    /**
     * Tests if the 7-node out tree input schedule satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public synchronized void test7NodeOutTree() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "Nodes_7_OutTree.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),28);
        AlgorithmManager aManager2 = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t2 = schedule(aManager2,AppTest.TEST_PATH + "Nodes_7_OutTree.dot",true);
        t.getB().startParsing(aManager2);
        assertTrue(satisfiesDependencies(t2.getA()));
        assertTrue(validStartTimeForTasks(t2.getA()));
        assertEquals(aManager2.getUpperBound(),22);
    }

    /**
     * Tests if the 8-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public synchronized void test8NodeRandom() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "Nodes_8_Random.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),581);
        AlgorithmManager aManager2 = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t2 = schedule(aManager2,AppTest.TEST_PATH + "Nodes_8_Random.dot",true);
        t.getB().startParsing(aManager2);
        assertTrue(satisfiesDependencies(t2.getA()));
        assertTrue(validStartTimeForTasks(t2.getA()));
        assertEquals(aManager.getUpperBound(),581);
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public synchronized void test9NodeSeriesParallel() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "Nodes_9_SeriesParallel.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),55);
        AlgorithmManager aManager2 = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t2 = schedule(aManager2,AppTest.TEST_PATH + "Nodes_9_SeriesParallel.dot",true);
        t.getB().startParsing(aManager2);
        assertTrue(satisfiesDependencies(t2.getA()));
        assertTrue(validStartTimeForTasks(t2.getA()));
        assertEquals(aManager.getUpperBound(),55);
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    @Category(SlowTest.class)
    public synchronized void test11NodeSeriesParallel() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager,AppTest.TEST_PATH + "Nodes_11_OutTree.dot",true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(),350);
        AlgorithmManager aManager2 = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t2 = schedule(aManager2,AppTest.TEST_PATH + "Nodes_11_OutTree.dot",true);
        t.getB().startParsing(aManager2);
        assertTrue(satisfiesDependencies(t2.getA()));
        assertTrue(validStartTimeForTasks(t2.getA()));
        assertEquals(aManager2.getUpperBound(),227);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 499);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_01_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 59);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_84_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.84_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 38);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_10_01_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 69);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 47);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 222);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_1_04_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_1.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 27);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 39);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 344);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 40);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 37);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 253);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_93_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.93_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 30);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_1_97_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 39);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 349);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_09_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.09_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 38);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_96_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.96_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 29);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_01_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 71);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 481);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_97_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 53);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 31);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_1_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 22);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_2_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_30_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 386);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_40_CCR_1_85_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.40_CCR_1.85_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_2_03_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_2.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_00_CCR_1_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.00_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 79);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_30_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 895);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 2680);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_99_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 274);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_10_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_2_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 126);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 494);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 51);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_98_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 55);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_10_03_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_10.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 76);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 448);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_01_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 53);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_98_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_10_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 62);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 515);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_97_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 51);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_10_04_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_10.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_2_05_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_2.05_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 49);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 579);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_00_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 45);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_99_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_9_97_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_9.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 59);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_0_10_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 450);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_01_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 77);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_97_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_9_98_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_9.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 46);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_02_WeightType_Random_gxl_2processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 342);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_01_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 45);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_84_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.84_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 33);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_10_01_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 69);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_1_97_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 47);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 144);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_1_04_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_1.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 21);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 36);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 278);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 38);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_1_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 26);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_10_07_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_10.07_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 52);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_2_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 27);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 206);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_93_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.93_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 22);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_1_97_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 280);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_09_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.09_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_96_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.96_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 26);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_01_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 71);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 481);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_97_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 53);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_20_CCR_1_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 17);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 17);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 18);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_50_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.50_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 98);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_1_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 18);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_2_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_30_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 341);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_40_CCR_1_85_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.40_CCR_1.85_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_2_03_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_2.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_00_CCR_1_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.00_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 79);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_30_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 895);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 2680);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_99_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 274);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_10_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_2_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 126);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 494);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 48);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_98_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 55);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_10_03_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_10.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 76);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 358);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_01_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 47);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_98_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_10_02_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 62);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 476);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_97_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 51);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_10_04_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_10.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_2_05_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_2.05_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 49);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 467);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_00_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 37);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_99_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_9_97_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_9.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 59);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 450);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_01_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 77);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_97_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_9_98_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_9.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 46);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_0_10_WeightType_Random_gxl_4processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(4);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 178);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 262);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_01_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 44);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_84_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.84_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 33);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_10_01_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 69);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 167);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_0_99_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_0.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 20);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_1_97_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 47);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testIndependent_Nodes_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Independent_Nodes_10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 10);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 144);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_1_04_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_1.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 21);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 36);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 278);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 38);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 130);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_1_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 26);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_10_07_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_10.07_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 52);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_2_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 24);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 206);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_93_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.93_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 22);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_1_97_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 280);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_09_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.09_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_96_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.96_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 26);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_01_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 71);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 481);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_97_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 53);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_20_CCR_1_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 16);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 17);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 18);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_50_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.50_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 88);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_1_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 18);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_2_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_30_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 341);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_40_CCR_1_85_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.40_CCR_1.85_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_2_03_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_2.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_00_CCR_1_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.00_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 79);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_30_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 895);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 2680);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_99_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 274);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_10_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_2_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 126);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 494);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 48);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_98_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 55);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_10_03_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_10.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 76);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 358);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_01_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 47);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_98_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_10_02_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 62);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 476);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_97_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 51);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_10_04_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_10.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_2_05_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_2.05_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 49);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 467);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_00_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 37);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_99_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_9_97_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_9.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 59);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_0_10_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 450);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_01_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 77);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_97_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_9_98_WeightType_Random_gxl_8processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(8);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_9.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 46);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 262);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_01_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 44);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_1_84_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_1.84_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 33);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Join_Nodes_10_CCR_10_01_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Join_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 69);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 167);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_0_99_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_0.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 20);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_1_97_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testFork_Nodes_10_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Fork_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 47);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testIndependent_Nodes_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Independent_Nodes_10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 10);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 144);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_1_04_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_1.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 21);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Balanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Balanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 36);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 278);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 38);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 56);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testInTree_Unbalanced_MaxBf_3_Nodes_10_CCR_2_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_2.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 130);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_1_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 26);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_10_07_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_10.07_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 52);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testJoin_Nodes_10_CCR_2_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Join_Nodes_10_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 24);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 206);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_0_93_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.93_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 22);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_1_97_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Balanced_MaxBf_3_Nodes_10_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Balanced-MaxBf-3_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 280);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_09_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.09_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_1_96_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_1.96_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 26);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testOutTree_Unbalanced_MaxBf_3_Nodes_10_CCR_10_01_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/OutTree-Unbalanced-MaxBf-3_Nodes_10_CCR_10.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 71);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 481);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_1_97_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 53);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testPipeline_Nodes_10_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Pipeline_Nodes_10_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_20_CCR_1_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 16);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 17);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_40_CCR_10_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.40_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 18);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_50_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.50_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 88);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_1_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 18);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_0_60_CCR_2_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_0.60_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 35);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_30_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 341);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_40_CCR_1_85_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.40_CCR_1.85_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 32);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_1_50_CCR_2_03_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_1.50_CCR_2.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 34);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_00_CCR_1_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.00_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 79);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_2_30_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_2.30_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 895);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 2680);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_0_99_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_0.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 274);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_10_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_10.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 66);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testRandom_Nodes_10_Density_4_50_CCR_2_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Random_Nodes_10_Density_4.50_CCR_2.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 126);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 494);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 48);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_1_98_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 55);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_2_Nodes_10_CCR_10_03_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-2_Nodes_10_CCR_10.03_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 76);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 358);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_01_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 47);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_1_98_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_1.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_3_Nodes_10_CCR_10_02_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-3_Nodes_10_CCR_10.02_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 62);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 476);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_0_97_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_0.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 51);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_10_04_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_10.04_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 50);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_4_Nodes_10_CCR_2_05_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-4_Nodes_10_CCR_2.05_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 49);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 467);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_00_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.00_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 37);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_1_99_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_1.99_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testSeriesParallel_MaxBf_5_Nodes_10_CCR_9_97_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/SeriesParallel-MaxBf-5_Nodes_10_CCR_9.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 59);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_0_10_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_0.10_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 450);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_01_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.01_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 77);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_1_97_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_1.97_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 57);
    }

    /**
     * auto-generated using a python script
     * @author Michael Kemp, Mohan Cao
     */
    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void testStencil_Nodes_10_CCR_9_98_WeightType_Random_gxl_16processors() throws FileNotFoundException {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(16);
        Tuple<TestResultListener,FileParser> t = schedule(aManager, "./test_inputs/Stencil_Nodes_10_CCR_9.98_WeightType_Random.gxl", true);
        t.getB().startParsing(aManager);
        assertTrue(satisfiesDependencies(t.getA()));
        assertTrue(validStartTimeForTasks(t.getA()));
        assertEquals(aManager.getUpperBound(), 46);
    }

}
