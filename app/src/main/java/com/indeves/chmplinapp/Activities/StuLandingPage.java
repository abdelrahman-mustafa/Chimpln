package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Fragments.ProEvents;
import com.indeves.chmplinapp.Fragments.ProLastWork;
import com.indeves.chmplinapp.Fragments.ProPackages;
import com.indeves.chmplinapp.Fragments.ProProfile;
import com.indeves.chmplinapp.Fragments.StuEvents;
import com.indeves.chmplinapp.Fragments.StuLastWork;
import com.indeves.chmplinapp.Fragments.StuPackages;
import com.indeves.chmplinapp.Fragments.StuProProfile;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;

public class StuLandingPage extends AppCompatActivity implements FirebaseEventsListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_landing_page);
        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        Fragment initialFragment = new StuLastWork();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, initialFragment).commit();

        ReadData readData = new ReadData();
        readData.searchPros();
        readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_lastWork:
                        fragment = new StuLastWork();
                        break;
                    case R.id.action_events:
                        fragment = new StuEvents();
                        break;
                    case R.id.action_packages:
                        fragment = new StuPackages();
                        break;
                    case R.id.action_profile:
                        fragment = new StuProProfile();
                        break;
                }
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            ProUserModel proUserModel = dataSnapshot.getValue(ProUserModel.class);
            setTitle(proUserModel.getName());
        }

    }
}
