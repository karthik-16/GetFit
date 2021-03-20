package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.Classes.SearchPeopleModel;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewOthersProfile extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    TextView textViewName,textViewUserName,textViewPosts,textViewFollowers,textViewFollowing;
    ImageView imageViewProfileImage;
    Button buttonFollow ,buttonDialogContinue , buttonDialogCancel;
    Dialog dialog;
    private String TAG = "VOP" ;
    public String passedUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_others_profile);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //-----------------------INSTANTIATING AND REFERENCING DIALOG BOX-------------------------//

        dialog = new Dialog(ViewOthersProfile.this);
        dialog.setContentView(R.layout.confirm_unfollow_dialogue_box);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);//if this was true the dialog will disappear when clicked outside it, by default it is true
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        //----------------------------------REFERENCING XML VIEWS---------------------------------//

        textViewName = findViewById(R.id.tvName);
        textViewUserName = findViewById(R.id.tvUserName);
        textViewPosts = findViewById(R.id.tvPosts);
        textViewFollowers = findViewById(R.id.tvFollowers);
        textViewFollowing = findViewById(R.id.tvFollowing);
        imageViewProfileImage = findViewById(R.id.ivProfileImage);
        buttonFollow = findViewById(R.id.btnFollow);
        buttonDialogContinue = dialog.findViewById(R.id.btnDialogContinue);
        buttonDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        ///OnClickListeners

        passedUserId = getIntent().getExtras().get("user_AuthId").toString();

        checkForWeatherFollowing(passedUserId);
        //---------------------------------ONCLICK LISTENERS--------------------------------------//

        textViewFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Right now we are checking for the text and deciding weather to switch to new activity or not but later we have to also make privacy option so tat u r able to c the followers of public ppl
                fStore.collection("user_s").document(passedUserId).collection("user_info").document(passedUserId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                passedUserId = getIntent().getExtras().get("user_AuthId").toString();
                                if (snapshot.getBoolean("private").equals(false)){

                                    ///////////See hot to send data from activity to fragment
                                    Intent intent = new Intent(ViewOthersProfile.this, FollowersAndFollowing.class);
                                    intent.putExtra("idOfPerson",passedUserId);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(ViewOthersProfile.this, "true", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });

        textViewFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //We query the user data with the id passed from previous activity and see if privacy is private or not. If its not we will let the current user view whom so ever's profile
                fStore.collection("user_s").document(passedUserId).collection("user_info").document(passedUserId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                passedUserId = getIntent().getExtras().get("user_AuthId").toString();
                                if (snapshot.getBoolean("private").equals(false)){

                                    //Passing the id to the FragActivity ...The key is same for both this and ViewProfile Activity but from the former we pass mAuth.getUid()
                                    Intent intent = new Intent(ViewOthersProfile.this, FollowersAndFollowing.class);
                                    intent.putExtra("idOfPerson",passedUserId);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(ViewOthersProfile.this, "Profile is Private", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

        //This is to check what the current social status is and create appropriate dialogues
        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a document in fire_store collection "follow_requests"
                switch (buttonFollow.getText().toString().toLowerCase()){
                    case "follow":
                        fStore.collection("ToSearch").document(mAuth.getUid()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        SearchPeopleModel searchPeopleModel = new SearchPeopleModel();

                                        searchPeopleModel.setUsername(snapshot.getString("username"));
                                        searchPeopleModel.setUser_AuthId(mAuth.getUid());
                                        searchPeopleModel.setName(snapshot.getString("name"));
                                        searchPeopleModel.setProfileimg(snapshot.getString("profileimg"));

                                        DocumentReference follow_requestRef = fStore.collection("user_s").document(passedUserId).collection("follow_requests").document(mAuth.getUid());
                                        Map<String,Object> followReq = new HashMap<>();
                                        followReq.put("user_AuthId",searchPeopleModel.getUser_AuthId());
                                        followReq.put("name",searchPeopleModel.getName());
                                        followReq.put("username",searchPeopleModel.getUsername());
                                        followReq.put("profileimg",searchPeopleModel.getProfileimg());
                                        follow_requestRef.set(followReq);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+e.getMessage());
                                    }
                                });

                        buttonFollow.setText("REQUESTED");
                        break;
                    case "following":
                        buttonDialogContinue.setText("Unfollow");
                        dialog.show();
                        break;
                    case "requested":
                        buttonDialogContinue.setText("Cancel Request");
                        dialog.show();
                        break;

                }
            }
        });

        buttonDialogContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (buttonDialogContinue.getText().toString().equals("Unfollow")){

                    //TODO u also have to remove the doc from the following collection of the other person

                    //deleting the document from the followers collection
                    fStore.collection("user_s").document(passedUserId).collection("followers").document(mAuth.getUid()).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ViewOthersProfile.this, "Successfully Unfollowed ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+ e.getMessage());
                                }
                            });

                    //Reducing the number of followers by 1

                    //Decreasing Number of followers
                    //First query the followers collection and retrieve the no.of followers
                    DocumentReference docRefIncreaseFollowersNumber = fStore.collection("user_s").document(passedUserId).collection("user_info").document(passedUserId);
                    docRefIncreaseFollowersNumber.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    int number = snapshot.getLong("followers").intValue();
                                    Log.d(TAG, "onSuccess: number1 =" + number);
                                    --number;

                                    Log.d(TAG, "onSuccess: number2 =" + number);
                                    //we have increased the no.of followers by one so now we can add to fire_store again
                                    DocumentReference docRef = fStore.collection("user_s").document(passedUserId).collection("user_info").document(passedUserId);
                                    Map<String,Object> followReq1 = new HashMap<>();
                                    followReq1.put("followers",number);
                                    docRef.update(followReq1);

                                    Log.d(TAG, "onSuccess: number3  = " + number);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+ e.getMessage());
                                }
                            });

                    //Removing the doc from the following collection
                    fStore.collection("user_s").document(mAuth.getUid()).collection("following").document(passedUserId).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "onSuccess: Successfully Removed the doc from following collection");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+ e.getMessage());
                        }
                    });

                    //Reducing the number of following by 1 of the other person

                    DocumentReference docRefIncreaseFollowingsNumber = fStore.collection("user_s").document(mAuth.getUid()).collection("user_info").document(mAuth.getUid());
                    docRefIncreaseFollowingsNumber.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    int number = snapshot.getLong("following").intValue();
                                    Log.d(TAG, "onSuccess: number1 =" + number);
                                    --number;

                                    Log.d(TAG, "onSuccess: number2 =" + number);
                                    //we have increased the no.of followers by one so now we can add to fire_store again
                                    DocumentReference docRef = fStore.collection("user_s").document(mAuth.getUid()).collection("user_info").document(mAuth.getUid());
                                    Map<String,Object> followReq1 = new HashMap<>();
                                    followReq1.put("following",number);
                                    docRef.update(followReq1);

                                    Log.d(TAG, "onSuccess: number3  = " + number);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+ e.getMessage());
                                }
                            });

                }
                else{
                    fStore.collection("user_s").document(passedUserId).collection("follow_requests").document(mAuth.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ViewOthersProfile.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+ e.getMessage());
                        }
                    });


                }
                buttonFollow.setText("Follow");
            }
        });
        buttonDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //-----------------------------------------FUNCTIONS--------------------------------------//
        settingUpTheProfileOfAnotherUser(passedUserId);
        settingUpTheAchievementsTags();
    }

    private void checkForWeatherFollowing(final String userId ) {

        fStore.collection("user_s").document(userId).collection("followers").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()){
                    buttonFollow.setText("Following");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+ e.getMessage());
            }
        });

        fStore.collection("user_s").document(userId).collection("follow_requests").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()){
                    buttonFollow.setText("Requested");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+ e.getMessage());
            }
        });
    }

    private void settingUpTheAchievementsTags() {
    }

    private void settingUpTheProfileOfAnotherUser(String userId) {
        fStore.collection("user_s").document(userId).collection("user_info").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                textViewName.setText(snapshot.getString("name"));
                textViewUserName.setText(snapshot.getString("username"));
                textViewPosts.setText(String.valueOf(snapshot.getLong("posts")));
                textViewFollowers.setText(String.valueOf(snapshot.getLong("followers")));
                textViewFollowing.setText(String.valueOf(snapshot.getLong("following")));
                Picasso.get().load(snapshot.getString("profileimg")).into(imageViewProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}