package com.example.eatmou.ui.Inbox.joined;

import android.graphics.Canvas;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.ui.Inbox.EmptyDataObserver;
import com.example.eatmou.model.Invitation;
import com.example.eatmou.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class JoinedFragment extends Fragment {

    //need to change to user
    private ArrayList<Invitation> invitationList;
    private RecyclerView joinedRecyclerView;
    private FirebaseFirestore db;
    private String userID;
    Invitation removedInvitation = null;

    JoinedAdapter adapter;

    View view;
    ImageButton sort_button;
    RelativeLayout empty_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_joined, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null) userID = bundle.getString("userID");

        empty_view = view.findViewById(R.id.empty_view);
        joinedRecyclerView = view.findViewById(R.id.joinedRecyclerView);
        invitationList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        setInvitationList();
        setAdapter();

        sort_button = view.findViewById(R.id.sort_button);
        sort_button.setOnClickListener(v ->{
            //
            Toast.makeText(getContext(), "Open sorting view", Toast.LENGTH_SHORT).show();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(joinedRecyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.RIGHT) {//need to change to user class
                removedInvitation = invitationList.get(position);
                invitationList.remove(position);
                adapter.notifyItemRemoved(position);
                String description = "Removed from history!";
                Snackbar.make(joinedRecyclerView, description, 10000)
                        .setAction("Undo", view -> {
                            invitationList.add(position, removedInvitation);
                            adapter.notifyItemInserted(position);
                        }).addCallback(new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                if(event == DISMISS_EVENT_TIMEOUT
                                        || event == DISMISS_EVENT_MANUAL
                                        || event == DISMISS_EVENT_CONSECUTIVE
                                        || event == DISMISS_EVENT_SWIPE)
                                    db.collection("Invitations")
                                        .document(removedInvitation.getInvitationID())
                                        .delete();
                            }
                        }).show();

            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.RED))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_cancel_24)
                    .addSwipeRightLabel("Remove from history")
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void setAdapter() {
        adapter = new JoinedAdapter(invitationList, userID);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        joinedRecyclerView.setLayoutManager(layoutManager);
        joinedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        joinedRecyclerView.setAdapter(adapter);

        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(empty_view, joinedRecyclerView);
        adapter.registerAdapterDataObserver(emptyDataObserver);
    }

    private void setInvitationList() {

        db.collection("Invitations")
                .whereEqualTo("Status", "Accepted")
                .orderBy("Date")
                .orderBy("StartTime")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                Invitation invitation = Invitation.toObject(doc.getDocument().getData());
                                if(invitation.getInvitedID().equals(userID)
                                        || invitation.getOrganiserID().equals(userID)){
                                    invitationList.add(invitation);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        System.out.println(invitationList.size());
    }
}