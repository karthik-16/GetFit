package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfitnessapp.Adapters.videoAdapter;
import com.example.myfitnessapp.Classes.videoModel;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoVIewer extends AppCompatActivity {

    private static final String TAG = "TAG";
    //---------------------------------DECLARING VIEWS--------------------------------------------//
    //TODO change the rewards name and also come up with the name for the 5th imageView
    ImageView imageViewUpload,imageViewWallet,imageViewRewards,imageViewProfile,imageViewSearch;
    //------------------------------DECLARING FIRE STORE TOKENS-----------------------------------//
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    videoAdapter adapter;
    //ImageView profileImg;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewer);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //-----------------------------REFERENCING THE VIEWS--------------------------------------//
        //
        imageViewProfile = findViewById(R.id.menuProfile);
        imageViewRewards = findViewById(R.id.menuRewards);
        imageViewUpload = findViewById(R.id.menuUpload);
        imageViewWallet = findViewById(R.id.menuWallet);
        imageViewSearch = findViewById(R.id.menuSearch);
        //---------------------------------------FUNCTIONS----------------------------------------//

        adapterCallForVideoFromFireStore();
        dataFromFireStoreToImageView();
        //--------------------------------ONCLICK LISTENERS FOR VIEWS-----------------------------//

        imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoVIewer.this,DecideWhatToDo.class);
                startActivity(intent);
                //FirebaseAuth.getInstance().signOut();
                //Intent intent =new Intent(VideoVIewer.this,LoginActivity.class);
                //startActivity(intent);
            }
        });
        imageViewWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoVIewer.this, WalletActivity.class);
                startActivity(intent);
            }
        });
        imageViewRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoVIewer.this, YourActivity.class);
                startActivity(intent);
            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoVIewer.this, ViewProfile.class);
                startActivity(intent);
            }
        });
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoVIewer.this, SearchPeopleActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.vPager).setOnTouchListener(new View.OnTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    //increase the like by one in firestore
                    Toast.makeText(getApplicationContext(), "double clicked", Toast.LENGTH_SHORT).show();

                    return super.onDoubleTapEvent(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;

            }
        });



    }

    private void dataFromFireStoreToImageView() {
        firebaseFirestore= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userId =mAuth.getUid();
        DocumentReference ImageRef = firebaseFirestore.collection("user_s").document(userId).collection("user_info").document(userId);
        ImageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                String ProfileUrl = snapshot.getString("profileimg");
                Picasso.get().load(ProfileUrl).into(imageViewProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("VideoViewError",e.getMessage()+"");
                Toast.makeText(VideoVIewer.this, "Could'nt Load Profile Image ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void adapterCallForVideoFromFireStore(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        final ViewPager2 viewPager2 = findViewById(R.id.vPager);
        firebaseFirestore.collection("videos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //the and statement is added coz it throws a warning saying this may produce
                //null point exception...alternatively we can also use try and catch
                if(task.isSuccessful() && task.getResult()!= null){
                    List<videoModel> items = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot: task.getResult()){
                        final videoModel item = snapshot.toObject(videoModel.class);
                        String challenge_name = item.getChallenge_name();
                        String profileimg = item.getProfileimg();
                        String username = item.getUsername();
                        String videourl = item.getVideourl();
                        long likes = item.getLikes();

                        item.challenge_name = challenge_name;
                        item.profileimg = profileimg;
                        item.username = username;
                        item.videourl=videourl;
                        item.likes = likes;
                        items.add(item);
                        adapter = new videoAdapter(items,getApplicationContext());
                        viewPager2.setAdapter(adapter);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"error loading video");
            }
        });
    }

}


