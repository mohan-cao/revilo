package nz.co.revilo.Scheduling;

import java.util.HashSet;
import java.util.Set;

public class AstartFirstAlgorithmManager extends AlgorithmManager {
    public AstartFirstAlgorithmManager(int processingCores) {
        super(processingCores);
    }

    @Override
    protected void execute() {
        Set<Integer> sechduled = new HashSet<>();
        Set<Integer> toSchedule = new HashSet<>();
        Set<Integer> schedulable = new HashSet<>();

    }
}
