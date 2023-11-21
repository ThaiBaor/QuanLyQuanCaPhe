package com.example.quanlyquancaphe.ultilities;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDonMangVe;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HoaDonUltility {
    private double tongTien;
    public static HoaDonUltility hdInstance;

    private HoaDonUltility() {
    }

    public static HoaDonUltility getHdInstance() {
        if (hdInstance == null) {
            hdInstance = new HoaDonUltility();
        }
        return hdInstance;
    }

    public void taoHoaDonMangVe(Context context, String tenKH) {
        tongTien = 0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(tenKH).child("HT");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        ChiTietMon chiTietMon = itemSnapshot.getValue(ChiTietMon.class);
                        tongTien += chiTietMon.tinhTongTien();
                    }
                }
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("HoaDon").child("MangVe");
                String id_HoaDon = databaseReference1.push().getKey();
                String ngayThanhToan = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String thoiGian_ThanhToan = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                HoaDonMangVe hoaDonMangVe = new HoaDonMangVe();
                hoaDonMangVe.setNgayThanhToan(ngayThanhToan);
                hoaDonMangVe.setThoiGian_ThanhToan(thoiGian_ThanhToan);
                hoaDonMangVe.setTenKH(tenKH);
                hoaDonMangVe.setTongTien(tongTien);
                hoaDonMangVe.setId_HoaDon(id_HoaDon);
                databaseReference1.child(id_HoaDon).setValue(hoaDonMangVe).addOnSuccessListener(unused -> Toast.makeText(context, "Đã tạo hóa đơn", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void tangSoLuongDaBan(ArrayList<ChiTietMon> data) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        for (ChiTietMon chiTietMon : data) {
            Task<DataSnapshot> task = databaseReference.child(chiTietMon.getId_Mon()).get();
            while (!task.isSuccessful()) ;
            int slCu = task.getResult().child("slDaBan").getValue(Integer.class);
            databaseReference.child(chiTietMon.getId_Mon()).child("slDaBan").setValue(slCu + chiTietMon.getSl());
        }
    }

    public void taoHoaDonTaiBan(Context context, String id_Ban) {
        tongTien = 0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_Ban).child("HT");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        ChiTietMon chiTietMon = itemSnapshot.getValue(ChiTietMon.class);
                        tongTien += chiTietMon.tinhTongTien();
                    }
                }
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("HoaDon").child("TaiBan");
                String id_HoaDon = databaseReference1.push().getKey();
                String ngayThanhToan = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String thoiGian_ThanhToan = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                HoaDonTaiBan hoaDonTaiBan = new HoaDonTaiBan();
                hoaDonTaiBan.setNgayThanhToan(ngayThanhToan);
                hoaDonTaiBan.setThoiGian_ThanhToan(thoiGian_ThanhToan);
                hoaDonTaiBan.setId_Ban(id_Ban);
                hoaDonTaiBan.setTongTien(tongTien);
                hoaDonTaiBan.setId_HoaDon(id_HoaDon);
                databaseReference1.child(id_HoaDon).setValue(hoaDonTaiBan).addOnSuccessListener(unused -> Toast.makeText(context, "Đã tạo hóa đơn", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
