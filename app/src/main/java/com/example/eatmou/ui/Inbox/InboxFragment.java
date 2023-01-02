package com.example.eatmou.ui.Inbox;

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
import android.widget.Toast;

import com.example.eatmou.ui.Inbox.joined.JoinedFragment;
import com.example.eatmou.ui.Inbox.received.ReceivedFragment;
import com.example.eatmou.ui.Inbox.sent.SentFragment;
import com.example.eatmou.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = null;
        if (user != null) userID = user.getUid();

        receivedFrgBtn = view.findViewById(R.id.receivedBtn);
        sentFrgBtn = view.findViewById(R.id.sentBtn);
        matchedFrgBtn = view.findViewById(R.id.joinedBtn);

        //pass user id to other fragments
        Bundle args = new Bundle();
        args.putString("userID", userID);

        {
            ReceivedFragment receivedFragment = new ReceivedFragment();
            receivedFragment.setArguments(args);
            replaceFragment(receivedFragment);
        }

        //swipe to change fragment******************************

        receivedFrgBtn.setOnClickListener(v -> {
            ReceivedFragment receivedFragment = new ReceivedFragment();
            receivedFragment.setArguments(args);
            replaceFragment(receivedFragment);
        });

        sentFrgBtn.setOnClickListener(v -> {
            SentFragment sentFragment = new SentFragment();
            sentFragment.setArguments(args);
            replaceFragment(sentFragment);
        });

        matchedFrgBtn.setOnClickListener(v -> {
            JoinedFragment joinedFragment = new JoinedFragment();
            joinedFragment.setArguments(args);
            replaceFragment(joinedFragment);
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.inboxFrameLayout,fragment);
        fragmentTransaction.commit();
    }
}