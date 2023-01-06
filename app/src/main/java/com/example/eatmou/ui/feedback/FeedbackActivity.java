package com.example.eatmou.ui.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatmou.R;
import com.example.eatmou.model.Feedback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FeedbackActivity extends AppCompatActivity {
    ImageView backSetting;
    SeekBar seekBar;
    TextView progressInt;
    TextView userExp, customerSupport, design, personalities, data_privacy, content;
    CardView userExpBtn, customerSupportBtn, DesignBtn, personalisationBtn, dataPrivacyBtn, contentBtn;
    Button confirmFeedbackBtn;
    int i = 1, j = 1, z = 1, y = 1, q = 1;
    int satisfaction_percent = 0;
    String[] improved = new String[6];

    //Declare fireStore and auth instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUserID = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //Back to setting page
        backSetting = findViewById(R.id.backSetting);
        backSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Set the changed value of seek bar on text view
        seekBar = findViewById(R.id.seekBar);
        progressInt = findViewById(R.id.progressInt);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressInt.setText(i + "%");
                satisfaction_percent = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Change the color of the card vieW when clicked
        userExp = findViewById(R.id.userExp);
        userExpBtn = findViewById(R.id.userExpBtn);
        userExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Increase 1 when CLICKED
                i += 1;
                if (i % 2 == 0) {
                    userExpBtn.setCardBackgroundColor(Color.parseColor("#FF5E51"));
                    userExp.setTextColor(Color.WHITE);

                    //Assign the data into the array
                    improved[0] = userExp.getText().toString();
                } else {
                    userExpBtn.setCardBackgroundColor(Color.parseColor("#FFEECC"));
                    userExp.setTextColor(Color.BLACK);

                    //Set the 0 index value as empty string
                    improved[0] = "";
                }
                Log.d("improve", "Array: " + Arrays.toString(improved));
            }
        });

        customerSupport = findViewById(R.id.customerSupport);
        customerSupportBtn = findViewById(R.id.customerSupportBtn);
        customerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Increase 1 when CLICKED
                j += 1;
                if (j % 2 == 0) {
                    customerSupportBtn.setCardBackgroundColor(Color.parseColor("#FF5E51"));
                    customerSupport.setTextColor(Color.WHITE);

                    improved[1] = customerSupport.getText().toString();

                } else {
                    customerSupportBtn.setCardBackgroundColor(Color.parseColor("#FFEECC"));
                    customerSupport.setTextColor(Color.BLACK);

                    improved[1] = "";
                }
            }
        });

        design = findViewById(R.id.design);
        DesignBtn = findViewById(R.id.DesignBtn);
        design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Increase 1 when CLICKED
                z += 1;
                if (z % 2 == 0) {
                    DesignBtn.setCardBackgroundColor(Color.parseColor("#FF5E51"));
                    design.setTextColor(Color.WHITE);

                    improved[2] = design.getText().toString();
                } else {
                    DesignBtn.setCardBackgroundColor(Color.parseColor("#FFEECC"));
                    design.setTextColor(Color.BLACK);

                    improved[2] = "";
                }
            }
        });

        personalities = findViewById(R.id.personalities);
        personalisationBtn = findViewById(R.id.personalisationBtn);
        personalities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Increase 1 when CLICKED
                y += 1;
                if (y % 2 == 0) {
                    personalisationBtn.setCardBackgroundColor(Color.parseColor("#FF5E51"));
                    personalities.setTextColor(Color.WHITE);

                    improved[3] = personalities.getText().toString();
                } else {
                    personalisationBtn.setCardBackgroundColor(Color.parseColor("#FFEECC"));
                    personalities.setTextColor(Color.BLACK);

                    improved[3] = "";
                }
            }
        });

        data_privacy = findViewById(R.id.data_privacy);
        dataPrivacyBtn = findViewById(R.id.dataPrivacyBtn);
        data_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Increase 1 when CLICKED
                q += 1;
                if (q % 2 == 0) {
                    dataPrivacyBtn.setCardBackgroundColor(Color.parseColor("#FF5E51"));
                    data_privacy.setTextColor(Color.WHITE);

                    improved[4] = data_privacy.getText().toString();
                } else {
                    dataPrivacyBtn.setCardBackgroundColor(Color.parseColor("#FFEECC"));
                    data_privacy.setTextColor(Color.BLACK);

                    improved[4] = "";
                }
            }
        });

        content = findViewById(R.id.content);
        contentBtn = findViewById(R.id.contentBtn);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Increase 1 when CLICKED
                q += 1;
                if (q % 2 == 0) {
                    contentBtn.setCardBackgroundColor(Color.parseColor("#FF5E51"));
                    content.setTextColor(Color.WHITE);

                    improved[5] = content.getText().toString();
                } else {
                    contentBtn.setCardBackgroundColor(Color.parseColor("#FFEECC"));
                    content.setTextColor(Color.BLACK);

                    improved[5] = "";
                }
            }
        });

        //Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Submit feedback");
        progressDialog.setMessage("Please wait...");

        //Confirm button to save the data in fireStore
        confirmFeedbackBtn = findViewById(R.id.confirmFeedbackBtn);
        confirmFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the progress dialog
                progressDialog.show();

                String improveString = "";
                for (int i = 0; i < improved.length; i++) {
                    if (improved[i] != null) {
                        if(!improved[i].isEmpty()) {
                            if(i == improved.length - 1) {
                                improveString += improved[i];
                            } else{
                                improveString += improved[i] + ",";
                            }
                        }
                    }
                }

                //Format the improvedString
                if(improveString.charAt(improveString.length() - 1) == ',') {
                    improveString = improveString.substring(0, improveString.length() - 1);
                }

                //Generate random id
                String id = UUID.randomUUID().toString();
                String id1 = UUID.randomUUID().toString();

                //Current date and time
                Date date = Calendar.getInstance().getTime();
                Feedback feedback = new Feedback(currentUserID, satisfaction_percent, improveString, date);
                Map<String, Object> map = feedback.toMap();

                if (currentUserID != null) {
                    //Create fireStore feedback document
                    db.collection("Feedback").document(currentUserID)
                            .collection(id).document(id1).set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.hide();
                                        Toast.makeText(FeedbackActivity.this, "Feedback is sent", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.hide();
                                    Toast.makeText(FeedbackActivity.this, "Error sending feedback", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}













