package com.example.myfitnessapp.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
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


public class FollowersAndFollowingActivity_Followers_Frag extends Fragment {

    private static final String TAG = "FAFAFF" ;
    View v;
    FollowersAndFollowingAdapter adapter;
    RecyclerView recyclerViewFollowersAndFollowingActivity_FollowersFrag;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    public FollowersAndFollowingActivity_Followers_Frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_followers_and_following_activity__followers_,container,false);

        recyclerViewFollowersAndFollowingActivity_FollowersFrag = v.findViewById(R.id.rvFollowersAndFollowingActivityFollowersFrag);

        recyclerViewFollowersAndFollowingActivity_FollowersFrag.setHasFixedSize(true);
        recyclerViewFollowersAndFollowingActivity_FollowersFrag.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFollowersAndFollowingActivity_FollowersFrag.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getDataFromFireStore();
    }

    private void getDataFromFireStore() {

        //Getting the id of the person from the previous activity and querying his followers
        String personsId = getArguments().getString("id");

        Query query = fStore.collection("user_s").document(personsId).collection("followers");

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<SearchPeopleModel> options = new FirestorePagingOptions.Builder<SearchPeopleModel>()
                .setLifecycleOwner(FollowersAndFollowingActivity_Followers_Frag.this)
                .setQuery(query,config,SearchPeopleModel.class)
                .build();

        adapter = new FollowersAndFollowingAdapter(options);
    }
}
