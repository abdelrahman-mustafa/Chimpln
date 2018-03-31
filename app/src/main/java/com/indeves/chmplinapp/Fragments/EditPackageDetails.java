package com.indeves.chmplinapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class EditPackageDetails extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, FirebaseEventsListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Spinner eventTypeSpinner, eventTimeSpinner;
    List<LookUpModel> eventTypesList;
    List<LookUpModel> eventTimeList;
    ArrayAdapter<LookUpModel> eventTypeArrayAdapter;
    ArrayAdapter<LookUpModel> eventTimeArrayAdapter;
    EditText packageTitle, packagePrice, packageDescription;
    Button addNewPackage;
    LookUpModel selectedEventType, selectedEventTime;
    ProUserModel proUserModel;
    private PackageModel selectedPackage;

    public EditPackageDetails() {
        // Required empty public constructor
    }

    public static EditPackageDetails newInstance(PackageModel param1, ProUserModel proUserModel) {
        EditPackageDetails fragment = new EditPackageDetails();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, proUserModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedPackage = (PackageModel) getArguments().getSerializable(ARG_PARAM1);
            proUserModel = (ProUserModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_package_details, container, false);
        eventTypeSpinner = rootView.findViewById(R.id.proProfile_spinner_package_type);
        eventTimeSpinner = rootView.findViewById(R.id.proProfile_spinner_package_time);
        packageTitle = rootView.findViewById(R.id.proProfile_editText_package_name);
        packageTitle.setText(selectedPackage.getPackageTitle());
        packagePrice = rootView.findViewById(R.id.proProfile_editText_package_price);
        packagePrice.setText(String.valueOf(selectedPackage.getPrice()));
        packageDescription = rootView.findViewById(R.id.proProfile_editText_package_desc);
        packageDescription.setText(selectedPackage.getPackageDescription());
        addNewPackage = rootView.findViewById(R.id.pro_packages_addPackage_button);
        addNewPackage.setOnClickListener(this);

        eventTypesList = new ArrayList<>();
        eventTimeList = new ArrayList<>();
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
                eventTypesList.addAll(eventTypeLookups);
                eventTypeArrayAdapter.notifyDataSetChanged();
                for (int i = 0; i < eventTypesList.size(); i++) {
                    if (selectedPackage.getEventTypeId() == eventTypesList.get(i).getId()) {
                        eventTypeSpinner.setSelection(i);
                    }
                }
            }
        });
        readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> lookups) {
                eventTimeList.addAll(lookups);
                eventTimeArrayAdapter.notifyDataSetChanged();
                for (int i = 0; i < eventTimeList.size(); i++) {
                    if (selectedPackage.getEventTimeId() == eventTimeList.get(i).getId()) {
                        eventTimeSpinner.setSelection(i);
                    }
                }
            }
        });
        eventTypeSpinner.setOnItemSelectedListener(this);
        eventTimeSpinner.setOnItemSelectedListener(this);
        return rootView;
    }

    boolean validatePackageForm() {
        boolean formIsValid = true;
        if (selectedEventType == null || selectedEventType.getId() == 0) {
            Toast.makeText(getContext(), "Please select event type", Toast.LENGTH_SHORT).show();
            formIsValid = false;
        } else if (selectedEventTime == null || selectedEventTime.getId() == 0) {
            Toast.makeText(getContext(), "Please select event time", Toast.LENGTH_SHORT).show();
            formIsValid = false;
        } else if (packageTitle.getText().toString().isEmpty()) {
            packageTitle.setError(getResources().getString(R.string.error_field_missing));
            formIsValid = false;

        } else if (packagePrice.getText().toString().isEmpty()) {
            packagePrice.setError(getResources().getString(R.string.error_field_missing));
            formIsValid = false;
        } else if (packageDescription.getText().toString().isEmpty()) {
            packageDescription.setError(getResources().getString(R.string.error_field_missing));
            formIsValid = false;
        }


        return formIsValid;

    }

    @Override
    public void onClick(View v) {
        if (validatePackageForm()) {
            //Todo: this shit way of edit should be changes ASAP
            PackageModel editedPackageModel = new PackageModel(packageTitle.getText().toString(), packageDescription.getText().toString(), selectedEventType.getId(), selectedEventTime.getId(), Integer.parseInt(packagePrice.getText().toString()));
            List<PackageModel> packageModelList = proUserModel.getPackages();
            for (int i = 0; i < packageModelList.size(); i++) {
                if (packageModelList.get(i) == selectedPackage) {
                    packageModelList.set(i, editedPackageModel);
                }
            }
            ProUserModel newModel = new ProUserModel();
            newModel.setPackages(packageModelList);
            WriteData writeData = new WriteData(this);
            try {
                writeData.updateUserProfileData(newModel);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == eventTimeSpinner.getId()) {

            selectedEventTime = eventTimeList.get(position);

        } else if (parent.getId() == eventTypeSpinner.getId()) {
            selectedEventType = eventTypesList.get(position);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        if (writeSuccessful) {
            Toast.makeText(getContext(), "Saved successfully!", Toast.LENGTH_SHORT).show();
            if (getFragmentManager() != null)
                getFragmentManager().popBackStack();
        }

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }
}
