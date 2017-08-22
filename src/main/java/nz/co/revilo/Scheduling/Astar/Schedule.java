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

    private Schedule(Set<Task> scheduled, Set<Task> schedulable, Set<Task> unschedulable) {
        _scheduled = new HashSet<>(scheduled);
        _schedulable = new HashSet<>(schedulable);
        _unschedulable = new HashSet<>(unschedulable);
    }

    public Schedule clone() {
        return new Schedule(_scheduled, _schedulable, _unschedulable);
    }
}
