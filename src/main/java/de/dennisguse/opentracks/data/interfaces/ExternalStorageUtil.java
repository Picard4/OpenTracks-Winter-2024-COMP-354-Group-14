package de.dennisguse.opentracks.data.interfaces;

import com.google.gson.JsonObject;

import de.dennisguse.opentracks.data.models.CRUDConstants;

public interface ExternalStorageUtil {
    /**
     * Creates a new entry in external storage.
     *
     * @param collection The collection in which to create the entry
     * @param id         The ID of the entry to be updated
     * @param jsonData   The data to be added as the new entry
     */
    void createEntry(final String collection, final String id, final JsonObject jsonData, final ActionCallback callback);

    /**
     * Updates an existing entry in external storage.
     *
     * @param collection The collection containing the entry to be updated
     * @param id         The ID of the entry to be updated
     * @param jsonData   The new data to be updated in the entry
     */
    void updateEntry(final String collection, final String id, final JsonObject jsonData, final ActionCallback callback);

    /**
     * Deletes an existing entry from external storage.
     *
     * @param collection The collection containing the entry to be deleted
     * @param id         The ID of the entry to be deleted
     */
    void deleteEntry(final String collection, final String id, final ActionCallback callback);

    /**
     * Retrieves an existing entry from external storage.
     *
     * @param collection The collection containing the entry to be retrieved
     * @param id         The ID of the entry to be retrieved
     * @return The retrieved entry
     */
    void getEntry(final String collection, final String id, final ReadCallback callback);

    /**
     * Creates a new user entry in external storage.
     *
     * @param jsonData The user data to be added as a new entry
     */
    default void createUser(final String id, final JsonObject jsonData, final ActionCallback callback) {
        createEntry(CRUDConstants.USERS_TABLE, id, jsonData, callback);
    }

    /**
     * Retrieves a user entry from external storage based on the user ID.
     *
     * @param id The ID of the user to retrieve
     * @return The retrieved user entry
     */
    default void getUser(final String id, final ReadCallback callback) {
        getEntry(CRUDConstants.USERS_TABLE, id, callback);
    }

    /**
     * Updates a user's data in external storage.
     *
     * @param id   The ID of the user to update
     * @param jsonData The new data for the user
     */
    default void updateUser(final String id, final JsonObject jsonData, final ActionCallback callback) {
        updateEntry(CRUDConstants.USERS_TABLE, id, jsonData, callback);
    }

    /**
     * Deletes a user from external storage.
     *
     * @param id The ID of the user to delete
     */
    default void deleteUser(final String id, final ActionCallback callback) {
        deleteEntry(CRUDConstants.USERS_TABLE, id, callback);
    }
}
