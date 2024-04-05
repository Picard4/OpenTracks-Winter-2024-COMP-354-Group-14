package de.dennisguse.opentracks.data.models;

/**
 * Keeps a list of constants that are useful to perform CRUD operations.
 * The constants include collection names, CRUD operations, ERROR and SUCCESS messages
 */
public class CRUDConstants {
    // Collection names
    public static final String RUNS_TABLE = "runs";
    public static final String USERS_TABLE = "users";

    // CRUD tags - will show on success
    public static final String TAG_CREATED = "CREATED";
    public static final String TAG_UPDATED = "UPDATED";
    public static final String TAG_DELETED = "DELETED";
    public static final String TAG_GET = "GET";

    // Error tag
    public static final String TAG_ERROR = "ERROR";

    // Error messages
    public static final String ERROR_CREATING_DOCUMENT = "Error creating entry: ";
    public static final String ERROR_UPDATING_DOCUMENT = "Error updating entry: ";
    public static final String ERROR_DELETING_DOCUMENT = "Error deleting entry: ";
    public static final String ERROR_RETRIEVING_ENTRY = "Error retrieving entry: ";

    // Success messages
    public static final String SUCCESS_CREATING_DOCUMENT = "Success creating entry: ";
    public static final String SUCCESS_UPDATING_DOCUMENT = "Success updating entry: ";
    public static final String SUCCESS_DELETING_DOCUMENT = "Success deleting entry: ";
    public static final String SUCCESS_RETRIEVING_ENTRY = "Success retrieving entry: ";

    // Reference
    public static final String CRUD_OPERATION = "CRUD_OPERATION";

}