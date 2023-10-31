package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.NguyenLieu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DangNhapActivity extends AppCompatActivity {
    EditText edtTenDangNhap, edtMatKhau;
    Button btnDangNhap;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    String tenDangNhap = "", matKhauNhap = "", viTri = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_dangnhap_layout);
        setControl();
        setEnvent();
    }

    private void setEnvent() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraDuLieuDangNhap()){
                    dangNhap(edtTenDangNhap.getText().toString(), edtMatKhau.getText().toString());
                }
            }
        });
    }

    private void dangNhap(String taiKhoan, String matKhau){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Nhanvien");
        ref.child(taiKhoan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() == false){
                    Toast.makeText(DangNhapActivity.this, "Tài khoản không tồn tài trên hệ thống. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String taiKhoanData = snapshot.child("maNhanVien").getValue(String.class);
                    String matKhauData = snapshot.child("matKhau").getValue(String.class);
                    if(kiemTraDangNhap(taiKhoanData, matKhauData)){
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(DangNhapActivity.this, "Mật khẩu sai. Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DangNhapActivity.this, "apvbapva", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean kiemTraDangNhap(String taiKhoan, String matKhau){
        if(taiKhoan.equals(edtTenDangNhap.getText().toString()) && matKhau.equals(edtMatKhau.getText().toString())){
            return true;
        }
        return false;
    }

    private boolean kiemTraDuLieuDangNhap(){
        if(edtTenDangNhap.getText().toString().isEmpty()){
            edtTenDangNhap.setError("Tên đăng nhập trống!!!");
            return false;
        }
        if(edtMatKhau.getText().toString().isEmpty()){
            edtMatKhau.setError("Mật khẩu trống!!!");
            return false;
        }
        return true;
    }

    private void setControl() {
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }
}