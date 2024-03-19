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
    public void createEntry(String collection, Map<String, Object> data) {
        db.collection(collection)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ADDED", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("ERROR", "Error adding user", e);
                    }
                });
    }

    /**
     * Updates an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void updateEntry(String collection, Map<String, Object> data) {
        String documentId = getDocumentId(collection, "id", data.get("id").toString());
        System.out.println("test update" + documentId + db.collection(collection));
//        db.collection(collection).document(documentId)
//                .update(data)
//                .addOnSuccessListener(documentReference -> {
//                    Log.d("UPDATED", "DocumentSnapshot updated with ID: " + documentId);
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("ERROR", "Error updating document: " + e.getMessage());
//                });
    }

    /**
     * Deletes an existing entry in db
     * @param collection The collection containing the entry to be updated
     * @param data The new data to be updated in the entry
     * TODO: discuss with the team, as I currently expect uniqueKey to be part of the data itself
     * TODO: make each table to have a  unique ID
     */
    public void deleteEntry(String collection, Map<String, Object> data) {
        String documentId = getDocumentId(collection, "id", data.get("id").toString());
        db.collection(collection).document(documentId)
                .delete()
                .addOnSuccessListener(documentReference -> {
                    Log.d("DELETED", "DocumentSnapshot deleted with ID: " + documentId);
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
    public void getEntry(String collection, Map<String, Object> data) {
        String documentId = getDocumentId(collection, "id", data.get("id").toString());
        db.collection(collection).document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d("GET", "DocumentSnapshot retrieved with ID: " + documentId);
                })
                .addOnFailureListener(e -> {
                    Log.e("ERROR", "Error retrieving entry: " + e.getMessage());
                });
    }

    /**
     * Helper method to retrieve the document ID based on a unique key-value pair
     * @param collection The collection in which to search for the document.
     * @param key  The unique key used to search for the document (e.g., "user" or "firstName")
     * @param value The value associated with the unique key
     * @return The document ID if found; otherwise, null
     */
    private String getDocumentId(String collection, String key, String value) {
        db.collection("your_collection").whereEqualTo("field_name", "field_value")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            System.out.println("dlfkdlfkd" + documentId);
                            // Use the documentId as needed
                        }
                    } else {
                        // Handle errors
                    }
                });
        return "";
    };
//        db.collection("runs").whereEqualTo(key, "53").get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    System.out.println("88888888888" + queryDocumentSnapshots.get());
//                })
//                .addOnFailureListener(e -> {
//                    // Handle any errors that occurred during the query
//                    System.out.println("Error getting documents: " + e.getMessage());
//                });
//        System.out.println("*********" +  collection + key + value);
//        Task<QuerySnapshot> task = db.collection(collection).whereEqualTo(key, value).get();
//
//        try {
//            QuerySnapshot querySnapshot = Tasks.await(task);
//            System.out.println("*********" +  task + querySnapshot);
//            if (!querySnapshot.isEmpty()) {
//                // Typically, we will expect to have one unique documentId
//                return querySnapshot.getDocuments().get(0).getId();
//            } else {
//                return null; // No such ID exists
//            }
//        } catch (Exception e) {
//            // TODO: Handle exceptions more gracefully (Phase 2)
//            return null;
//        }


    }
