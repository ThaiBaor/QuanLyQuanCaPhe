package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.models.LoaiMon;

import java.util.ArrayList;

public class ThongKeHoaDonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongkehoadon_layout);
        ArrayList data = new ArrayList();
        data.add(new Khu(2, "test"));
        data.add(new LoaiMon(1,"aklcbao"));
    }
}