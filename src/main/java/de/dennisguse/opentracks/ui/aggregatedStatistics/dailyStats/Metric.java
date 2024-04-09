package de.dennisguse.opentracks.ui.aggregatedStatistics.dailyStats;

import androidx.annotation.NonNull;

/**
 * An enum of all metrics available in the visualize daily statistics feature.
 */
public enum Metric {
    AVG_SPEED(0, "Avg Speed"),
    AVG_SLOPE(1, "Avg Slope"),
    CHAIRLIFT_SPEED(2, "Chairlift Speed"),
    TOTAL_DISTANCE(3, "Total Distance");

    private final int id;
    private final String displayName;

    Metric(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public static Metric findById(int id) {
        for (Metric metric : Metric.values()) {
            if (metric.id == id) {
                return metric;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }
}
