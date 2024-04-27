package de.dennisguse.opentracks.ui.aggregatedStatistics.dailyStats;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MetricTest {

    @Test
    public void findById_AvgSpeed_EnumFound() {
        assertEquals(Metric.AVG_SPEED, Metric.findById(0));
    }

    @Test
    public void findById_AvgSlope_EnumFound() {
        assertEquals(Metric.AVG_SLOPE, Metric.findById(1));
    }

    @Test
    public void findById_ChairliftSpeed_EnumFound() {
        assertEquals(Metric.CHAIRLIFT_SPEED, Metric.findById(2));
    }

    @Test
    public void findById_TotalDistance_EnumFound() {
        assertEquals(Metric.TOTAL_DISTANCE, Metric.findById(3));
    }

    @Test
    public void findById_EnumNotFound() {
        assertNull(Metric.findById(99)); // Using a non-existing ID
    }
}
