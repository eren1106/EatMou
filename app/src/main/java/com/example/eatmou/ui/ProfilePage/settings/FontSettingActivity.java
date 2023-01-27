package com.example.eatmou.ui.ProfilePage.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eatmou.R;

public class FontSettingActivity extends AppCompatActivity {

    private Button mid, big;
    ImageView font_size_back_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_setting);

        font_size_back_Btn = findViewById(R.id.font_size_back_Btn);

        mid = findViewById(R.id.midBtn);
        big = findViewById(R.id.bigBtn);

        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = fontPreference.edit();

        mid.setOnClickListener(view -> {
            editor.putInt("FONT_SP", 16);
            editor.apply();
            changeFontSize();
            createToast();
        });

        big.setOnClickListener(view -> {
            editor.putInt("FONT_SP", 22);
            editor.apply();
            changeFontSize();
            createToast();
        });

        font_size_back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changeFontSize();
    }

    private void changeFontSize(){
        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int size = fontPreference.getInt("FONT_SP",0);
        mid.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        big.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    private void createToast(){
        Toast.makeText(this, "Please restart the app to apply the changes", Toast.LENGTH_SHORT).show();
    }
}