package com.example.eatmou.ui.homePage.InviteDialog;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eatmou.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;

public class InviteDialog extends DialogFragment {
    private EditText input_date;
    private TimePickerDialog _timePickerDialog;
    private TextView displayTimeStart;
    private TextView displayTimeEnd;

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
        String restaurantName = input_restaurant.getText().toString();

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
