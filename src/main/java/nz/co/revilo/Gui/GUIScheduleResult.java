package nz.co.revilo.Gui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;

/**
 * Helper class to notify the GUI that values have been changed with change listeners.
 * @author Terran Kroft
 */
public class GUIScheduleResult {
    private BooleanProperty isDoneProcessing = new SimpleBooleanProperty();



    public final boolean getIsDoneProcessing() {
        return isDoneProcessing.get();
    }

    public final void setIsDoneProcessing(boolean isDoneProcessing) {
        this.isDoneProcessing.set(isDoneProcessing);
    }

    public BooleanProperty isDoneProcessingProperty() {
        return isDoneProcessing;
    }



}
