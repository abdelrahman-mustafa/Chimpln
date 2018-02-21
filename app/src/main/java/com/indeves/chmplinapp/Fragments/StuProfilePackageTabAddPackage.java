package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class StuProfilePackageTabAddPackage extends android.support.v4.app.Fragment {


    Spinner spinner1, spinner2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_stu_profile_tab_add_package, container, false);

        spinner1 = rootView.findViewById(R.id.proProfile_spinner_package_type);
        spinner2 = rootView.findViewById(R.id.proProfile_spinner_package_time);

        spinner1.setPrompt(getResources().getString(R.string.selectPackTy));
        spinner2.setPrompt(getResources().getString(R.string.selectPackTyime));
        List<String> categories4 = new ArrayList<String>();

        categories4.add(getResources().getString(R.string.selectPackTy));
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories4);
        // Drop down layout style - list view with radio button
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<String> categories3 = new ArrayList<String>();

        categories3.add(getResources().getString(R.string.selectPackTyime));
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories3);
        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner1.setAdapter(dataAdapter4);
        spinner2.setAdapter(dataAdapter3);
        return rootView;
    }

}
