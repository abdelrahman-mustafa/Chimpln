package com.indeves.chmplinapp.Utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.indeves.chmplinapp.R;
import com.kofigyan.stateprogressbar.StateProgressBar;

/**
 * Created by HP on 13/2/2018.
 */

public abstract class StepProgressBar extends android.support.v4.app.Fragment implements View.OnClickListener {  protected String[] descriptionData = {"Info", "Approval","Payment", "Booked"};

     protected StateProgressBar stateprogressbar;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root =super.onCreateView(inflater, container, savedInstanceState);


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        stateprogressbar = (StateProgressBar) view.findViewById(R.id.usage_stateprogressbar);
        stateprogressbar.setStateDescriptionData(descriptionData);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }

}


