package de.dennisguse.opentracks.data.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class Gson_DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        jsonWriter.value((duration != null) ? duration.toString() : null); // Writes the Duration in standard ISO-8601 format.
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.parse(jsonReader.nextString()); // Parses standard ISO-8601 formatted string to Duration.
    }
}
