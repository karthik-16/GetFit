package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.Adapters.AchivementsTags;
import com.example.myfitnessapp.Adapters.RankingAdapter;
import com.example.myfitnessapp.Classes.AcievementsTagsModel;
import com.example.myfitnessapp.Classes.RankModel;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.Fragments.FollowersAndFollowingActivity_Following_Frag;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firestore.v1.TargetOrBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewProfile extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    RecyclerView recyclerViewAchievementsTags;
    AchivementsTags adapter;

    TextView textViewName,textViewUsername,textViewFollowers,textViewFollowing,textViewPosts,textViewBio;
    ImageView imageViewProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerViewAchievementsTags = findViewById(R.id.rvAchievementTags);
        textViewName = findViewById(R.id.tvName);
        textViewUsername = findViewById(R.id.tvUserName);
        imageViewProfileImage = findViewById(R.id.ivProfileImage);
        textViewFollowers = findViewById(R.id.tvFollowers);
        textViewFollowing = findViewById(R.id.tvFollowing);
        textViewPosts = findViewById(R.id.tvPosts);
        textViewBio = findViewById(R.id.tvBio);

        //----------------------------------ONCLICK LISTENERS-------------------------------------//

        textViewFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, FollowersAndFollowing.class);
                intent.putExtra("idOfPerson",mAuth.getUid());
                startActivity(intent);
            }
        });

        textViewFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, FollowersAndFollowing.class);
                intent.putExtra("idOfPerson",mAuth.getUid());
                startActivity(intent);
            }
        });

        //---------------------------------------FUNCTIONS----------------------------------------//

        getAchievementsTagsFromFireStore();
        getProfileDataFromFireStore();

    }

    private void getProfileDataFromFireStore() {

        String UserId = mAuth.getUid();
        DocumentReference profileRef = fStore.collection("user_s").document(UserId).collection("user_info").document(UserId);
        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                textViewUsername.setText("@"+snapshot.getString("username"));
                textViewName.setText(snapshot.getString("name"));
                textViewPosts.setText(String.valueOf(snapshot.getLong("posts")));
                textViewFollowing.setText(String.valueOf(snapshot.getLong("following")));
                textViewFollowers.setText(String.valueOf(snapshot.getLong("followers")));
                textViewBio.setText(snapshot.getString("bio"));
                Picasso.get().load(snapshot.getString("profileimg")).into(imageViewProfileImage);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGVp",e.getMessage());
                Toast.makeText(ViewProfile.this, "Couldn't LoadProfile Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAchievementsTagsFromFireStore() {

        String UserId = mAuth.getUid();
        CollectionReference achievementsRef = fStore.collection("user_s").document(UserId).collection("achievements");
        achievementsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult()!= null){
                    List<AcievementsTagsModel> items = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot: task.getResult()){
                        final AcievementsTagsModel item = snapshot.toObject(AcievementsTagsModel.class);
                        String aTags = item.getaTag();
                        item.setaTag(aTags);

                        items.add(item);
                        adapter = new AchivementsTags(items,getApplicationContext());

                        //recyclerViewAchievementsTags.setHasFixedSize(true);
                        //recyclerViewAchievementsTags.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        //recyclerViewAchievementsTags.setAdapter(adapter);
                        recyclerViewAchievementsTags.setHasFixedSize(true);
                        recyclerViewAchievementsTags.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL));
                        recyclerViewAchievementsTags.setAdapter(adapter);


                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("TAGVp",e.getMessage());
            }
        });

    }


}
