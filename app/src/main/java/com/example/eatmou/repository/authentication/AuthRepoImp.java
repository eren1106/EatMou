package com.example.eatmou.repository.authentication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eatmou.model.AppLock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class AuthRepoImp implements AuthRepo {
    boolean success = false;
    @Override
    public boolean uploadAppLock(String pass, boolean exist) {
        //Create fireStore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserID = FirebaseAuth.getInstance().getUid();

        if(currentUserID != null) {
            AppLock appLock = new AppLock(currentUserID, pass, exist);
            //Get map of appLock class
            Map<String, Object> appLockMap = appLock.toMap();

            db.collection("AppLock").document(currentUserID).set(appLockMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                success = true;
                                Log.d("AppLock", "App lock successfully applied");
                            } else {
                                Log.d("AppLock", "Error");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("AppLock", e.getMessage());
                        }
                    });
        }
        return success;
    }
}
