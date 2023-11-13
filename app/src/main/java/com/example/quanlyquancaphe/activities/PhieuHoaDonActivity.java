package com.example.quanlyquancaphe.activities;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.PhieuHoaDonAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDon;
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

import java.io.InputStream;
import java.util.ArrayList;

public class PhieuHoaDonActivity extends AppCompatActivity {
    TextView tvMHD, tvGioHD, tvNgayHD, tvBanHD, tvGiaHD, tvKhu, tvNV, tvTongTien;
    Button btnQuayLai, btnThanhToan;
    ImageView ivHinh;
    RecyclerView recyclerView;
    Bundle bundle;
    String id_Ban = "";
    ArrayList<ChiTietMon> dataChiTietMon = new ArrayList<>();
    HoaDon hoaDonTaiBan = new HoaDon();
    Ban ban = new Ban();
    Khu khu = new Khu();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PhieuHoaDonAdapter adapter;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_phieuhoadontaiban_layout);
        bundle = getIntent().getExtras();
        setControl();
        loadDataThongTin();
        datachitietmon();
        //loadQR();
        adapter = new PhieuHoaDonAdapter(PhieuHoaDonActivity.this, id_Ban, dataChiTietMon);
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
                    DatabaseReference ref = database.getReference("HoaDon");
                    ref.child(hoaDonTaiBan.getId_HoaDon()).child("daThanhToan").setValue(tt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

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
            tvTongTien.setText(hoaDonTaiBan.getTongTien() + "");
        }
        dialog.dismiss();
    }

    private void datachitietmon() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(id_Ban);

        databaseReference.child("HT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataChiTietMon.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ChiTietMon chiTietMon = item.getValue(ChiTietMon.class);
                        dataChiTietMon.add(chiTietMon);
                        System.out.println(chiTietMon.toString());
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
