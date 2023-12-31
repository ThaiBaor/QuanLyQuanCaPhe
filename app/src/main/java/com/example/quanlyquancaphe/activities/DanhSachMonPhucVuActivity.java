package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.ViewPaperAdapter;
import com.google.android.material.tabs.TabLayout;

public class DanhSachMonPhucVuActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Toolbar toolBar;
    ViewPager viewPager;
    ViewPaperAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danhsachmonphucvu_layout);
        setControl();
        toolBar.setNavigationOnClickListener(view -> {
            GioHangActivity.currentData.clear();
            finish();
        });
        viewPagerAdapter = new ViewPaperAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.cupofcoffee_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.cake_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.cupoftea_icon);



    }

    private void setControl() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        toolBar = findViewById(R.id.toolBar);

    }
}