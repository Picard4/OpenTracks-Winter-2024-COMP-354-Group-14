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
    public void createEntry(String collection, Map < String, Object > data) {
        String id = data.get("id").toString();
        db.collection(collection)
                .document(id).set(data)
                .addOnSuccessListener(documentReference-> {
                        Log.d("CREATED", "DocumentSnapshot updated with ID: " + id);
            })
            .addOnFailureListener(e-> {
                Log.e("ERROR", "Error updating document: " + e.getMessage());
            });
    }

    /**
     * Updates an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void updateEntry(String collection, Map < String, Object > data) {
        String id = data.get("id").toString();
        db.collection(collection).document(id)
                .update(data)
                .addOnSuccessListener(documentReference-> {
                        Log.d("UPDATED", "DocumentSnapshot updated with ID: " + id);
            })
            .addOnFailureListener(e-> {
                Log.e("ERROR", "Error updating document: " + e.getMessage());
            });
    }

    /**
     * Deletes an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void deleteEntry(String collection, Map < String, Object > data) {
        String id = data.get("id").toString();
        db.collection(collection).document(id)
                .delete()
                .addOnSuccessListener(documentReference -> {
                        Log.d("DELETED", "DocumentSnapshot deleted with ID: " + id);
            })
            .addOnFailureListener(e -> {
                Log.e("ERROR", "Error deleting document: " + e.getMessage());
            });
    }

    /**
     * Gets an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void getEntry(String collection, Map < String, Object > data) {
        String id = data.get("id").toString();
        db.collection(collection).document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                        Log.d("GET", "DocumentSnapshot retrieved with ID: " + id);
            })
            .addOnFailureListener(e -> {
                Log.e("ERROR", "Error retrieving entry: " + e.getMessage());
            });
    }
}