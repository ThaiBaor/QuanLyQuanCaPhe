package com.example.quanlyquancaphe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.HoaDonTaiBanAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDon;
import com.example.quanlyquancaphe.models.Khu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HoaDonTaiBanActivity extends AppCompatActivity  {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener ValueEventListener;
    HoaDonTaiBanAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<HoaDon> data = new ArrayList<>();
    ArrayList<Khu> dataKhu = new ArrayList<>();
    ArrayList<Ban> dataBan = new ArrayList<>();
    TextView tvMHD,tvGioHD,tvNgayHD,tvBanHD,tvGiaHD;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_hoadontaiban_layout);
        setControl();
        loadDataBan();
        loadDataKhu();
        loadDataHoaDonTaiBan();
        adapter = new HoaDonTaiBanAdapter(HoaDonTaiBanActivity.this, data, dataBan, dataKhu, new HoaDonTaiBanAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                HoaDon hoaDonTaiBan = data.get(position);
                Intent intent = new Intent(HoaDonTaiBanActivity.this, PhieuHoaDonActivity.class);
                intent.putExtra("id_HoaDon", hoaDonTaiBan.getId_HoaDon());
                intent.putExtra("id_Ban", hoaDonTaiBan.getId_Ban());
                intent.putExtra("thoiGian_ThanhToan", hoaDonTaiBan.getThoiGian_ThanhToan());
                intent.putExtra("ngayThanhToan", hoaDonTaiBan.getNgayThanhToan());
                intent.putExtra("tongTien", hoaDonTaiBan.getTongTien());
                intent.putExtra("daThanhToan", hoaDonTaiBan.getDaThanhToan());
                for (Ban item : dataBan){
                    if (hoaDonTaiBan.getId_Ban().equals(item.getId_Ban())){
                        intent.putExtra("tenBan",item.getTenBan());
                        for (Khu item2 : dataKhu){
                            if (item2.getId_Khu().equals(item.getId_Khu())){
                                intent.putExtra("tenKhu",item2.getTenKhu());
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
    }


    private void setControl() {
        recyclerView = findViewById(R.id.recyclerviewHDTB);
        tvMHD = findViewById(R.id.tvMaHD);
        tvGioHD = findViewById(R.id.tvGioHD);
        tvGiaHD = findViewById(R.id.tvGiaHD);
        tvBanHD = findViewById(R.id.tvBanHD);
        tvNgayHD = findViewById(R.id.tvNgayHD);
    }

    public void loadDataHoaDonTaiBan(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("HoaDon");
        ValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()){
                    HoaDon hoaDonTaiBan = item.getValue(HoaDon.class);
                    data.add(hoaDonTaiBan);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HoaDonTaiBanActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadDataBan(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Ban");
        ValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()){
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
    public void loadDataKhu(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Khu");
        ValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()){
                    Khu khu = item.getValue(Khu.class);
                    dataKhu.add(khu);
                    Log.e(khu.getId_Khu()+"","loi");
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HoaDonTaiBanActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
