package com.kitarsoft.reservalia.dao;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kitarsoft.reservalia.database.DBManager;
import com.kitarsoft.reservalia.models.Establishment;

import java.util.ArrayList;
import java.util.List;

public class EstablishmentDao {

    private final String COLLECTION = "Locales";

    public void create(Establishment establishment, String documentName){
        DBManager.create(establishment, COLLECTION, documentName);
    }

    public void getEstablishment(String userId, ReadEstablishment myCallback){
        DBManager.getDocument(COLLECTION, userId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Establishment result = documentSnapshot.toObject(Establishment.class);
                myCallback.onCallback(result);
            }
        });
    }

    public void getEstablishments(String queryParam, String queryFilter, ReadEstablishments myCallback){

        List<Establishment> results = new ArrayList();

        DBManager.getCollectionTask(COLLECTION, queryParam, queryFilter)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("[DBM]", document.getId() + " => " + document.getData());
                                results.add(new Establishment(
                                        document.getId(),
                                        (String)document.get("nombre"),
                                        (String)document.get("telefono"),
                                        (double)document.get("puntuacion"),
                                        (double)document.get("precio"),
                                        (GeoPoint) document.get("posicion")));
                            }
                        } else {
                            Log.w("[DBM]", "Error getting documents.", task.getException());
                        }
                        myCallback.onCallback(results);
                    }
                });
    }

    public void delete(String documentName){
        DBManager.delete(COLLECTION, documentName);
    }

    public void update(Establishment establishment, String documentName){
        create(establishment, documentName);
    }

    public interface ReadEstablishment {
        void onCallback(Establishment result);
    }

    public interface ReadEstablishments {
        void onCallback(List<Establishment> results);
    }
}
