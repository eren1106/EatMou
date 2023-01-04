package com.example.eatmou.ui.appLock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatmou.R;
import com.example.eatmou.model.AppLock;
import com.example.eatmou.ui.Authentication.LoginPage;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AppLockStart extends AppCompatActivity {
    ConstraintLayout appLockLayout, splashScreen;
    RelativeLayout relativeLayout4;
    TextView num1,num2,num3,num4,num5,num6,num7,num8,num9,num0;
    ImageView eraseIcon, tickIcon, lockIcon;
    ImageView appCode1,appCode2,appCode3,appCode4;
    MutableLiveData<String> listen = new MutableLiveData<>();
    AppLock appLock = new AppLock();
    String appLockPassword = "";
    int lengthOri = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock_start);

        appLockLayout = findViewById(R.id.appLockLayout);
        splashScreen = findViewById(R.id.splashScreen);

        //Set appLockLayout as invisible
        appLockLayout.setVisibility(View.INVISIBLE);

        //Number pad functionality
        numberPad();

        //Erase function
        eraseIcon = findViewById(R.id.eraseIcon);
        eraseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = appLockPassword.length();
                if(length > 0 && length <= 4) {
                    appLockPassword = appLockPassword.substring(0, length - 1);
                    listen.setValue(appLockPassword);
                }
            }
        });

        //Dot change function
        appCode1 = findViewById(R.id.appCode1);
        appCode2 = findViewById(R.id.appCode2);
        appCode3 = findViewById(R.id.appCode3);
        appCode4 = findViewById(R.id.appCode4);
        List<ImageView> dotList = new ArrayList<>();
        dotList.add(appCode1);
        dotList.add(appCode2);
        dotList.add(appCode3);
        dotList.add(appCode4);

        listen.observe(this,new Observer<String>() {
            @Override
            public void onChanged(String changedValue) {
                //Do something with the changed value
                int ind = changedValue.length();
                Log.d("clicked","ind = " + ind + "\nLengthOri = " + lengthOri);
                if(ind < lengthOri) {
                    //When the appLockPassword is erase set the black dot become grey dot
                    dotList.get(ind).setImageResource(R.drawable.dot_grey);
                    lengthOri -= 1;
                } else {
                    dotList.get(ind - 1).setImageResource(R.drawable.dot_black);
                }
            }
        });

        //Check the user has set ap lock
        lockIcon = findViewById(R.id.lockIcon);
        relativeLayout4 = findViewById(R.id.relativeLayout4);

        String currentUserID = FirebaseAuth.getInstance().getUid();
        if(currentUserID != null) {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("AppLock").document(currentUserID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            //Set visibility of appLockLayout
                            appLockLayout.setVisibility(View.VISIBLE);
                            splashScreen.setVisibility(View.GONE);

                            //Convert the document to AppLock object
                            appLock = document.toObject(AppLock.class);
                            boolean check = false;
                            if(appLock != null) {
                                check = appLock.isExistPass();
                                if(check) {
                                    //Tick function
                                    tickIcon = findViewById(R.id.tickIcon);
                                    tickIcon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(appLockPassword != null) {
                                                if(appLockPassword.equals(appLock.getPassword())) {
                                                    //Change the lock to unlock icon
                                                    lockIcon.setImageResource(R.drawable.unlock_icon);

                                                    //Add animation of rotation of unlock
                                                    lockIcon.startAnimation(AnimationUtils.loadAnimation(
                                                            getApplicationContext(),
                                                            R.anim.rotate
                                                    ));

                                                    //Delay action
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //Navigate to main activity
                                                            toMain();
                                                        }
                                                    }, 500);
                                                } else {
                                                    Toast.makeText(AppLockStart.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(AppLockStart.this, "Password incomplete", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    toMain();
                                }
                            }

                            Log.d("AppLock", "Successfully get the app lock details");
                        }
                    }
                }
            });
        } else {
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            finish();
        }
    }

    private void toMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void numberPad() {
        //NumberPad 0
        num0 = findViewById(R.id.num0);
        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num0.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num1 = findViewById(R.id.num1);
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num1.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num2 = findViewById(R.id.num2);
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num2.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num3 = findViewById(R.id.num3);
        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num3.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num4 = findViewById(R.id.num4);
        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num4.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num5 = findViewById(R.id.num5);
        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num5.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num6 = findViewById(R.id.num6);
        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num6.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num7 = findViewById(R.id.num7);
        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num7.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num8 = findViewById(R.id.num8);
        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num8.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });

        num9 = findViewById(R.id.num9);
        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appLockPassword.length() < 4) {
                    appLockPassword += num9.getText().toString();
                    listen.setValue(appLockPassword);
                    lengthOri = appLockPassword.length();
                }
            }
        });
    }
}













