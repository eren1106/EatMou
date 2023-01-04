package com.example.eatmou.ui.ProfilePage.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.eatmou.R;
import com.example.eatmou.ui.Authentication.appLock.AppLock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppLockSettings extends AppCompatActivity {
    CardView changePasscode;
    CardView turnOffPasscode;
    ImageView backSettingPage;
    com.example.eatmou.model.AppLock appLock = new com.example.eatmou.model.AppLock();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_lock_setting);

        //Back top setting page
        backSettingPage = findViewById(R.id.backSettingPage);
        backSettingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changePasscode = findViewById(R.id.changePasscode);
        changePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AppLock.class));
            }
        });

        //Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Turning off");
        progressDialog.setMessage("Please wait...");

        turnOffPasscode = findViewById(R.id.turnOffPasscode);
        turnOffPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the progress dialog
                progressDialog.show();

                String currentUserID = FirebaseAuth.getInstance().getUid();
                if (currentUserID != null) {
                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("AppLock").document(currentUserID);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //Convert the document to AppLock object
                                    appLock = document.toObject(com.example.eatmou.model.AppLock.class);
                                    if (appLock != null) {
                                        appLock.setExistPass(false);
                                        docRef.set(appLock)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.hide();
                                                        Toast.makeText(AppLockSettings.this, "Successfully turn off passcode", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("passcode", e.getMessage());
                                                    }
                                                });
                                        finish();
                                    } else {
                                        Log.d("passcode","Error of getting appLock");
                                    }
                                } else {
                                    Toast.makeText(AppLockSettings.this, "Error of turning off", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AppLockSettings.this, "Error of turning off", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
