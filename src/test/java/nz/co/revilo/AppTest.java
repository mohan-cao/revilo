package nz.co.revilo;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests Application interface
 */
public class AppTest {
    public static String TEST_PATH = "test_inputs/";

    @AfterClass
    public static void cleanup(){
        new File(TEST_PATH + "input-output.dot").delete();
    }

    /**
     * Test for no valid params
     */
    @Test
    public void AppNoParams() {
        try{
            App.main(new String[]{});
        }catch(RuntimeException re){
            assertEquals(re.getMessage(),"Insufficient arguments given. Needs [input file] [# processors]");
        }
    }

    /**
     * Test that a valid file was given to the application
     */
    @Test
    public void AppFileError() {
        try{
            App.main(new String[]{TEST_PATH + "asdfghjkl-nice.dot","1"});
        }catch(RuntimeException re){
            assertEquals(re.getMessage(),"Input file does not exist");
        }
    }

    /**
     * Test that a valid file was given to the application
     */
    @Test
    public void AppProcNumError() {
        try{
            App.main(new String[]{TEST_PATH + "input.dot","yes"});
        }catch(RuntimeException re){
            assertEquals(re.getMessage(),"Invalid number of processors");
        }
    }
    
    /**
     * TODO: test passing in 0 or negative number of processors
     */

    /**
     * SMOKE TEST. Runs automatically with Maven build. Disable with -DexcludedGroups=SlowTest.class
     */
    @Test
    @Category(SlowTest.class)
    public void AppSmokeTest(){
        App.main(new String[]{TEST_PATH + "input.dot","1"});
    }

    @Test
    @Category(SlowTest.class)
    public void AppConcurrencyTest() {
        IntStream.range(0,20).parallel().forEach(i->{
            App.main(new String[]{TEST_PATH + "Random_Nodes_10_Density_0.20_CCR_1.00_WeightType_Random.gxl","4","-p","4"});
        });
    }
}
