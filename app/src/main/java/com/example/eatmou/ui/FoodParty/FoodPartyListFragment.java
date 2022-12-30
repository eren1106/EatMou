package com.example.eatmou.ui.FoodParty;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eatmou.R;

import java.util.ArrayList;
import java.util.Arrays;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setupData();

        View view = inflater.inflate(R.layout.fragment_food_party_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.RV_FoodParty);
        recyclerView.setHasFixedSize(true);

        FoodPartyRecyclerViewAdapter adapter = new FoodPartyRecyclerViewAdapter(this.getActivity(), foodPartyModels, this); // getActivity => context
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());

        Button createBtn = view.findViewById(R.id.B_FoodPartyTopBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateFoodPartyActivity.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void setupData() {
        JoinedPersonModel jp1 = new JoinedPersonModel("Eren", R.drawable.eren);
        JoinedPersonModel jp2 = new JoinedPersonModel("Ali", R.drawable.eren);
        JoinedPersonModel jp3 = new JoinedPersonModel("Akau", R.drawable.eren);
        JoinedPersonModel jp4 = new JoinedPersonModel("Muthu", R.drawable.eren);
        JoinedPersonModel jp5 = new JoinedPersonModel("Jibai", R.drawable.eren);
        JoinedPersonModel jp6 = new JoinedPersonModel("Lancao", R.drawable.eren);

        FoodPartyModel fp1 = new FoodPartyModel(
                "Vegetarian gathering",
                "Eren Yeager",
                "Lai Vege Restaurant",
                "06/09/2022",
                "09:00 p.m.",
                new ArrayList<>(Arrays.asList(jp1, jp2, jp3))
        );

        FoodPartyModel fp2 = new FoodPartyModel(
                "SE buddy night",
                "SE bitch",
                "Bitch Restaurant",
                "09/06/2022",
                "06:00 p.m.",
                new ArrayList<>(Arrays.asList(jp4, jp5, jp6))
        );

        FoodPartyModel fp3 = new FoodPartyModel(
                "Ri hack gathering",
                "Jit Sin Kia",
                "Canteen at Jit Sin High School",
                "11/11/2022",
                "11:00 p.m.",
                new ArrayList<>(Arrays.asList(jp1))
        );

        FoodPartyModel fp4 = new FoodPartyModel(
                "Vegetarian gathering",
                "Eren Yeager",
                "Lai Vege Restaurant",
                "06/09/2022",
                "09:00 p.m.",
                new ArrayList<>(Arrays.asList(jp2, jp3, jp4, jp5, jp6))
        );

        FoodPartyModel fp5 = new FoodPartyModel(
                "SE buddy night",
                "SE bitch",
                "Bitch Restaurant",
                "09/06/2022",
                "06:00 p.m.",
                new ArrayList<>(Arrays.asList(jp1, jp2, jp3, jp4, jp5, jp6))
        );

        FoodPartyModel fp6 = new FoodPartyModel(
                "Ri hack gathering",
                "Jit Sin Kia",
                "Canteen at Jit Sin High School",
                "11/11/2022",
                "11:00 p.m.",
                new ArrayList<>(Arrays.asList(jp5))
        );

        foodPartyModels.add(fp1);
        foodPartyModels.add(fp2);
        foodPartyModels.add(fp3);
        foodPartyModels.add(fp4);
        foodPartyModels.add(fp5);
        foodPartyModels.add(fp6);

        for (FoodPartyModel fpm : foodPartyModels) {
            System.out.println(fpm.getTitle());
        }
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(getActivity(), FoodPartyDetailActivity.class);
        intent.putExtra("FoodPartyObject", foodPartyModels.get(position));
        startActivity(intent);
    }
}