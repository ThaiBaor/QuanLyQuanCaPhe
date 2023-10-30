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
import com.example.quanlyquancaphe.services.MenuSideBarPhucVu;
import com.google.android.material.navigation.NavigationView;

public class MenuChucNangQuanLyActicity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolBar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuchucnang_phucvu_layout);
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
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_phuc_vu);
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
        MenuSideBarPhucVu menuSideBarPhucVu = new MenuSideBarPhucVu();
        menuSideBarPhucVu.chonManHinh(item.getItemId(), MenuChucNangQuanLyActicity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}