//-- ParaTask related imports//####[-1]####
import pt.runtime.*;//####[-1]####
import java.util.concurrent.ExecutionException;//####[-1]####
import java.util.concurrent.locks.*;//####[-1]####
import java.lang.reflect.*;//####[-1]####
import pt.runtime.GuiThread;//####[-1]####
import java.util.concurrent.BlockingQueue;//####[-1]####
import java.util.ArrayList;//####[-1]####
import java.util.List;//####[-1]####
//####[-1]####
public class testPTJava {//####[1]####
    static{ParaTask.init();}//####[1]####
    /*  ParaTask helper method to access private/protected slots *///####[1]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[1]####
        if (m.getParameterTypes().length == 0)//####[1]####
            m.invoke(instance);//####[1]####
        else if ((m.getParameterTypes().length == 1))//####[1]####
            m.invoke(instance, arg);//####[1]####
        else //####[1]####
            m.invoke(instance, arg, interResult);//####[1]####
    }//####[1]####
}//####[1]####
