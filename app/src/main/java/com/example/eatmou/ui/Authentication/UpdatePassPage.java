package com.example.eatmou.ui.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eatmou.model.Users;
import com.example.eatmou.ui.homePage.MainActivity;
import com.example.eatmou.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdatePassPage extends AppCompatActivity {

    ImageView back_home_arrow;
    EditText newPass, newConfirmPass;
    Button newPassSubmitBtn;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass_page);

        back_home_arrow = findViewById(R.id.back_home_arrow);

        back_home_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        //Get the new password
        newPass = findViewById(R.id.newPass);

        //Get the new confirm password
        newConfirmPass = findViewById(R.id.newConfirmPass);

        //Check the confirm password
        newPassSubmitBtn = findViewById(R.id.newPassSubmitBtn);
        newPassSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = newPass.getText().toString();
                String new_pass = newConfirmPass.getText().toString();
                if(pass.trim().isEmpty() || new_pass.trim().isEmpty()) {
                    Toast.makeText(UpdatePassPage.this, "Please fill all the field", Toast.LENGTH_SHORT).show();
                } else {
                    if(pass.equals(new_pass)) {
                        //Update password in firebase authentication
                        user.updatePassword(pass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(UpdatePassPage.this, "Password updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdatePassPage.this, "Error updating password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //Navigate to login page as password updated
                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                        overridePendingTransition(0,0);
                        finish();
                    } else {
                        Toast.makeText(UpdatePassPage.this, "The password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}