package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.CityLookUpModel;
import com.indeves.chmplinapp.Models.EventOfTypeModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.MyDatePickerFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserProfilePhotographersTabSearchSelect extends android.support.v4.app.Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    Spinner spinGender, spinCity, eventTimesSpinner, spinEventType;
    Button search;
    List<CityLookUpModel> citiesList;
    List<String> genderCategories;
    List<String> dayTimeCategories;
    List<LookUpModel> eventTypesList, eventTimesList;
    ArrayAdapter<CityLookUpModel> citiesArrayAdapter;
    ArrayAdapter<LookUpModel> eventTypeArrayAdapter, eventTimeArrayAdapter;
    TextView selectedDate;
    String selectedBirthDate;
    Calendar calendar;
    ImageView getDate;
    int year, month, day;
    int dpyear, dpday, dpmonth;
    String selectedGender;
    String selectedCity;
    TextView date;
    LookUpModel selectedEventType, selectedEventTime;
    LinearLayout linear;
    private DatePickerDialog.OnDateSetListener dlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dpyear = i;
            dpmonth = i1 + 1;
            dpday = i2;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            date.setText(String.valueOf(dpday) + "-" + months[dpmonth - 1] + "-" + String.valueOf(dpyear));

        }
    };

    public UserProfilePhotographersTabSearchSelect() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers_search_select, container, false);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        linear = rootView.findViewById(R.id.linear);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        spinCity = rootView.findViewById(R.id.userProfile_phot_spinner_city);
        spinCity.setOnItemSelectedListener(this);
        selectedDate =  rootView.findViewById(R.id.date);
        date = rootView.findViewById(R.id.date);
        spinGender = rootView.findViewById(R.id.userProfile_phot_spinner_gender);
        spinGender.setOnItemSelectedListener(this);
        //This spinner wil be used for event times
        eventTimesSpinner = rootView.findViewById(R.id.userProfile_phot_spinner_event_date);
        eventTimesSpinner.setOnItemSelectedListener(this);
        spinEventType = rootView.findViewById(R.id.userProfile_phot_spinner_event_type);
        spinEventType.setOnItemSelectedListener(this);
        getDate = rootView.findViewById(R.id.get_date);

        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog c = new DatePickerDialog(getContext(), dlistener, dpyear, dpmonth, dpday);
                c.getDatePicker().setMinDate(new Date().getTime());
                c.show();

            }
        });
        search = rootView.findViewById(R.id.userProfile_button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ReadData readData = new ReadData();
                readData.getAllPros(new ReadData.AllProsListener() {
                    @Override
                    public void onProsResponse(ArrayList<ProUserModel> pros) {
                        //deal with pros
                        //    Log.v("ALlPros", pros.toString());

                        linear.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if (spinCity.getSelectedItemPosition() == 0 && spinGender.getSelectedItemPosition() == 0 && eventTimesSpinner.getSelectedItemPosition()== 0) {
                            UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput(pros);
                            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container_o, output);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (spinCity.getSelectedItemPosition() != 0) {
                            ArrayList<ProUserModel> customList = new ArrayList<>();


                            for (int i = 0; i < pros.size(); i++) {
                                ArrayList<String> avaList = new ArrayList<>();
                                avaList = pros.get(i).getEventAvailablity();


                                if (pros.get(i).getCity() != null && pros.get(i).getCity().equals(spinCity.getSelectedItem().toString())) {
                                    customList.add(pros.get(i));
                                }
                            }
                            UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput(customList);
                            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container_o, output);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        } else if (spinCity.getSelectedItemPosition() != 0 && eventTimesSpinner.getSelectedItemPosition() != 0) {
                            ArrayList<ProUserModel> customList = new ArrayList<>();


                            for (int i = 0; i < pros.size(); i++) {
                                ArrayList<String> avaList = new ArrayList<>();
                                avaList = pros.get(i).getEventAvailablity();


                                if (pros.get(i).getCity() != null && pros.get(i).getCity().equals(spinCity.getSelectedItem().toString()) && check(eventTimesSpinner.getSelectedItem().toString(), avaList)) {
                                    customList.add(pros.get(i));
                                }
                            }
                            UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput(customList);
                            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container_o, output);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        } else if (spinGender.getSelectedItemPosition() != 0) {
                            ArrayList<ProUserModel> customList = new ArrayList<>();


                            for (int i = 0; i < pros.size(); i++) {
                                ArrayList<String> avaList = new ArrayList<>();
                                avaList = pros.get(i).getEventAvailablity();
                                if (pros.get(i).getGender() != null && pros.get(i).getGender().equals(spinGender.getSelectedItem().toString())) {
                                    customList.add(pros.get(i));


                                }

                            }
                            UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput(customList);
                            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container_o, output);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        } else if (spinGender.getSelectedItemPosition() != 0 && eventTimesSpinner.getSelectedItemPosition() != 0 ) {
                            ArrayList<ProUserModel> customList = new ArrayList<>();


                            for (int i = 0; i < pros.size(); i++) {
                                ArrayList<String> avaList = new ArrayList<>();
                                avaList = pros.get(i).getEventAvailablity();
                                if (pros.get(i).getGender() != null && pros.get(i).getGender().equals(spinGender.getSelectedItem().toString()) && check(eventTimesSpinner.getSelectedItem().toString(), avaList)) {
                                    customList.add(pros.get(i));


                                }

                            }
                            UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput(customList);
                            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container_o, output);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        } else {
                            ArrayList<ProUserModel> customList = new ArrayList<>();

                            for (int i = 0; i < pros.size(); i++) {

                                ArrayList<String> avaList = new ArrayList<>();
                                avaList = pros.get(i).getEventAvailablity();
                                if (pros.get(i).getCity() != null && pros.get(i).getCity().contains(spinCity.getSelectedItem().toString()) && pros.get(i).getGender().contains(spinGender.getSelectedItem().toString())&& check(eventTimesSpinner.getSelectedItem().toString(), avaList)) {
                                    customList.add(pros.get(i));


                                }

                            }
                            UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput(customList);
                            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.container_o, output);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }

                    }
                });


            }
        });


        genderCategories = new ArrayList<String>();
        genderCategories.add(getResources().getString(R.string.gender));
        genderCategories.add("Male");
        genderCategories.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, genderCategories);
        // Drop down layout style - list view with radio button
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(genderArrayAdapter);
        //------------------------------------------------
        ReadData readData = new ReadData();

        citiesList = new ArrayList<CityLookUpModel>();
        citiesList.add(new CityLookUpModel(0, 0, "City"));
        citiesArrayAdapter = new ArrayAdapter<CityLookUpModel>(getContext(), android.R.layout.simple_spinner_item, citiesList);
        citiesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCity.setAdapter(citiesArrayAdapter);
        readData.getCitiesLookUpsWithCountryId(-1, new ReadData.CityLookUpsListener() {
            @Override
            public void onLookUpsResponse(List<CityLookUpModel> returnedCitiesList) {
                citiesList.addAll(returnedCitiesList);
                citiesArrayAdapter.notifyDataSetChanged();
            }
        });
        //-------------------------------------------------
        eventTypesList = new ArrayList<>();
        eventTypesList.add(0, new LookUpModel(0, getResources().getString(R.string.selectPackTy)));
        // Creating adapter for spinner
        eventTypeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTypesList);
        // Drop down layout style - list view with radio button
        eventTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinEventType.setAdapter(eventTypeArrayAdapter);

        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eventTypesList.addAll(eventTypeLookups);
                eventTypeArrayAdapter.notifyDataSetChanged();
            }
        });

        //-------------------------------------------------
        eventTimesList = new ArrayList<>();
        eventTimesList.add(new LookUpModel(0, "Event time"));
        eventTimeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eventTimesList);
        eventTimeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTimesSpinner.setAdapter(eventTimeArrayAdapter);

        readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> lookups) {
                eventTimesList.addAll(lookups);
                eventTimeArrayAdapter.notifyDataSetChanged();
            }
        });
        //K.A: example of getting events based on type
//        readData.getProEventsBasedOnType("dvim55FWlihlaQeOfJ9JETomdki1", new ReadData.ProEventsBasedOnTypeListener() {
//            @Override
//            public void onResponse(ArrayList<EventOfTypeModel> eventOfTypeModels) {
//                if (eventOfTypeModels != null) {
//                    Log.v("EventsBasedOntTypes", eventOfTypeModels.toString());
//                }
//
//            }
//        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == search) {
        /*    // add query function to get the data match the outputs of search
            ReadData readData = new ReadData();
            readData.getAllPros(new ReadData.AllProsListener() {
                @Override
                public void onProsResponse(ArrayList<ProUserModel> pros) {
                    //deal with pros
                    Log.v("ALlPros", pros.toString());
                    for (ProUserModel d : pros) {
                        if (d.getCity() != null && d.getCity().contains(spinCity.getSelectedItem().toString())) {
                            ArrayList<ProUserModel> customList = new ArrayList<>();
                            customList.add(d);


                        }
                    }
                }
            });*/

        }

    }


    private Boolean check(String search, ArrayList<String> myList) {
        for (String str : myList) {
            if (str.trim().contains(search))
                return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == eventTimesSpinner.getId()) {
            selectedEventTime = eventTimesList.get(position);
        } else if (parent.getId() == spinGender.getId()) {
            selectedGender = genderCategories.get(position);

        } else if (parent.getId() == spinCity.getId()) {
            selectedCity = citiesList.get(position).getEnglishName();

        } else if (parent.getId() == spinEventType.getId()) {
            selectedEventType = eventTypesList.get(position);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        // Create a Date variable/object with user chosen date
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        Date chosenDate = cal.getTime();

        // Format the date using style and locale
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String formattedDate = df.format(chosenDate);

        // Display the chosen date to app interface
        date.setText(formattedDate);
    }
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    date.setText(  view.getYear() +
                            " / " + (view.getMonth()+1) +
                            " / " + view.getDayOfMonth());
                }
            };
}
