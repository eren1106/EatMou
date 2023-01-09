package com.example.eatmou.ui.ProfilePage.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eatmou.CommunityGuideline;
import com.example.eatmou.FontSettingActivity;
import com.example.eatmou.R;
import com.example.eatmou.model.AppLock;
import com.example.eatmou.ui.Authentication.LoginPage;
import com.example.eatmou.ui.ProfilePage.PrivacyPolicyActivity;
import com.example.eatmou.ui.ProfilePage.ProfilePage;
import com.example.eatmou.ui.ProfilePage.TermsOfService;
import com.example.eatmou.ui.feedback.FeedbackActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SettingsFragment extends Fragment {

    ImageView backBtn;
    Button appLockBtn, feedbackBtn, privacy_policy_button, termsofservice_button, community_guideline_button, fontSizeBtn;
    AppLock appLock = new AppLock();
    Button btnLogOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        community_guideline_button = view.findViewById(R.id.community_guideline_button);
        community_guideline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CommunityGuideline.class));
            }
        });

        privacy_policy_button = view.findViewById(R.id.privacy_policy_button);
        privacy_policy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
            }
        });

        termsofservice_button = view.findViewById(R.id.termsofservice_button);
        termsofservice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TermsOfService.class));
            }
        });

        //Navigate to feedback apge
        feedbackBtn = view.findViewById(R.id.feedbackBtn);
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
            }
        });

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
                                } else {
                                    startActivity(new Intent(getActivity(), com.example.eatmou.ui.Authentication.appLock.AppLock.class));
                                }
                            } else {
                                startActivity(new Intent(getActivity(), com.example.eatmou.ui.Authentication.appLock.AppLock.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startActivity(new Intent(getActivity(), com.example.eatmou.ui.Authentication.appLock.AppLock.class));
                        }
                    });
                }
            }
        });

        fontSizeBtn = view.findViewById(R.id.fontSizeBtn);
        fontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FontSettingActivity.class));
            }
        });
        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn = view.findViewById(R.id.back_BtnSettings);
        backBtn.setOnClickListener(v -> replaceFragment(new ProfilePage()));

        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginPage.class);
            startActivity(intent);
            getActivity().finish();
        });

        changeFontSize();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profilePageFrame, fragment);
        fragmentTransaction.commit();
    }

    private void changeFontSize(){
        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        int size = fontPreference.getInt("FONT_SP",0);
        feedbackBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        privacy_policy_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        termsofservice_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        community_guideline_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        appLockBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        fontSizeBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        btnLogOut.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}
