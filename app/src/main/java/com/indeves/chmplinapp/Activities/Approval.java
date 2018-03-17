package com.indeves.chmplinapp.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.TextView;

import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class Approval extends StepProgressBar {
    TextView date,time,type,share,pro,note,location;
    String sdate,stime,stype,sshare,spro,snote,slocation;
    Button approvalbtn;
    EventModel model;
    public Approval() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Approval(EventModel model) {
        this.model = model;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.activity_approval, container, false);
        date=(TextView)rootview.findViewById(R.id.approval_date_textView);
        time=(TextView)rootview.findViewById(R.id.approval_time);
        type=(TextView)rootview.findViewById(R.id.approval_type);
        share=(TextView)rootview.findViewById(R.id.approval_sharing);
        pro=(TextView)rootview.findViewById(R.id.approval__pro);
        note=(TextView)rootview.findViewById(R.id.approval_notes);
        location=(TextView)rootview.findViewById(R.id.approval_location);
        date.setText(model.getEventDate());
        time.setText(model.getStartTime());
        type.setText(model.getTypeId());
        share.setText(model.getSharingOptionId());
        pro.setText(model.getPhotographerName());
        note.setText(model.getNoteToPro());
        location.setText(model.getEventCity());
      /*  Intent intent=getIntent();
        slocation=intent.getStringExtra("address");
        sdate=intent.getStringExtra("date");
        stime=intent.getStringExtra("time");
        snote=intent.getStringExtra("note");
        sshare=intent.getStringExtra("share");
        stype=intent.getStringExtra("type");
        date.setText(sdate);
        time.setText(stime);
        type.setText(stype);
        share.setText(sshare);
        pro.setText("ahmed"+"\"n"+"cairo-male");
        note.setText(snote);
        location.setText(slocation);*/
        approvalbtn=(Button)rootview.findViewById(R.id.approvalbtnn);
        approvalbtn.setOnClickListener(this);


        return rootview;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        stateprogressbar.setAllStatesCompleted(false);





    }



   @Override
    public void onClick(View v) {
        stateprogressbar.checkStateCompleted(true);
       Contactpro output = new Contactpro();
       android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

       transaction.replace(R.id.container_o, output).commit();

   }


}
