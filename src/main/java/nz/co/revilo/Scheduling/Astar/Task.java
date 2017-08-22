package nz.co.revilo.Scheduling.Astar;


public class Task implements Cloneable {
    public int _start = -1;
    public int _processor = -1;

    private Task(int start, int processor) {
        _start = start;
        _processor = processor;
    }

    public Task clone() {
        return new Task(_start, _processor);
    }
}
