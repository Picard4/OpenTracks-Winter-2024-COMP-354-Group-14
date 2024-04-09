package de.dennisguse.opentracks.data.interfaces;
import org.junit.Test;
import com.google.gson.JsonObject;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for JsonSerializer Interface
 */
public class JsonSerializerTest {
    final String description = "Completed track";
    final String externalId = "track_01";

    @Test
    /**
     * Testing the toJSON method, making sure the expect key value pairs appear
     * The unexpected keys shouldn't resolve to true
     */
    public void testIfValidToJson() {
        TestTrack testObject = new TestTrack(description, externalId);

        JsonObject json = testObject.toJSON();

        assertNotNull(json);
        assertTrue(json.has("description"));
        assertTrue(json.has("externalId"));
        assertFalse(json.has("not there"));

        assertEquals(description, json.get("description").getAsString());
        assertEquals(externalId, json.get("externalId").getAsString());
    }

    // TODO: add tests for FromJSON

    /**
     * Sample class of Track that extends the JSONSerializable
     * This class is a simplified version of the Track class that has only a few fields
     */
    private static class TestTrack implements JSONSerializable<TestTrack> {
        private String description;
        private String externalId;

        /**
         * The constructor will set static value for the purpose of the test
         */
        public TestTrack(String description, String externalId) {
            this.description = description;
            this.externalId = externalId;
        }
    }
}
