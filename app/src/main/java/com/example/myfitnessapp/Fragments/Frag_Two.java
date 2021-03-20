package com.example.myfitnessapp.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.myfitnessapp.Activity.PurchasingReward;
import com.example.myfitnessapp.Activity.ViewOthersProfile;
import com.example.myfitnessapp.Adapters.RewardsAdapter;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Frag_Two extends Fragment{

    View v;
    RewardsAdapter adapter;
    RecyclerView recyclerViewBazarRewards;
    FirebaseFirestore fStore;
    List<RewardsModel> items = new ArrayList<>();

    public Frag_Two(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_frag__two,container,false);
        recyclerViewBazarRewards = v.findViewById(R.id.rvFragmentTwo);

        //the adapter was supposed to set here but when i do , then it only comes when i scroll till the end and come back and if i put it down then it only show the first time and when i switch and come
        //back to this fragment it disappears so ive put in both the places so get this code checked coz maybe the no of reads given to fire_base is more
        //adapter = new RewardsAdapter(options,this);
        recyclerViewBazarRewards.setHasFixedSize(true);
        //if we were using grid layout instead of staggered grid we need getApplicationContext also
        recyclerViewBazarRewards.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerViewBazarRewards.setAdapter(adapter);



        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();

        Query query = fStore.collection("Rewards").whereEqualTo("category","shoes");
        getDataFromFireStore(query);
    }

    private void getDataFromFireStore(Query query) {
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<RewardsModel> options = new FirestorePagingOptions.Builder<RewardsModel>()
                .setLifecycleOwner(Frag_Two.this)
                .setQuery(query,config,RewardsModel.class)
                .build();

        adapter = new RewardsAdapter(options);
    }
}
