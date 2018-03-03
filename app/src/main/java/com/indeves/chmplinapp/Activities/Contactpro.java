package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class Contactpro extends StepProgressBar implements View.OnClickListener {
    Button button;

    public Contactpro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.activity_contactpro, container, false);
        button=(Button)rootview.findViewById(R.id.button4);
        button.setOnClickListener(this);



        return rootview;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        stateprogressbar.setAllStatesCompleted(true);





    }








    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "well done", Toast.LENGTH_LONG).show();
    }


}
