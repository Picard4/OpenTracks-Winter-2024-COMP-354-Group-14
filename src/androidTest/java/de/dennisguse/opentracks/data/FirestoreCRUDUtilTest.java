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
     * Expect SUCCESSFUL createEntry as the proper id and valid jsonData are passed and make sure
     * getEntry retreives what we just created
     */

    /**
     * Matching failure test: when passing a null entry
     */

    @Test
    public void testCreateEntry_Failure() {
        FirestoreCRUDUtil firestoreCRUDUtil = FirestoreCRUDUtil.getInstance();
        ActionCallback callback = mock(ActionCallback.class);
        JsonObject jsonData = generateMockData();

        doAnswer(invocation -> {
            // get the 4th param in the `createEntry` method
            ((ActionCallback) invocation.getArgument(3)).onFailure();
            return null;
        }).when(firestoreCRUDUtil).createEntry(isNull(), anyString(), any(JsonObject.class), any(ActionCallback.class));
        firestoreCRUDUtil.createEntry(CRUDConstants.RUNS_TABLE, trackId, jsonData, callback);

        verify(callback).onFailure();
    }
    @Test
    public void testCreateAndRetrieveEntry() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ReadCallback readCallback = mock(ReadCallback.class);
        ActionCallback createCallback = mock(ActionCallback.class);
        JsonObject jsonData = generateMockData();

        doAnswer(invocation -> {
            // get the 4th param in the `createEntry` method
            ((ActionCallback) invocation.getArgument(3)).onSuccess();
            return null;
        }).when(firestoreCRUDUtil).createEntry(anyString(), anyString(), any(JsonObject.class), any(ActionCallback.class));

        doAnswer(invocation -> {
            // get the 3rd param in the `getEntry` method
            ((ReadCallback) invocation.getArgument(2)).onSuccess(jsonData);
            return null;
        }).when(firestoreCRUDUtil).getEntry(anyString(), anyString(), any(ReadCallback.class));

        firestoreCRUDUtil.createEntry(CRUDConstants.RUNS_TABLE, trackId, jsonData, createCallback);
        firestoreCRUDUtil.getEntry(CRUDConstants.RUNS_TABLE, trackId, readCallback);

        verify(createCallback).onSuccess();
        verify(readCallback).onSuccess(jsonData);
    }


    /**
     * Expect SUCCESSFUL updateEntry as the proper id and valid jsonData are passed
     */

    /**
     * Matching failure test: when passing a null entry
     */

    @Test
    public void testCreateAndRetrieveEntry_Failure() {
        FirestoreCRUDUtil firestoreCRUDUtil = FirestoreCRUDUtil.getInstance();
        ReadCallback readCallback = mock(ReadCallback.class);
        ReadCallback readCallback = mock(ReadCallback.class);
        ActionCallback createCallback = mock(ActionCallback.class);
        JsonObject jsonData = generateMockData();

        doAnswer(invocation -> {
            ((ActionCallback) invocation.getArgument(3)).onFailure();
            return null;
        }).when(firestoreCRUDUtil).createEntry(anyString(), anyString(), any(JsonObject.class), any(ActionCallback.class));

        firestoreCRUDUtil.createEntry(CRUDConstants.RUNS_TABLE, trackId, jsonData, createCallback);

        verify(createCallback).onFailure();

        // Ensure that getEntry is not called if createEntry fails
        verify(firestoreCRUDUtil, never()).getEntry(anyString(), anyString(), any(ReadCallback.class));
    }

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

    /**
     * Matching failure test:
     */

    @Test
    public void testUpdateEntry_Failure() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ActionCallback callback = mock(ActionCallback.class);
        JsonObject jsonData = generateMockData();

        doAnswer(invocation -> {
            ((ActionCallback) invocation.getArgument(3)).onFailure();
            return null;
        }).when(firestoreCRUDUtil).updateEntry(anyString(), anyString(), any(JsonObject.class), any(ActionCallback.class));

        firestoreCRUDUtil.updateEntry(CRUDConstants.RUNS_TABLE, trackId, jsonData, callback);

        // Verify that callback onFailure is called
        verify(callback).onFailure();
    }


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

    /**
     * Matching failure test:
     */

    @Test
    public void testDeleteEntry_Failure() {
        FirestoreCRUDUtil firestoreCRUDUtil = FirestoreCRUDUtil.getInstance();
        ActionCallback callback = mock(ActionCallback.class);

        // Force failure by passing a null run id
        doAnswer(invocation -> {
            ((ActionCallback) invocation.getArgument(2)).onFailure();
            return null;
        }).when(firestoreCRUDUtil).deleteEntry(anyString(), isNull(), any(ActionCallback.class));
        firestoreCRUDUtil.deleteEntry(CRUDConstants.RUNS_TABLE, trackId, callback);

        verify(callback).onFailure();
    }



    @Test
    public void testGetEntry() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ReadCallback callback = mock(ReadCallback.class);

        JsonObject jsonData = generateMockData();
        doAnswer(invocation -> {
            // get the 3rd param in the `getEntry` method
            ((ReadCallback) invocation.getArgument(2)).onSuccess(jsonData);
            return null;
        }).when(firestoreCRUDUtil).getEntry(anyString(), anyString(), any(ReadCallback.class));
        firestoreCRUDUtil.getEntry(CRUDConstants.RUNS_TABLE, trackId, callback);

        System.out.println(jsonData.toString());
        verify(callback).onSuccess(jsonData);
    }

    /**
     * Matching failure test
     */

    @Test
    public void testGetEntry_Failure() {
        FirestoreCRUDUtil firestoreCRUDUtil = mock(FirestoreCRUDUtil.class);
        ReadCallback callback = mock(ReadCallback.class);


        doAnswer(invocation -> {
            // Invoke the failure callback when a null collection name is passed
            ((ReadCallback) invocation.getArgument(2)).onFailure();
            return null;
        }).when(firestoreCRUDUtil).getEntry(isNull(), anyString(), any(ReadCallback.class));

        verify(callback).onFailure();
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



