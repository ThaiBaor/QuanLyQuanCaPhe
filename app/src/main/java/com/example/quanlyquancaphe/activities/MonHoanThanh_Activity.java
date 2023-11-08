package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quanlyquancaphe.R;

public class MonHoanThanh_Activity extends AppCompatActivity {
RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_mon_hoan_thanh_layout);
        setControl();
        setEvent();

    }
    private void setEvent() {
    }
    private void setControl() {
        recyclerView = findViewById(R.id.recycleMonHoanThanh);
    }
}