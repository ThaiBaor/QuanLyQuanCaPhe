package com.example.quanlyquancaphe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.HoaDonTaiBanAdapter;
import com.example.quanlyquancaphe.models.Ban;
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

public class HoaDonTaiBanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText edtSearchBox;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener ValueEventListener;
    HoaDonTaiBanAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<HoaDonTaiBan> data = new ArrayList<>();
    ArrayList<Khu> dataKhu = new ArrayList<>();
    ArrayList<Ban> dataBan = new ArrayList<>();
    ArrayList<HoaDonTaiBan> dataFillter = new ArrayList<>();
    DrawerLayout drawerLayout;
    Toolbar toolBar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_hoadontaiban_layout);
        setControl();
        setdrawer();
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        toolBar.setTitle("Hóa đơn tại bàn");
        loadDataBan();
        loadDataKhu();
        loadDataHoaDonTaiBan();
        //tvMHD.setText(cutTextView(tvMHD.toString()));
        adapter = new HoaDonTaiBanAdapter(HoaDonTaiBanActivity.this, data, dataBan, dataKhu, new HoaDonTaiBanAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                HoaDonTaiBan hoaDonTaiBan = data.get(position);
                Intent intent = new Intent(HoaDonTaiBanActivity.this, PhieuHoaDonTaiBanActivity.class);
                intent.putExtra("id_HoaDon", hoaDonTaiBan.getId_HoaDon());
                intent.putExtra("id_Ban", hoaDonTaiBan.getId_Ban());
                intent.putExtra("thoiGian_ThanhToan", hoaDonTaiBan.getThoiGian_ThanhToan());
                intent.putExtra("ngayThanhToan", hoaDonTaiBan.getNgayThanhToan());
                intent.putExtra("tongTien", hoaDonTaiBan.getTongTien());
                intent.putExtra("daThanhToan", hoaDonTaiBan.getDaThanhToan());
                for (Ban item : dataBan) {
                    if (hoaDonTaiBan.getId_Ban().equals(item.getId_Ban())) {
                        intent.putExtra("tenBan", item.getTenBan());
                        for (Khu item2 : dataKhu) {
                            if (item2.getId_Khu().equals(item.getId_Khu())) {
                                intent.putExtra("tenKhu", item2.getTenKhu());
                            }
                        }
                    }
                }
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtSearchBox.getText().equals("")){
                    search(charSequence);
                    adapter = new HoaDonTaiBanAdapter(HoaDonTaiBanActivity.this, dataFillter, dataBan, dataKhu, new HoaDonTaiBanAdapter.ItemClickListener() {
                        @Override
                        public void OnItemClick(int position) {
                            HoaDonTaiBan hoaDonTaiBan = dataFillter.get(position);
                            Intent intent = new Intent(HoaDonTaiBanActivity.this, PhieuHoaDonTaiBanActivity.class);
                            intent.putExtra("id_HoaDon", hoaDonTaiBan.getId_HoaDon());
                            intent.putExtra("id_Ban", hoaDonTaiBan.getId_Ban());
                            intent.putExtra("thoiGian_ThanhToan", hoaDonTaiBan.getThoiGian_ThanhToan());
                            intent.putExtra("ngayThanhToan", hoaDonTaiBan.getNgayThanhToan());
                            intent.putExtra("tongTien", hoaDonTaiBan.getTongTien());
                            intent.putExtra("daThanhToan", hoaDonTaiBan.getDaThanhToan());
                            for (Ban item : dataBan) {
                                if (hoaDonTaiBan.getId_Ban().equals(item.getId_Ban())) {
                                    intent.putExtra("tenBan", item.getTenBan());
                                    for (Khu item2 : dataKhu) {
                                        if (item2.getId_Khu().equals(item.getId_Khu())) {
                                            intent.putExtra("tenKhu", item2.getTenKhu());
                                        }
                                    }
                                }
                            }
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void setControl() {
        recyclerView = findViewById(R.id.recyclerviewHDTB);
        edtSearchBox = findViewById(R.id.edtSearchBox);
    }

    public void loadDataHoaDonTaiBan() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("HoaDon");
        ValueEventListener = databaseReference.child("TaiBan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    HoaDonTaiBan hoaDonTaiBan = item.getValue(HoaDonTaiBan.class);
                    if (!hoaDonTaiBan.getDaThanhToan()) {
                        data.add(hoaDonTaiBan);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HoaDonTaiBanActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    public void loadDataBan() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Ban");
        ValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBan.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Ban ban = item.getValue(Ban.class);
                    dataBan.add(ban);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HoaDonTaiBanActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadDataKhu() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Khu");
        ValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKhu.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Khu khu = item.getValue(Khu.class);
                    dataKhu.add(khu);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HoaDonTaiBanActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setdrawer() {
        toolBar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_thungan);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_nav, R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_thanhtoantaiban);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarThuNgan menuSideBarThuNgan = new MenuSideBarThuNgan();
        menuSideBarThuNgan.chonManHinh(item.getItemId(), HoaDonTaiBanActivity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void search(CharSequence charSequence) {
        dataFillter.clear();
        for (HoaDonTaiBan item: data){
            for (Ban itemBan: dataBan){
                if (item.getId_Ban().equals(itemBan.getId_Ban())){
                    if (itemBan.getTenBan().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        dataFillter.add(item);
                    }
                }
            }
        }
    }
}
