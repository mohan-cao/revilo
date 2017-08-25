package nz.co.revilo;

import nz.co.revilo.Scheduling.AlgorithmManager;
import nz.co.revilo.Scheduling.Topological.ImprovedTopologicalAlgorithmManager;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test cases designed for use with the topological sort implementation(s) of AlgorithmManager. These tests only ensure
 * that schedules are valid, NOT optimal. This is done by ensuring all dependencies are satisfied (i.e. arcs correctly
 * acknowledged) in scheduling, and that is no more than one process running on a processor at once.
 * @author Aimee, Mohan Cao
 */
public class TopologicalTest extends ValidityTest {

    /**
     * Tests against the simple diamond DAG
     * @author Mohan Cao
     */
    @Test
    public void testSimpleDiamondDAG(){
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        for(TestResultListener.Node n : t.getNodes()){
            String s = n.toString();
            int starttime = n.getStartTime();
            int weight = n.getWeight();
            if(s.equals("a")){
                assertEquals(starttime,0);
                assertEquals(weight,2);
            }else if(s.equals("b")){
                assertTrue(starttime==2||starttime==3);
                assertEquals(weight,3);
            }else if(s.equals("c")){
                assertTrue(starttime==2||starttime==5);
                assertEquals(weight,1);
            }else if(s.equals("d")){
                assertEquals(starttime,6);
                assertEquals(weight,4);
            }
        }
    }


    /**
     * Tests against a linearly dependent DAG
     */
    @Test
    public void testLinearDAG(){
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input1.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        for(TestResultListener.Node n : t.getNodes()){
            String s = n.toString();
            int starttime = n.getStartTime();
            int weight = n.getWeight();
            if(s.equals("0")){
                assertEquals(starttime,0);
                assertEquals(weight,7);
            }else if(s.equals("1")){
                assertEquals(starttime,7);
                assertEquals(weight,3);
            }else if(s.equals("2")){
                assertEquals(starttime,10);
                assertEquals(weight,5);
            }else if(s.equals("3")){
                assertEquals(starttime,15);
                assertEquals(weight,6);
            }else if(s.equals("4")){
                assertEquals(starttime,21);
                assertEquals(weight,4);
            }
        }
    }

    /**
     * Tests against a linearly dependent (with some branching) DAG
     */
    @Test
    public void testLinearDAG2(){
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input2.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        for(TestResultListener.Node n : t.getNodes()){
            String s = n.toString();
            int starttime = n.getStartTime();
            int weight = n.getWeight();
            if(s.equals("0")){
                assertEquals(starttime,0);
                assertEquals(weight,7);
            }else if(s.equals("1")){
                assertEquals(starttime,7);
                assertEquals(weight,3);
            }else if(s.equals("2")){
                assertEquals(starttime,10);
                assertEquals(weight,5);
            }else if(s.equals("3")){
                assertEquals(starttime,15);
                assertEquals(weight,6);
            }else if(s.equals("4")){
                assertEquals(starttime,21);
                assertEquals(weight,4);
            }
        }
    }


    /**
     * Tests against a small branching DAG with arbitrary branching
     * Deprecated. Does not fully work in all cases.
     *
     * @author Abby S
     */
    @Test
    @Ignore
    @Deprecated
    public void testSmallBranchingDAG(){
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input3.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        for(TestResultListener.Node n : t.getNodes()){
            String s = n.toString();
            int starttime = n.getStartTime();
            int weight = n.getWeight();
            if(s.equals("0")){
                assertEquals(starttime,0);
                assertEquals(weight,4);
            }else if(s.equals("1")||s.equals("2")||s.equals("3")){
                assertTrue(starttime==4||starttime==6||starttime==8);
                assertEquals(weight,2);
            }else if(s.equals("4")||s.equals("5")){
                assertTrue(starttime==10||starttime==15);
                assertEquals(weight,5);
            }else if(s.equals("6")){
                assertEquals(starttime,20);
                assertEquals(weight,10);
            }
        }
    }

    /**
     * Tests against a simple fanning DAG
     */
    @Test
    public void testSimpleFanningDAG(){
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "input4.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        for(TestResultListener.Node n : t.getNodes()){
            String s = n.toString();
            int starttime = n.getStartTime();
            int weight = n.getWeight();
            if(s.equals("0")){
                assertEquals(starttime,0);
                assertEquals(weight,5);
            }else if(s.equals("1")||s.equals("2")||s.equals("3")||s.equals("4")){
                assertTrue(starttime==5||starttime==10||starttime==15||starttime==20);
                assertEquals(weight,5);
            }
        }
    }

    /**
     * Tests if the 10-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test10NodesRandom() {
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_10_Random.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }

    /**
     * Tests if the 7-node out tree input schedule satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test7NodeOutTree() {
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_7_OutTree.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }

    /**
     * Tests if the 8-node random input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test8NodeRandom() {
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_8_Random.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }

    /**
     * Tests if the 9-node series parallel input satisfies the constraints outlines (dependencies, timing)
     */
    @Test
    public void test9NodeSeriesParallel() {
        AlgorithmManager aManager = new ImprovedTopologicalAlgorithmManager(1);
        TestResultListener t = schedule(aManager,AppTest.TEST_PATH + "Nodes_9_SeriesParallel.dot",false);
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
    }
}
