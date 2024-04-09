package de.dennisguse.opentracks.ui.aggregatedStatistics.dailyStats;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FrequencyTest {
    @Test
    public void enumFound(){
        Frequency freqFound = Frequency.findById(1);
        assertEquals(Frequency.FIVE, freqFound);
    }
    @Test
    public void enumNotFound(){
        Frequency freqFound = Frequency.findById(2);
        assertNull(null, freqFound);
    }
}