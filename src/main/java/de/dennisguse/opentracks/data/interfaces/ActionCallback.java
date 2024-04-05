package de.dennisguse.opentracks.data.interfaces;

public interface ActionCallback {
    /**
     * Method called when the action succeeds
     */
    void onSuccess();

    /**
     * Method called when the action fails with an error message
     */
    void onFailure();
}
