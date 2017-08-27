package nz.co.revilo.Scheduling.Astar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Basic representation of an Astar schedule
 *
 * @author Michael Kemp
 */
public class AstarSchedule implements Cloneable {
    public Set<AstarTask> _scheduled = new HashSet<>();
    public Set<AstarTask> _schedulable = new HashSet<>();
    public Set<AstarTask> _unschedulable = new HashSet<>();
    public List<AstarSchedule> _subSchedules = new ArrayList<>();
    public ArrayList<Integer> _processorLastUsed = new ArrayList<>();

    public AstarSchedule() {
    }

    private AstarSchedule(Set<AstarTask> scheduled, Set<AstarTask> schedulable, Set<AstarTask> unschedulable, List<Integer> processorLastUsed) {
        _scheduled = new HashSet<>(scheduled);
        _schedulable = new HashSet<>(schedulable);
        _unschedulable = new HashSet<>(unschedulable);
        _processorLastUsed = new ArrayList<>(processorLastUsed);
    }

    /**
     * Calculates the cost of the schedule so far
     *
     * @return cost of the schedule
     */
    public int cost() {
        int cost = 0;
        for (Integer processorCost : _processorLastUsed) {
            if (processorCost > cost) {
                cost = processorCost;
            }
        }
        return cost;
    }

    /**
     * Clones the schedule to make a child schedule
     * @return schedule clone
     */
    public AstarSchedule clone() {
        return new AstarSchedule(_scheduled, _schedulable, _unschedulable, _processorLastUsed);
    }
}