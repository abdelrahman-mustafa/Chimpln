package com.indeves.chmplinapp.PrefsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by boda on 2/25/18.
 */

public class PrefGet {
    private SharedPreferences mypreference;

    public PrefGet(Context context) {
        this.mypreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getId() {
        return mypreference.getString("user_id", null);
    }

    public Boolean getLogInStatus() {
        return mypreference.getBoolean("LoggedIn", false);
    }

   public    String getUserType() {
        Log.d("hhh",mypreference.getString("user_type", null));
        return mypreference.getString("user_type", null);
    }
}
