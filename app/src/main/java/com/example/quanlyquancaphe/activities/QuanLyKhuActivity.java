package com.example.quanlyquancaphe.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.QuanLyKhuAdapter;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

public class QuanLyKhuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolBar;
    EditText edtSearchBox, edtMa, edtKhu;
    ImageButton btnadd, btnSort;
    Button btnUpdate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SwipeableRecyclerView recyclerView;
    QuanLyKhuAdapter quanLyKhuAdapter;
    ArrayList<Khu> data= new ArrayList<>();
    ArrayList<Khu> filterdata= new ArrayList<>();
    ValueEventListener valueEventListener;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Integer sortc = 0;
    Khu khuAdd = new Khu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_quanlykhu_layout);
        setdrawer();
        setControl();
        setEvent();
    }

    private void setControl() {
        recyclerView =findViewById(R.id.recycle);
        edtSearchBox = findViewById(R.id.edtSearchBox);
        btnadd = findViewById(R.id.btnAdd);
        edtMa = findViewById(R.id.edtMa);
        edtKhu = findViewById(R.id.edtTenKhu);
        btnSort = findViewById(R.id.btnSort);
        btnUpdate = findViewById(R.id.btnCapNhat);
        toolBar = findViewById(R.id.toolBar);
    }

    private void setEvent() {
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        toolBar.setTitle("Quản lý khu");
        khoiTao();
        quanLyKhuAdapter = new QuanLyKhuAdapter(QuanLyKhuActivity.this,data);
        SwipeableRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(quanLyKhuAdapter);
        recyclerView.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                Integer id = Integer.parseInt(data.get(position).getId_Khu().toString());
                String tenKhu = data.get(position).getTenKhu();
                edtMa.setText(id + "");
                edtKhu.setText(tenKhu);
                quanLyKhuAdapter.notifyDataSetChanged();
            }
            @Override
            public void onSwipedRight(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyKhuActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn xóa " + data.get(position).getId_Khu() + " không ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("Khu");
                        databaseReference.child(String.valueOf(data.get(position).getId_Khu())).removeValue();
                        data.remove(position);
                        data.clear();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        quanLyKhuAdapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        /*
         * Additional attributes:
         * */
        recyclerView.setRightBg(com.tsuryo.swipeablerv.R.color.blue);
        recyclerView.setRightImage(R.drawable.baseline_edit_24);

        recyclerView.setLeftBg(com.tsuryo.swipeablerv.R.color.red);
        recyclerView.setLeftImage(R.drawable.baseline_delete_24);

        recyclerView.setTextSize(62);
        recyclerView.setTextColor(R.color.white);

        // tim kiem
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtSearchBox.getText().equals("")){
                    search(charSequence);
                    quanLyKhuAdapter = new QuanLyKhuAdapter(QuanLyKhuActivity.this,filterdata);
                    recyclerView.setAdapter(quanLyKhuAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // them
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraThem() == true){
                    data.clear();
                    kiemTraThem();
                    String khu = edtKhu.getText().toString();
                    Integer id = Integer.parseInt(edtMa.getText().toString());
                    khuAdd = new Khu(id,khu);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("Khu");
                    databaseReference.child(String.valueOf(id)).setValue(khuAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(QuanLyKhuActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QuanLyKhuActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(QuanLyKhuActivity.this, "Nhập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // sap xep
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortc == 1){
                    ++sortc;
                    data.sort(new Comparator<Khu>() {
                        @Override
                        public int compare(Khu khu1, Khu khu2) {
                            return khu1.getTenKhu().substring(0,khu1.getTenKhu().length()).compareTo(khu2.getTenKhu().substring(0,khu2.getTenKhu().length())) > 1 ? 1 : -1;
                        }
                    });
                }
                else if(sortc == 0){
                    ++sortc;
                    data.sort(new Comparator<Khu>() {
                        @Override
                        public int compare(Khu khu1, Khu khu2) {
                            return khu1.getTenKhu().substring(0,khu1.getTenKhu().length()).compareTo(khu2.getTenKhu().substring(0,khu2.getTenKhu().length())) > 1 ? 1 : -1;
                        }
                    });
                }
                else {
                    data.clear();
                    khoiTao();
                    sortc = 0;
                }
                quanLyKhuAdapter.notifyDataSetChanged();
            }
        });
        // Sua
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraUpdate() == true){
                    update();
                    data.clear();
                    quanLyKhuAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(QuanLyKhuActivity.this, "Nhập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void search(CharSequence charSequence){
        filterdata.clear();
        for (Khu khu : data){
            if(khu.getTenKhu().toLowerCase().contains(charSequence.toString().toLowerCase())){
                filterdata.add(khu);
            }
        }
    }

    private void setdrawer(){
        toolBar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_admin);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_qlkhu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void update() {
        kiemTraUpdate();
        Khu khu = new Khu();
        khu.setId_Khu(Integer.parseInt(edtMa.getText().toString()));
        khu.setTenKhu(edtKhu.getText().toString());
        databaseReference = FirebaseDatabase.getInstance().getReference("Khu");
        databaseReference.child(khu.getId_Khu() + "").setValue(khu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(QuanLyKhuActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuanLyKhuActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Boolean kiemTraThem(){
        if (edtMa.getText().toString().isEmpty()){
            edtMa.setError("Empty");
            return false;
        }
        khuAdd.setId_Khu(Integer.parseInt(edtMa.getText().toString()));
        if (edtKhu.getText().toString().isEmpty()){
            edtKhu.setError("Empty");
            return false;
        }
        khuAdd.setTenKhu(edtKhu.getText().toString());
        return true;
    }

    private boolean kiemTraUpdate(){
        if (edtMa.getText().toString().isEmpty()){
            edtMa.setError("Empty");
            return false;
        }
        if (edtKhu.getText().toString().isEmpty()){
            edtKhu.setError("Empty");
            return false;
        }
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        quanLyKhuAdapter.notifyDataSetChanged();
    }

    private void khoiTao(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Khu");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()){
                    Khu khu = item.getValue(Khu.class);
                    data.add(khu);
                }
                quanLyKhuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarAdmin menuSideBarAdmin = new MenuSideBarAdmin();
        menuSideBarAdmin.chonManHinh(item.getItemId(), QuanLyKhuActivity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
