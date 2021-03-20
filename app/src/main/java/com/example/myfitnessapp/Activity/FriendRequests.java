package com.example.myfitnessapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;

import com.example.myfitnessapp.Adapters.FriendRequestsAdapter;
import com.example.myfitnessapp.Adapters.RewardsAdapter;
import com.example.myfitnessapp.Classes.FriendRequestsModel;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.Fragments.Frag_One;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FriendRequests extends AppCompatActivity {

    RecyclerView recyclerViewFriendRequests;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    FriendRequestsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerViewFriendRequests = findViewById(R.id.rvFriendRequests);

        gettingTheRequestsFromFireStore();
    }

    private void gettingTheRequestsFromFireStore() {

        Query query = fStore.collection("user_s").document(mAuth.getUid()).collection("follow_requests");

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<FriendRequestsModel> options = new FirestorePagingOptions.Builder<FriendRequestsModel>()
                .setQuery(query,config, FriendRequestsModel.class)
                .build();

        adapter = new FriendRequestsAdapter(options);

        recyclerViewFriendRequests.setHasFixedSize(true);
        recyclerViewFriendRequests.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewFriendRequests.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
