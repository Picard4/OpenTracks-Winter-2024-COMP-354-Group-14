package de.dennisguse.opentracks.data.models;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/*
\created a chairliftest to see whenever it would work, or if there is something to debug in the code.
 */
public class ChairliftTest {
    @Test
    public void testCalculateTotalDistance() {
        Chairlift chairlift = new Chairlift("Test Lift", 1, 0, 1, 0, 0, 0, 0, 0);

        //Create a list of track points
        List<TrackPoint> trackPoints = new ArrayList<>();

        trackPoints.add(new TrackPoint(TrackPoint.Type.TRACKPOINT, Instant.now())); // Add track points as needed

        double expectedTotalDistance = 100;


        chairlift.updateMetrics(trackPoints);
        double actualTotalDistance = chairlift.getTotalDistance();

        assertEquals(expectedTotalDistance, actualTotalDistance, 0.01);
    }

}