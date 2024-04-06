package de.dennisguse.opentracks.data.interfaces;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface ReadCallback {
    /**
     * Method called when the action succeeds
     */
    void onSuccess(JsonObject data);

    /**
     * Method called when the action fails with an error message
     */
    void onFailure();
}
