package com.example.eatmou.Inbox.sent;

import static android.content.ContentValues.TAG;

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

import com.example.eatmou.Inbox.EmptyDataObserver;
import com.example.eatmou.Inbox.Invitation;
import com.example.eatmou.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SentFragment extends Fragment {

    private ArrayList<Invitation> invitationList;
    private RecyclerView sentRecyclerView;
    private FirebaseFirestore db;

    SentAdapter adapter;
    View view;
    ImageButton sort_button;
    RelativeLayout empty_view;
    Invitation canceledInvitation = null;

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

        empty_view = view.findViewById(R.id.empty_view);
        sentRecyclerView = view.findViewById(R.id.sentRecyclerView);
        invitationList = new ArrayList<>();
        adapter = new SentAdapter(invitationList);

        db = FirebaseFirestore.getInstance();

        setInvitationList();
        setAdapter();

        sort_button = view.findViewById(R.id.sort_button);
        sort_button.setOnClickListener(v ->{
            //
            Toast.makeText(getContext(), "Open sorting view", Toast.LENGTH_SHORT).show();
        });

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(sentRecyclerView);
    }

//
//    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getAdapterPosition();
//            switch (direction) {
//                case ItemTouchHelper.RIGHT:
//                    getCanceledUsername(position);
//                    deleteInvitation();
//                    break;
//            }
//        }
//
//        @Override
//        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    .addSwipeRightBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.RED))
//                    .addSwipeRightActionIcon(R.drawable.ic_baseline_cancel_24)
//                    .addSwipeRightLabel("Cancel Invitation")
//                    .create()
//                    .decorate();
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
//    };

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
        String yourID = "dC8RVWnivbo1v2WvFdyF";
        db.collection("Invitations")
                .whereEqualTo("OrganiserID", yourID)
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
                        invitationList.add(Invitation.toObject(doc.getDocument().getData()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

//    private void getCanceledUsername(int position){
//        canceledInvitation = invitationList.get(position);
//        DocumentReference docRef = db.collection("Users").document(canceledInvitation.getInvitedID());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        String name = document.getString("Username");
//                        invitationList.remove(position);
//                        adapter.notifyItemRemoved(position);
//                        String description = "You have canceled the invitation for " + name;
//                        Snackbar.make(sentRecyclerView, description, 10000)
//                                .setAction("Undo", view -> {
//                                    invitationList.add(position,canceledInvitation);
//                                    adapter.notifyItemInserted(position);
//                                }).show();
//                    }
//                    else Log.d(TAG, "No such document");
//                }
//                else Log.d(TAG, "get failed with ", task.getException());
//            }
//        });
//    }
//
//    private void deleteInvitation(){
//        db.collection("Invitations")
//                .document(canceledInvitation.getInvitationID())
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error deleting document", e);
//                    }
//                });
//    }
}