package com.kitarsoft.reservalia.database;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kitarsoft.reservalia.models.User;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static Task<DocumentReference> create(Object obj, String collectionPath){

        Task<DocumentReference> reference;

        reference = db.collection(collectionPath)
                .add(obj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("[DBM]", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("[DBM]", "Error adding document", e);
                    }
                });
        return reference;
    }

    public static void readData(Object obj, String collectionPath, String queryParam, String queryFilter, ReadUsers myCallback) {

        List results = new ArrayList();

        if(queryParam != null && queryParam.isEmpty()){
            db.collection(collectionPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("[DBM]", document.getId() + " => " + document.getData());
                                results.add(document.toObject(obj.getClass()));
                            }
                        } else {
                            Log.w("[DBM]", "Error getting documents.", task.getException());
                        }
                        myCallback.onCallback(results);
                    }
                });
        }else{
            db.collection(collectionPath)
                .whereEqualTo(queryParam, queryFilter)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("[DBM]", document.getId() + " => " + document.getData());
                                results.add(document.toObject(obj.getClass()));
                            }
                        } else {
                            Log.w("[DBM]", "Error getting documents.", task.getException());
                        }
                        myCallback.onCallback(results);
                    }
                });
        }
    }

    public interface ReadUsers {
        void onCallback(List<User> results);
    }


}
