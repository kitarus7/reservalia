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
import com.kitarsoft.reservalia.models.Booking;
import com.kitarsoft.reservalia.models.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDao {

    private final String COLLECTION = "Locales";
    private final String SUBCOLLECTION = "Reservas";

    public void create(Booking booking, String documentName, String subDocumentName){
        DBManager.createSubCollection(booking, COLLECTION, documentName, SUBCOLLECTION, subDocumentName);
    }

    public String getSubcollectionId(String establishmentId, String queryParam, Object queryFilter){
        return DBManager.getSubDocumentIdString(COLLECTION, establishmentId, SUBCOLLECTION, queryParam, queryFilter);
    }

    public void getBooking(String establishmentId, String bookingId, ReadBooking myCallback){
        DBManager.getSubDocument(COLLECTION, establishmentId, SUBCOLLECTION, bookingId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Booking result = documentSnapshot.toObject(Booking.class);
                myCallback.onCallback(result);
            }
        });
    }

    public void getBookings(String establishmentId, String queryParam, Object queryFilter, ReadBookings myCallback){

        List<Booking> results = new ArrayList();

        DBManager.getSubCollectionTask(COLLECTION, establishmentId, SUBCOLLECTION, queryParam, queryFilter)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("[DBM]", document.getId() + " => " + document.getData());
                                results.add(new Booking(
                                        (String)document.get("id"),
                                        (String)document.get("userId"),
                                        (String)document.get("mesaId"),
                                        (String)document.get("nombre"),
                                        (String)document.get("apellidos"),
                                        (String)document.get("telefono"),
                                        (Timestamp) document.get("fecha_reserva"),
                                        (long)document.get("comensales"),
                                        (boolean)document.get("terraza")));
                            }
                        } else {
                            Log.w("[DBM]", "Error getting documents.", task.getException());
                        }
                        myCallback.onCallback(results);
                    }
                });
    }

    public void getCollisionBookings(String documentName, Table mesa, Timestamp fecha){
        DBManager.getSubCollectionReference(COLLECTION, documentName, SUBCOLLECTION)
                .whereEqualTo("mesaId", mesa)
                .whereLessThanOrEqualTo("fecha_reserva", fecha)
                .whereGreaterThanOrEqualTo("fecha_reserva", fecha);
    }

    public void delete(String documentName, String bookingId){
        DBManager.deleteSub(COLLECTION, documentName, SUBCOLLECTION, bookingId);
    }

    public void update(Booking booking, String documentName, String subDocumentName){
        create(booking, documentName, subDocumentName);
    }

    public interface ReadBooking {
        void onCallback(Booking result);
    }

    public interface ReadBookings {
        void onCallback(List<Booking> results);
    }
}
