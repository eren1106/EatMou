package com.example.eatmou.ui.homePage.InviteDialog;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eatmou.R;
import com.example.eatmou.model.Invitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class InviteDialog extends DialogFragment {
    private EditText input_date;
    private TimePickerDialog _timePickerDialog;
    private TextView displayTimeStart;
    private TextView displayTimeEnd;
    private Button inviteBtn;
    Date date = new Date();
    Date startTime = new Date();
    Date endTime = new Date();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_invite_dialog, container, false);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        //Input restaurant
        EditText input_restaurant = view.findViewById(R.id.input_restaurant);

        //Date picker
        ImageView calendar_icon = view.findViewById(R.id.calendar_icon);
        input_date = view.findViewById(R.id.input_date);
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date").build();
        calendar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getParentFragmentManager(), "Material Date Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(datePicker.getHeaderText());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        input_date.setText(datePicker.getHeaderText());
                    }
                });
            }
        });

        //Time picker
        RelativeLayout time1 = view.findViewById(R.id.time1);
        displayTimeStart = view.findViewById(R.id.displayTimeStart);
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_TimePickerDialog(displayTimeStart);
            }
        });


        RelativeLayout time2 = view.findViewById(R.id.time2);
        displayTimeEnd = view.findViewById(R.id.displayTimeEnd);
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_TimePickerDialog(displayTimeEnd);
            }
        });

        //Current userID
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        String tap_Username = sharedPreferences.getString("USERNAME_SHARED_PREF","");

        //Invite button
        inviteBtn = view.findViewById(R.id.inviteBtn);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get restaurant
                String restaurantName = input_restaurant.getText().toString();

                //Generate random id
                String id = UUID.randomUUID().toString();

                //Create invitation object
                Invitation invitation = new Invitation(id, userID, tap_Username, restaurantName,date,startTime,endTime,"Pending");
                Map<String, Object> map = invitation.toMap();

                //Sent the invitation to fireStore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Invitations").document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            getDialog().dismiss();
                            Toast.makeText(getContext(), "Invitation sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Invitation fail to send out", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private void open_TimePickerDialog(TextView displayTime) {
        int hourOfDay = 23;
        int minute = 55;
        boolean is24HourView = true;

        _timePickerDialog= new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int intHourOFDay, int intMinute) {
                //Time format
                Time time_format = new Time(intHourOFDay,intMinute,0);//seconds by default set to zero
                Format formatter = new SimpleDateFormat("h:mm a");

                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                try {
                    Date date = f24Hours.parse(time_format.toString());
                    if(displayTime.getText().equals("Start")) {
                        startTime = date;
                    } else {
                        endTime = date;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Set the time in textview
                displayTime.setText(formatter.format(time_format));
            }
        },hourOfDay,minute,is24HourView);

        //Display the time picker dialog
        _timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        _timePickerDialog.setTitle("Select time");
        _timePickerDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(950, 1400);
    }
}
