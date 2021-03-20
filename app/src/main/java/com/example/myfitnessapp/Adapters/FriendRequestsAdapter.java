package com.example.myfitnessapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessapp.Classes.FriendRequestsModel;
import com.example.myfitnessapp.Classes.RewardsModel;
import com.example.myfitnessapp.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class FriendRequestsAdapter extends FirestorePagingAdapter<FriendRequestsModel, FriendRequestsAdapter.FriendRequestsViewHolder> {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String TAG = "FRAdapter";

    public FriendRequestsAdapter(@NonNull FirestorePagingOptions<FriendRequestsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final FriendRequestsViewHolder holder, int position, @NonNull final FriendRequestsModel model) {

        holder.textViewFriendRequestItemUserName.setText(model.getUsername());
        holder.textViewFriendRequestItemName.setText(model.getName());
        Picasso.get().load(model.getProfileimg()).into(holder.imageViewFriendRequestItemProfileImg);
        holder.textViewFriendRequetItemAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 1)Add user info to the followers collection // 2)Delete the user info from the follow_Request collection // 3)Increase the number of followers of the mAuth user
                //TODO 4)Increase the number of following of the other user // 5)Add mAuth user to the Following collection of the other user


                //todo 1
                //Adding the userInfo of the follower to the Followers Collection
                DocumentReference docRefAddToFollowersCollection =fStore.collection("user_s").document(mAuth.getUid()).collection("followers").document(model.getUser_AuthId());
                Map<String,Object> addingToFollowers = new HashMap<>();
                addingToFollowers.put("name",model.getName());
                addingToFollowers.put("profileimg",model.getProfileimg());
                addingToFollowers.put("username",model.getUsername());
                addingToFollowers.put("user_AuthId",model.getUser_AuthId());
                docRefAddToFollowersCollection.set(addingToFollowers);

                //todo 3
                //Increasing Number of followers
                //First query the followers collection and retrieve the no.of followers
                DocumentReference docRefIncreaseFollowersNumber = fStore.collection("user_s").document(mAuth.getUid()).collection("user_info").document(mAuth.getUid());
                docRefIncreaseFollowersNumber.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                int number = snapshot.getLong("followers").intValue();
                                ++number;

                                //we have increased the no.of followers by one so now we can add to fire_store again
                                DocumentReference docRef = fStore.collection("user_s").document(mAuth.getUid()).collection("user_info").document(mAuth.getUid());
                                Map<String,Object> followReq1 = new HashMap<>();
                                followReq1.put("followers",number);
                                docRef.update(followReq1);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "onFailure: "+ e.getMessage());
                            }
                        });

                //todo 2
                //Delete the document from follow Request collection
                fStore.collection("user_s").document(mAuth.getUid()).collection("follow_requests").document(model.getUser_AuthId()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess:(Follow Request Accepted) Removed the doc from follow request collection" );
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "onFailure: "+e.getMessage());
                            }
                        });
                //The item is supposed to sync directly and get removed but since it isnt rn im doing it manually
               holder.itemView.setVisibility(View.GONE);



                //todo 5
                //Adding the user info to the following collection

                fStore.collection("user_s").document(mAuth.getUid()).collection("user_info").document(mAuth.getUid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {

                                DocumentReference docRefAddToFollowingCollection =fStore.collection("user_s").document(model.getUser_AuthId()).collection("following").document(mAuth.getUid());
                                Map<String,Object> addingToFollowing = new HashMap<>();
                                addingToFollowing.put("name",snapshot.getString("name"));
                                addingToFollowing.put("profileimg",snapshot.getString("profileimg"));
                                addingToFollowing.put("username",snapshot.getString("username"));
                                addingToFollowing.put("user_AuthId",mAuth.getUid());
                                docRefAddToFollowingCollection.set(addingToFollowing);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });

                //todo 4
                //Increasing number of following
                DocumentReference docRefIncreaseFollowingNumber = fStore.collection("user_s").document(model.getUser_AuthId()).collection("user_info").document(model.getUser_AuthId());
                docRefIncreaseFollowingNumber.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                int number = snapshot.getLong("following").intValue();
                                ++number;

                                //we have increased the no.of followers by one so now we can add to fire_store again
                                DocumentReference docRef2 = fStore.collection("user_s").document(model.getUser_AuthId()).collection("user_info").document(model.getUser_AuthId());
                                Map<String,Object> followingReq1 = new HashMap<>();
                                followingReq1.put("following",number);
                                docRef2.update(followingReq1);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "onFailure: "+ e.getMessage());
                            }
                        });


            }
        });
        holder.textViewFriendRequetItemDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setVisibility(View.GONE);

                fStore.collection("user_s").document(mAuth.getUid()).collection("follow_requests").document(model.getUser_AuthId()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess:(Follow Request Declined) Removed the doc from follow request collection" );
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "onFailure: "+e.getMessage());
                            }
                        });
            }
        });
    }

    @NonNull
    @Override
    public FriendRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_friend_requet_item,
                parent,
                false
        );
        return new FriendRequestsAdapter.FriendRequestsViewHolder(view);
    }

    public class FriendRequestsViewHolder extends RecyclerView.ViewHolder{

        TextView textViewFriendRequestItemUserName, textViewFriendRequestItemName ,textViewFriendRequetItemAccept,textViewFriendRequetItemDecline;
        ImageView imageViewFriendRequestItemProfileImg;

        public FriendRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFriendRequestItemProfileImg = itemView.findViewById(R.id.friendRequestItemProfileImg);
            textViewFriendRequestItemName = itemView.findViewById(R.id.friendRequestItemName);
            textViewFriendRequestItemUserName = itemView.findViewById(R.id.friendRequestItemUserName);
            textViewFriendRequetItemAccept = itemView.findViewById(R.id.friendRequestItemAccept);
            textViewFriendRequetItemDecline = itemView.findViewById(R.id.friendRequestItemDecline);
        }
    }
}
