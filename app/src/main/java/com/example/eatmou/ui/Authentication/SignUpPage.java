package com.example.eatmou.ui.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eatmou.ui.homePage.MainActivity;
import com.example.eatmou.R;
import com.example.eatmou.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class SignUpPage extends AppCompatActivity {
    Button signUpBtn;
    ImageView back_home_arrow;
    EditText username, email, pass, confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        signUpBtn = findViewById(R.id.signUpBtn);

        //Back to home page for testing
        back_home_arrow = findViewById(R.id.back_home_arrow);
        back_home_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Check the field in username, email, password, confirmPass
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPass);

        //Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sign up");
        progressDialog.setMessage("Please wait...");

        //Check any empty field haven't fill
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameTemp = username.getText().toString();
                String emailTemp = email.getText().toString();
                String passTemp = pass.getText().toString();
                String confirmPassTemp = confirmPass.getText().toString();
                if(usernameTemp.isEmpty() || emailTemp.isEmpty() || passTemp.isEmpty() || confirmPassTemp.isEmpty()) {
                    Toast.makeText(SignUpPage.this, "Please filled up the all the field", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Check the password is matched or not
                if(!passTemp.equals(confirmPassTemp)) {
                    Toast.makeText(SignUpPage.this, "Password not correct", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Show loading dialog
                progressDialog.show();

                // SIGN UP USER
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                auth.createUserWithEmailAndPassword(emailTemp, passTemp).addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Auth", "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();

                            Date tempDate = new Date(); // mock

                            UserModel userModel = new UserModel(user.getUid(), usernameTemp, emailTemp, tempDate, "", "", "", "");
                            firestore.collection("users").document(user.getUid()).set(userModel.toMap());
                            //Hide the progress dialog
                            progressDialog.hide();

                            //Click submit button to login page
                            startActivity(new Intent(getApplicationContext(), LoginPage.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Auth", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}