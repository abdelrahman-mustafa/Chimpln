package com.indeves.chmplinapp.PrefsManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.indeves.chmplinapp.Activities.LogIn;
import com.indeves.chmplinapp.Activities.ProLandingPage;
import com.indeves.chmplinapp.Activities.SignUpConfirmCode;
import com.indeves.chmplinapp.Activities.StuLandingPage;
import com.indeves.chmplinapp.Activities.UserProfileMain;

/**
 * Created by boda on 2/25/18.
 */

@SuppressLint("Registered")
public class PrefsManager extends Activity {
    Context context;
    PrefGet prefGet;

    public PrefsManager() {
    }

    public PrefsManager(Context context) {
        this.context = context;
        //put initialization in constructor so you can use passed context.
       this.prefGet = new PrefGet(context);
    }

    public Boolean checkLogIn() {
        if (prefGet.getLogInStatus()) {
            //K.A: what difference does this make rather than calling getLogInStatus() from its class directly?
            return true;
        } else {
            return false;
        }
    }

    public void goMainProfile(Context context) {
        Log.d("context",context.toString());
        Log.d("user",prefGet.getUserType());
        switch (prefGet.getUserType()) {
            case "stu":
                context.startActivity(new Intent(context, StuLandingPage.class));
                finish();
                break;
            case "user":
                context.startActivity(new Intent(context, UserProfileMain.class));
                finish();
                break;
            case "pro":
                context.startActivity(new Intent(context, ProLandingPage.class));
                finish();
                break;
        }
    }

}
