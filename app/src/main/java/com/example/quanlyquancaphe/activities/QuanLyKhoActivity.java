package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.NguyenLieuAdapter;
import com.example.quanlyquancaphe.models.Mon;
import com.example.quanlyquancaphe.models.NguyenLieu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class QuanLyKhoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    EditText edtSearch;
    ImageButton btnSort, btnAdd;
    SwipeableRecyclerView swipeableRecyclerView;
    ArrayList<NguyenLieu> data = new ArrayList<>();
    ArrayList<NguyenLieu> filterData = new ArrayList<>();
    NguyenLieuAdapter nguyenLieuAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ValueEventListener valueEventListener;

    Integer sortCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_kho);
        setdrawer();
        setControl();
        setEvent();
    }

    private void setEvent() {

        getData();
        nguyenLieuAdapter = new NguyenLieuAdapter(this, data);
        swipeableRecyclerView.setAdapter(nguyenLieuAdapter);
        swipeableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeableRecyclerView.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                NguyenLieu nguyenLieu = data.get(position);
                Intent intent = new Intent(QuanLyKhoActivity.this, CapNhatNguyenLieu.class);
                intent.putExtra("maNguyenLieu", nguyenLieu.getMaNguyenLieu());
                intent.putExtra("tenNguyenLieu", nguyenLieu.getTenNguyenLieu());
                intent.putExtra("ngayNhap", nguyenLieu.getNgayNhap());
                intent.putExtra("tonKho", nguyenLieu.getTonKho());
                intent.putExtra("donVi", nguyenLieu.getDonVi());
                intent.putExtra("soLuongNhap", nguyenLieu.getSoLuongNhap());
                startActivity(intent);
            }

            @Override
            public void onSwipedRight(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyKhoActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn xóa " + data.get(position).getTenNguyenLieu() + " không ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("NguyenLieu");
                        databaseReference.child(String.valueOf(data.get(position).getMaNguyenLieu())).removeValue();
                        data.remove(position);
                        nguyenLieuAdapter.notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nguyenLieuAdapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtSearch.getText().equals("")){
                    search(charSequence);
                    nguyenLieuAdapter = new NguyenLieuAdapter(QuanLyKhoActivity.this, filterData );
                    swipeableRecyclerView.setAdapter(nguyenLieuAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sortCount == 0){
                    sortCount = 1;
                    data.sort(new Comparator<NguyenLieu>() {
                        @Override
                        public int compare(NguyenLieu nguyenLieu, NguyenLieu nguyenLieu2) {
                            if(nguyenLieu.getNgayNhap().compareTo(nguyenLieu2.getNgayNhap()) == 1){
                                return 1;
                            }
                            else {
                                return -1;
                            }
                        }
                    });
                }
                else if(sortCount == 1){
                    sortCount = 2;
                    data.sort(new Comparator<NguyenLieu>() {
                        @Override
                        public int compare(NguyenLieu nguyenLieu, NguyenLieu nguyenLieu2) {
                            if(nguyenLieu2.getNgayNhap().compareTo(nguyenLieu.getNgayNhap()) == 1){
                                return 1;
                            }
                            else {
                                return -1;
                            }
                        }
                    });
                }
                else {
                    getData();
                    sortCount = 0;
                }
                nguyenLieuAdapter.notifyDataSetChanged();

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuanLyKhoActivity.this, ThemNguyenLieuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                QuanLyKhoActivity.this.startActivity(intent);
            }
        });
    }

    private void getData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyKhoActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("NguyenLieu");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item: snapshot.getChildren()){

                    String maNguyenLieu = item.child("maNguyenLieu").getValue().toString();
                    String ngayNhap = item.child("ngayNhap").getValue().toString();
                    Double soLuongNhap = Double.parseDouble(item.child("soLuongNhap").getValue().toString());
                    Double tonKho = Double.parseDouble(item.child("tonKho").getValue().toString());
                    String tenNguyenLieu = item.child("tenNguyenLieu").getValue().toString();
                    String donVi = item.child("donVi").getValue().toString();
                    NguyenLieu nl = new NguyenLieu(maNguyenLieu,tenNguyenLieu, donVi, ngayNhap, soLuongNhap, tonKho);
                    data.add(nl);
                }
                nguyenLieuAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(QuanLyKhoActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void search(CharSequence hint){
        filterData.clear();
        for (NguyenLieu nguyenLieu : data){
            if (nguyenLieu.getTenNguyenLieu().toLowerCase().contains(hint.toString().toLowerCase())){
                filterData.add(nguyenLieu);
            }
        }
    }



    private void setControl() {
        swipeableRecyclerView = findViewById(R.id.recyclerview);
        edtSearch = findViewById(R.id.edtSearchBox);
        btnSort = findViewById(R.id.btnSort);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void setdrawer(){
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_admin);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
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