package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfitnessapp.Adapters.RankingAdapter;
import com.example.myfitnessapp.Adapters.RewardsAdapter;
import com.example.myfitnessapp.Adapters.videoAdapter;
import com.example.myfitnessapp.Classes.RankModel;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.Classes.videoModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.internal.RecaptchaActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class SelectChallenge extends AppCompatActivity  {

    private static final String TAG = "SelCha";
    private TextView textViewChallengeName,textViewRankMeChallengeName,textViewRankMePoints,textViewChallengeTime;
    private ImageView imageViewChallengePic,imageViewRankMeProfileImage;
    CardView cardViewRankYou;
    Button buttonUpload;
    FirebaseFirestore fStore;
    private RankingAdapter adapter;
    private RecyclerView mFireStoreList;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_challenge);

        //--------------------------------INSTANTIATING FIRE_BASE TOKENS--------------------------//

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //-----------------------------------REFERENCING XML VIEWS--------------------------------//

        mFireStoreList = findViewById(R.id.rvRankFireStoreList);
        textViewChallengeName =findViewById(R.id.t1name);
        textViewChallengeTime = findViewById(R.id.t1time);
        imageViewChallengePic =findViewById(R.id.i1);
        imageViewRankMeProfileImage = findViewById(R.id.ivRankMeProfileImage);
        textViewRankMePoints = findViewById(R.id.tvRankMePoints);
        textViewRankMeChallengeName = findViewById(R.id.tvRankMeChallengeName);
        buttonUpload = findViewById(R.id.btnUpload);
        cardViewRankYou = findViewById(R.id.cvRankYou);

        //----------------------------------------------------------------------------------------//
        String toImageView = getIntent().getStringExtra("image");
        String toTextViewChallengeName = getIntent().getStringExtra("challengeDuration");
        String queryingChallengeName = getIntent().getStringExtra("theRankingQuery");

        switch (toImageView){
            case "pushUps": imageViewChallengePic.setImageResource(R.drawable.push_ups);
                            switch (toTextViewChallengeName){
                                case "30": textViewChallengeName.setText("Max Push Ups");
                                            textViewChallengeTime.setText("(30s)");
                                    break;
                                case "20": textViewChallengeName.setText("Max Push Ups");
                                    textViewChallengeTime.setText("(20s)");
                                    break;
                                case "10": textViewChallengeName.setText("Max Push Ups");
                                    textViewChallengeTime.setText("(10s)");
                                    break;
                            }
                break;
            case "pullUps": imageViewChallengePic.setImageResource(R.drawable.pullups);
                            switch (toTextViewChallengeName){
                                case "30": textViewChallengeName.setText("Max Pull Ups");
                                    textViewChallengeTime.setText("(30s)");
                                    break;
                                case "20": textViewChallengeName.setText("Max Pull Ups");
                                    textViewChallengeTime.setText("(20s)");
                                    break;
                                case "10": textViewChallengeName.setText("Max Pull Ups");
                                    textViewChallengeTime.setText("(10s)");
                                    break;
                            }
                break;
            case "benchPress": imageViewChallengePic.setImageResource(R.drawable.bench_press);
                            switch (toTextViewChallengeName){
                                case "30": textViewChallengeName.setText("Bench Press ");
                                    textViewChallengeTime.setText("(30kg)");
                                    break;
                                case "20": textViewChallengeName.setText("Bench Press");
                                    textViewChallengeTime.setText("(20kg)");
                                    break;
                                case "10": textViewChallengeName.setText("Bench Press");
                                    textViewChallengeTime.setText("(10kg)");
                                    break;
                            }
                break;
            case "lifts": imageViewChallengePic.setImageResource(R.drawable.lifts);
                        switch (toTextViewChallengeName){
                            case "30": textViewChallengeName.setText("Lifts");
                                textViewChallengeTime.setText("(30kg)");
                                break;
                            case "20": textViewChallengeName.setText("Lifts");
                                textViewChallengeTime.setText("(20kg)");
                                break;
                            case "10": textViewChallengeName.setText("Lifts");
                                textViewChallengeTime.setText("(10kg)");
                                break;
                }
                break;
        }
        //--------------------------------------ONCLICK LISTENERS---------------------------------//
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChallenge.this,CameraAttempt.class);
                startActivity(intent);
            }
        });

        //TODO set the firebase rules . right now it allows read and write access to anyone

        //-------------------------------------FUNCTIONS------------------------------------------//
        getDataFromFireStore(queryingChallengeName);
        displayingYouRankInfo(queryingChallengeName);
    }

    private void getDataFromFireStore(String queryingChallengeName) {



        fStore.collection("videos").orderBy("points", Query.Direction.DESCENDING).whereEqualTo("challenge_name",queryingChallengeName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult()!= null){
                    List<RankModel> items = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot: task.getResult()){
                        final RankModel item = snapshot.toObject(RankModel.class);
                        String challenge_name = item.getChallenge_name();
                        String profileimg = item.getProfileimg();
                        String username = item.getUsername();

                        item.challenge_name = challenge_name;
                        item.profileimg = profileimg;
                        item.username = username;
                        items.add(item);
                        adapter = new RankingAdapter(items,getApplicationContext());

                        mFireStoreList.setHasFixedSize(true);
                        mFireStoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mFireStoreList.setAdapter(adapter);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RewardsActivity","error loading video");
            }
        });

    }

    private void displayingYouRankInfo(final String queringChallengeName) {


        fStore.collection("videos").whereEqualTo("username","kihtrak").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult()!= null){
                    for (QueryDocumentSnapshot snapshot : task.getResult()){

                        if (snapshot.getString("challenge_name").equals(queringChallengeName)){
                            int MePoints = snapshot.getLong("points").intValue();
                            String MeChallengeName = snapshot.getString("challenge_name");
                            String MeProfileImage =snapshot.getString("profileimg");

                            textViewRankMeChallengeName.setText(MeChallengeName);
                            textViewRankMePoints.setText(String.valueOf(MePoints));
                            Picasso.get().load(MeProfileImage).into(imageViewRankMeProfileImage);

                            //TODO In the aapter try to get the rank of the current user
                            //TODO the user name is hard coded, we need to run another query to get the current user name
                        }
                        else {
                            cardViewRankYou.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("YOU_RANK","Couldn't load your data");
            }
        });
    }


}
