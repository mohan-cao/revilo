package nz.co.revilo.Scheduling.Astar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AstarSchedule implements Cloneable {
    public Set<Task> _scheduled = new HashSet<>();
    public Set<Task> _schedulable = new HashSet<>();
    public Set<Task> _unschedulable = new HashSet<>();
    public List<AstarSchedule> _subSchedules = new ArrayList<>();
    public ArrayList<Integer> _processorLastUsed = new ArrayList<>();

    public AstarSchedule() {
    }

    private AstarSchedule(Set<Task> scheduled, Set<Task> schedulable, Set<Task> unschedulable, List<Integer> processorLastUsed) {
        _scheduled = new HashSet<>(scheduled);
        _schedulable = new HashSet<>(schedulable);
        _unschedulable = new HashSet<>(unschedulable);
        _processorLastUsed = new ArrayList<>(processorLastUsed);
    }

    public int cost() {
        int cost = 0;
        for (Integer processorCost : _processorLastUsed) {
            if (processorCost > cost) {
                cost = processorCost;
            }
        }
        return cost;
    }

    public AstarSchedule clone() {
        return new AstarSchedule(_scheduled, _schedulable, _unschedulable, _processorLastUsed);
    }
}