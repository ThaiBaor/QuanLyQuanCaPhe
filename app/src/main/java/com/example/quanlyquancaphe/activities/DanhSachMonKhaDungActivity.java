package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.DanhSachMonKhaDungAdapter;
import com.example.quanlyquancaphe.adapters.NguyenLieuAdapter;
import com.example.quanlyquancaphe.models.Mon;
import com.example.quanlyquancaphe.models.NguyenLieu;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.example.quanlyquancaphe.services.MenuSideBarPhaChe;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DanhSachMonKhaDungActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolBar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<Mon> data = new ArrayList<>();
    ArrayList<Mon> fillterdata = new ArrayList<>();
    DanhSachMonKhaDungAdapter danhSachMonKhaDungAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danhsachmonkhadung_layout);
        getData();
        setControl();
        setEvent();
        setdrawer();
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        toolBar.setTitle("Danh sách món khả dụng");
        danhSachMonKhaDungAdapter = new DanhSachMonKhaDungAdapter(this, data);
        recyclerView.setAdapter(danhSachMonKhaDungAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setEvent() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtSearch.getText().equals("")){
                    search(charSequence);
                    danhSachMonKhaDungAdapter = new DanhSachMonKhaDungAdapter(DanhSachMonKhaDungActivity.this, fillterdata );
                    recyclerView.setAdapter(danhSachMonKhaDungAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachMonKhaDungActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Mon");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item: snapshot.getChildren()){

                    Mon mon = item.getValue(Mon.class);
                    data.add(mon);
                }
                danhSachMonKhaDungAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(DanhSachMonKhaDungActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setdrawer(){
        toolBar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_phache);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_danhsachmonkhadung);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void search(CharSequence hint){
        fillterdata.clear();
        for (Mon mon : data){
            if (mon.getTenMon().toLowerCase().contains(hint.toString().toLowerCase())){
                fillterdata.add(mon);
            }
        }
    }

    private void setControl() {
        edtSearch = findViewById(R.id.edtSearchBox);
        recyclerView = findViewById(R.id.recyclerviewdanhsachmonkhadung);
        toolBar = findViewById(R.id.toolBar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarPhaChe menuSideBarPhaChe = new MenuSideBarPhaChe();
        menuSideBarPhaChe.chonManHinh(item.getItemId(), DanhSachMonKhaDungActivity.this);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}