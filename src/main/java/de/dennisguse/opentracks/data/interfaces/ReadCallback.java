package de.dennisguse.opentracks.data.interfaces;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public interface ReadCallback {
    /**
     * Method called when the action succeeds
     */
    void onSuccess(JsonObject data);
    void onSuccess(ArrayList<JsonObject> data);

    /**
     * Method called when the action fails with an error message
     */
    void onFailure();
}
