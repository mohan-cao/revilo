package nz.co.revilo.Output;

import nz.co.revilo.Scheduling.BnBSchedule;

/**
 * @author Terran Kroft
 */
public interface NewOptimalResultListener {

    /**
     * @param optimal
     * @author Terran Kroft
     */
    public void newOptimal(BnBSchedule optimal);

}
