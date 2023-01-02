package com.example.eatmou.ui.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatmou.model.Users;
import com.example.eatmou.ui.homePage.MainActivity;
import com.example.eatmou.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ForgotPassPage extends AppCompatActivity {
    ImageView back_home_arrow;
    EditText code1, code2, code3, code4;
    TextView verifyEmail;
    Button confirmPinBtn;
    int randNumCode = 0;
    String code = "";
    String verifiedCode = "";
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_page);

        back_home_arrow = findViewById(R.id.back_home_arrow);

        back_home_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);

        //Autofocus on pin code
        numberMove();

        //Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");

        //Alert dialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View emailDialogView = factory.inflate(R.layout.email_dialog, null);
        final AlertDialog emailDialog = new AlertDialog.Builder(this).create();
        emailDialog.setView(emailDialogView);
        emailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Get the email from dialog
        EditText emailVerified = emailDialogView.findViewById(R.id.emailVerified);
        Button verifiedBtn = emailDialogView.findViewById(R.id.verifiedBtn);

        //Check sdk
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Verification Code","Verification Code", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //Verify email
        verifyEmail = findViewById(R.id.verifyEmail);
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the email dialog
                emailDialog.show();

                verifiedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Get email from email dialog
                        email = emailVerified.getText().toString();

                        //Get the users document
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference docRef = db.collection("users");
                        docRef.get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //Show the progress dialog
                                progressDialog.show();
                                if(task.isSuccessful()) {
                                    QuerySnapshot document = task.getResult();
                                    List<Users> usersList = document.toObjects(Users.class);

                                    //Search the email is existing or not
                                    boolean exist = false;
                                    for(int i = 0; i < usersList.size(); i++) {
                                        String existingUserEmail = usersList.get(i).getEmail();
                                        if(email.equals(existingUserEmail)) {
                                            exist = true;
                                            break;
                                        }
                                    }

                                    //Check the existing of email
                                    if(exist) {
                                        Toast.makeText(ForgotPassPage.this, "Verified email", Toast.LENGTH_SHORT).show();
                                        progressDialog.hide();
                                        emailDialog.dismiss();

                                        //Random code
                                        randNumCode = (int)(Math.random() * 8999 + 1000);

                                        //Notification builder
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ForgotPassPage.this, "Verification Code");
                                        builder.setContentTitle("Verification Code");
                                        builder.setContentText("Your verification code is " + randNumCode);
                                        builder.setSmallIcon(R.drawable.code_fork);
                                        builder.setAutoCancel(true);

                                        //Setup notification for verification code
                                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ForgotPassPage.this);
                                        managerCompat.notify(1, builder.build());
                                    } else {
                                        Toast.makeText(ForgotPassPage.this, "Email is not yet registered", Toast.LENGTH_SHORT).show();
                                        progressDialog.hide();
                                        emailDialog.dismiss();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

        //Clicked continue button to check the verification code
        confirmPinBtn = findViewById(R.id.confirmPinBtn);
        confirmPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check verification code
                if(code.equals(String.valueOf(randNumCode))) {
                    //Redirect to email link to reset password
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ForgotPassPage.this, "Please check your email", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                    overridePendingTransition(0,0);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ForgotPassPage.this, "Error sending link", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(ForgotPassPage.this, "The verification code entered is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void numberMove() {
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code += charSequence.toString();
                if(!charSequence.toString().trim().isEmpty()) {
                    code2.requestFocus();
                } else {
                    code = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code += charSequence.toString();
                if(!charSequence.toString().trim().isEmpty()) {
                    code3.requestFocus();
                } else {
                    code1.requestFocus();
                    code = code.substring(0,1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code += charSequence.toString();
                if(!charSequence.toString().trim().isEmpty()) {
                    code4.requestFocus();
                } else {
                    code2.requestFocus();
                    code = code.substring(0,2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    code += charSequence.toString();
                } else {
                    code3.requestFocus();
                    code = code.substring(0,3);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}