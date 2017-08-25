package nz.co.revilo;

import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.BranchAndBoundAlgorithmManager;
import org.junit.Test;
import org.junit.experimental.categories.Category;

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
    public void testSimpleDiamondDAG(){
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),10);
        aManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t2 = schedule(aManager,AppTest.TEST_PATH + "input.dot",true);
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(aManager.getUpperBound(),10);
    }


    /**
     * Tests against a linearly dependent DAG
     */
    @Test
    public void testLinearDAG(){
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input1.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),25);
    }

    /**
     * Tests against a linearly dependent (with some branching) DAG
     */
    @Test
    public void testLinearDAG2(){
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input2.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),25);
    }

    /**
     * Tests against a simple fanning DAG
     */
    @Test
    public void testSimpleFanningDAG(){
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input4.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),25);
    }

    /**
     * Tests if the 10-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test10NodesRandom() {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_10_Random.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),50);
        aManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = schedule(aManager,AppTest.TEST_PATH + "Nodes_10_Random.dot",true);
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(aManager.getUpperBound(),50);
    }

    /**
     * Tests if the 7-node out tree input schedule satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test7NodeOutTree() {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_7_OutTree.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),28);
        aManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = schedule(aManager,AppTest.TEST_PATH + "Nodes_7_OutTree.dot",true);
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(aManager.getUpperBound(),22);
    }

    /**
     * Tests if the 8-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test8NodeRandom() {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_8_Random.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),581);
        aManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = schedule(aManager,AppTest.TEST_PATH + "Nodes_8_Random.dot",true);
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(aManager.getUpperBound(),581);
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test9NodeSeriesParallel() {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_9_SeriesParallel.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),55);
        aManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = schedule(aManager,AppTest.TEST_PATH + "Nodes_9_SeriesParallel.dot",true);
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(aManager.getUpperBound(),55);
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    @Category(SlowTest.class)
    public void test11NodeSeriesParallel() {
        AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_11_OutTree.dot",true);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        assertEquals(aManager.getUpperBound(),350);
        aManager = new BranchAndBoundAlgorithmManager(4);
        TestResultListener t2 = schedule(aManager,AppTest.TEST_PATH + "Nodes_11_OutTree.dot",true);
        assertTrue(satisfiesDependencies(t2));
        assertTrue(validStartTimeForTasks(t2));
        assertEquals(aManager.getUpperBound(),227);
    }

    @Test(timeout=30000)
    @Category(SlowTest.class)
    public void ForkGraphs2Processors(){
        try {
            List<Path> files = Files.find(Paths.get(""),
                    Integer.MAX_VALUE,
                    (path, basicFileAttributes) -> path.toFile().getName().matches("Fork.*_Nodes_10_.*_WeightType_Random\\.gxl")
            ).collect(Collectors.toList());
            for(Path p : files){
                System.out.println(p.toString());
                String file = p.getFileName().toString();
                AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
                TestResultListener t = schedule(aManager,".\\"+p.toString(),true);
                assertTrue(satisfiesDependencies(t));
                assertTrue(validStartTimeForTasks(t));
                if(file.contains("Join")) { //Fork join nodes
                    if (file.contains("CCR_0.10")) {
                        assertEquals(aManager.getUpperBound(), 499);
                    } else if (file.contains("CCR_1.01")) {
                        assertEquals(aManager.getUpperBound(), 59);
                    } else if (file.contains("CCR_1.84")) {
                        assertEquals(aManager.getUpperBound(), 38);
                    } else if (file.contains("CCR_10.01")) {
                        assertEquals(aManager.getUpperBound(), 69);
                    }
                }else{ //Just fork nodes
                    if (file.contains("CCR_0.10")) {
                        assertEquals(aManager.getUpperBound(), 300);
                    } else if (file.contains("CCR_0.99")) {
                        assertEquals(aManager.getUpperBound(), 39);
                    } else if (file.contains("CCR_1.97")) {
                        assertEquals(aManager.getUpperBound(), 45);
                    } else if (file.contains("CCR_10.00")) {
                        assertEquals(aManager.getUpperBound(), 47);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
