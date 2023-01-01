package com.example.eatmou;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eatmou.Inbox.Invitation;
import com.example.eatmou.Inbox.received.ReceivedAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

//    public void retrieveInvitations(ArrayList<Invitation> list, ReceivedAdapter adapter){
//        firestore.collection("Invitations").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null){
//                    Log.e("Firestore error", error.getMessage());
//                    return;
//                }
//                for(DocumentChange doc : value.getDocumentChanges()){
//                    if(doc.getType()==DocumentChange.Type.ADDED){
//                        String OrganiserID =
//
//                        list.add(doc.getDocument().toObject(Invitation.class));
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
//    }
}
