package com.indeves.chmplinapp.Activities;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.indeves.chmplinapp.Fragments.*;
import com.indeves.chmplinapp.Fragments.UserProfileEventsTab;
import com.indeves.chmplinapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileMain extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
  //  private ViewPager viewPager;
  //  private TabLayout tabLayout;
    private ImageView imageView;
    private RatingBar ratingBar;
    private android.support.v4.app.Fragment fragment,initialFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_main);
      //  viewPager = (ViewPager) findViewById(R.id.container);
        imageView=findViewById(R.id.userProfile_pic);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        Picasso.with(this).load(String.valueOf(getResources().getDrawable(R.drawable.user))).placeholder(getResources().getDrawable(R.drawable.user)).resize(40, 40).into(imageView);

        ratingBar = findViewById(R.id.userProfile_rating);
        ratingBar.setNumStars(5);
        ratingBar.setRating(3);

        fragmentManager = getSupportFragmentManager();
        initialFragment = new UserProfileEventsTab();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_o, initialFragment).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_events:
                        fragment = new UserProfileEventsTab();
                        break;
                    case R.id.action_photographers:
                        fragment = new UserProfilePhotographersTab();
                        break;
                    case R.id.action_profile:
                        fragment = new UserProfileEditProfileTab();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_o, fragment).commit();
                return true;
            }
        });

        /*
    /*    tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
*//*
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);*/
        //setupViewPager(viewPager);


    }

   /* private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserProfileEventsTab(), "Events");
        adapter.addFragment(new UserProfilePhotographersTab(), "Photographers");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
*/}
