package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.Adapters.videoAdapter;
import com.example.myfitnessapp.Classes.videoModel;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PurchasingReward extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    TextView textViewRewardTitlePurchasePage,textViewRewardDescPurchasePage;
    ImageView imageViewRewardImagePurchasePage;
    Button buttonPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasing_reward);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textViewRewardTitlePurchasePage = findViewById(R.id.tvRewardTitlePurchasePage);
        textViewRewardDescPurchasePage = findViewById(R.id.tvRewardDescPurchasePage);
        imageViewRewardImagePurchasePage = findViewById(R.id.ivRewardImagePurchasePage);
        buttonPurchase = findViewById(R.id.btnPurchase);

        gettingRewardDataFromFireStore();
    }

    private void gettingRewardDataFromFireStore() {
        final String passedRewardIdStringValue = getIntent().getExtras().get("id").toString();

       fStore.collection("Rewards").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

               for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                   if (snapshot.getLong("id") == Integer.parseInt(passedRewardIdStringValue)){
                       textViewRewardTitlePurchasePage.setText(snapshot.getString("title"));
                       textViewRewardDescPurchasePage.setText(snapshot.getString("desc"));
                       Picasso.get().load(snapshot.getString("image")).into(imageViewRewardImagePurchasePage);
                       buttonPurchase.setText(String.valueOf(snapshot.getLong("price")));
                   }
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
