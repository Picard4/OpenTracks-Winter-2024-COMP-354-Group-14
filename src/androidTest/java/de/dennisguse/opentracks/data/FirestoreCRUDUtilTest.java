package de.dennisguse.opentracks.data;

import org.junit.Test;
import com.google.gson.JsonObject;
import de.dennisguse.opentracks.data.interfaces.ActionCallback;
import de.dennisguse.opentracks.data.interfaces.ReadCallback;
import de.dennisguse.opentracks.data.models.CRUDConstants;
import static org.mockito.Mockito.*;


/**
 * Tests for FirestoreCRUDUtil class
 */
public class FirestoreCRUDUtilTest {

    final String trackId = "track_01";

    /**
     * Expect SUCCESSFUL createEntry as the proper id and valid jsonData are passed
     */
    @Test
    public void testCreateEntry() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ActionCallback callback = mock(ActionCallback.class);

        JsonObject jsonData = generateMockData();
        doAnswer(invocation -> {
            // get the 4th param in the `createEntry` method
            ((ActionCallback) invocation.getArgument(3)).onSuccess();
            return null;
        }).when(firestoreCRUDUtil).createEntry(anyString(), anyString(), any(JsonObject.class), any(ActionCallback.class));
        firestoreCRUDUtil.createEntry(CRUDConstants.RUNS_TABLE, trackId, jsonData, callback);

        verify(callback).onSuccess();
    }

    /**
     * Expect SUCCESSFUL updateEntry as the proper id and valid jsonData are passed
     */
    @Test
    public void testUpdateEntry() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ActionCallback callback = mock(ActionCallback.class);

        JsonObject jsonData = generateMockData();
        doAnswer(invocation -> {
            // get the 4th param in the `updateEntry` method
            ((ActionCallback) invocation.getArgument(3)).onSuccess();
            return null;
        }).when(firestoreCRUDUtil).updateEntry(anyString(), anyString(), any(JsonObject.class), any(ActionCallback.class));
        firestoreCRUDUtil.updateEntry(CRUDConstants.RUNS_TABLE, trackId, jsonData, callback);

        verify(callback).onSuccess();
    }
    /**
     * Expect SUCCESSFUL deleteEntry as the proper id is passed
     */
    @Test
    public void testDeleteEntry() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ActionCallback callback = mock(ActionCallback.class);

        JsonObject jsonData = generateMockData();
        doAnswer(invocation -> {
            // get the 3rd param in the `deleteEntry` method
            ((ActionCallback) invocation.getArgument(2)).onSuccess();
            return null;
        }).when(firestoreCRUDUtil).deleteEntry(anyString(), anyString(), any(ActionCallback.class));
        firestoreCRUDUtil.deleteEntry(CRUDConstants.RUNS_TABLE, trackId, callback);

        verify(callback).onSuccess();
    }

    /**
     * Expect SUCCESSFUL getEntry as the proper id is passed
     */
    @Test
    public void testGetEntry() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ReadCallback callback = mock(ReadCallback.class);

        JsonObject jsonData = generateMockData();
        doAnswer(invocation -> {
            // get the 3rd param in the `getEntry` method
            ((ActionCallback) invocation.getArgument(2)).onSuccess();
            return null;
        }).when(firestoreCRUDUtil).getEntry(anyString(), anyString(), any(ReadCallback.class));
        firestoreCRUDUtil.getEntry(CRUDConstants.RUNS_TABLE, trackId, callback);

        verify(callback).onSuccess(jsonData);
    }

    /**
     * Generates mock data for testing purposes
     * @return JsonObject with mock data
     */
    public static JsonObject generateMockData() {
        JsonObject mockData = new JsonObject();

        JsonObject idObject = new JsonObject();
        idObject.addProperty("id", 42);
        idObject.addProperty("name", "2024-04-04T12:12:12");

        JsonObject altitudeExtremitiesObject = new JsonObject();
        altitudeExtremitiesObject.addProperty("max", "-Infinity");
        altitudeExtremitiesObject.addProperty("min", "Infinity");

        JsonObject maxSpeedObject = new JsonObject();
        maxSpeedObject.addProperty("speed_mps", 0);

        JsonObject movingTimeObject = new JsonObject();
        movingTimeObject.addProperty("skiing", false);
        movingTimeObject.addProperty("slopePercentageSeason", 0);

        JsonObject totalDistanceObject = new JsonObject();
        totalDistanceObject.addProperty("distance_m", 0);
        totalDistanceObject.addProperty("totalRunsSeason", 0);
        totalDistanceObject.addProperty("totalSkiDaysSeason", 0);

        JsonObject totalTimeObject = new JsonObject();
        totalTimeObject.addProperty("waiting", false);

        JsonObject trackStatisticsObject = new JsonObject();
        trackStatisticsObject.add("altitudeExtremities", altitudeExtremitiesObject);
        trackStatisticsObject.addProperty("chairlift", false);
        trackStatisticsObject.addProperty("isIdle", false);
        trackStatisticsObject.add("maxSpeed", maxSpeedObject);
        trackStatisticsObject.add("movingTime", movingTimeObject);
        trackStatisticsObject.add("startTime", new JsonObject());
        trackStatisticsObject.add("stopTime", new JsonObject());
        trackStatisticsObject.add("totalDistance", totalDistanceObject);
        trackStatisticsObject.add("totalTime", totalTimeObject);
        trackStatisticsObject.addProperty("uuid", "test");

        mockData.addProperty("activityType", "SKI");
        mockData.addProperty("activityTypeLocalized", "unknown");
        mockData.addProperty("description", "Updated track");
        mockData.addProperty("externalId", "track_01");
        mockData.add("id", idObject);
        mockData.add("trackStatistics", trackStatisticsObject);

        return mockData;
    }
}



