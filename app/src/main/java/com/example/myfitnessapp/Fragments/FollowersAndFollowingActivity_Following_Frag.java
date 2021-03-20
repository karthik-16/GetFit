package com.example.myfitnessapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfitnessapp.Adapters.FollowersAndFollowingAdapter;
import com.example.myfitnessapp.Classes.SearchPeopleModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FollowersAndFollowingActivity_Following_Frag extends Fragment {

    View v;
    FollowersAndFollowingAdapter adapter;
    RecyclerView recyclerViewFollowersAndFollowingActivity_FollowingFrag;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    public FollowersAndFollowingActivity_Following_Frag() {
        // Required empty public constructor
    }

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           v = inflater.inflate(R.layout.fragment_followers_and_following_activity__following_,container,false);

           recyclerViewFollowersAndFollowingActivity_FollowingFrag = v.findViewById(R.id.rvFollowersAndFollowingActivityFollowingFrag);


           recyclerViewFollowersAndFollowingActivity_FollowingFrag.setHasFixedSize(true);
           recyclerViewFollowersAndFollowingActivity_FollowingFrag.setLayoutManager(new LinearLayoutManager(getContext()));
           recyclerViewFollowersAndFollowingActivity_FollowingFrag.setAdapter(adapter);

           return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        getDataFromFireStore();

    }

    private void getDataFromFireStore(){
        Query query = fStore.collection("user_s").document(mAuth.getUid()).collection("following");

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(8)
                .build();

        //use a different single item and model
        FirestorePagingOptions<SearchPeopleModel> options = new FirestorePagingOptions.Builder<SearchPeopleModel>()
                .setLifecycleOwner(FollowersAndFollowingActivity_Following_Frag.this)
                .setQuery(query,config,SearchPeopleModel.class)
                .build();

        adapter = new FollowersAndFollowingAdapter(options);
    }
}
