package com.example.eatmou.ui.ProfilePage;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.TypedValue;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment {

    private EditText date, editNameField, editBioField, editLocationField;
    private EditText k1, k2, k3;
    private CircleImageView editProfileImg;
    private ImageView editBgImg;
    private ImageView backBtn;
    private ImageView calendar_icon;
    private Button saveButton;
    private Uri bgImageUri, profileImgUri;
    private FirebaseFirestore db;
    private String UserID;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imageURL;
    String[] arr;

    //global test
    String indication;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        editNameField = view.findViewById(R.id.editNameField);
        date = view.findViewById(R.id.date);
        calendar_icon = view.findViewById(R.id.calendar_icon);
        editProfileImg = view.findViewById(R.id.editProfileImg);
        editBgImg = view.findViewById(R.id.editBgImg);
        editBioField = view.findViewById(R.id.editBioField);
        editLocationField = view.findViewById(R.id.editLocationField);
        k1 = view.findViewById(R.id.keyword1);
        k2 = view.findViewById(R.id.keyword2);
        k3 = view.findViewById(R.id.keyword3);

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
            arr = args.getStringArray("Keywords");
            System.out.println(Arrays.toString(arr));
            k1.setText(arr[0]);
            k2.setText(arr[1]);
            k3.setText(arr[2]);
        }

        backBtn.setOnClickListener(v -> replaceFragment(new ProfilePage()));

        // Edit DOB
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").build();
        calendar_icon.setOnClickListener(view15 -> {
            datePicker.show(getParentFragmentManager(), "Material Date Picker");
            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis((Long)selection);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate  = format.format(calendar.getTime());
                    date.setText(formattedDate);
                }
            });
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
            //edit name
            if(!editNameField.getText().toString().equals(args.getString("Username"))){
                db.collection("users").document(UserID)
                        .update("username", editNameField.getText().toString());
            }

            //edit DOB
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

            //edit Bio
            if(!editBioField.getText().toString().equals(args.getString("Bio"))){
                db.collection("users").document(UserID)
                        .update("bio", editBioField.getText().toString());
            }

            //edit Address
            if(!editLocationField.getText().toString().equals(args.getString("Location"))){
                db.collection("users").document(UserID)
                        .update("location", editLocationField.getText().toString());
            }
            Toast.makeText(getContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();


            DocumentReference keywordRef = db.collection("users").document(UserID);
            //k1
            String text1 = arr[0];
            String text2 = arr[1];
            String text3 = arr[2];
            if(!k1.getText().toString().equals(text1)
                    || !k2.getText().toString().equals(text2)
                    || !k3.getText().toString().equals(text3)){
                if(k1.getText().toString().equals("")
                        || k2.getText().toString().equals("")
                        || k3.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Please fill in every field", Toast.LENGTH_SHORT).show();
                }
                else{
                    keywordRef.update("Keywords.0", k1.getText().toString());
                    keywordRef.update("Keywords.1", k2.getText().toString());
                    keywordRef.update("Keywords.2", k3.getText().toString());
                }
            }
        });

        changeFontSize();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
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
                    uploadImage("profilePicUrl", profileImgUri);
                    break;

                case "bgImg":
                    bgImageUri = data.getData();
                    editBgImg.setImageURI(bgImageUri);
                    uploadImage("profileBgPicUrl", bgImageUri);
                    break;

                default:
                    Toast.makeText(requireContext(),"Test", Toast.LENGTH_SHORT).show();
            }

        }else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String imageField, Uri imageUri){
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"  + UUID.randomUUID().toString());

            ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Picture Uploaded!", Toast.LENGTH_SHORT).show();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURL = uri.toString();
                        System.out.println(imageURL);
                        updateImage(imageField, imageURL);
                    }
                });

            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int)progress + "%");
            });
        }
    }

    private void updateImage(String imageField, String imageURL){
        db.collection("users").document(UserID).update(imageField, imageURL);
    }

    private void changeFontSize(){
        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        int size = fontPreference.getInt("FONT_SP",0);
        editNameField.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        date.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        editBioField.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        editLocationField.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        k1.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        k2.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        k3.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}