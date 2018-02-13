package com.indeves.chmplinapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.indeves.chmplinapp.Fragments.ProEvents;
import com.indeves.chmplinapp.Fragments.ProLastWork;
import com.indeves.chmplinapp.Fragments.ProPackages;
import com.indeves.chmplinapp.Fragments.ProProfile;
import com.indeves.chmplinapp.R;

public class UserLandingPage extends AppCompatActivity implements ProEvents.OnFragmentInteractionListener,ProLastWork.OnFragmentInteractionListener,ProPackages.OnFragmentInteractionListener,ProProfile.OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private Fragment fragment,initialFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_landing_page);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        initialFragment = new ProLastWork();
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
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
