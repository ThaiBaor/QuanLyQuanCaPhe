package com.example.quanlyquancaphe.services;

import android.content.Context;

public class LocalDataManager {
    private static final String LAST_USERNAME = "LAST_USERNAME";
    private static final String LAST_PASSWORD = "LAST_PASSWORD";
    private static LocalDataManager instance;
    private MySharedPreference mySharedPreference;

    public static void init(Context context){
        instance = new LocalDataManager();
        instance.mySharedPreference = new MySharedPreference(context);
    }

    public static LocalDataManager getInstance(){
        if (instance == null){
            instance = new LocalDataManager();
        }
        return instance;
    }
    public static void setLastLogin(String username, String password){
        getInstance().mySharedPreference.putValueUsername(LAST_USERNAME,username);
        getInstance().mySharedPreference.putValuePassword(LAST_PASSWORD,password);
    }
    public static String getLastUsername (){
        return getInstance().mySharedPreference.getValueUsername(LAST_USERNAME);
    }
    public static String getLastPassword (){
        return getInstance().mySharedPreference.getValuePassword(LAST_PASSWORD);
    }
}
