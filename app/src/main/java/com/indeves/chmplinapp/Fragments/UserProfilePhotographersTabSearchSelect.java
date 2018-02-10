package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class UserProfilePhotographersTabSearchSelect extends android.support.v4.app.Fragment {

    public UserProfilePhotographersTabSearchSelect() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Spinner spinner1, spinner2, spinner3, spinner4;
    Button search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers_search_select, container, false);

        spinner1 = rootView.findViewById(R.id.userProfile_phot_spinner_city);
        spinner2 = rootView.findViewById(R.id.userProfile_phot_spinner_gender);
        spinner3 = rootView.findViewById(R.id.userProfile_phot_spinner_event_date);
        spinner4 = rootView.findViewById(R.id.userProfile_phot_spinner_event_type);
        search = rootView.findViewById(R.id.userProfile_button_search);
 /*       search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfileEventsTabHistory nextFrag= new UserProfileEventsTabHistory();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });*/

        spinner3.setPrompt(getResources().getString(R.string.selectEvDate));
        spinner4.setPrompt(getResources().getString(R.string.selectEvType));
        spinner1.setPrompt(getResources().getString(R.string.selectEvCity));
        spinner2.setPrompt(getResources().getString(R.string.selectEvGender));


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(getResources().getString(R.string.selectEvType));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //-------------------------------------------------------
        List<String> categories2 = new ArrayList<String>();
        categories2.add(getResources().getString(R.string.selectEvGender));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories2);
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//----------------------------------------------------
        List<String> categories3 = new ArrayList<String>();
        categories3.add(getResources().getString(R.string.selectEvDate));


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories3);
        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//--------------------------------------------------------
        List<String> categories4 = new ArrayList<String>();
        categories4.add(getResources().getString(R.string.selectEvCity));


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories4);
        // Drop down layout style - list view with radio button
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//--------------------------

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);
        spinner3.setAdapter(dataAdapter3);
        spinner4.setAdapter(dataAdapter4);
        return rootView;
    }
}
