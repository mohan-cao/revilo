package nz.co.revilo.Output;

import nz.co.revilo.Scheduling.AlgorithmManager;

import java.io.BufferedOutputStream;

public abstract class DotFileProducer implements ScheduleResultListener {

    private String _outputFilename;
    private AlgorithmManager _manager;

    public void finalSchedule() {
        //TODO take argument of data structure and produce output
        produceOutput(new BufferedOutputStream(System.out));
    }

    public DotFileProducer(String outputFilename) {
        _outputFilename = outputFilename;
    }

    protected String getOutputFilename() {
        return _outputFilename;
    }

    protected abstract void produceOutput(BufferedOutputStream output);
}
