package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.Fragments.Frag_Four;
import com.example.myfitnessapp.Fragments.Frag_One;
import com.example.myfitnessapp.Fragments.Frag_Three;
import com.example.myfitnessapp.Fragments.Frag_Two;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Bazzar_Rewards extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Frag_One frag_one;
    private Frag_Two frag_two;
    private Frag_Three frag_three;
    private Frag_Four frag_four;
    TextView textViewWalletBalanceFromFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazzar__rewards);
//TODO set the fire_base rules . right now it allows read and write access to anyone

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        viewPager = findViewById(R.id.vpFragment);
        tabLayout = findViewById(R.id.tabLayout);
        textViewWalletBalanceFromFireStore = findViewById(R.id.tvWalletBalanceFromFireStore);

        frag_one = new Frag_One();
        frag_two = new Frag_Two();
        frag_three = new Frag_Three();
        frag_four = new Frag_Four();

        walletDetailsFromFireStore();

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(frag_one,"FASHION");
        viewPagerAdapter.addFragment(frag_two,"SHOES");
        viewPagerAdapter.addFragment(frag_three,"ELECTRONICS");
        viewPagerAdapter.addFragment(frag_four,"COUPONS");
        viewPager.setAdapter(viewPagerAdapter);


    }

    private void walletDetailsFromFireStore() {

        String UserId = mAuth.getUid();

        fStore.collection("user_s").document(UserId).collection("wallet_info").document(UserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                //TODO Since the balance value is on type int and v have converted to string , so convert it back to int while using arithmetic operations on it
                int WalletBalance = snapshot.getLong("balance").intValue(); //This is to get integer value from FireStore
                textViewWalletBalanceFromFireStore.setText( String.valueOf(WalletBalance) ); // This is to convert the int value to string and setText() requires String value
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("RewardsError",e.getMessage()+"");
                Toast.makeText(Bazzar_Rewards.this,"Couldn't Load Wallet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentsTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment,String title){
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
