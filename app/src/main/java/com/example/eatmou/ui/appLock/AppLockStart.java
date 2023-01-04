package com.example.eatmou.ui.appLock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.eatmou.R;
import com.example.eatmou.model.AppLock;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppLockStart extends AppCompatActivity {
    ConstraintLayout appLockLayout, splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock_start);

        appLockLayout = findViewById(R.id.appLockLayout);
        splashScreen = findViewById(R.id.splashScreen);

        //Set appLockLayout as invisible
        appLockLayout.setVisibility(View.INVISIBLE);

        //Check the user has set ap lock
        String currentUserID = FirebaseAuth.getInstance().getUid();
        if(currentUserID != null) {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("AppLock").document(currentUserID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            //Set visibility of appLockLayout
                            appLockLayout.setVisibility(View.VISIBLE);
                            splashScreen.setVisibility(View.GONE);

                            //Convert the document to AppLock object
                            AppLock appLock = new AppLock();
                            appLock = document.toObject(AppLock.class);
                            boolean check = false;
                            if(appLock != null) {
                                check = appLock.isExistPass();
                                if(check) {
                                    //TODO: Button check app lock password logic
                                } else {
                                    toMain();
                                }
                            }

                            Log.d("AppLock", "Successfully get the app lock details");
                        }
                    }
                }
            });
        }
    }

    private void toMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
}