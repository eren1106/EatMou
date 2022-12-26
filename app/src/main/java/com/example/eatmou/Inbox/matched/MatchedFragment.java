package com.example.eatmou.Inbox.matched;

import android.graphics.Canvas;
import android.os.Bundle;
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
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MatchedFragment extends Fragment {

    //need to change to user
    private ArrayList<Invitation> invitationList;
    private RecyclerView matchedRecyclerView;
    MatchedAdapter adapter;

    View view;
    ImageButton sort_button;
    RelativeLayout empty_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matched, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        empty_view = view.findViewById(R.id.empty_view);
        matchedRecyclerView = view.findViewById(R.id.matchedRecyclerView);
        invitationList = new ArrayList<>();

        setInvitationList();
        setAdapter();

        sort_button = view.findViewById(R.id.sort_button);
        sort_button.setOnClickListener(v ->{
            //
            Toast.makeText(getContext(), "Open sorting view", Toast.LENGTH_SHORT).show();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(matchedRecyclerView);
    }

    //need to change to user class
    Invitation removedUser = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.RIGHT:
                    //need to change to user class
                    removedUser = invitationList.get(position);
                    invitationList.remove(position);
                    adapter.notifyItemRemoved(position);
                    String description = "You have removed your friend " + removedUser.getUsername();
                    Snackbar.make(matchedRecyclerView, description, 10000)
                            .setAction("Undo", view -> {
                                invitationList.add(position,removedUser);
                                adapter.notifyItemInserted(position);
                            }).show();

                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.RED))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_cancel_24)
                    .addSwipeRightLabel("Remove your friend")
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void setAdapter() {
        adapter = new MatchedAdapter(invitationList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        matchedRecyclerView.setLayoutManager(layoutManager);
        matchedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        matchedRecyclerView.setAdapter(adapter);

        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(empty_view, matchedRecyclerView);
        adapter.registerAdapterDataObserver(emptyDataObserver);
    }

    private void setInvitationList() {
        invitationList.add(new Invitation("Bob", "There", new Date(), new Time(0)));
        invitationList.add(new Invitation("Dane", "Where", new Date(), new Time(0)));
    }
}