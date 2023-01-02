package com.example.eatmou.ui.Inbox.sent;

import android.os.Bundle;
import android.util.Log;
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

import com.example.eatmou.ui.Inbox.EmptyDataObserver;
import com.example.eatmou.model.Invitation;
import com.example.eatmou.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SentFragment extends Fragment {

    private ArrayList<Invitation> invitationList;
    private RecyclerView sentRecyclerView;
    private FirebaseFirestore db;
    private String userID;

    SentAdapter adapter;
    View view;
    ImageButton sort_button;
    RelativeLayout empty_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sent, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) userID = bundle.getString("userID");

        empty_view = view.findViewById(R.id.empty_view);
        sentRecyclerView = view.findViewById(R.id.sentRecyclerView);
        invitationList = new ArrayList<>();
        adapter = new SentAdapter(invitationList, userID, getContext());

        db = FirebaseFirestore.getInstance();

        setInvitationList();
        setAdapter();

        sort_button = view.findViewById(R.id.sort_button);
        sort_button.setOnClickListener(v ->{
            //
            Toast.makeText(getContext(), "Open sorting view", Toast.LENGTH_SHORT).show();
        });
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        sentRecyclerView.setLayoutManager(layoutManager);
        sentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sentRecyclerView.setAdapter(adapter);

        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(empty_view, sentRecyclerView);
        adapter.registerAdapterDataObserver(emptyDataObserver);
        adapter.notifyDataSetChanged();
    }

    private void setInvitationList() {
        // to be changed
        db.collection("Invitations")
                .whereEqualTo("OrganiserID", userID)
                .orderBy("Date")
                .orderBy("StartTime")
                .addSnapshotListener((value, error) -> {
                    if(error!=null){
                        Log.e("Firestore error", error.getMessage());
                        return;
                    }
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            invitationList.add(Invitation.toObject(doc.getDocument().getData()));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}