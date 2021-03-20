package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.Adapters.RewardsAdapter;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.Fragments.Frag_Four;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends AppCompatActivity{

    TextView textViewWalletBalanceFromFireStore,textViewWalletMoneySpent,textViewTotalEarned,textViewBuyFromStore;
    RecyclerView recyclerViewWalletActivity;
    ImageView imageViewBackButton;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    RewardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        //------------------------------INSTANTIATING FIRE_STORE TOKENS---------------------------//

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //---------------------------------REFERENCING XML VIEWS----------------------------------//

        textViewWalletBalanceFromFireStore= findViewById(R.id.tvWalletBalanceFromFireStore);
        textViewTotalEarned = findViewById(R.id.tvTotalMoneyEarned);
        textViewWalletMoneySpent = findViewById(R.id.tvWalletMoneySpent);
        recyclerViewWalletActivity = findViewById(R.id.rvWalletActivity);
        textViewBuyFromStore = findViewById(R.id.tvBuyFromStore);
        imageViewBackButton = findViewById(R.id.ivBackButton);

        //------------------------------------ONCLICK LISTENERS-----------------------------------//

        textViewBuyFromStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WalletActivity.this,Bazzar_Rewards.class));
            }
        });
        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WalletActivity.this , VideoVIewer.class));
            }
        });

        //----------------------------------------FUNCTIONS---------------------------------------//
        walletDetailsFromFireStore();
        String userId = mAuth.getUid();
        Query query = fStore.collection("user_s").document(userId).collection("purchases");
        purchaseDetailsFromFireStore(query);

    }

    private void purchaseDetailsFromFireStore(Query query) {
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<RewardsModel> options = new FirestorePagingOptions.Builder<RewardsModel>()
                .setLifecycleOwner(WalletActivity.this)
                .setQuery(query,config,RewardsModel.class)
                .build();

        adapter = new RewardsAdapter(options);
        recyclerViewWalletActivity.setHasFixedSize(true);
        //if we were using grid layout instead of staggered grid we need getApplicationContext also
        recyclerViewWalletActivity.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerViewWalletActivity.setAdapter(adapter);
    }

    private void walletDetailsFromFireStore() {
        String userId = mAuth.getUid();
        fStore.collection("user_s").document(userId).collection("wallet_info").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                //TODO Since the balance value is on type int and v have converted to string , so convert it back to int while using arithmetic operations on it
                int WalletBalance = snapshot.getLong("balance").intValue(); //This is to get integer value from FireStore
                int WalletMoneySpent = snapshot.getLong("money_spent").intValue();
                int WalletTotalMoneyEarned = snapshot.getLong("total_earned").intValue();
                textViewWalletBalanceFromFireStore.setText( String.valueOf(WalletBalance) ); // This is to convert the int value to string and setText() requires String value
                textViewTotalEarned.setText(String.valueOf(WalletTotalMoneyEarned));
                textViewWalletMoneySpent.setText(String.valueOf(WalletMoneySpent));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("WalletError",e.getMessage()+"");
                Toast.makeText(WalletActivity.this,"Couldn't Load Wallet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
