package com.example.quanlyquancaphe.interfaces;

import android.widget.EditText;
import android.widget.TextView;

public interface GioHangInterface {
    void onDeleteButtonClick(Integer position);
    void onPlusButtonClick(Integer position, EditText edtSL, TextView tvGia);
    void onMinusButtonClick(Integer position, EditText edtSL, TextView tvGia);
    void onNoteChange(Integer position, String note);
    void onQtyChange(Integer position, Integer qty);
}
