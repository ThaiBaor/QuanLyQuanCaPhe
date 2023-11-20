package com.example.quanlyquancaphe.ultilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDonMangVe;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    ArrayList<ChiTietMon> dataChiTietMonQK = new ArrayList<>();

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
                        Log.d("TAG", "Tongtien: " + tongTien);
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
                        Log.d("TAG", "Tongtien: " + tongTien);
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
    public void thanhToanTaiBan(String id_Ban, String id_HoaDon){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_Ban).child("HT");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        ChiTietMon chiTietMon = itemSnapshot.getValue(ChiTietMon.class);
                        dataChiTietMonQK.add(chiTietMon);
                    }
                }
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_Ban).child("QK");
                for (ChiTietMon item : dataChiTietMonQK){
                    ChiTietMon chiTietMon = new ChiTietMon();
                    chiTietMon.setId_Mon(item.getId_Mon());
                    chiTietMon.setSl(item.getSl());
                    chiTietMon.setId_Ban(item.getId_Ban());
                    chiTietMon.setGia(item.getGia());
                    chiTietMon.setTenMon(item.getTenMon());
                    chiTietMon.setTenKH(item.getTenKH());
                    chiTietMon.setGioGoiMon(item.getGioGoiMon());
                    chiTietMon.setHinh(item.getHinh());
                    chiTietMon.setNgayGoiMon(item.getNgayGoiMon());
                    chiTietMon.setId_TrangThai(item.getId_TrangThai());
                    chiTietMon.setGhiChu(item.getGhiChu());
                    String ke = chiTietMon.getGioGoiMon();
                    String dau = ":";
                    String thayDoi = "0";
                    String sauThayDoi = ke.replaceAll(String.valueOf(dau),String.valueOf(thayDoi));
                    databaseReference1.child(id_HoaDon).child(sauThayDoi).child(chiTietMon.getId_Mon()).setValue(chiTietMon).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void thanhToanMangVe(String tenKH, String id_HoaDon){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(tenKH).child("HT");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        ChiTietMon chiTietMon = itemSnapshot.getValue(ChiTietMon.class);
                        dataChiTietMonQK.add(chiTietMon);
                    }
                }
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(tenKH).child("QK");
                for (ChiTietMon item : dataChiTietMonQK){
                    ChiTietMon chiTietMon = new ChiTietMon();
                    chiTietMon.setId_Mon(item.getId_Mon());
                    chiTietMon.setSl(item.getSl());
                    chiTietMon.setId_Ban(item.getId_Ban());
                    chiTietMon.setGia(item.getGia());
                    chiTietMon.setTenMon(item.getTenMon());
                    chiTietMon.setTenKH(item.getTenKH());
                    chiTietMon.setGioGoiMon(item.getGioGoiMon());
                    chiTietMon.setHinh(item.getHinh());
                    chiTietMon.setNgayGoiMon(item.getNgayGoiMon());
                    chiTietMon.setId_TrangThai(item.getId_TrangThai());
                    chiTietMon.setGhiChu(item.getGhiChu());
                    String ke = chiTietMon.getGioGoiMon();
                    String dau = ":";
                    String thayDoi = "0";
                    String sauThayDoi = ke.replaceAll(String.valueOf(dau),String.valueOf(thayDoi));
                    databaseReference1.child(id_HoaDon).child(sauThayDoi).child(chiTietMon.getId_Mon()).setValue(chiTietMon).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
