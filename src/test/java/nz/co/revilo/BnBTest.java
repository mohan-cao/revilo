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

    @Test(timeout=30000)
    @Category(SlowTest.class)
    public synchronized void ForkGraphs2Processors(){
        try {
            List<Path> files = Files.find(Paths.get(""),
                    Integer.MAX_VALUE,
                    (path, basicFileAttributes) -> path.toFile().getName().matches("Fork.*_Nodes_10_.*_WeightType_Random\\.gxl")
            ).collect(Collectors.toList());
            for(Path p : files){
                System.out.println(p.toString());
                String file = p.getFileName().toString();
                AlgorithmManager aManager = new BranchAndBoundAlgorithmManager(2);
                Tuple<TestResultListener,FileParser> t = schedule(aManager,p.toString(),true);
                t.getB().startParsing(aManager);
                assertTrue(satisfiesDependencies(t.getA()));
                assertTrue(validStartTimeForTasks(t.getA()));
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
