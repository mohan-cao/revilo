package nz.co.revilo.Scheduling.Astar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Schedule implements Cloneable {
    public Set<Task> _scheduled = new HashSet<>();
    public Set<Task> _schedulable = new HashSet<>();
    public Set<Task> _unschedulable = new HashSet<>();
    public List<Schedule> _subSchedules = new ArrayList<>();
    public ArrayList<Integer> _processorLastUsed = new ArrayList<>();

    public Schedule() {
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

    private Schedule(Set<Task> scheduled, Set<Task> schedulable, Set<Task> unschedulable, List<Integer> processorLastUsed) {
        _scheduled = new HashSet<>(scheduled);
        _schedulable = new HashSet<>(schedulable);
        _unschedulable = new HashSet<>(unschedulable);
        _processorLastUsed = new ArrayList<>(processorLastUsed);
    }

    public Schedule clone() {
        return new Schedule(_scheduled, _schedulable, _unschedulable, _processorLastUsed);
    }
}
