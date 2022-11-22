package com.my.newvoicetyping;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MySharedPreferences {
    private static String IS_PICK_FROM_INTENT = "pref_is_pick_from_intent";
    private static String CURRENT_TIME = "pref_current_time";

    public static boolean getIsPickFromIntent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_PICK_FROM_INTENT, false);
    }

    public static void setIsFromPickIntent(Context context, boolean isEnabled) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(IS_PICK_FROM_INTENT, isEnabled);
        editor.apply();
        setCurrentTime(context);
    }

    public static void setCurrentTime(Context context) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(CURRENT_TIME, System.currentTimeMillis());
        editor.apply();
    }

    public static Long getTime(Context context) {
        Long defTime=System.currentTimeMillis()+System.currentTimeMillis()/2;
        return System.currentTimeMillis()-PreferenceManager.getDefaultSharedPreferences(context).getLong(CURRENT_TIME,defTime);
    }
}
