package com.example.eatmou.Inbox.received;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.Inbox.EmptyDataObserver;
import com.example.eatmou.Inbox.Invitation;
import com.example.eatmou.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class ReceivedFragment extends Fragment {

    private ArrayList<Invitation> invitationList;
    private RecyclerView receivedRecyclerView;

    View view;
    ImageButton sort_button;
    RelativeLayout empty_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_received, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        empty_view = view.findViewById(R.id.empty_view);
        receivedRecyclerView = view.findViewById(R.id.receivedRecyclerView);
        invitationList = new ArrayList<>();

        setInvitationList();
        setAdapter();

        sort_button = view.findViewById(R.id.sort_button);
        sort_button.setOnClickListener(v ->{
            //
            Toast.makeText(getContext(), "Open sorting view", Toast.LENGTH_SHORT).show();
        });

    }

    private void setAdapter() {
        ReceivedAdapter adapter = new ReceivedAdapter(invitationList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        receivedRecyclerView.setLayoutManager(layoutManager);
        receivedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        receivedRecyclerView.setAdapter(adapter);

        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(empty_view, receivedRecyclerView);
        adapter.registerAdapterDataObserver(emptyDataObserver);
    }

    private void setInvitationList() {

        invitationList.add(new Invitation("You", "Your House", new Date(), new Time(0)));
        invitationList.add(new Invitation("Adam", "Here", new Date(), new Time(0)));
        invitationList.add(new Invitation("Bob", "There", new Date(), new Time(0)));
        invitationList.add(new Invitation("Cris", "Where", new Date(), new Time(0)));
    }

}