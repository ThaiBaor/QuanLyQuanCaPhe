package com.example.quanlyquancaphe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.HoaDonMangVeAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.HoaDonMangVe;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.services.MenuSideBarThuNgan;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HoaDonMangVeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    HoaDonMangVeAdapter adapter;
    ArrayList<HoaDonMangVe> data = new ArrayList<>();

    DrawerLayout drawerLayout;

    Toolbar toolBar;

    NavigationView navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_hoadonmangve_layout);
        setControl();
        setdrawer();
        loadData();
        adapter = new HoaDonMangVeAdapter(HoaDonMangVeActivity.this, data, new HoaDonMangVeAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                HoaDonMangVe hoaDonMangVe = data.get(position);
                Intent intent = new Intent(HoaDonMangVeActivity.this, PhieuHoaDonMangVeActivity.class);
                intent.putExtra("id_HoaDon", hoaDonMangVe.getId_HoaDon());
                intent.putExtra("tenKH", hoaDonMangVe.getTenKH());
                intent.putExtra("thoiGian_ThanhToan", hoaDonMangVe.getThoiGian_ThanhToan());
                intent.putExtra("ngayThanhToan", hoaDonMangVe.getNgayThanhToan());
                intent.putExtra("tongTien", hoaDonMangVe.getTongTien());
                intent.putExtra("daThanhToan", hoaDonMangVe.getDaThanhToan());
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void loadData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("HoaDon");
        valueEventListener = databaseReference.child("MangVe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    HoaDonMangVe hoaDonMangVe = item.getValue(HoaDonMangVe.class);
                    if (!hoaDonMangVe.getDaThanhToan()) {
                        data.add(hoaDonMangVe);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerviewHDMV);
        toolBar = findViewById(R.id.toolBar);
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        toolBar.setTitle("Hóa đơn mang về");
    }

    private void setdrawer() {
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_thungan);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_nav, R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_thanhtoanmangdi);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarThuNgan menuSideBarThuNgan = new MenuSideBarThuNgan();
        menuSideBarThuNgan.chonManHinh(item.getItemId(), HoaDonMangVeActivity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
