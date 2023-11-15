package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.GioHangAdapter;
import com.example.quanlyquancaphe.interfaces.GioHangInterface;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.ultilities.HoaDonUltility;
import com.example.quanlyquancaphe.ultilities.NotificationUtility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GioHangActivity extends AppCompatActivity implements GioHangInterface {
    Toolbar toolBar;
    RecyclerView rv;
    Button btnXacNhan;
    DatabaseReference databaseReference;
    GioHangAdapter adapter;
    String time;
    boolean firstNoti = true;
    public static ArrayList<ChiTietMon> currentData = new ArrayList<>();
    public static String id_Ban = " ", tenKH = " ";
    private ArrayList<ChiTietMon> dataOnFB = new ArrayList<>();
    private ArrayList<ChiTietMon> finalData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_giohang_layout);
        setControl();
        adapter = new GioHangAdapter(GioHangActivity.this, finalData, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getDataAdapter();
        toolBar.setNavigationOnClickListener(view -> finish());
        btnXacNhan.setOnClickListener(view -> {
            luuGioHang();
            NotificationUtility.updateNotiOnFirebase(0, "Có đơn hàng mới");
        });
        getNotification();
    }

    private void getNotification() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ThongBao");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (firstNoti) {
                    firstNoti = false;
                    return;
                }
                if (snapshot.child("id").getValue(Integer.class) == null) {
                    return;
                }
                if (snapshot.child("id").getValue(Integer.class) == 1) {
                    NotificationUtility.pushNotification(GioHangActivity.this, snapshot.child("contentText").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getDataAdapter() {
        // Kiểm tra tại bàn hay mang về
        if (!id_Ban.equals(" ")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_Ban);
        }
        if (!tenKH.equals(" ")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(tenKH);
        }
        // Lấy dữ liệu danh sách món hiện có
        databaseReference.child("HT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataOnFB.clear();
                finalData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot items : dataSnapshot.getChildren()) {
                        ChiTietMon _chiTietMon = items.getValue(ChiTietMon.class);
                        dataOnFB.add(_chiTietMon);
                    }
                }
                // Trường hợp chỉ lấy dữ liệu trên Firebase
                if (dataOnFB.size() != 0 && currentData.size() == 0) {
                    finalData.addAll(dataOnFB);
                    adapter.notifyDataSetChanged();
                    return;
                }
                // Trường hợp chỉ lấy dữ liệu tạm thòi
                if (dataOnFB.size() == 0 && currentData.size() != 0) {
                    finalData.addAll(currentData);
                    adapter.notifyDataSetChanged();
                    return;
                }
                // Trường hợp vừa lấy dữ liệu tạm thời vừa lấy dũ liệu trên Firebase
                if (dataOnFB.size() != 0) {
                    dataOnFB.addAll(currentData);
                    finalData.addAll(dataOnFB);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void luuGioHang() {
        // Nếu giỏ hàng trống thì không lưu
        if (currentData.size() == 0 && dataOnFB.size() == 0) {
            return;
        }
        // Hiển thị dialog thông báo
        AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        // Lấy ngày và giờ hiện tại
        String ngayGoiMon = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String gioGoiMon = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).replace(':', '0');
        if (!id_Ban.equals(" ")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_Ban);
        }
        if (!tenKH.equals(" ")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(time + "-" + tenKH);
        }
        for (ChiTietMon _chiTietMon : currentData) {
            _chiTietMon.setId_TrangThai(0);
            _chiTietMon.setId_Ban(id_Ban);
            _chiTietMon.setTenKH(time + "-" + tenKH);
            _chiTietMon.setNgayGoiMon(ngayGoiMon);
            _chiTietMon.setGioGoiMon(gioGoiMon);
            databaseReference.child("HT").child(time).child(_chiTietMon.getId_Mon()).setValue(_chiTietMon).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(GioHangActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        Toast.makeText(GioHangActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        if (!id_Ban.equals(" ")) {
            chuyenTrangThaiBan(id_Ban);
        }
        if (!tenKH.equals(" ")) {
            taoHoaDonMangVe();
        }
        currentData.clear();
        finish();
    }

    private void taoHoaDonMangVe() {
        HoaDonUltility.getHdInstance().taoHoaDonMangVe(this, time + "-" + tenKH);
    }

    // Đổi trạng thái bàn
    private void chuyenTrangThaiBan(String id_Ban) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Ban");
        databaseReference.child(id_Ban).child("id_TrangThaiBan").setValue(1);
    }

    private void setControl() {
        toolBar = findViewById(R.id.toolBar);
        rv = findViewById(R.id.rv);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }

    @Override
    public void onDeleteButtonClick(Integer position) {
        boolean flag = false;
        // Trường hợp vừa xóa dữ liệu tạm thời vừa xóa dữ liệu trên Firebase
        if (currentData.size() != 0 && dataOnFB.size() != 0) {
            // Xóa dữ liệu tạm thời
            for (int i = 0; i < currentData.size(); ++i) {
                if (finalData.get(position).getId_Mon().equals(currentData.get(i).getId_Mon()) && finalData.get(position).getGioGoiMon() == null) {
                    currentData.remove(finalData.get(position));
                    finalData.remove((int) position);
                    adapter.notifyItemRemoved(position);
                    flag = true;
                    break;
                }
            }
            // Xóa dữ liệu trên Firebase
            if (!flag) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Thông báo");
                builder.setMessage("Món hiện tại đã có trên hệ thống. Xác nhận xóa ?");
                builder.setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                    String id_Mon = finalData.get((int) position).getId_Mon();
                    String gioGoiMon = finalData.get((int) position).getGioGoiMon().replace(':', '0');
                    String id_Ban = finalData.get((int) position).getId_Ban();
                    databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon");
                    databaseReference.child(id_Ban).child("HT").child(gioGoiMon).child(id_Mon).removeValue();
                });
                builder.setNegativeButton("Hủy", (dialogInterface, i) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return;
        }
        // Trường hợp chỉ xóa dữ liệu ở danh sách tạm thời
        if (currentData.size() != 0) {
            for (int c = 0; c < currentData.size(); ++c) {
                if (currentData.get(c).getId_Mon().equals(finalData.get(position).getId_Mon())) {
                    currentData.remove(c);
                    break;
                }
            }
            finalData.remove((int) position);
            adapter.notifyItemRemoved(position);
            return;
        }

        // Trường hợp chỉ xóa dữ liệu trên Firebase
        if (dataOnFB.size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Thông báo");
            builder.setMessage("Món hiện tại đã có trên hệ thống. Xác nhận xóa ?");
            builder.setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                String id_Mon = finalData.get((int) position).getId_Mon();
                String gioGoiMon = finalData.get((int) position).getGioGoiMon().replace(':', '0');
                String id_Ban = finalData.get((int) position).getId_Ban();
                databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon");
                databaseReference.child(id_Ban).child("HT").child(gioGoiMon).child(id_Mon).removeValue();
            });
            builder.setNegativeButton("Hủy", (dialogInterface, i) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPlusButtonClick(Integer position, EditText edtSL, TextView tvGia) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        finalData.get(position).tang();
        edtSL.setText(String.valueOf(finalData.get(position).getSl()));
        tvGia.setText(nf.format(finalData.get(position).tinhTongTien()) + "đ");

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMinusButtonClick(Integer position, EditText edtSL, TextView tvGia) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        finalData.get(position).giam();
        edtSL.setText(String.valueOf(finalData.get(position).getSl()));
        tvGia.setText(nf.format(finalData.get(position).getSl() * finalData.get(position).getGia()) + "đ");
    }

    @Override
    public void onNoteChange(Integer position, String note) {
        finalData.get(position).setGhiChu(note);
    }

    @Override
    public void onQtyChange(Integer position, Integer qty, TextView tvGia) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        finalData.get(position).setSl(qty);
        tvGia.setText(nf.format(finalData.get(position).getSl() * finalData.get(position).getGia()) + "đ");
    }
}