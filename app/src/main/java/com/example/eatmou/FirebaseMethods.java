package com.example.eatmou;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseMethods {
    private FirebaseFirestore firestore;

    public FirebaseMethods() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void addFoodParty(String title, String organiserId, String location, Date date,
                             Date startTime, Date endTime, int maxParticipant) {
        Map<String, Object> foodParty = new HashMap<>();

        foodParty.put("title", title);
        foodParty.put("organiserId", organiserId);
        foodParty.put("location", location);
        foodParty.put("date", date);
        foodParty.put("startTime", startTime);
        foodParty.put("endTime", endTime);
        foodParty.put("maxParticipant", maxParticipant);
        foodParty.put("participantId", Arrays.asList());

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
}
