package com.example.quanlyquancaphe.ultilities;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.quanlyquancaphe.models.ChiTietMon;
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

public class ChiTietMonQKUtility {
    public static ChiTietMonQKUtility hdqkInstance;
    ArrayList<ChiTietMon> dataChiTietMonQK = new ArrayList<>();

    public ChiTietMonQKUtility() {
    }
    public static ChiTietMonQKUtility getHdqkInstance(){
        if (hdqkInstance == null){
            hdqkInstance = new ChiTietMonQKUtility();
        }
        return hdqkInstance;
    }
    public void taoChiTietMonQKTaiBan(Context context,String id_Ban){
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
                        databaseReference1.child(sauThayDoi).setValue(chiTietMon).addOnCompleteListener(new OnCompleteListener<Void>() {
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
