package com.example.eatmou.ui.Authentication.appLock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eatmou.R;
import com.example.eatmou.repository.authentication.AuthRepoImp;
import com.example.eatmou.ui.appLock.AppLockStart;
import com.example.eatmou.ui.splashScreen.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class AppLock extends AppCompatActivity {
    Button confirmBtn;
    EditText appLockPass, confirmAppLockPass;
    ImageView backProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        appLockPass = findViewById(R.id.appLockPass);
        confirmAppLockPass = findViewById(R.id.confirmAppLockPass);

        //Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating app lock");
        progressDialog.setMessage("Please wait...");

        //back to setting fragment
        backProfileBtn = findViewById(R.id.backProfileBtn);
        backProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Pass the password to the repo to perform the logic
        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the loading dialog
                progressDialog.show();

                String pass = appLockPass.getText().toString();
                String confirmPass = confirmAppLockPass.getText().toString();

                if(pass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(AppLock.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if(pass.length() == 4) {
                        if(pass.equals(confirmPass)) {
                            //Create fireStore instance
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            String currentUserID = FirebaseAuth.getInstance().getUid();

                            if(currentUserID != null) {
                                com.example.eatmou.model.AppLock appLock = new com.example.eatmou.model.AppLock(currentUserID, pass, true);
                                //Get map of appLock class
                                Map<String, Object> appLockMap = appLock.toMap();

                                db.collection("AppLock").document(currentUserID).set(appLockMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    progressDialog.hide();
                                                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                                                    finish();
                                                    Log.d("AppLock", "App lock successfully applied");
                                                } else {
                                                    Toast.makeText(AppLock.this, "Error creating app lock", Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(AppLock.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(AppLock.this, "4 digit password allowed ONLY", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}