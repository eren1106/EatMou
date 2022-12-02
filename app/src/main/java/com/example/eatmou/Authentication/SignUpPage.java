package com.example.eatmou.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.eatmou.R;

public class SignUpPage extends AppCompatActivity {
    Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setBackground(getResources().getDrawable(R.drawable.submitbtnbg));
    }
}