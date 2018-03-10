package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProProfilePackageTabAddPackage extends android.support.v4.app.Fragment {


    Spinner eventTypeSpinner, eventTimeSpinner;
    List<LookUpModel> eventTypesList = new ArrayList<>();
    List<String> eventTimeList = new ArrayList<String>();
    ArrayAdapter<LookUpModel> eventTypeArrayAdapter;
    ArrayAdapter<String> eventTimeArrayAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_pro_profile_tab_add_package, container, false);

        eventTypeSpinner = rootView.findViewById(R.id.proProfile_spinner_package_type);
        eventTimeSpinner = rootView.findViewById(R.id.proProfile_spinner_package_time);


        eventTypesList.add(0, new LookUpModel(0, getResources().getString(R.string.selectPackTy)));
        // Creating adapter for spinner
        eventTypeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTypesList);
        // Drop down layout style - list view with radio button
        eventTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventTimeList.add(0, getResources().getString(R.string.selectPackTyime));
        // Creating adapter for spinner
        eventTimeArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, eventTimeList);
        // Drop down layout style - list view with radio button
        eventTimeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        eventTypeSpinner.setAdapter(eventTypeArrayAdapter);
        eventTimeSpinner.setAdapter(eventTimeArrayAdapter);
        ReadData readData = new ReadData();
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eventTypeArrayAdapter.addAll(eventTypeLookups);
            }
        });

        return rootView;
    }


}
