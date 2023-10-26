package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.quanlyquancaphe.models.Khu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.BanAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Comparator;

public class QuanLyBanActivity extends AppCompatActivity{
    FirebaseDatabase database;
    DatabaseReference reference;
    SwipeableRecyclerView recyclerView;
    ValueEventListener valueEventListener;
ArrayList<Ban> data = new ArrayList<>();
ArrayList<Khu> dataKhu = new ArrayList<>();
ArrayList<Ban> filterData = new ArrayList<>();
BanAdapter adapter;
ImageButton btnSort,btnAdd;
EditText edtSearchBox;

//menu chuc nang
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Integer sortCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_quanlyban_layout);
        setControl();
        initDataKhu();
        initDataBan();
        setEvent();
    }

    private void setEvent() {

        adapter = new BanAdapter(data, this, dataKhu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        //Thêm data bàn
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenManHinh(QuanLyBanActivity.this, ThemBanActivity.class);
            }
        });
        //Bắt sự kiện kéo trái, kéo phải trên item
        recyclerView.setListener(new SwipeLeftRightCallback.Listener() {
            //Kéo sang trái
            @Override
            public void onSwipedLeft(int position) {
                initDataKhu();
                initDataBan();
                Ban ban = data.get(position);
                Intent intent = new Intent(QuanLyBanActivity.this, CapNhatBanActivity.class);
                intent.putExtra("id_Ban", ban.getId_Ban());
                intent.putExtra("tenBan", ban.getTenBan());
                intent.putExtra("soChoNgoi", ban.getSoChoNgoi());
                intent.putExtra("id_Khu", ban.getId_Khu());
                startActivity(intent);

            }
            //Kéo sang phải

            @Override
            public void onSwipedRight(int position) {
                DeleteItemDatabase(data.get(position).getId_Ban(), "Ban");
                initDataKhu();
                initDataBan();
            }
        });
        //Set sự kiện cho nút tìm kiếm
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edtSearchBox.getText().equals("")) {
                    Search(s);
                    adapter = new BanAdapter(filterData, QuanLyBanActivity.this, dataKhu);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //nút sắp xếp
        //sắp xếp theo thứ tự từ A-Z
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortCount == 1) {
                    ++sortCount;
                    data.sort(new Comparator<Ban>() {
                        @Override
                        public int compare(Ban ban1, Ban ban2) {
                            return ban1.getTenBan().substring(0,1).compareTo(ban2.getTenBan().substring(0,1)) > 1 ? 1 : -1;
                        }
                    });
                }
                else if (sortCount == 0){
                    ++sortCount;
                    data.sort(new Comparator<Ban>() {
                        @Override
                        public int compare(Ban ban1, Ban ban2) {
                            return ban1.getTenBan().substring(0,1).compareTo(ban2.getTenBan().substring(0,1)) < 1 ? 1:-1;
                        }
                    });
                }
                else {
                    loadData();
                    sortCount = 0;
                    }
                adapter.notifyDataSetChanged();
            }
        });
    }
    //Xóa item database
    private void DeleteItemDatabase(String key,String tenBang){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(tenBang);
        reference.child(key).removeValue();
    }
    //Hàm tìm kiếm
    private void Search(CharSequence text){
        filterData.clear();
        for (Ban ban : data){
            if (ban.getTenBan().toLowerCase().contains(text.toString().toLowerCase())){
                filterData.add(ban);
            }
        }
    }
    //Cập nhật data bàn

    private void initDataBan() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Ban");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    Ban ban = item.getValue(Ban.class);
                    data.add(ban);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Cập nhật data khu
    private void initDataKhu() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Khu");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKhu.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    Khu khu = item.getValue(Khu.class);
                    dataKhu.add(khu);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Hàm chuyển đổi màn hình
    private void chuyenManHinh(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Hàm load data
    private void loadData(){
        // Tạo thông báo đang load dữ liệu
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        // Tham chiếu tới bảng trong database
        reference = FirebaseDatabase.getInstance().getReference("Ban");
        // Bắt sự kiện khi dữ liệu trên database bị thay đổi
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Ban ban = dataSnapshot.getValue(Ban.class);
                    data.add(ban);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(QuanLyBanActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setControl() {
        recyclerView = findViewById(R.id.danhSachSwipeable);
        btnAdd = findViewById(R.id.btnAdd);
        btnSort = findViewById(R.id.btnSort);
        edtSearchBox = findViewById(R.id.edtSearchBox);

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}