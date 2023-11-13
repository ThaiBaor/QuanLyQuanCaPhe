package com.example.quanlyquancaphe.interfaces;

import android.widget.EditText;

import com.example.quanlyquancaphe.models.MonTrongDS;

public interface DSMonPVInterface {
    void onAddButtonClick(Integer position, Integer sl);
    void onPlusButtonClick(Integer position, EditText edtSL);
    void onMinusButtonClick(Integer position, EditText edtSL);
}
