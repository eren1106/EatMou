package com.example.eatmou.ui.FoodParty;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.ui.ProfilePage.settings.FirebaseMethods;
import com.example.eatmou.R;
import com.example.eatmou.model.UserModel;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class FoodPartyRecyclerViewAdapter extends RecyclerView.Adapter<FoodPartyRecyclerViewAdapter.MyViewHolder> {

    private final String CHANNEL_ID = "channel_01";
    private final int NOTIFICATION_ID = 0;

    Context context;
    ArrayList<FoodPartyModel> foodPartyModels;
    UserModel currentUser = MainActivity.user;
    FirebaseMethods firebaseMethods;
    int currentPosition;

    private OnCardListener onCardListener;

    public FoodPartyRecyclerViewAdapter(Context context, ArrayList<FoodPartyModel> foodPartyModels, OnCardListener onCardListener) {
        this.context = context;
        this.foodPartyModels = foodPartyModels;
        this.onCardListener = onCardListener;
    }

    @NonNull
    @Override
    public FoodPartyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_party_card, parent, false);
        return new FoodPartyRecyclerViewAdapter.MyViewHolder(view, this.onCardListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodPartyRecyclerViewAdapter.MyViewHolder holder, int position) {
        firebaseMethods = new FirebaseMethods();
        currentPosition = holder.getBindingAdapterPosition();
        createNotificationChannel();
        fetchOrganiserName(holder);
        holder.title.setText(foodPartyModels.get(currentPosition).getTitle());
        holder.location.setText(foodPartyModels.get(currentPosition).getLocation());
        holder.date.setText(foodPartyModels.get(currentPosition).getDateText());
        holder.time.setText(foodPartyModels.get(currentPosition).getStartTimeText() + " - " + foodPartyModels.get(currentPosition).getEndTimeText());
        holder.personNumber.setText(foodPartyModels.get(currentPosition).getJoinedPersons().size() + "/" + foodPartyModels.get(currentPosition).getMaxParticipant());

        setCardButton(holder, currentPosition);
    }

    private boolean checkJoined(int currentPosition) {
        for(String joinedPerson : foodPartyModels.get(currentPosition).getJoinedPersons()) {
            if(joinedPerson.equals(currentUser.getUserID())) return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return foodPartyModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // grabbing views from food_party_card layout file
        // kinda like in onCreate method

        TextView title, organizer, location, date, time, personNumber;
        TextView organiserLabel, locationLabel, dateLabel, timeLabel;
        Button cardBtn;
        OnCardListener onCardListener;

        public MyViewHolder(@NonNull View itemView, OnCardListener onCardListener) {
            super(itemView);

            title = itemView.findViewById(R.id.TV_CreatePartyTitle);
            organizer = itemView.findViewById(R.id.TV_OrganizerText);
            location = itemView.findViewById(R.id.TV_LocationText);
            date = itemView.findViewById(R.id.TV_DateText);
            time = itemView.findViewById(R.id.TV_TimeText);
            personNumber = itemView.findViewById(R.id.TV_JoinedPersonNumber);
            cardBtn = itemView.findViewById(R.id.B_CardBtn);

            organiserLabel = itemView.findViewById(R.id.TV_OrganizerLabel);
            locationLabel = itemView.findViewById(R.id.TV_LocationLabel);
            dateLabel = itemView.findViewById(R.id.TV_DateLabel);
            timeLabel = itemView.findViewById(R.id.TV_TimeLabel);

            this.onCardListener = onCardListener;

            itemView.setOnClickListener(this);
            changeFontSize();
        }

        @Override
        public void onClick(View view) {
            onCardListener.onCardClick(getAdapterPosition());
        }

        private void changeFontSize(){
            SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            int size = fontPreference.getInt("FONT_SP",0)-1;
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size+6);
            organizer.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            location.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            date.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            time.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            personNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, size+4);
            cardBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            organiserLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            locationLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            dateLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            timeLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        }
    }

    public interface OnCardListener{
        void onCardClick(int position);
    }

    private void setCardButton (FoodPartyRecyclerViewAdapter.MyViewHolder holder, int currentPosition) {
        if(foodPartyModels.get(currentPosition).getOrganiserId().equals(currentUser.getUserID())) {
            holder.cardBtn.setText("Manage");
            holder.cardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CreateFoodPartyActivity.class);
                    intent.putExtra("FoodPartyObject", foodPartyModels.get(currentPosition));
                    // extras
                    context.startActivity(intent);
                }
            });
        }
        else if(checkJoined(currentPosition)){
            holder.cardBtn.setText("Leave");
            holder.cardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> tempList = foodPartyModels.get(currentPosition).getJoinedPersons();
                    for (int i = 0; i < tempList.size(); i++) {
                        if(tempList.get(i).equals(currentUser.getUserID())){
                            tempList.remove(i);
                            break;
                        }
                    }
                    foodPartyModels.get(currentPosition).setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModels.get(currentPosition));
                    setCardButton(holder, currentPosition);
                }
            });
        }
        else {
            holder.cardBtn.setText("Join");
            holder.cardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(foodPartyModels.get(currentPosition).getJoinedPersons().size() >= foodPartyModels.get(currentPosition).getMaxParticipant()){
                        Toast.makeText(context, "This food party is already full!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        setReminder(foodPartyModels.get(currentPosition));
                        ArrayList<String> tempList = foodPartyModels.get(currentPosition).getJoinedPersons();
                        tempList.add(currentUser.getUserID());
                        foodPartyModels.get(currentPosition).setJoinedPersons(tempList);
                        firebaseMethods.updateFoodParty(foodPartyModels.get(currentPosition));
                        setCardButton(holder, currentPosition);
                    }
                }
            });
        }
    }

    private void fetchOrganiserName (FoodPartyRecyclerViewAdapter.MyViewHolder holder) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").document(foodPartyModels.get(currentPosition).getOrganiserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel tempUser = UserModel.toObject(document.getData());
                        holder.organizer.setText(tempUser.getUsername());
                    } else {
                        // Document with the specified ID does not exist
                    }
                } else {
                    // Task failed
                }
            }
        });
    }

    private void setReminder(FoodPartyModel foodPartyModel) {
        FirebaseFirestore.getInstance().collection("foodParties").document(foodPartyModel.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        FoodPartyModel fpm = FoodPartyModel.toObject(document.getData());
                        Calendar calendar = fpm.getCalendar();
                        calendar.add(Calendar.HOUR, -1); // show reminder before 1 hour

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.party_icon)
                                .setContentTitle("Food Party Reminder")
                                .setContentText(fpm.getTitle() + " starting soon at " + fpm.getStartTimeText())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        Calendar currentTime = Calendar.getInstance();
                        long triggerTime = System.currentTimeMillis();
                        if(currentTime.before(calendar)) {
                            triggerTime = calendar.getTimeInMillis();
                        }
                        builder.setWhen(triggerTime);
                        builder.setShowWhen(true);

                        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
                        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, NOTIFICATION_ID);
                        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, builder.build());

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis() % Integer.MAX_VALUE, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                    } else {
                        Log.d("Reminder", "No such document");
                    }
                } else {
                    Log.d("Reminder", "get failed with ", task.getException());
                }
            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder";
            String description = "My Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

