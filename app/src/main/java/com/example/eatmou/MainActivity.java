package com.example.eatmou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eatmou.Authentication.ForgotPassPage;
import com.example.eatmou.Authentication.LoginPage;
import com.example.eatmou.Authentication.SignUpPage;
import com.example.eatmou.Authentication.TwoFactorAutheticationPage;
import com.example.eatmou.Authentication.UpdatePassPage;

public class MainActivity extends AppCompatActivity {

    Button toSignUpPage;
    Button toLoginPage;
    Button toForgotPassPage;
    Button toUpdatePassPage;
    Button toTwoFactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toSignUpPage = findViewById(R.id.toSignUpPage);

        toSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpPage.class));
            }
        });

        toLoginPage = findViewById(R.id.toLoginPage);
        toLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });

        toForgotPassPage = findViewById(R.id.toForgotPassPage);
        toForgotPassPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPassPage.class));
            }
        });

        toUpdatePassPage = findViewById(R.id.toUpdatePassPage);
        toUpdatePassPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdatePassPage.class));
            }
        });

        toTwoFactor = findViewById(R.id.toTwoFactor);
        toTwoFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TwoFactorAutheticationPage.class));
            }
        });

    }
}