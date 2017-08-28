package nz.co.revilo.Output;

import nz.co.revilo.Scheduling.BnBSchedule;

/**
 * Listens for new optimal result
 * @author Terran Kroft
 */
public interface NewOptimalResultListener {
    /**
     * Called when a new optimal is available
     * @param optimal
     * @author Terran Kroft
     */
    void newOptimal(BnBSchedule optimal);
}
