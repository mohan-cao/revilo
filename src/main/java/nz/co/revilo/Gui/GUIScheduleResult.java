package nz.co.revilo.Gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Helper class to notify the GUI that values have been changed with change listeners.
 * @author Terran Kroft
 */
public class GUIScheduleResult {
    private BooleanProperty isDoneProcessing = new SimpleBooleanProperty();

    /**
     * Setter for isDoneProcessing for change listeners
     * @param isDoneProcessing boolean
     */
    public final void setIsDoneProcessing(boolean isDoneProcessing) {
        this.isDoneProcessing.set(isDoneProcessing);
    }

    /**
     * Getter for isDoneProcessing for change listeners
     * @return isDoneProcessing
     */
    public BooleanProperty isDoneProcessingProperty() {
        return isDoneProcessing;
    }
}
