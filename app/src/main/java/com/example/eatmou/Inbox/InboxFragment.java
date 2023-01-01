package com.example.eatmou.Inbox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eatmou.Inbox.joined.JoinedFragment;
import com.example.eatmou.Inbox.received.ReceivedFragment;
import com.example.eatmou.Inbox.sent.SentFragment;
import com.example.eatmou.R;

public class InboxFragment extends Fragment {

    Button receivedFrgBtn;
    Button sentFrgBtn;
    Button matchedFrgBtn;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receivedFrgBtn = view.findViewById(R.id.receivedBtn);
        sentFrgBtn = view.findViewById(R.id.sentBtn);
        matchedFrgBtn = view.findViewById(R.id.joinedBtn);

        replaceFragment(new ReceivedFragment());

        //swipe to change fragment******************************


        receivedFrgBtn.setOnClickListener(v -> replaceFragment(new ReceivedFragment()));
        sentFrgBtn.setOnClickListener(v -> replaceFragment(new SentFragment()));
        matchedFrgBtn.setOnClickListener(v -> replaceFragment(new JoinedFragment()));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.inboxFrameLayout,fragment);
        fragmentTransaction.commit();
    }
}