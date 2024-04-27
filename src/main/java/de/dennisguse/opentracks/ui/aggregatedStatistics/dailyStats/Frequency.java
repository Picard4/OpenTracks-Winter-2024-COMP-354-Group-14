package de.dennisguse.opentracks.ui.aggregatedStatistics.dailyStats;

import androidx.annotation.NonNull;

/**
 * An enum with all data points frequency available in the visualize daily statistics feature.
 */
public enum Frequency {
    THREE(0,3),
    FIVE(1,5);

    private final int value;
    private final int id;

    Frequency(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
    public int getId() {
        return this.id;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Frequency findById(int id){
        for (Frequency freq : Frequency.values()){
            if (freq.getId() == id){
                return freq;
            }
        }
        return null;
    }
}