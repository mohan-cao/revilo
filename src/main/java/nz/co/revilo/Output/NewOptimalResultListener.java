package nz.co.revilo.Output;

import nz.co.revilo.Scheduling.BnBSchedule;

public interface NewOptimalResultListener {

    void newOptimal(BnBSchedule optimal);

}
