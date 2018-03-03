package com.indeves.chmplinapp.Activities;

import android.app.Fragment;
import android.content.Intent;
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
import android.widget.LinearLayout;
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
    private android.support.v4.app.Fragment fragment, initialFragment;
    private FragmentManager fragmentManager;
    LinearLayout userProLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_main);
        //  viewPager = (ViewPager) findViewById(R.id.container);
        imageView = findViewById(R.id.userProfile_pic);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        Picasso.with(this).load(String.valueOf(getResources().getDrawable(R.drawable.user))).placeholder(getResources().getDrawable(R.drawable.user)).resize(40, 40).into(imageView);

        ratingBar = findViewById(R.id.userProfile_rating);
        ratingBar.setNumStars(5);
        ratingBar.setRating(3);
         userProLayout=(LinearLayout)findViewById(R.id.userProfile_LinearLayout) ;

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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
