package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Button btnDTTheoNgay, btnDTTrongThang, btnDHTheoGio, btnDTTrongNam;
    Integer [] arr;
    Map<Integer,Button> hashMap = new HashMap<>();
    Toolbar toolBar;
    DrawerLayout drawerLayout;

    NavigationView navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongke_layout);
        setControl();
        setdrawer();
        setEvent();
    }

    private void setEvent() {
        // Random mảng từ 1 đến 4, vị trí của các phần tử là ngẫu nhiên
        arr = randomArr();
        // put button vào map
        hashMap.put(arr[0], btnDTTheoNgay);
        hashMap.put(arr[1], btnDTTrongThang);
        hashMap.put(arr[2], btnDHTheoGio);
        hashMap.put(arr[3], btnDTTrongNam);
        // Duyệt map, set lại background
        for (Map.Entry<Integer, Button> entry : hashMap.entrySet() ){
            if (entry.getKey() == 1){
                entry.getValue().setBackgroundResource(R.drawable.background_button_1);
            }
            else if (entry.getKey() == 2){
                entry.getValue().setBackgroundResource(R.drawable.background_button_2);
            }
            else if (entry.getKey() == 3){
                entry.getValue().setBackgroundResource(R.drawable.background_button_3);
            }
            else {
                entry.getValue().setBackgroundResource(R.drawable.background_button_4);
            }
        }
        btnDHTheoGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenManHinh(ThongKeActivity.this, ThongKeDonHangTheoGioActivity.class);
            }
        });
        btnDTTheoNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenManHinh(ThongKeActivity.this, BaoCaoDoanhThuTrongNgayActivity.class);
            }
        });

        btnDTTrongNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenManHinh(ThongKeActivity.this, BaoCaoDoanhThuTrongNamActivity.class);
            }
        });
        btnDTTrongThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenManHinh(ThongKeActivity.this, ThongKeDoanhThuTrongThangActivity.class);
            }
        });
    }
    private Integer [] randomArr(){
        Integer[] arr = {1, 2, 3, 4};
        List<Integer> list = Arrays.asList(arr);
        // Xáo trộn ngẫu nhiên vị trí của các phần tử trong mảng
        Collections.shuffle(list);
        // Chuyển danh sách đã xáo trộn về lại mảng
        list.toArray(arr);
        return arr;
    }

    private void setControl() {
        btnDTTheoNgay = findViewById(R.id.btnDTTrongNgay);
        btnDTTrongThang = findViewById(R.id.btnDTTrongThang);
        btnDHTheoGio = findViewById(R.id.btnDHTheoGio);
        btnDTTrongNam = findViewById(R.id.btnDTTrongNam);
        toolBar = findViewById(R.id.toolBar);
    }
    private void setdrawer(){
        toolBar.setTitle("Thống kê");
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_admin);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_thongke);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarAdmin menuSideBarAdmin = new MenuSideBarAdmin();
        menuSideBarAdmin.chonManHinh(item.getItemId(), ThongKeActivity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void chuyenManHinh(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}