package com.example.eatmou.ui.Inbox.joined;

import static android.content.ContentValues.TAG;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class JoinedFragment extends Fragment {

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
            PopupMenu popupMenu = new PopupMenu(getContext(), sort_button);
            popupMenu.getMenuInflater().inflate(R.menu.joined_sorting_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                String chosen = (String) menuItem.getTitle();
                switch (chosen) {
                    case "Type":
                        Collections.sort(invitationList, (inv1, inv2) -> {
                            if(inv1.isCurrentUserInvitation()) return -1;
                            else if (inv2.isCurrentUserInvitation()) return 1;
                            else return 0;
                        });
                        break;
                    case "Location":
                        Collections.sort(invitationList, (inv1, inv2) -> inv1.getLocation().compareToIgnoreCase(inv2.getLocation()));
                        break;
                    case "Date":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(invitationList, Comparator.comparing(Invitation::getDate));
                            Collections.reverse(invitationList);
                        }
                        break;
                    case "Time":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(invitationList, Comparator.comparing(Invitation::getStartTime));
                        }
                        break;
                }
                adapter.notifyDataSetChanged();
                return true;
            });
            popupMenu.show();
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
        adapter = new JoinedAdapter(invitationList, userID, getContext());
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
                                if(invitation.getInvitedID().equals(userID)){
                                    //get and set organiser name
                                    DocumentReference docRef = db.collection("users").document(invitation.getOrganiserID());
                                    docRef.get().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String name = document.getString("username");
                                                invitation.setOrganiserName(name);
                                                invitation.setCurrentUserInvitation(false);
                                            } else Log.d(TAG, "No such document");
                                        } else Log.d(TAG, "get failed with ", task.getException());
                                    });
                                }
                                else if (invitation.getOrganiserID().equals(userID)){
                                    DocumentReference docRef = db.collection("users").document(invitation.getInvitedID());
                                    docRef.get().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String name = document.getString("username");
                                                invitation.setInvitedName(name);
                                                invitation.setCurrentUserInvitation(true);
                                            } else Log.d(TAG, "No such document");
                                        } else Log.d(TAG, "get failed with ", task.getException());
                                    });
                                }
                                invitationList.add(invitation);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}