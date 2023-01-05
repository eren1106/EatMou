package com.example.eatmou.ui.splashScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.eatmou.R;
import com.example.eatmou.model.AppLock;
import com.example.eatmou.ui.Authentication.LoginPage;
import com.example.eatmou.ui.appLock.AppLockStart;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {
    AppLock appLock = new AppLock();
    boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        //Share Preferences
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Check the user has app lock which act as loading screen
        String currentUserID = FirebaseAuth.getInstance().getUid();
        if(currentUserID != null) {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("AppLock").document(currentUserID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            appLock = document.toObject(AppLock.class);
                            if(appLock != null ){
                                check = appLock.isExistPass();
                                if(check) {
                                    //Set password and pass to app lock page
                                    editor.putString("APP_LOCK", appLock.getPassword());
                                    editor.apply();

                                    //Navigate to app lock page
                                    startActivity(new Intent(getApplicationContext(), AppLockStart.class));
                                    Log.d("Check","App lock: " + check);
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    Log.d("Check","App lock: " + check);
                                }
                            }
                        } else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Log.d("Check","App lock: No document found");
                        }
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Log.d("Check","App lock: No task found");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        } else {
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            Log.d("Check","App lock: First time user");
        }
    }
}