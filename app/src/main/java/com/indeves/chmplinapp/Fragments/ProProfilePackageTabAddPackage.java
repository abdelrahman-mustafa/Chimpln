package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.PackageModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProProfilePackageTabAddPackage extends android.support.v4.app.Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, FirebaseEventsListener {


    Spinner eventTypeSpinner, eventTimeSpinner;
    List<LookUpModel> eventTypesList = new ArrayList<>();
    List<LookUpModel> eventTimeList = new ArrayList<>();
    ArrayAdapter<LookUpModel> eventTypeArrayAdapter;
    ArrayAdapter<LookUpModel> eventTimeArrayAdapter;
    EditText packageTitle, packagePrice, packageDescription;
    Button addNewPackage;
    LookUpModel selectedEventType, selectedEventTime;


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
        packageTitle = rootView.findViewById(R.id.proProfile_editText_package_name);
        packagePrice = rootView.findViewById(R.id.proProfile_editText_package_price);
        packageDescription = rootView.findViewById(R.id.proProfile_editText_package_desc);
        addNewPackage = rootView.findViewById(R.id.pro_packages_addPackage_button);
        addNewPackage.setOnClickListener(this);


        eventTypesList.add(0, new LookUpModel(0, getResources().getString(R.string.selectPackTy)));
        // Creating adapter for spinner
        eventTypeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTypesList);
        // Drop down layout style - list view with radio button
        eventTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventTimeList.add(0, new LookUpModel(0, getResources().getString(R.string.selectPackTyime)));
        // Creating adapter for spinner
        eventTimeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTimeList);
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
        readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> lookups) {
                eventTimeArrayAdapter.addAll(lookups);
            }
        });
        eventTypeSpinner.setOnItemSelectedListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (v == addNewPackage) {
            if (validatePackageForm()) {
                PackageModel packageModel = new PackageModel(packageTitle.getText().toString(), packageDescription.getText().toString(), selectedEventType.getId(), selectedEventTime.getId(), Integer.parseInt(packagePrice.getText().toString()));
                WriteData writeData = new WriteData(this);
                writeData.addNewProPackage(packageModel);
            }
        }
    }

    boolean validatePackageForm() {
        boolean formIsValid = true;
        if (selectedEventType.getId() == 0) {
            Toast.makeText(getContext(), "Please select event type", Toast.LENGTH_SHORT).show();
            formIsValid = false;
        } else if (selectedEventTime.getId() == 0) {
            Toast.makeText(getContext(), "Please select event time", Toast.LENGTH_SHORT).show();
            formIsValid = false;
        } else if (packageTitle.getText().toString().isEmpty()) {
            packageTitle.setError(getResources().getString(R.string.error_field_missing));
            formIsValid = false;

        } else if (packagePrice.getText().toString().isEmpty()) {
            packageTitle.setError(getResources().getString(R.string.error_field_missing));
            formIsValid = false;
        } else if (packageDescription.getText().toString().isEmpty()) {
            packageTitle.setError(getResources().getString(R.string.error_field_missing));
            formIsValid = false;
        }


        return formIsValid;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view == eventTimeSpinner) {

            selectedEventTime = eventTimeList.get(position);

        } else if (view == eventTypeSpinner) {
            selectedEventType = eventTypesList.get(position);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        }

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }
}
