package de.dennisguse.opentracks.util;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

import de.dennisguse.opentracks.data.models.CRUDConstants;

/**
 * Util class the performs CRUD operations on a given collection and handles
 * onSuccess and onError callbacks.
 */
public class FirestoreCRUDUtil {
    private FirebaseFirestore db;

    public FirestoreCRUDUtil() {
        db = FirebaseFirestore.getInstance(); // TODO: Singleton pattern will be implemented later, use it
    }

    /**
     * Creates a new entry in db
     * @param collection The collection in which to create the entry ("users" or "runs")
     * @param data The data to be added as the new entry
     */
    public void createEntry(final String collection, final Map<String, Object> data) {
        String id = data.get("id").toString();
        db.collection(collection)
                .document(id).set(data)
                .addOnSuccessListener(documentReference-> {
                    Log.d(CRUDConstants.TAG_CREATED, CRUDConstants.SUCCESS_CREATING_DOCUMENT + id);
                })
                .addOnFailureListener(e-> {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_CREATING_DOCUMENT + e.getMessage());
                });
    }

    /**
     * Updates an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void updateEntry(final String collection, final Map<String, Object> data) {
        String id = data.get("id").toString();
        db.collection(collection).document(id)
                .update(data)
                .addOnSuccessListener(documentReference-> {
                    Log.d(CRUDConstants.TAG_UPDATED, CRUDConstants.SUCCESS_UPDATING_DOCUMENT + id);
                })
                .addOnFailureListener(e-> {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_UPDATING_DOCUMENT + e.getMessage());
                });
    }

    /**
     * Deletes an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void deleteEntry(final String collection, final Map<String, Object> data) {
        String id = data.get("id").toString();
        db.collection(collection).document(id)
                .delete()
                .addOnSuccessListener(documentReference -> {
                    Log.d(CRUDConstants.TAG_DELETED, CRUDConstants.SUCCESS_DELETING_DOCUMENT + id);
                })
                .addOnFailureListener(e -> {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_DELETING_DOCUMENT + e.getMessage());
                });
    }

    /**
     * Gets an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void getEntry(final String collection, final Map<String, Object> data) {
        String id = data.get("id").toString();
        db.collection(collection).document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d(CRUDConstants.TAG_GET, CRUDConstants.SUCCESS_RETRIEVING_ENTRY + id);
                })
                .addOnFailureListener(e -> {
                    Log.e(CRUDConstants.TAG_ERROR, CRUDConstants.ERROR_RETRIEVING_ENTRY + e.getMessage());
                });
    }
}
