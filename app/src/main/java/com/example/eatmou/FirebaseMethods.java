package com.example.eatmou;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.example.eatmou.ui.FoodParty.FoodPartyModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

        firestore.collection("foodParties").document(id).set(foodParty);
    }

    public void deleteFoodParty(String foodPartyId) {
        firestore.collection("foodParties").document(foodPartyId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("Food Party Deleted", "Food Party Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Delete Food Party Error", e.toString());
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
    public void updateFoodParty(FoodPartyModel foodPartyModel) {
        String id = foodPartyModel.getId();
        Map<String, Object> foodParty = foodPartyModel.toMap();

        firestore.collection("foodParties").document(id).set(foodParty);
    }
}
