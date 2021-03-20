package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfitnessapp.Classes.SearchPeopleModel;
import com.example.myfitnessapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.cache.DiskLruCache;

public class UpdatingUserInfo extends AppCompatActivity {

    private static final String TAG = "TAG";
    //DECLARING THE VIEWS
    ImageView imageViewDisplayImage,imageViewCheckUsername;
    EditText editTextName,editTextSetUsername,editTextSetBio;
    TextView textViewSaveInfo;
    //DECLARING DATA TYPES
    String user_id;
    //DECLARING FIRE_BASE TOKENS
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating_user_info);

        //INITIALISING FIRE_BASE TOKENS
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //REFERENCING THE VIEWS
        imageViewDisplayImage = findViewById(R.id.ivDisplayImage);
        imageViewCheckUsername = findViewById(R.id.ivCheckUsername);
        editTextName = findViewById(R.id.etName);
        editTextSetUsername = findViewById(R.id.etSetUserName);
        textViewSaveInfo = findViewById(R.id.tvSaveInfo);
        editTextSetBio = findViewById(R.id.etSetBio);

        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        editTextName.setText(account.getDisplayName());
        Glide.with(this).load(account.getPhotoUrl()).into(imageViewDisplayImage);

        editTextSetUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageViewCheckUsername.setImageResource(R.drawable.just_a_tick);
            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (s.toString().isEmpty()){
                    imageViewCheckUsername.setImageResource(R.drawable.just_a_tick);
                    textViewSaveInfo.setVisibility(View.INVISIBLE);
                }
                else {
                    fStore.collection("usernames").get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int flag = 0;
                                    String username = s.toString().trim();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        if(documentSnapshot.get("username").equals(username)){
                                            flag = 1;
                                            break;
                                        }
                                    }
                                    if (flag == 0){
                                        imageViewCheckUsername.setImageResource(R.drawable.green_check);
                                        textViewSaveInfo.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        imageViewCheckUsername.setImageResource(R.drawable.red_check);
                                        textViewSaveInfo.setVisibility(View.INVISIBLE);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdatingUserInfo.this, "Failed to check Username", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        imageViewCheckUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextSetUsername.getText().toString().trim().isEmpty()){
                    Toast.makeText(UpdatingUserInfo.this, "Enter Username", Toast.LENGTH_SHORT).show();
                }
                else {
                    fStore.collection("usernames").get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    String username = editTextSetUsername.getText().toString().trim();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        if(documentSnapshot.get("username").equals(username)){
                                            imageViewCheckUsername.setImageResource(R.drawable.red_check);
                                            textViewSaveInfo.setVisibility(View.INVISIBLE);
                                        }
                                        else{
                                            imageViewCheckUsername.setImageResource(R.drawable.green_check);
                                            textViewSaveInfo.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdatingUserInfo.this, "Failed to check Username", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        textViewSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = mAuth.getCurrentUser().getUid();

                settingUpInitialWalletCollection(user_id);
                addingTheUserToThe_ToSearch_collection(user_id, account.getPhotoUrl().toString());
                addingTheUsernameInThe_userNames_collection(user_id);
                settingUpSocialStatus(user_id, account.getPhotoUrl().toString());//followers and following

                Intent intent = new Intent(UpdatingUserInfo.this,VideoVIewer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void settingUpSocialStatus(String user_id,String profileImgUrl) {
        DocumentReference documentReference = fStore.collection("user_s").document(user_id).collection("user_info").document(user_id);
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("name",editTextName.getText().toString().trim());
        userInfo.put("profileimg",profileImgUrl);
        userInfo.put("username",editTextSetUsername.getText().toString().trim());
        userInfo.put("followers",0);
        userInfo.put("following",0);
        userInfo.put("posts",0);
        userInfo.put("bio",editTextSetBio.getText().toString());
        userInfo.put("private",false);
        documentReference.set(userInfo);
    }

    private void addingTheUsernameInThe_userNames_collection(String user_id) {
        DocumentReference documentReference = fStore.collection("usernames").document(user_id);
        Map<String,Object> username = new HashMap<>();
        username.put("username",editTextSetUsername.getText().toString().trim());
        documentReference.set(username);
    }

    private void addingTheUserToThe_ToSearch_collection(String user_id,String profileImgUrl) {
        DocumentReference documentReference = fStore.collection("ToSearch").document(user_id);
        SearchPeopleModel searchPeopleModel = new SearchPeopleModel();
        searchPeopleModel.setUsername(editTextSetUsername.getText().toString().trim());
        searchPeopleModel.setName(editTextName.getText().toString().trim());
        searchPeopleModel.setProfileimg(profileImgUrl);
        searchPeopleModel.setUser_AuthId(mAuth.getUid());
        documentReference.set(searchPeopleModel);
    }

    private void settingUpInitialWalletCollection(String user_id) {
        DocumentReference documentReference = fStore.collection("user_s").document(user_id).collection("wallet_info").document(user_id);
        Map<String,Object> walletInfo = new HashMap<>();
        walletInfo.put("balance",0);
        walletInfo.put("money_spent",0);
        walletInfo.put("total_earned",0);
        documentReference.set(walletInfo);
    }
}
