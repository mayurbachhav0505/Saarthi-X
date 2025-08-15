package codecharioteers.apptracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the data model for a single application's usage.
 * This class uses JavaFX Properties so the TableView can automatically
 * update when the data changes.
 */
public class AppUsage {
    private final StringProperty appName;
    private final StringProperty formattedTime;

    public AppUsage(String appName, long totalSeconds) {
        this.appName = new SimpleStringProperty(appName);
        this.formattedTime = new SimpleStringProperty(formatDuration(totalSeconds));
    }

    public String getAppName() {
        return appName.get();
    }

    public StringProperty appNameProperty() {
        return appName;
    }

    public String getFormattedTime() {
        return formattedTime.get();
    }

    public StringProperty formattedTimeProperty() {
        return formattedTime;
    }

    /**
     * Helper method to convert total seconds into a HH:MM:SS format.
     * @param totalSeconds The total seconds of usage.
     * @return A formatted string like "01:23:45".
     */
    private String formatDuration(long totalSeconds) {
        if (totalSeconds < 0) {
            totalSeconds = 0;
        }
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}