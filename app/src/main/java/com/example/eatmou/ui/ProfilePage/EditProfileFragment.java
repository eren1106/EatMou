package com.example.eatmou.ui.ProfilePage;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment {

    private View view;
    private EditText date, editNameField, editBioField, editLocationField;
    private DatePickerDialog datePickerDialog;
    private CircleImageView editProfileImg;
    private ImageView editBgImg;
    private ImageView backBtn;
    private Button saveButton;
    private Uri bgImageUri, profileImgUri;
    private FirebaseFirestore db;
    private String UserID;

    //global test
    String indication;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        editNameField = view.findViewById(R.id.editNameField);
        date = view.findViewById(R.id.date);
        editProfileImg = view.findViewById(R.id.editProfileImg);
        editBgImg = view.findViewById(R.id.editBgImg);
        editBioField = view.findViewById(R.id.editBioField);
        editLocationField = view.findViewById(R.id.editLocationField);

        backBtn = view.findViewById(R.id.back_BtnEditProfile);
        saveButton = view.findViewById(R.id.saveButton);

        Bundle args = this.getArguments();
        if(args != null) {
            UserID = args.getString("UserID");
            editNameField.setText(args.getString("Username"));
            date.setText(args.getString("Dob"));
            Glide.with(view).load(args.getString("ProfilePicUrl")).into(editProfileImg);
            Glide.with(view).load(args.getString("ProfileBgPicUrl")).into(editBgImg);
            editBioField.setText(args.getString("Bio"));
            editLocationField.setText(args.getString("Location"));
        }

        backBtn.setOnClickListener(v -> replaceFragment(new ProfilePage()));

        // perform click event on edit text
        date.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(date.getContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        // set day of month , month and year value in the edit text
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        // Edit Circle Profile Image
        editProfileImg.setOnClickListener(view12 -> {
            ImagePicker.with(EditProfileFragment.this)
                    .crop()	    		//Crop image(Optional), Check Customization for more option
                    .start(111);//Default Request Code is ImagePicker.REQUEST_CODE
            indication = "profileImg";
        });

        // Edit Background Image
        editBgImg.setOnClickListener(view13 -> {
            ImagePicker.with(EditProfileFragment.this)
                    .crop()	    		//Crop image(Optional), Check Customization for more option
                    .start(111);//Default Request Code is ImagePicker.REQUEST_CODE
            indication = "bgImg";
        });

        // Save Button
        saveButton.setOnClickListener(view14 -> {
//                String timestampString = "2022-01-01T00:00:00Z";
//                String format = "dd/MM/yyyy";
//
//                SimpleDateFormat sdf = new SimpleDateFormat(format);
//                Date date = null;
//                try {
//                    date = sdf.parse(timestampString);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Timestamp dob = new Timestamp(date);

//                Map<String, Object> user = new HashMap<>();
//                user.put("username", editNameField.getText().toString());
//                user.put("dob", dob);
//                user.put("profilePicUrl", editProfileImg);
//                user.put("profileBgPicUrl", "USA");
//                user.put("bio", editBioField.getText().toString());
//                user.put("location", editLocationField.getText().toString());

            if(!editNameField.getText().toString().equals(args.getString("Username"))){
                db.collection("users").document(UserID)
                        .update("username", editNameField.getText().toString());
            }
            if (!date.getText().toString().equals(args.getString("Dob"))){
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date userDOB = sdf.parse(date.getText().toString());
                    Timestamp timestampDOB = new Timestamp(userDOB);
                    db.collection("users").document(args.getString("UserID"))
                            .update("dob", timestampDOB);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(!editBioField.getText().toString().equals(args.getString("Bio"))){
                db.collection("users").document(UserID)
                        .update("bio", editBioField.getText().toString());
            }
            if(!editLocationField.getText().toString().equals(args.getString("Location"))){
                db.collection("users").document(UserID)
                        .update("username", editLocationField.getText().toString());
            }
            Toast.makeText(getContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
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