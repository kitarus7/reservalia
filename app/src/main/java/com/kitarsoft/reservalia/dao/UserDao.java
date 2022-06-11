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
import com.kitarsoft.reservalia.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final String COLLECTION = "Usuarios";

    public void create(User user, String documentName){
        DBManager.create(user, COLLECTION, documentName);
    }

    public void getUser(String userId, ReadUser myCallback){
        DBManager.getDocument(COLLECTION, userId)
        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                myCallback.onCallback(user);
            }
        });
    }

    public void getUsers(String userId, String queryParam, String queryFilter, ReadUsers myCallback){

        List<User> results = new ArrayList();

        DBManager.getCollectionTask(COLLECTION, queryParam, queryFilter)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("[DBM]", document.getId() + " => " + document.getData());
                                results.add(new User(
                                        (String)document.getId(),
                                        (String)document.get("contrasenia"),
                                        (String)document.get("nombre"),
                                        (String)document.get("apellidos"),
                                        (String)document.get("telefono"),
                                        (boolean)document.get("esPropietario")
                                        ));
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

    public void update(User user, String documentName){
        create(user, documentName);
    }

    public interface ReadUser {
        void onCallback(User result);
    }

    public interface ReadUsers {
        void onCallback(List<User> results);
    }
}
