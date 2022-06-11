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
import com.kitarsoft.reservalia.models.MenuItem;

import java.util.List;

public class DBManager {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void create(Object obj, String collectionName, String documentName){
        db.collection(collectionName).document(documentName).set(obj);
    }

    public static DocumentReference getDocument(String collectionPath, String documentName){
        return db.collection(collectionPath).document(documentName);
    }

    public static Task<QuerySnapshot> getCollectionTask(String collectionPath, String queryParam, String queryFilter){
        if(queryParam == null || queryParam.isEmpty()){
            return db.collection(collectionPath).get();
        }else{
            return db.collection(collectionPath).whereEqualTo(queryParam, queryFilter).get();
        }
    }

    public static void delete(String collectionName, String documentName){
        db.collection(collectionName).document(documentName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("[DBM]", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("[DBM]", "Error deleting document", e);
                    }
                });
    }

    //  SUBCOLLECTION
    public static void createSubCollection(Object obj, String collectionName, String documentName, String subCollectionName, String subDocumentName){
        if(subDocumentName != null){
            db.collection(collectionName).document(documentName).collection(subCollectionName).document(subDocumentName).set(obj);
        }else{
            db.collection(collectionName).document(documentName).collection(subCollectionName).add(obj);
        }
    }

    public static DocumentReference getSubDocument(String collectionPath, String documentName, String subCollectionName, String subDocumentName){
        return db.collection(collectionPath).document(documentName).collection(subCollectionName).document(subDocumentName);
    }

    public static String getSubDocumentIdString(String collectionPath, String documentName,
                                                           String subCollectionName, String queryParam, Object queryFilter){
        return db.collection(collectionPath).document(documentName)
                .collection(subCollectionName)
                .whereEqualTo(queryParam, queryFilter).get()
                .getResult().getDocuments().get(0).getId();
    }

    public interface ReadMenuItems {
        void onCallback(List<MenuItem> results);
    }

    public static Task<QuerySnapshot> getSubCollectionTask(String collectionPath, String documentName,
                                                           String subCollectionName, String queryParam, Object queryFilter){
        if(queryParam == null || queryParam.isEmpty()){
            return db.collection(collectionPath).document(documentName)
                    .collection(subCollectionName).get();
        }else{
            return db.collection(collectionPath).document(documentName)
                    .collection(subCollectionName).whereEqualTo(queryParam, queryFilter).get();
        }
    }

    public static void deleteSub(String collectionName, String documentName, String subCollectionName, String subDocumentName){
        db.collection(collectionName).document(documentName)
                .collection(subCollectionName).document(subDocumentName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("[DBM]", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("[DBM]", "Error deleting document", e);
                    }
                });
    }

//    public static Task<DocumentReference> create(Object obj, String collectionName){
//
//        Task<DocumentReference> reference;
//
//        reference = db.collection(collectionName)
//                .add(obj)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("[DBM]", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("[DBM]", "Error adding document", e);
//                    }
//                });
//        return reference;
//    }

//    public static void getUsers(Object obj, String collectionPath, String queryParam, String queryFilter, ReadUsers myCallback) {
//
//        List results = new ArrayList();
//
//        if(queryParam != null && queryParam.isEmpty()){
//            db.collection(collectionPath)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("[DBM]", document.getId() + " => " + document.getData());
//                                results.add(document.toObject(obj.getClass()));
//                            }
//                        } else {
//                            Log.w("[DBM]", "Error getting documents.", task.getException());
//                        }
//                        myCallback.onCallback(results);
//                    }
//                });
//        }else{
//            db.collection(collectionPath)
//                .whereEqualTo(queryParam, queryFilter)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("[DBM]", document.getId() + " => " + document.getData());
//                                results.add(document.toObject(obj.getClass()));
//                            }
//                        } else {
//                            Log.w("[DBM]", "Error getting documents.", task.getException());
//                        }
//                        myCallback.onCallback(results);
//                    }
//                });
//        }
//    }

//    public interface ReadUsers {
//        void onCallback(List<User> results);
//    }

}
