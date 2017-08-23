package nz.co.revilo.Scheduling;

import javafx.event.Event;
import javafx.event.EventType;

public class AlgorithmUpdate extends Event {
    public AlgorithmUpdate(EventType<? extends Event> eventType) {
        super(eventType);
    }

}
