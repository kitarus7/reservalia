package com.kitarsoft.reservalia.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kitarsoft.reservalia.database.DBManager;
import com.kitarsoft.reservalia.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItemDao {

    private final String COLLECTION = "Locales";
    private final String SUBCOLLECTION = "Carta";

    public void create(MenuItem menuItem, String documentName, String subDocumentName){
        DBManager.createSubCollection(menuItem, COLLECTION, documentName, SUBCOLLECTION, subDocumentName);
    }

    public String getSubcollectionId(String establishmentId, String queryParam, Object queryFilter){
        return DBManager.getSubDocumentIdString(COLLECTION, establishmentId, SUBCOLLECTION, queryParam, queryFilter);
    }

    public void getMenuItem(String establishmentId, String menuItemId, ReadMenuItem myCallback){
        DBManager.getSubDocument(COLLECTION, establishmentId, SUBCOLLECTION, menuItemId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MenuItem result = documentSnapshot.toObject(MenuItem.class);
                myCallback.onCallback(result);
            }
        });
    }

    public void getMenuItems(String establishmentId, String queryParam, Object queryFilter, ReadMenuItems myCallback){

        List<MenuItem> results = new ArrayList();

        DBManager.getSubCollectionTask(COLLECTION, establishmentId, SUBCOLLECTION, queryParam, queryFilter)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("[DBM]", document.getId() + " => " + document.getData());
                                results.add(new MenuItem(
                                        (String)document.get("menuId"),
                                        (boolean)document.get("menu"),
                                        (String)document.get("categoria"),
                                        (String)document.get("nombre"),
                                        (String)document.get("descripcion"),
                                        (String)document.get("observaciones"),
                                        Math.round((double)document.get("precio")*100.0)/100.0));
                            }
                        } else {
                            Log.w("[DBM]", "Error getting documents.", task.getException());
                        }
                        myCallback.onCallback(results);
                    }
                });
    }



    public void delete(String documentName, String menuItemId){
        DBManager.deleteSub(COLLECTION, documentName, SUBCOLLECTION, menuItemId);
    }

    public void update(MenuItem menuItem, String documentName, String subDocumentName){
        create(menuItem, documentName, subDocumentName);
    }

    public interface ReadMenuItem {
        void onCallback(MenuItem result);
    }

    public interface ReadMenuItems {
        void onCallback(List<MenuItem> results);
    }
}
