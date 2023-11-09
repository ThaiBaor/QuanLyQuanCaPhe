package com.example.quanlyquancaphe.ultilities;

import com.example.quanlyquancaphe.models.MonTrongDS;

import java.util.ArrayList;

public class SearchUltility {
    private static SearchUltility instance;

    private SearchUltility() {
    }
    public static synchronized SearchUltility getInstance(){
        if (instance == null){
            instance = new SearchUltility();
        }
        return instance;
    }
}
