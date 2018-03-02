package com.indeves.chmplinapp.Utility;

import android.content.Context;
import android.widget.Toast;

import com.indeves.chmplinapp.R;

/**
 * Created by boda on 2/28/18.
 */

public class Toasts {
    Context context;

    public Toasts(Context context) {
        this.context = context;
    }

    public void completeData(){

        Toast.makeText(context,context.getResources().getText(R.string.complete_signup),Toast.LENGTH_SHORT).show();

    }
    public void weakPass(){

        Toast.makeText(context,context.getResources().getText(R.string.check_pass),Toast.LENGTH_SHORT).show();

    }
    public void passDismatch(){

        Toast.makeText(context,context.getResources().getText(R.string.check_pass_match),Toast.LENGTH_SHORT).show();

    }
    public void noType(){

        Toast.makeText(context,context.getResources().getText(R.string.check_type),Toast.LENGTH_SHORT).show();

    }
    public void invVerf(){

        Toast.makeText(context,context.getResources().getText(R.string.invalid_code),Toast.LENGTH_SHORT).show();

    }
    public void wrongMe(){

        Toast.makeText(context,context.getResources().getText(R.string.wrong_message),Toast.LENGTH_SHORT).show();

    }
}
