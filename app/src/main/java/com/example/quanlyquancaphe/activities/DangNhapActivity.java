package com.example.quanlyquancaphe.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.services.LocalDataManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DangNhapActivity extends AppCompatActivity {
    EditText edtTenDangNhap, edtMatKhau;
    Button btnDangNhap;
    public static String tenDangNhap = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_dangnhap_layout);
        setControl();
        getLastLogin();
        setEnvent();
    }

    private void setEnvent() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraDuLieuDangNhap()) {
                    dangNhap(edtTenDangNhap.getText().toString());
                }
            }
        });
    }

    private void dangNhap(String taiKhoan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DangNhapActivity.this).setTitle("").setMessage("Đang đăng nhập...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Nhanvien");
        ref.child(taiKhoan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() == false) {
                    Toast.makeText(DangNhapActivity.this, "Tài khoản không tồn tài trên hệ thống. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String taiKhoanData = snapshot.child("maNhanVien").getValue(String.class);
                    String matKhauData = snapshot.child("matKhau").getValue(String.class);
                    String viTri = snapshot.child("viTri").getValue(String.class);
                    if (kiemTraDangNhap(taiKhoanData, matKhauData)) {
                        tenDangNhap = taiKhoanData;
                        dangNhapTheoRole(viTri);
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Mật khẩu sai. Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(DangNhapActivity.this, "Lỗi!!! Vui lòng kiểm tra kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dangNhapTheoRole(String viTri) {
        setLastLogin();
        switch (viTri) {
            case "Phục vụ":
                chuyenManHinhTheoRole(DangNhapActivity.this, DanhSachBanActivity.class);
                break;
            case "Pha chế":
                chuyenManHinhTheoRole(DangNhapActivity.this, DanhSachMonKhaDungActivity.class);
                break;
            case "Thu ngân":
                chuyenManHinhTheoRole(DangNhapActivity.this, ThongKeHoaDonActivity.class);
                break;
            case "Quản lý":
                chuyenManHinhTheoRole(DangNhapActivity.this, QuanLyMonActivity.class);
                break;
        }
    }

    private void chuyenManHinhTheoRole(Activity activity, Class secondActivity) {

        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }

    private boolean kiemTraDangNhap(String taiKhoan, String matKhau) {
        if (taiKhoan.equals(edtTenDangNhap.getText().toString()) && matKhau.equals(edtMatKhau.getText().toString())) {
            return true;
        }
        return false;
    }

    private boolean kiemTraDuLieuDangNhap() {
        if (edtTenDangNhap.getText().toString().isEmpty()) {
            edtTenDangNhap.setError("Tên đăng nhập trống!!!");
            return false;
        }
        if (edtMatKhau.getText().toString().isEmpty()) {
            edtMatKhau.setError("Mật khẩu trống!!!");
            return false;
        }
        return true;
    }


    private void getLastLogin() {
        if (LocalDataManager.getLastUsername() != null && LocalDataManager.getLastPassword() != null) {
            edtTenDangNhap.setText(LocalDataManager.getLastUsername());
            edtMatKhau.setText(LocalDataManager.getLastPassword());
        }
    }

    private void setLastLogin() {
        LocalDataManager.setLastLogin(edtTenDangNhap.getText().toString(), edtMatKhau.getText().toString());
    }

    private void setControl() {
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }
}