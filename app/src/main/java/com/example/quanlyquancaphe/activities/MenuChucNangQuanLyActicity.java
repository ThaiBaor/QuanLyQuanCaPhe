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
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.google.android.material.navigation.NavigationView;

public class MenuChucNangQuanLyActicity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolBar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuchucnang_quanly_layout);
        setControl();
        setEvent();
        setdrawer();
    }

    private void setEvent() {

    }

    private void setControl() {

    }

    private void setdrawer(){
        toolBar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_admin);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void chuyenManHinh(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_thongke:
                //hàm recreate dùng để load lại màn hình khi nhấn chuyển màn hình về màn hình hiện tại
                //recreate();
                Toast.makeText(this, "Thống kê", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_qlnhanvien:
                //hàm chuyển màn hình dùng để chuyển màn hình
                // tham số thứ nhất là tên màn hình hiện tại .this
                // VD: manhinhthunhat.this
                // tham số thứ 2 là tên màn hình cần chuyển .class
                // VD: manhinhthuhai.class
                //chuyenManHinh(MenuChucNangAdminActicity.this, chuyenmanhinh.class);
                Toast.makeText(this, "Quản lý nhân viên", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_qlban:
                Toast.makeText(this, "Quản lý bàn", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_qlkhu:
                Toast.makeText(this, "Quản lý khu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_qlmon:
                Toast.makeText(this, "Quản lý món", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_qlkho:
                Toast.makeText(this, "Quản lý kho", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_dangxuat:
                Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}