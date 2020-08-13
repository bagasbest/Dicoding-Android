package com.bagasbest.mypreloaddata.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    private static final String PREFS_NAMAE = "MahasiswaPref";
    private static final String APP_FIRST_RU = "app_first_run";
    private SharedPreferences prefs;

    public AppPreference(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAMAE, Context.MODE_PRIVATE);
    }

    public void setFirstRun (Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_FIRST_RU, input);
        editor.apply();
    }

    public Boolean getFirstRun () {
        return prefs.getBoolean(APP_FIRST_RU, true);
    }

}
