package com.example.quanlyquancaphe.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.PhieuHoaDonAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;
import com.example.quanlyquancaphe.models.Khu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhieuHoaDonTaiBanActivity extends AppCompatActivity {
    TextView tvMHD, tvGioHD, tvNgayHD, tvBanHD, tvGiaHD, tvKhu, tvNV, tvTongTien;
    Button btnQuayLai, btnThanhToan;
    ImageView ivHinh;
    RecyclerView recyclerView;
    Bundle bundle;
    String id_Ban = "";
    ArrayList<ChiTietMon> dataChiTietMon = new ArrayList<>();
    ArrayList<ChiTietMon> dataGop = new ArrayList<>();
    HoaDonTaiBan hoaDonTaiBan = new HoaDonTaiBan();
    Ban ban = new Ban();
    Khu khu = new Khu();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PhieuHoaDonAdapter adapter;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_phieuhoadontaiban_layout);
        bundle = getIntent().getExtras();
        setControl();
        loadDataThongTin();
        datachitietmon();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        adapter = new PhieuHoaDonAdapter(PhieuHoaDonTaiBanActivity.this, id_Ban, dataChiTietMon);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle != null) {
                    hoaDonTaiBan.setId_HoaDon(bundle.getString("id_HoaDon"));
                    hoaDonTaiBan.setDaThanhToan(bundle.getBoolean("daThanhToan"));
                    Boolean tt = true;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("HoaDon").child("TaiBan");
                    ref.child(hoaDonTaiBan.getId_HoaDon()).child("daThanhToan").setValue(tt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(PhieuHoaDonTaiBanActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_Ban);
                            databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "onSuccess: Xoa thanh cong");
                                }
                            });
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                dataChiTietMon.clear();
            }
        });
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclePhieuHoaDon);
        tvMHD = findViewById(R.id.tvMa);
        tvGioHD = findViewById(R.id.tvGio);
        tvGiaHD = findViewById(R.id.tvGiaHD);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvBanHD = findViewById(R.id.tvBan);
        tvNgayHD = findViewById(R.id.tvNgay);
        tvKhu = findViewById(R.id.tvKhu);
        tvNV = findViewById(R.id.tvTenNV);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        ivHinh = findViewById(R.id.ivHinh);
    }

    private void loadDataThongTin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (bundle != null) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            hoaDonTaiBan.setId_HoaDon(bundle.getString("id_HoaDon"));
            tvMHD.setText(hoaDonTaiBan.getId_HoaDon());
            hoaDonTaiBan.setThoiGian_ThanhToan(bundle.getString("thoiGian_ThanhToan"));
            tvGioHD.setText(hoaDonTaiBan.getThoiGian_ThanhToan());
            hoaDonTaiBan.setNgayThanhToan(bundle.getString("ngayThanhToan"));
            tvNgayHD.setText(hoaDonTaiBan.getNgayThanhToan());
            hoaDonTaiBan.setId_Ban(bundle.getString("id_Ban"));
            id_Ban = hoaDonTaiBan.getId_Ban();
            ban.setTenBan(bundle.getString("tenBan"));
            tvBanHD.setText(ban.getTenBan());
            khu.setTenKhu(bundle.getString("tenKhu"));
            tvKhu.setText(khu.getTenKhu());
            hoaDonTaiBan.setTongTien(bundle.getDouble("tongTien"));
            tvTongTien.setText(nf.format(hoaDonTaiBan.getTongTien()) + "đ");
        }
        dialog.dismiss();
    }

    private void datachitietmon() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(id_Ban);

        databaseReference.child("HT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataGop.clear();
                dataChiTietMon.clear();
                // nếu trùng id_Mon tăng số lượng món
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ChiTietMon chiTietMon = item.getValue(ChiTietMon.class);
                        dataChiTietMon.add(chiTietMon);
                    }
                }
                Map<String, Integer> mapGopSL = new HashMap<>();
                for (ChiTietMon item : dataChiTietMon){
                    Integer current = mapGopSL.getOrDefault(item.getId_Mon(),0);
                    mapGopSL.put(item.getId_Mon(),current + item.getSl());
                }
                for (Map.Entry<String, Integer> entry : mapGopSL.entrySet()){
                    ChiTietMon chiTietMon = new ChiTietMon();
                    chiTietMon.setId_Mon(entry.getKey());
                    chiTietMon.setSl(entry.getValue());
                    for (ChiTietMon item : dataChiTietMon){
                        ChiTietMon chiTietMon1 = new ChiTietMon();
                        if (item.getId_Mon().equals(entry.getKey())){
                            chiTietMon1.setId_Mon(item.getId_Mon());
                            chiTietMon1.setSl(chiTietMon.getSl());
                            chiTietMon1.setId_Ban(item.getId_Ban());
                            chiTietMon1.setGia(item.getGia());
                            chiTietMon1.setTenMon(item.getTenMon());
                            chiTietMon1.setTenKH(item.getTenKH());
                            chiTietMon1.setGioGoiMon(item.getGioGoiMon());
                            chiTietMon1.setHinh(item.getHinh());
                            chiTietMon1.setNgayGoiMon(item.getNgayGoiMon());
                            chiTietMon1.setId_TrangThai(item.getId_TrangThai());
                            chiTietMon1.setGhiChu(item.getGhiChu());
                            dataGop.add(chiTietMon1);
                            break;
                        }
                    }
                }
                dataChiTietMon.clear();
                dataChiTietMon.addAll(dataGop);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
