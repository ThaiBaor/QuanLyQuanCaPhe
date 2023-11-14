package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.DanhSachOrderPhaCheAdapter;
import com.example.quanlyquancaphe.models.DanhSachOder;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.example.quanlyquancaphe.services.MenuSideBarPhaChe;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DanhSachOderPhaCheActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerView;
    DanhSachOrderPhaCheAdapter adapter;
    List<DanhSachOder> list_Order = new ArrayList<>();
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danhsachoder_phache_layout);
        setControl();
        toolBar.setTitle("Danh Sách Order");
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        setdrawer();
        getData();
    }

    private void getData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhSachOrderPhaCheAdapter(DanhSachOderPhaCheActivity.this, list_Order);
        recyclerView.setAdapter(adapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_Order.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("HT").getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                            String gioGoiMon = dataSnapshot2.child("gioGoiMon").getValue(String.class);
                            String id_Ban = dataSnapshot2.child("id_Ban").getValue(String.class);
                            String tenKH = dataSnapshot2.child("tenKH").getValue(String.class);
                            DanhSachOder danhSachOder = new DanhSachOder(id_Ban, tenKH, gioGoiMon);
                            list_Order.add(danhSachOder);
                            break;
                        }
                    }
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                list_Order.sort(new Comparator<DanhSachOder>() {
                    @Override
                    public int compare(DanhSachOder danhSachOder, DanhSachOder danhSachOder1) {
                        return LocalTime.parse(danhSachOder.getGioGoiMon(), formatter).compareTo(LocalTime.parse(danhSachOder1.getGioGoiMon(), formatter)) >= 0 ? 1 : -1;
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /* lấy node đầu tên của bảng*/
    private void setControl() {
        recyclerView = findViewById(R.id.recycleDanhSachOrderPhaChe);
        toolBar = findViewById(R.id.toolBar);
    }

    private void setdrawer(){
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_phache);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_danhsachorder);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarPhaChe menuSideBarPhaChe = new MenuSideBarPhaChe();
        menuSideBarPhaChe.chonManHinh(item.getItemId(), DanhSachOderPhaCheActivity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
