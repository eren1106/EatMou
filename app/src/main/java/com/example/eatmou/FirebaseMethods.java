package com.example.eatmou;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eatmou.FoodParty.FoodPartyModel;
import com.example.eatmou.FoodParty.JoinedPersonModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FirebaseMethods {
    private FirebaseFirestore firestore;

    public FirebaseMethods() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void addFoodParty(String title, String organiserId, String location, Date date,
                             Date startTime, Date endTime, int maxParticipant) {

        String id = UUID.randomUUID().toString();
        FoodPartyModel fpm = new FoodPartyModel(id, title, organiserId, location, date, startTime, endTime, 9, new ArrayList<>(Arrays.asList()));
        Map<String, Object> foodParty = fpm.toMap();

        firestore.collection("foodParties").add(foodParty).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Added Food Party");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Fail to add Food Party");
            }
        });
    }

//    public ArrayList<FoodPartyModel> getAllFoodParties() {
//        ArrayList<FoodPartyModel> res = new ArrayList<>();
//        firestore.collection("foodParties").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        FoodPartyModel foodPartyModel = FoodPartyModel.toObject(document.getData());
//                        res.add(foodPartyModel);
//                    }
//                }
//                else {
//                    System.out.println("Error");
//                }
//            }
//        });
//
//        return res;
//    }
}
