package com.indeves.chmplinapp.Fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class UserProfilePhotographersTabSearch extends android.support.v4.app.Fragment {

    Button search;

    public UserProfilePhotographersTabSearch() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers_search, container, false);

        UserProfilePhotographersTabSearchSelect select =  new UserProfilePhotographersTabSearchSelect();
        android.support.v4.app.FragmentTransaction transaction0 = getChildFragmentManager().beginTransaction();
        transaction0.replace(R.id.container, select);
        transaction0.addToBackStack("tag");
        transaction0.commit();
       /* search = rootView.findViewById(R.id.userProfile_button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setVisibility(View.GONE);
                UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput();
                android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                transaction.replace(R.id.container, output);
                transaction.commit();            }
        });
*/
        return rootView;
    }
}
