package com.indeves.chmplinapp.Utility;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.indeves.chmplinapp.R;
import com.kofigyan.stateprogressbar.StateProgressBar;

/**
 * Created by HP on 13/2/2018.
 */

public abstract class StepProgressBar extends FragmentActivity implements View.OnClickListener {  protected String[] descriptionData = {"Info", "Approval", "Booked"};

     protected StateProgressBar stateprogressbar;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectCommonViews();
    }



     protected void injectCommonViews() {


        stateprogressbar = (StateProgressBar) findViewById(R.id.usage_stateprogressbar);
        stateprogressbar.setStateDescriptionData(descriptionData);
    }

   protected void injectBackView() {

    }


    @Override
    public void onClick(View view) {

    }
}


