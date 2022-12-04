package com.example.eatmou.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eatmou.MainActivity;
import com.example.eatmou.R;

public class SignUpPage extends AppCompatActivity {
    Button signUpBtn;
    ImageView back_home_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setBackground(getResources().getDrawable(R.drawable.submitbtnbg));

        back_home_arrow = findViewById(R.id.back_home_arrow);
        back_home_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}