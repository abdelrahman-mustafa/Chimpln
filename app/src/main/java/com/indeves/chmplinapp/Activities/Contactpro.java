package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class Contactpro extends StepProgressBar {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactpro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);

        toolbar.setTitle("Booked");
        button=(Button)findViewById(R.id.button4);
        button.setOnClickListener(this);
        stateprogressbar.checkStateCompleted(true);



    }
    @Override
    public void onClick(View v) {
        Toast.makeText(this, "well done", Toast.LENGTH_LONG).show();
    }

}
