package com.example.myfitnessapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.myfitnessapp.Fragments.Frag_One;
import com.example.myfitnessapp.Fragments.Frag_Two;
import com.example.myfitnessapp.Fragments.Your_Activity_Frag_Two;
import com.example.myfitnessapp.Fragments.Your_Activity_Frag_one;
import com.example.myfitnessapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class YourActivity extends AppCompatActivity {

    private Your_Activity_Frag_one your_activity_frag_one;
    private Your_Activity_Frag_Two your_activity_frag_two;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your);

        viewPager = findViewById(R.id.vpFragment);
        tabLayout = findViewById(R.id.tabLayout);

        your_activity_frag_one = new Your_Activity_Frag_one();
        your_activity_frag_two = new Your_Activity_Frag_Two();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(your_activity_frag_one,"YOUR ACTIVITY");
        viewPagerAdapter.addFragment(your_activity_frag_two,"FOLLOWERS");
        viewPager.setAdapter(viewPagerAdapter);

        //To set the icons also dynamically we can do the following
        //tabLayout.getTabAt(0).setIcon(R.drawable.back_arrow);
        //btw the icons should be set after setting the fragments not before
        // you can also set up a badge( a dot on top as the notification etc.. it can contain numbers also signifying how many notifications so tat will be useful for later. see the video for more info

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
