package com.example.quanlyquancaphe.services;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
    public static final String SHARED_USERNAME = "SHARED_USERNAME";
    public static final String SHARED_PASSWORD = "SHARED_PASSWORD";
    private Context context;

    public MySharedPreference(Context context) {
        this.context = context;
    }

    public void putValueUsername(String key_username, String value_username) {
        SharedPreferences sharedPreferences_USERNAME = context.getSharedPreferences(SHARED_USERNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_USERNAME = sharedPreferences_USERNAME.edit();
        editor_USERNAME.putString(key_username, value_username);
        editor_USERNAME.apply();
    }

    public void putValuePassword(String key_password, String value_password) {
        SharedPreferences sharedPreferences_PASSWORD = context.getSharedPreferences(SHARED_PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_PASSWORD = sharedPreferences_PASSWORD.edit();
        editor_PASSWORD.putString(key_password, value_password);
        editor_PASSWORD.apply();
    }

    public String getValueUsername(String key_username) {
        SharedPreferences sharedPreferences_USERNAME = context.getSharedPreferences(SHARED_USERNAME, Context.MODE_PRIVATE);
        return sharedPreferences_USERNAME.getString(key_username, null);
    }

    public String getValuePassword(String key_password) {
        SharedPreferences sharedPreferences_PASSWORD = context.getSharedPreferences(SHARED_PASSWORD, Context.MODE_PRIVATE);
        return sharedPreferences_PASSWORD.getString(key_password, null);
    }
}
