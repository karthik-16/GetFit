package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.myfitnessapp.Fragments.FollowersAndFollowingActivity_Followers_Frag;
import com.example.myfitnessapp.Fragments.FollowersAndFollowingActivity_Following_Frag;
import com.example.myfitnessapp.Fragments.Your_Activity_Frag_Two;
import com.example.myfitnessapp.Fragments.Your_Activity_Frag_one;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FollowersAndFollowing extends AppCompatActivity {

    private FollowersAndFollowingActivity_Followers_Frag followersAndFollowingActivity_followers_frag;
    private FollowersAndFollowingActivity_Following_Frag followersAndFollowingActivity_following_frag;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView textViewUsernameFollowersAndFollowing;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_and_following);


        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        viewPager = findViewById(R.id.vpFragment);
        tabLayout = findViewById(R.id.tabLayout);
        textViewUsernameFollowersAndFollowing = findViewById(R.id.tvUsernameFollowersAndFollowing);

        //Getting the id from intentExtra
        String thePersonsId = getIntent().getStringExtra("idOfPerson");

        //Creating a bundle to pass to fragments
        Bundle b = new Bundle();
        b.putString("id",thePersonsId);

        followersAndFollowingActivity_followers_frag = new FollowersAndFollowingActivity_Followers_Frag();
        followersAndFollowingActivity_followers_frag.setArguments(b);

        followersAndFollowingActivity_following_frag = new FollowersAndFollowingActivity_Following_Frag();

        ///////////////////////////////////See how to get data from activity to fragment
        ///String idOfTheOtherPerson = getIntent().getExtras().get("idOfPerson").toString();



        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(followersAndFollowingActivity_followers_frag,"FOLLOWERS");
        viewPagerAdapter.addFragment(followersAndFollowingActivity_following_frag,"FOLLOWING");
        viewPager.setAdapter(viewPagerAdapter);

        getTheUserName();

    }

    private void getTheUserName() {

        String id = getIntent().getStringExtra("idOfPerson");

        fStore.collection("user_s").document(id).collection("user_info").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        textViewUsernameFollowersAndFollowing.setText(snapshot.getString("username"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        // This is to statically assign the titles to the fragments with are both in array-lists
        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentsTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment,String title){
            // In this method we will be assigning the title to the respective fragments
            fragments.add(fragment);
            fragmentsTitle.add(title);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitle.get(position);
        }
    }

}
