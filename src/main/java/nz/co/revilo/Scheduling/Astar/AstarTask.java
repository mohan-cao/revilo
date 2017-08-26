package nz.co.revilo.Scheduling.Astar;


public class AstarTask implements Cloneable, Comparable<AstarTask> {
    public int _taskNum = -1;
    public int _start = -1;
    public int _processor = -1;

    public AstarTask(int taskNum) {
        _taskNum = taskNum;
    }

    private AstarTask(int start, int processor, int taskNum) {
        _start = start;
        _processor = processor;
        _taskNum = taskNum;
    }

    public AstarTask clone() {
        return new AstarTask(_start, _processor, _taskNum);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstarTask) {
            return ((AstarTask) obj)._taskNum == _taskNum;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return _taskNum;
    }

    @Override
    public int compareTo(AstarTask o) {
        return Integer.compare(_taskNum, o._taskNum);
    }
}
