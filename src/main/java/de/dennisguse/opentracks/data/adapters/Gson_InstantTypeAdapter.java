package de.dennisguse.opentracks.data.adapters;
import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;

public class Gson_InstantTypeAdapter extends TypeAdapter < Instant >
{
    @Override
    public void write ( JsonWriter jsonWriter , Instant instant ) throws IOException
    {
        jsonWriter.value(instant.toString());  // Writes in standard ISO 8601 format.
    }

    @Override
    public Instant read ( JsonReader jsonReader ) throws IOException
    {
        String stringValue = jsonReader.nextString(); // Read the string value
        return Instant.parse(stringValue); // Parse the string into Instant
    }
}