/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.mridx.freegaana.config.AppConfig;

public class FirstRunChecker {

    public FirstRunChecker() {
    }

    public boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
        String PREFERENCE_FIRST_RUN = "IS_FIRST";
        boolean isFirstRun = sharedPreferences.getBoolean(PREFERENCE_FIRST_RUN, true);
        if (!isFirstRun)
            sharedPreferences.edit().putBoolean(PREFERENCE_FIRST_RUN, false).apply();
        return isFirstRun;
    }
}
