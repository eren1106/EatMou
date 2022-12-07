package com.example.eatmou.FoodParty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.eatmou.R;

public class CreateFoodPartyActivity extends AppCompatActivity {

    EditText title;
    ImageButton backBtn;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food_party);

        title = findViewById(R.id.ET_Title);
        backBtn = findViewById(R.id.B_BackBtn);
        createBtn = findViewById(R.id.B_CreateBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create food party logic

                finish();
            }
        });
    }
}