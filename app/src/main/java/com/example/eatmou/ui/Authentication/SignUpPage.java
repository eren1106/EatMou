package com.example.eatmou.ui.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eatmou.ui.homePage.MainActivity;
import com.example.eatmou.R;

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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

                //Click submit button to login page
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }
        });

    }
}