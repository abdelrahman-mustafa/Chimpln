package com.indeves.chmplinapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.indeves.chmplinapp.Activities.Booking;
import com.indeves.chmplinapp.R;

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
        search = rootView.findViewById(R.id.userProfile_button_create);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(getResources().getString(R.string.selectEvType));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  go to booking  activity
                startActivity(new Intent(getContext(), Booking.class));
            }
        });
        return rootView;
    }
}
