package com.example.eatmou.ui.FoodParty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eatmou.ui.ProfilePage.settings.FirebaseMethods;
import com.example.eatmou.R;
import com.example.eatmou.model.UserModel;
import com.example.eatmou.ui.homePage.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateFoodPartyActivity extends AppCompatActivity {

    TextView TV_CreatePartyTitle;

    UserModel currentUser;
    EditText etTitle;
    EditText etLocation;
    EditText etMaxPerson;
    ImageView backBtn;
    Button createBtn;

    TextView tvDate;
    Date date = new Date();

    TextView tvStartTime;
    TextView tvEndTime;
    Date startTime = new Date();
    Date endTime = new Date();

    int RESULT_OK = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food_party);

        currentUser = MainActivity.user;

        TV_CreatePartyTitle = findViewById(R.id.TV_CreatePartyTitle);
        etTitle = findViewById(R.id.ET_Title);
        etLocation = findViewById(R.id.ET_Location);
        etMaxPerson = findViewById(R.id.ET_MaxPerson);
        backBtn = findViewById(R.id.B_BackBtn);
        createBtn = findViewById(R.id.B_CreateBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvDate = findViewById(R.id.TV_Date);
        setupDatePicker(tvDate);

        tvStartTime = findViewById(R.id.TV_StartTime);
        tvEndTime = findViewById(R.id.TV_EndTime);

        setupCreateManage();
        setupTimePicker(tvStartTime, "startTime");
        setupTimePicker(tvEndTime, "endTime");

        Intent intent = getIntent();

        if (intent.hasExtra("restaurant name")) {
            String name = intent.getStringExtra("restaurant name");
            etLocation.setText(name);
        }
    }

    private void setupDatePicker(TextView tvDate) {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateFoodPartyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String selectedDate = day + "/"  + month + "/" + year;
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvDate.setText(selectedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
    private void setupTimePicker(TextView tv, String tvName) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] hour = new int[1];
                final int[] minute = new int[1];
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateFoodPartyActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                hour[0] = i;
                                minute[0] = i1;
                                String time = i + ":" + i1;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24Hours.parse(time);
                                    if(tvName.equals("startTime")) {
                                        startTime = date;
                                    }
                                    else{
                                        endTime = date;
                                    }
                                    SimpleDateFormat f12hours = new SimpleDateFormat("hh:mm aa");
                                    tv.setText(f12hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false);

                // set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //display previous selected time
                timePickerDialog.updateTime(hour[0], minute[0]);
                // show dialog
                timePickerDialog.show();
            }
        });
    }

    private void setupCreateManage() {
        FirebaseMethods firebaseMethods = new FirebaseMethods();
        FoodPartyModel foodPartyModel = (FoodPartyModel) getIntent().getSerializableExtra("FoodPartyObject");

        if(foodPartyModel == null) { // CREATE PARTY
            createBtn.setText("Create");

            createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create food party logic
                    String title = etTitle.getText().toString();
                    String location = etLocation.getText().toString();
                    String maxPersonString = etMaxPerson.getText().toString();

                    if(title.equals("") || location.equals("")
                            || date==null || startTime==null || endTime==null || maxPersonString.equals("")){
                        Toast.makeText(getApplicationContext(), "Please fill in every field", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseMethods.addFoodParty(etTitle.getText().toString(), currentUser.getUserID(), etLocation.getText().toString(), date, startTime, endTime, Integer.parseInt(etMaxPerson.getText().toString()));
                    Toast.makeText(getApplicationContext(), "Food Party Created!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else { // UPDATE PARTY
            TV_CreatePartyTitle.setText("Manage Party");
            createBtn.setText("Update");

            etTitle.setText(foodPartyModel.getTitle());
            etLocation.setText(foodPartyModel.getLocation());
            etMaxPerson.setText(Integer.toString(foodPartyModel.getMaxParticipant()));
            tvDate.setText(foodPartyModel.getDateText());
            tvStartTime.setText(foodPartyModel.getStartTimeText());
            tvEndTime.setText(foodPartyModel.getEndTimeText());
            date = foodPartyModel.getDate();
            startTime = foodPartyModel.getStartTime();
            endTime = foodPartyModel.getEndTime();

            createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String title = etTitle.getText().toString();
                    String location = etLocation.getText().toString();
                    String maxPersonString = etMaxPerson.getText().toString();

                    if(title.equals("") || location.equals("")
                            || date==null || startTime==null || endTime==null || maxPersonString.equals("")){
                        Toast.makeText(getApplicationContext(), "Please fill in every field", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FoodPartyModel updated = new FoodPartyModel(
                            foodPartyModel.getId(),
                            etTitle.getText().toString(),
                            foodPartyModel.getOrganiserId(),
                            etLocation.getText().toString(),
                            date,
                            startTime,
                            endTime,
                            Integer.parseInt(maxPersonString),
                            foodPartyModel.getJoinedPersons()
                    );
                    firebaseMethods.updateFoodParty(updated);

                    Toast.makeText(getApplicationContext(), "Food Party Updated!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("UpdatedFoodPartyObject", updated);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}