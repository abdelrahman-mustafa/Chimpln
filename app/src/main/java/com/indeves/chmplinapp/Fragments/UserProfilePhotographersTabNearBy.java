package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;

public class UserProfilePhotographersTabNearBy extends android.support.v4.app.Fragment {

    public UserProfilePhotographersTabNearBy() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers_nerby, container, false);
   /*     ReadData readData = new ReadData();
        readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());

        readData.getAllPros(new ReadData.AllProsListener() {
            @Override
            public void onProsResponse(ArrayList<ProUserModel> pros) {

                for (int i = 0; i < pros.size(); i++) {
                    if (pros.get(i).getCity() != null && pros.get(i).getCity().contains()){
                    ArrayList<ProUserModel> customList = new ArrayList<>();
                        customList.add(pros.get(i));
                        UserProfilePhotographersTabSearchOutput output = new UserProfilePhotographersTabSearchOutput(customList);
                        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                        transaction.replace(R.id.container_special, output);
                        transaction.commit();


                    }
                }
            }
        });*/

        return rootView;
    }
}
