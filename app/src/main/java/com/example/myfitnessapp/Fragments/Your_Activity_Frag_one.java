package com.example.myfitnessapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myfitnessapp.Activity.FriendRequests;
import com.example.myfitnessapp.Adapters.RewardsAdapter;
import com.example.myfitnessapp.R;
import com.google.firebase.firestore.FirebaseFirestore;


public class Your_Activity_Frag_one extends Fragment {
    View v;
    LinearLayout linearLayoutFriendRequest;
    public Your_Activity_Frag_one() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_your__activity__frag_one,container,false);

        linearLayoutFriendRequest = v.findViewById(R.id.llFriendRequest);
        linearLayoutFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FriendRequests.class));
            }
        });

        return v;
    }


}
