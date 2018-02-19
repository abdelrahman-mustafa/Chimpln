package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.indeves.chmplinapp.Adapters.UserAccPhotographersAdaptor;
import com.indeves.chmplinapp.Models.UserAccPhotographerData;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfilePhotographersTabSearchOutputSelectPhotographer extends android.support.v4.app.Fragment {

    public UserProfilePhotographersTabSearchOutputSelectPhotographer() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    Spinner spinner1;

    Button search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__user_photographer_profile, container, false);
        spinner1 = rootView.findViewById(R.id.userProfile_phot_spinner_type);
        search = rootView.findViewById(R.id.userProfile_button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //  go to booking  activity
            }
        });
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(getResources().getString(R.string.selectEvType));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

        return rootView;
    }
}
