package com.kitarsoft.reservalia.dao;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kitarsoft.reservalia.database.DBManager;
import com.kitarsoft.reservalia.models.Table;

import java.util.ArrayList;
import java.util.List;

public class TableDao {

    private final String COLLECTION = "Locales";
    private final String SUBCOLLECTION = "Mesas";

    public void create(Table table, String documentName, String subDocumentName){
        DBManager.createSubCollection(table, COLLECTION, documentName, SUBCOLLECTION, subDocumentName);
    }

    public void getTable(String establishmentId, String tableId, ReadTable myCallback){
        DBManager.getSubDocument(COLLECTION, establishmentId, SUBCOLLECTION, tableId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Table result = documentSnapshot.toObject(Table.class);
                myCallback.onCallback(result);
            }
        });
    }

    public void getTables(String establishmentId, String queryParam, Object queryFilter, ReadTables myCallback){

        List<Table> results = new ArrayList();

        DBManager.getSubCollectionTask(COLLECTION, establishmentId, SUBCOLLECTION, queryParam, queryFilter)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("[DBM]", document.getId() + " => " + document.getData());
                                results.add(new Table(
                                        document.getId(),
                                        (long)document.get("tamaño"),
                                        (boolean)document.get("terraza")));
                            }
                        } else {
                            Log.w("[DBM]", "Error getting documents.", task.getException());
                        }
                        myCallback.onCallback(results);
                    }
                });
    }

    public void delete(String documentName, String tableId){
        DBManager.deleteSub(COLLECTION, documentName, SUBCOLLECTION, tableId);
    }

    public void update(Table table, String documentName, String subDocumentName){
        create(table, documentName, subDocumentName);
    }

    public interface ReadTable {
        void onCallback(Table result);
    }

    public interface ReadTables {
        void onCallback(List<Table> results);
    }
}
