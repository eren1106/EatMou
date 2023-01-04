package com.example.eatmou.ui.ProfilePage.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eatmou.R;
import com.example.eatmou.model.AppLock;
import com.example.eatmou.ui.Authentication.LoginPage;
import com.example.eatmou.ui.ProfilePage.ProfilePage;
import com.example.eatmou.ui.appLock.AppLockStart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SettingsFragment extends Fragment {

    ImageView backBtn;
    Button appLockBtn;
    AppLock appLock = new AppLock();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        //Navigate to app lock page
        appLockBtn = view.findViewById(R.id.appLockBtn);
        appLockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check the user has passcode
                String currentUserID = FirebaseAuth.getInstance().getUid();
                if(currentUserID != null) {
                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("AppLock").document(currentUserID);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()) {
                                    //Convert the document to AppLock object
                                    appLock = document.toObject(AppLock.class);
                                    boolean check = false;
                                    if(appLock != null) {
                                        check = appLock.isExistPass();
                                        if(check) {
                                            //Navigate to appLockSetting
                                            startActivity(new Intent(getActivity(), AppLockSettings.class));
                                        } else {
                                            //Navigate to appLock
                                            startActivity(new Intent(getActivity(), com.example.eatmou.ui.Authentication.appLock.AppLock.class));
                                        }
                                    }

                                    Log.d("AppLock", "Successfully get the app lock details");
                                }
                            }
                        }
                    });
                }
            }
        });
        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        backBtn = view.findViewById(R.id.back_BtnSettings);
        backBtn.setOnClickListener(v -> replaceFragment(new ProfilePage()));
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profilePageFrame, fragment);
        fragmentTransaction.commit();
    }
}
