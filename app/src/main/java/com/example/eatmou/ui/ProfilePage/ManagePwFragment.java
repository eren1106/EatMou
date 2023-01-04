package com.example.eatmou.ui.ProfilePage;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
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

import com.example.eatmou.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ManagePwFragment extends Fragment {

    ImageView backBtn;
    EditText input_old_pw, input_new_pw, confirm_pw;
    Button confirm_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        input_old_pw = view.findViewById(R.id.input_old_pw);
        input_new_pw = view.findViewById(R.id.input_new_pw);
        confirm_pw = view.findViewById(R.id.confirm_pw);
        backBtn = view.findViewById(R.id.back_BtnManagePw);
        confirm_button = view.findViewById(R.id.confirm_button);

        confirm_button.setOnClickListener(view1 -> {
            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            String oldPw = input_old_pw.getText().toString();
            String newPw = input_new_pw.getText().toString();
            String conPw = confirm_pw.getText().toString();
            System.out.println(user.getEmail());
            if(!oldPw.equals("") && !newPw.equals("") && !conPw.equals("")) {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPw);
                if(newPw.equals(conPw)){
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPw).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d(TAG, "Password updated");
                                    Toast.makeText(getContext(), "Password Updated!", Toast.LENGTH_SHORT).show();
                                    input_old_pw.setText("");
                                    input_new_pw.setText("");
                                    confirm_pw.setText("");
                                }
                                else {
                                    Log.d(TAG, "Error password not updated");
                                    Toast.makeText(getContext(), "Password must contain at least six characters!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.d(TAG, "Wrong Password!");
                            Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(getContext(), "Password does not match", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(getContext(), "Please complete the fields", Toast.LENGTH_SHORT).show();
        });

        backBtn.setOnClickListener(v -> replaceFragment(new ProfilePage()));
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_pw, container, false);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profilePageFrame, fragment);
        fragmentTransaction.commit();
    }
}