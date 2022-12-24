package com.example.eatmou.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.eatmou.HomePage.MainActivity;
import com.example.eatmou.R;

public class UpdatePassPage extends AppCompatActivity {

    ImageView back_home_arrow;

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
    }
}