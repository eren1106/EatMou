package com.example.eatmou.ui.Inbox.received;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.ui.Inbox.EmptyDataObserver;
import com.example.eatmou.model.Invitation;
import com.example.eatmou.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReceivedFragment extends Fragment {

    private ArrayList<Invitation> invitationList;
    private RecyclerView receivedRecyclerView;
    private FirebaseFirestore db;
    private String userID;

    View view;
    ImageButton sort_button;
    RelativeLayout empty_view;

    ReceivedAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        Bundle bundle = this.getArguments();
        if(bundle != null) userID = bundle.getString("userID");

        empty_view = view.findViewById(R.id.empty_view);
        receivedRecyclerView = view.findViewById(R.id.receivedRecyclerView);
        invitationList = new ArrayList<>();
        adapter = new ReceivedAdapter(invitationList, userID, getContext());

        db = FirebaseFirestore.getInstance();

        setInvitationList();
        setAdapter();

        sort_button = view.findViewById(R.id.sort_button);
        sort_button.setOnClickListener(v ->{
            PopupMenu popupMenu = new PopupMenu(getContext(), sort_button);
            popupMenu.getMenuInflater().inflate(R.menu.received_sorting_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                String chosen = (String) menuItem.getTitle();
                switch (chosen) {
                    case "Username":
                        Collections.sort(invitationList, (inv1, inv2) -> inv1.getOrganiserName().compareToIgnoreCase(inv2.getOrganiserName()));
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

    }

    private void setAdapter() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        receivedRecyclerView.setLayoutManager(layoutManager);
        receivedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        receivedRecyclerView.setAdapter(adapter);

        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(empty_view, receivedRecyclerView);
        adapter.registerAdapterDataObserver(emptyDataObserver);
        adapter.notifyDataSetChanged();
    }

    private void setInvitationList() {
        System.out.println(userID);
        db.collection("Invitations")
                .whereEqualTo("InvitedID", userID)
                .whereEqualTo("Status","Pending")
                .orderBy("Date", Query.Direction.DESCENDING)
                .orderBy("StartTime", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if(error!=null){
                        Log.e("Firestore error", error.getMessage());
                        return;
                    }
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Invitation invitation = Invitation.toObject(doc.getDocument().getData());

                            //get and set organiser name
                            DocumentReference docRef = db.collection("users").document(invitation.getOrganiserID());
                            docRef.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String name = document.getString("username");
                                        invitation.setOrganiserName(name);
                                        System.out.println(invitation.getOrganiserName());
                                    } else Log.d(TAG, "No such document");
                                } else Log.d(TAG, "get failed with ", task.getException());
                            });

                            invitationList.add(invitation);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        progressDialog.dismiss();
    }
}