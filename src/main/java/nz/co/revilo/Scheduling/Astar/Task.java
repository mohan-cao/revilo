package nz.co.revilo.Scheduling.Astar;


public class Task implements Cloneable, Comparable<Task> {
    public int _taskNum = -1;
    public int _start = -1;
    public int _processor = -1;

    public Task(int taskNum) {
        _taskNum = taskNum;
    }

    private Task(int start, int processor, int taskNum) {
        _start = start;
        _processor = processor;
        _taskNum = taskNum;
    }

    public Task clone() {
        return new Task(_start, _processor, _taskNum);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            return ((Task) obj)._taskNum == _taskNum;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return _taskNum;
    }

    @Override
    public int compareTo(Task o) {
        return Integer.compare(_taskNum, o._taskNum);
    }
}
