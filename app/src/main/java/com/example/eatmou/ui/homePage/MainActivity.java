package com.example.eatmou.ui.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.example.eatmou.ui.FoodParty.FoodPartyListFragment;
import com.example.eatmou.R;
import com.example.eatmou.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomBarChart;
    BottomNavigationView bottomBarViewChart;
    FloatingActionButton partyPageBtn;
    public static UserModel user;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchUser();

        //Set the background of bottomAppBar as null
        bottomBarChart = findViewById(R.id.bottomBarChart);
        bottomBarChart.setBackground(null);

        //Set the fragment replace layout
        bottomBarViewChart = findViewById(R.id.bottomBarViewChart);
        bottomBarViewChart.setOnNavigationItemSelectedListener(navListener);
        //TODO: Disable the default selected item in bottomNavBar

        //Default launch matching page
        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();

        //Click to launch Matching page
        partyPageBtn = findViewById(R.id.partyPageBtn);
        partyPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.party:
                    selectedFragment = new FoodPartyListFragment();
                    break;
                default:
                    return false;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
            return true;
        }
    };

    private void fetchUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = UserModel.toObject(document.getData());
                        Log.d("USER", user.toString());
                    } else {
                        Log.e("USER", "User not exists");
                    }
                } else {
                    Log.e("USER", "User error");
                }
            }
        });
    }
}