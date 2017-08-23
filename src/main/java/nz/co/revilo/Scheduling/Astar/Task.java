package nz.co.revilo.Scheduling.Astar;


public class Task implements Cloneable {
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
}
