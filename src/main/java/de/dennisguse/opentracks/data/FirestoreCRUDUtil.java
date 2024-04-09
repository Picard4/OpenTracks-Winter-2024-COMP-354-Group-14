package de.dennisguse.opentracks.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import de.dennisguse.opentracks.data.adapters.FireStoreAdapter;
import de.dennisguse.opentracks.data.interfaces.ActionCallback;
import de.dennisguse.opentracks.data.interfaces.ExternalStorageUtil;
import de.dennisguse.opentracks.data.interfaces.ReadCallback;
import de.dennisguse.opentracks.data.models.CRUDConstants;

/**
 * Util class that performs CRUD operations on a given collection and handles
 * success and failure callbacks.
 */
public class FirestoreCRUDUtil implements ExternalStorageUtil {
    private final FirebaseFirestore db;
    private static FirestoreCRUDUtil instance;

    private FirestoreCRUDUtil() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Gets an instance of FirestoreCRUDUtil.
     *
     * @return An instance of FirestoreCRUDUtil.
     */
    public static FirestoreCRUDUtil getInstance() {
        if (instance == null) {
            instance = new FirestoreCRUDUtil();
        }
        return instance;
    }

    /**
     * Creates a new entry in the Firestore database.
     *
     * @param collection The collection in which to create the entry ("users" or "runs").
     * @param id         The unique identifier of the entry.
     * @param jsonData   The data to be added as the new entry.
     * @param callback   The callback to be invoked on operation success or failure.
     */
    public void createEntry(final String collection, final String id, final JsonObject jsonData, final ActionCallback callback) {
        Map<String, Object> adaptedData = FireStoreAdapter.toMap(jsonData);
        adaptedData.put("externalId", id);

        db.collection(collection)
                .document(id).set(adaptedData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(CRUDConstants.TAG_CREATED, CRUDConstants.SUCCESS_CREATING_DOCUMENT + id);
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_CREATING_DOCUMENT + e.getMessage());
                    if (callback != null) callback.onFailure();
                });

    }

    /**
     * Updates an existing entry in the Firestore database.
     *
     * @param collection The collection containing the entry to be updated.
     * @param id         The unique identifier of the entry.
     * @param data       The new data to be updated in the entry.
     * @param callback   The callback to be invoked on operation success or failure.
     */
    public void updateEntry(final String collection, final String id, final JsonObject data, final ActionCallback callback) {
        Map<String, Object> adaptedData = FireStoreAdapter.toMap(data);
        db.collection(collection).document(id)
                .update(adaptedData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(CRUDConstants.TAG_UPDATED, CRUDConstants.SUCCESS_UPDATING_DOCUMENT + id);
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_UPDATING_DOCUMENT + e.getMessage());
                    if (callback != null) callback.onFailure();
                });
    }

    /**
     * Deletes an existing entry from the Firestore database.
     *
     * @param collection The collection containing the entry to be deleted.
     * @param id         The unique identifier of the entry.
     * @param callback   The callback to be invoked on operation success or failure.
     */
    public void deleteEntry(final String collection, final String id, final ActionCallback callback) {
        db.collection(collection).document(id)
                .delete()
                .addOnSuccessListener(documentReference -> {
                    Log.d(CRUDConstants.TAG_DELETED, CRUDConstants.SUCCESS_DELETING_DOCUMENT + id);
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_DELETING_DOCUMENT + e.getMessage());
                    if (callback != null) callback.onFailure();
                });
    }

    /**
     * Retrieves an existing entry from the Firestore database.
     *
     * @param collection The collection containing the entry to be retrieved.
     * @param id         The unique identifier of the entry.
     * @param callback   The callback to be invoked on operation success or failure.
     */
    public void getEntry(final String collection, final String id, final ReadCallback callback) {
        DocumentReference docRef = db.collection(collection).document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(CRUDConstants.TAG_GET, CRUDConstants.SUCCESS_RETRIEVING_ENTRY + id);
                        if (document.exists()) {
                            callback.onSuccess(FireStoreAdapter.toJson(document.getData()));
                        } else {
                            callback.onFailure();
                        }
                    }
                } else {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_RETRIEVING_ENTRY + task.getException());
                    callback.onFailure();
                }
            }
        });
    }

    /**
     * Retrieves all documents from an existing collection from the db, adapted from Firestore docs.
     *
     * @param collection The collection to be retrieved
     * @param callback   The callback to be invoked on operation success or failure.
     */
    public void getCollection(String collection, ReadCallback callback) {
        db.collection(collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<JsonObject> documents = new ArrayList<JsonObject>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document != null) {
                            documents.add(FireStoreAdapter.toJson(document.getData()));

                        }
                    }
                    callback.onSuccess(documents);
                    Log.d(CRUDConstants.TAG_GET_COLLECTION, CRUDConstants.SUCCESS_RETRIEVING_COLLECTION + " => " + collection);


                } else {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_RETRIEVING_COLLECTION + task.getException());
                    callback.onFailure();
                }
            }
        });


    }

    /**
     * Get runs by a specific filed and value
     * @param field, specific attribute to search
     * @param id, the value of that field
     */
    @Override
    public void getRunsByField(String field,String id, ReadCallback callback) {
        db.collection("runs").whereEqualTo(field,id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<JsonObject> documents = new ArrayList<JsonObject>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document != null) {
                            documents.add(FireStoreAdapter.toJson(document.getData()));

                        }
                    }
                    callback.onSuccess(documents);
                    Log.d(CRUDConstants.TAG_GET_COLLECTION, CRUDConstants.SUCCESS_CREATING_DOCUMENT + " => " + id);


                } else {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_RETRIEVING_COLLECTION + task.getException());
                    callback.onFailure();
                }
            }
        });

    }
}
