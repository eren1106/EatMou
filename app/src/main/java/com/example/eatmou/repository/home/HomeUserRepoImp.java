package com.example.eatmou.repository.home;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eatmou.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeUserRepoImp implements HomeUserRepo{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Users user = new Users();
    List<Users> usersList = new ArrayList<>();

    @Override
    public List<Users> getAllUser() {
        db.collection("users")
            .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if(documentSnapshots.isEmpty()) {
                            return;
                        } else {
                           usersList = documentSnapshots.toObjects(Users.class);
                           Log.d("User document", usersList.get(0).getUsername());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("User Document","Error getting data");
                    }
                });
        Log.d("User document", "Size" + usersList.size());
        return usersList;
    }

    @Override
    public Users getUser(String userID) {
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(Users.class);
                        Log.d("User document", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("User document", "No such document");
                    }
                } else {
                    Log.d("User document", "get failed with ", task.getException());
                }
            }
        });
        return user;
    }
}
