package nz.co.revilo;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for edge cases of algorithm
 * 
 * TODO: make input.dor
 * 
 * @author Abby S
 *
 */
public class AppTest {

    /**
<<<<<<< HEAD
     * Sample test
=======
     * Test for no valid params
>>>>>>> 96afc36d9ef039b6c6b52d871b145781e12990fc
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
     * Serial
     */
    
    
    /**
     * One on each processor
     */
    
    
    /**
     * Even split
     */
    
    
    /**
     * No gaps
     */


    /**
     * Test that a valid file was given to the application
     */
    @Test
    public void AppFileError() {
        try{
            App.main(new String[]{"asdfghjkl-nice.dot","1"});
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
            App.main(new String[]{"input.dot","yes"});
        }catch(RuntimeException re){
            assertEquals(re.getMessage(),"Invalid number of processors");
        }
    }

    /**
     * SMOKE TEST. Runs automatically with Maven build. Disable with -DexcludedGroups=SlowTest.class
     */
    @Test
    @Category(SlowTest.class)
    public void AppSmokeTest(){
        App.main(new String[]{"input.dot","1"});
    }
}
