package nz.co.revilo;

import junit.framework.TestResult;
import nz.co.revilo.Scheduling.ImprovedTopologicalAlgorithmManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
     * Tests against the simple diamond DAG
     * @author Mohan Cao
     */
    @Test
    public void testSimpleDiamondDAG(){
        TestResultListener t = schedule(AppTest.TEST_PATH + "input.dot");
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
        TestResultListener t = schedule(AppTest.TEST_PATH + "input1.dot");
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
        TestResultListener t = schedule(AppTest.TEST_PATH + "input2.dot");
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
     * 
     * TODO: Test accepted output currently doesn't cover all correct output
     * @author Abby S : uncomment @Test annotation when fixed
     */
//    @Test
    public void testSmallBranchingDAG(){
        TestResultListener t = schedule(AppTest.TEST_PATH + "input3.dot");
        assertTrue(satisfiesDependencies(t));
        assertTrue(validStartTimeForTasks(t));
        for(TestResultListener.Node n : t.getNodes()){
            String s = n.toString();
            int starttime = n.getStartTime();
            int weight = n.getWeight();
            if(s.equals("0")){
                assertEquals(starttime,0);
                assertEquals(weight,4);
            }else if(s.equals("1")||s.equals("2")||s.equals("3")){ //TODO
                assertTrue(starttime==4||starttime==6||starttime==8);
                assertEquals(weight,2);
            }else if(s.equals("4")||s.equals("5")){ //TODO
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
        TestResultListener t = schedule(AppTest.TEST_PATH + "input4.dot");
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
