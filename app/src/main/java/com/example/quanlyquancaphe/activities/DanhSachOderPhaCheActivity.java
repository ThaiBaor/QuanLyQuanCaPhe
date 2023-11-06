package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

import com.example.quanlyquancaphe.R;

public class DanhSachOderPhaCheActivity extends AppCompatActivity {
    CardView carview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danh_sach_oder_pha_che_layout);
        setControl();
    }

    private void setControl() {
carview = findViewById(R.id.cardViewItems);
    }
}