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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Fragments.*;
import com.indeves.chmplinapp.Fragments.UserProfileEventsTab;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileMain extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    LinearLayout userProLayout;
    TextView userName;
    //  private ViewPager viewPager;
    //  private TabLayout tabLayout;
    private ImageView imageView;
    private RatingBar ratingBar;
    private android.support.v4.app.Fragment fragment, initialFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_main);
        //  viewPager = (ViewPager) findViewById(R.id.container);
        imageView = findViewById(R.id.userProfile_pic);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        userName = findViewById(R.id.userProfile_name);
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Picasso.with(this).load(String.valueOf(getResources().getDrawable(R.drawable.user))).placeholder(getResources().getDrawable(R.drawable.user)).resize(40, 40).into(imageView);
        ratingBar = findViewById(R.id.userProfile_rating);
        ratingBar.setNumStars(5);
        ratingBar.setRating(3);
        userProLayout = (LinearLayout) findViewById(R.id.userProfile_LinearLayout);
//        ReadData readData = new ReadData(firebaseEventsListener);
//        readData.getEventsByProId("dvim55FWlihlaQeOfJ9JETomdki1");

        fragmentManager = getSupportFragmentManager();
        initialFragment = new UserProfilePhotographersTab();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_o, initialFragment).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_photographers:
                        fragment = new UserProfilePhotographersTab();
                        break;
                    case R.id.action_events:
                        fragment = new UserProfileEventsTab();
                        break;

                    case R.id.action_profile:
                        fragment = new UserProfileProfileTab();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_o, fragment).commit();
                return true;
            }
        });

    }

//    private FirebaseEventsListener firebaseEventsListener = new FirebaseEventsListener() {
//        @Override
//        public void onWriteDataCompleted(boolean writeSuccessful) {
//
//        }
//
//        @Override
//        public void onReadDataResponse(DataSnapshot dataSnapshot) {
//            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//                Log.v("AllProEvents", dataSnapshot.getValue().toString());
//            }
//
//        }
//    };

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pro_profile_fragment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pro_profile_menu_edit) {
            //Go to edit screen4
            fragmentManager = getSupportFragmentManager();
            fragment = new UserProfileEditProfileTab();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_o, fragment).commit();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

}
