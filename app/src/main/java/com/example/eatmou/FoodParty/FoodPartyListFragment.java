package com.example.eatmou.FoodParty;

import android.app.ProgressDialog;
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

import com.example.eatmou.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodPartyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodPartyListFragment extends Fragment implements FoodPartyRecyclerViewAdapter.OnCardListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodPartyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodPartyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodPartyListFragment newInstance(String param1, String param2) {
        FoodPartyListFragment fragment = new FoodPartyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<FoodPartyModel> foodPartyModels = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

        ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        eventListener = new EventListener<QuerySnapshot>() { // addSnapshotListener -> will update the recyclerview whenever there is data change in database
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        foodPartyModels.add(FoodPartyModel.toObject(dc.getDocument().getData()));
                    }
//                    else if(dc.getType() == DocumentChange.Type.REMOVED) {
//                        foodPartyModels.remove(FoodPartyModel.toObject(dc.getDocument().getData()));
//                    }

                    adapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        };

        fetchData();

        setupCreateButton(view);

        setupToggleButton(view);

//        if(foodPartyModels.size() == 0 && progressDialog.isShowing())
//            progressDialog.dismiss();

        // Inflate the layout for this fragment
        return view;
    }

    private void fetchData() {
        foodPartyModels.clear();
        if(listenerRegistration != null) {
            listenerRegistration.remove();
        }
        if(showMine) {
            listenerRegistration = firestore.collection("foodParties").whereEqualTo("organiserId", "temp").addSnapshotListener(eventListener);
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

//    @Override
//    public void onStop() {
//        super.onStop();
//        if(listenerRegistration != null) {
//            listenerRegistration.remove(); // if no remove, it will still run when your app is closed
//        }
//    }
}