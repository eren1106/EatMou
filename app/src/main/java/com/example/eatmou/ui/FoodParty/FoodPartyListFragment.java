package com.example.eatmou.ui.FoodParty;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatmou.R;
import com.example.eatmou.model.UserModel;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FoodPartyListFragment extends Fragment implements FoodPartyRecyclerViewAdapter.OnCardListener {

    ArrayList<FoodPartyModel> foodPartyModels = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    UserModel currentUser = MainActivity.user;
    TextView tvNoFoodParties;

    boolean showMine = false;
    EventListener<QuerySnapshot> eventListener;
    ListenerRegistration listenerRegistration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_party_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.RV_FoodParty);
        recyclerView.setHasFixedSize(true);
        FoodPartyRecyclerViewAdapter adapter = new FoodPartyRecyclerViewAdapter(this.getActivity(), foodPartyModels, this); // getActivity => context
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());

        tvNoFoodParties = view.findViewById(R.id.TV_NoFoodParties);
        tvNoFoodParties.setVisibility(View.INVISIBLE);

//        ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();

        eventListener = new EventListener<QuerySnapshot>() { // addSnapshotListener -> will update the recyclerview whenever there is data change in database
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.isEmpty()) {
                    foodPartyModels.clear();
                    adapter.notifyDataSetChanged();
                    tvNoFoodParties.setVisibility(View.VISIBLE);
                    return;
                }
                if (error != null) {
//                    if(progressDialog.isShowing())
//                        progressDialog.dismiss();
                    Log.e("Firestore error", error.getMessage());
                    Toast.makeText(getActivity(), "error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        foodPartyModels.add(FoodPartyModel.toObject(dc.getDocument().getData()));
                    }
                    else if(dc.getType() == DocumentChange.Type.REMOVED) {
                        FoodPartyModel temp = FoodPartyModel.toObject(dc.getDocument().getData());

                        for(int i = 0; i<foodPartyModels.size(); i++) {
                            if(foodPartyModels.get(i).getId().equals(temp.getId())){
                                foodPartyModels.remove(i);
                                break;
                            }
                        }
                    }
                    else if(dc.getType() == DocumentChange.Type.MODIFIED) {
                        FoodPartyModel temp = FoodPartyModel.toObject(dc.getDocument().getData());

                        for(int i = 0; i<foodPartyModels.size(); i++) {
                            if(foodPartyModels.get(i).getId().equals(temp.getId())){
                                foodPartyModels.set(i, temp);
                                break;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    tvNoFoodParties.setVisibility(View.INVISIBLE);
//                    if(progressDialog.isShowing())
//                        progressDialog.dismiss();
                }
            }
        };

        fetchData();

        setupCreateButton(view);

        setupToggleButton(view);

//        setupLogout(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void fetchData() {
        foodPartyModels.clear();
        if(listenerRegistration != null) {
            listenerRegistration.remove();
        }
        if(showMine) {
            listenerRegistration = firestore.collection("foodParties").whereEqualTo("organiserId", currentUser.getUserID()).addSnapshotListener(eventListener);
        }
        else {
            listenerRegistration = firestore.collection("foodParties").addSnapshotListener(eventListener);
        }
    }

    private void setupCreateButton(View view) {
        Button createBtn = view.findViewById(R.id.B_FoodPartyTopBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateFoodPartyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupToggleButton(View view) {
        Button toggleButton = view.findViewById(R.id.B_Toggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showMine) {
                    toggleButton.setText("All");
                    showMine = false;
                }
                else{
                    toggleButton.setText("Mine");
                    showMine = true;
                }
                fetchData();
            }
        });
    }
    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(getActivity(), FoodPartyDetailActivity.class);
        intent.putExtra("FoodPartyObject", foodPartyModels.get(position));
        startActivity(intent);
    }

//    private void setupLogout(View view) {
//        TextView tvTitle = view.findViewById(R.id.PageTitle);
//        tvTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getActivity(), LoginPage.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
//    }
}