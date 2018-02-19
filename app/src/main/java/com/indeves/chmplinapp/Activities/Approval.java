package com.indeves.chmplinapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class Approval extends StepProgressBar {
    TextView date,time,type,share,pro,note,location;
    String sdate,stime,stype,sshare,spro,snote,slocation;
    Button approvalbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Approval");
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

        date=(TextView)findViewById(R.id.approval_date_textView);
        time=(TextView)findViewById(R.id.approval_time);
        type=(TextView)findViewById(R.id.approval_type);
        share=(TextView)findViewById(R.id.approval_sharing);
        pro=(TextView)findViewById(R.id.approval__pro);
        note=(TextView)findViewById(R.id.approval_notes);
        location=(TextView)findViewById(R.id.approval_location);
        Intent intent=getIntent();
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
        location.setText(slocation);
        approvalbtn=(Button)findViewById(R.id.approvalbtnn);
        approvalbtn.setOnClickListener(this);






    }
    @Override
    public void onClick(View v) {
        stateprogressbar.checkStateCompleted(true);
        Intent intent = new Intent(getApplicationContext(), Contactpro.class);
        startActivity(intent);
    }


}
