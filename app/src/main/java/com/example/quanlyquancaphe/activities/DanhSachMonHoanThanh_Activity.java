package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.DanhSachMonHoanThanhAdapter;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.ultilities.NotificationUtility;
import com.example.quanlyquancaphe.services.MenuSideBarPhaChe;
import com.example.quanlyquancaphe.services.MenuSideBarPhucVu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhSachMonHoanThanh_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    ValueEventListener listener;
    List<String> key_node_CT = new ArrayList<>();
    DanhSachMonHoanThanhAdapter adapter;
    List<ChiTietMon> CT_TaiBan = new ArrayList<>();
    Toolbar toolBar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_monhoanthanh_layout);
        setControl();
        toolBar.setTitle("Danh Sách Món Hoàn Thành");
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        setdrawer();
        getKey();
    }
    private void getData(List<String> CT) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhSachMonHoanThanhAdapter(DanhSachMonHoanThanh_Activity.this, CT_TaiBan);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < CT.size(); i++) {
            String key_node = CT.get(i);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(key_node).child("HT");
            listener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                            Integer id_TrangThai = snapshot1.child("id_TrangThai").getValue(Integer.class);
                            if ( id_TrangThai==2) {
                               ChiTietMon CT_Mon = snapshot1.getValue(ChiTietMon.class);
                               CT_TaiBan.add(CT_Mon);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    // lấy key đầu tiên trong node
    private void getKey() {
        DatabaseReference reference_key = FirebaseDatabase.getInstance().getReference("ChiTietMon");
        listener = reference_key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                key_node_CT.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key_node_CT.add(dataSnapshot.getKey());
                }
                CT_TaiBan.clear();
                getData(key_node_CT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setControl() {
        toolBar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recycleMonHoanThanh);
    }

    private void setdrawer(){
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_phuc_vu);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dsmonhoanthanh);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarPhucVu menuSideBarPhucVu = new MenuSideBarPhucVu();
        menuSideBarPhucVu.chonManHinh(item.getItemId(), DanhSachMonHoanThanh_Activity.this);
        if (item.getItemId() == R.id.nav_mangve) {
            openCustomDialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnTiep = dialog.findViewById(R.id.btnTiep);
        EditText edtTenKH = dialog.findViewById(R.id.edtTenKH);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setCheckedItem(R.id.nav_danhsachban);
                dialog.dismiss();
            }
        });
        btnTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTenKH.getText().toString().equals("")) {
                    edtTenKH.requestFocus();
                    edtTenKH.setError("Phải nhập tên khách hàng");
                } else {
                    GioHangActivity.tenKH = edtTenKH.getText().toString();
                    Intent intent = new Intent(DanhSachMonHoanThanh_Activity.this, DanhSachMonPhucVuActivity.class);
                    dialog.dismiss();
                    startActivity(intent);
                }
            }
        });
        dialog.show();

    }
}