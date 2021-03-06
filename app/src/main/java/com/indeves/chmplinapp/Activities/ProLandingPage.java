package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Fragments.ProEditProfileFragment;
import com.indeves.chmplinapp.Fragments.ProEvents;
import com.indeves.chmplinapp.Fragments.ProLastWork;
import com.indeves.chmplinapp.Fragments.ProPackages;
import com.indeves.chmplinapp.Fragments.ProProfile;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;

public class ProLandingPage extends AppCompatActivity implements FirebaseEventsListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_landing_page);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        ReadData readData = new ReadData(this);
        readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Fragment initialFragment = new ProEvents();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        transaction.replace(R.id.main_container, initialFragment).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_lastWork:
                        fragment = new ProLastWork();
                        break;
                    case R.id.action_events:
                        fragment = new ProEvents();
                        break;
                    case R.id.action_packages:
                        fragment = new ProPackages();
                        break;
                    case R.id.action_profile:
                        fragment = new ProProfile();
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
//        super.onBackPressed();
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
