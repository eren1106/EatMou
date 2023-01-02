package com.example.eatmou.ui.ProfilePage;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eatmou.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment {

    View view;
    EditText date;
    DatePickerDialog datePickerDialog;
    CircleImageView editProfileImg;
    ImageView editBgImg;
    ImageView backBtn;
    private Uri bgImageUri, profileImgUri;

    //global test
    String indication;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        editProfileImg = view.findViewById(R.id.editProfileImg);
        editBgImg = view.findViewById(R.id.editBgImg);
        backBtn = view.findViewById(R.id.back_BtnEditProfile);
        backBtn.setOnClickListener(v -> replaceFragment(new ProfilePage()));

        // {Date Picker} initiate the date picker and a button
        date = (EditText) view.findViewById(R.id.date);
        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(date.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        view.findViewById(R.id.editProfileImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditProfileFragment.this)
                        .galleryOnly()	//User can only select image from Gallery
                        .start(111);//Default Request Code is ImagePicker.REQUEST_CODE
                indication = "profileImg";
            }
        });

        view.findViewById(R.id.editBgImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditProfileFragment.this)
                        .galleryOnly()	//User can only select image from Gallery
                        .start(111);//Default Request Code is ImagePicker.REQUEST_CODE
                indication = "bgImg";
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
       return view;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profilePageFrame, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 & resultCode == RESULT_OK) {

            switch (indication){
                case "profileImg":
                    profileImgUri = data.getData();
                    editProfileImg.setImageURI(profileImgUri);
                    break;

                case "bgImg":
                    bgImageUri = data.getData();
                    editBgImg.setImageURI(bgImageUri);
                    break;
                default:
                    Toast.makeText(requireContext(),"Test", Toast.LENGTH_SHORT).show();
            }

        }else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}