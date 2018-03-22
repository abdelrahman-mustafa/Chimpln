package com.indeves.chmplinapp.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.CityLookUpModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserProfilePhotographersTabSearchSelect extends android.support.v4.app.Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {
    Spinner spinGender, spinCity, spinbirthPicker, spinEventType;
    Button search;
    List<CityLookUpModel> citiesList;
    List<LookUpModel> countriesList;
    List<String> genderCategories;
    List<String> dayTimeCategories;
    List<LookUpModel> eventTypesList = new ArrayList<>();
    ArrayAdapter<CityLookUpModel> citiesArrayAdapter;
    ArrayAdapter<LookUpModel> eventTypeArrayAdapter;
    TextView selectedDate;
    String selectedBirthDate;
    Calendar calendar;
    int year, month, day;
    String selectedGender;
    String selectedCity;
    LookUpModel selectedEventType;

    public UserProfilePhotographersTabSearchSelect() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
//                    String ageString = String.valueOf(arg3) + "-" + String.valueOf(arg2) + "-" + String.valueOf(arg1);
                    selectedBirthDate = String.valueOf(arg3) + "-" + String.valueOf(arg2 + 1) + "-" + String.valueOf(arg1);
                }
            };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers_search_select, container, false);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        spinCity = rootView.findViewById(R.id.userProfile_phot_spinner_city);
        spinbirthPicker.setOnItemSelectedListener(this);
        spinbirthPicker.setOnTouchListener(this);
        spinGender = rootView.findViewById(R.id.userProfile_phot_spinner_gender);
        spinbirthPicker = rootView.findViewById(R.id.userProfile_phot_spinner_event_date);
        spinEventType = rootView.findViewById(R.id.userProfile_phot_spinner_event_type);
        search = rootView.findViewById(R.id.userProfile_button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setVisibility(View.GONE);
                UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput();
                android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                transaction.replace(R.id.container, output);
                transaction.commit();
            }
        });


        spinbirthPicker.setPrompt(getResources().getString(R.string.selectEvDate));
        spinEventType.setPrompt(getResources().getString(R.string.selectEvType));
        spinCity.setPrompt(getResources().getString(R.string.selectEvCity));
        spinGender.setPrompt(getResources().getString(R.string.selectEvGender));

        genderCategories = new ArrayList<String>();
        genderCategories.add(getResources().getString(R.string.gender));
        genderCategories.add("Male");
        genderCategories.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.spinner_item, genderCategories);
        // Drop down layout style - list view with radio button
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(genderArrayAdapter);
        //------------------------------------------------
        citiesList = new ArrayList<CityLookUpModel>();
        citiesList.add(new CityLookUpModel(0, 0, "City"));
        citiesArrayAdapter = new ArrayAdapter<CityLookUpModel>(getContext(), R.layout.spinner_item, citiesList);
        citiesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCity.setAdapter(citiesArrayAdapter);
        //-------------------------------------------------

        eventTypesList.add(0, new LookUpModel(0, getResources().getString(R.string.selectPackTy)));
        // Creating adapter for spinner
        eventTypeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTypesList);
        // Drop down layout style - list view with radio button
        eventTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinEventType.setAdapter(eventTypeArrayAdapter);
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

    @Override
    public void onClick(View v) {
        if (v == search){
            // add query function to get the data match the outputs of search
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == spinbirthPicker.getId()) {
            //showDialog(999);
        } else if (parent.getId() == spinGender.getId()) {
            selectedGender = genderCategories.get(position);

        } else if (parent.getId() == spinCity.getId()) {
            selectedCity = citiesList.get(position).getEnglishName();

        }else if (parent.getId() == spinEventType.getId()){
            selectedEventType = eventTypesList.get(position);

        }



        /* else if (parent.getId() == sCountry.getId()) {
            selectedCountry = countriesList.get(position).getEnglishName();
            country.setText(selectedCountry);
            ReadData readData = new ReadData();
            //TODO: issue occurs in reselecting multiple times
            readData.getCitiesLookUpsWithCountryId(countriesList.get(position).getId(), new ReadData.CityLookUpsListener() {
                @Override
                public void onLookUpsResponse(List<CityLookUpModel> newCitiesList) {
                    Log.v("CitiesReturned", newCitiesList.toString());
                    citiesList.clear();
                    citiesList.addAll(newCitiesList);
                    citiesArrayAdapter.notifyDataSetChanged();
                }
            });


        } */


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
