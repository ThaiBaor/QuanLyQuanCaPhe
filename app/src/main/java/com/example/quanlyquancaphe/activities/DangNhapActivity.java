package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        dangNhap("PC1", "123456789");
    }

    private void dangNhap(String taiKhoan, String matKhau){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Nhanvien");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null){
                    Toast.makeText(DangNhapActivity.this, "no", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (DataSnapshot item: snapshot.getChildren()){
                         if(tenDangNhap.equals(item.child("maNhanVien").toString()) && matKhauNhap.equals(item.child("matKhau").toString())){
                             Toast.makeText(DangNhapActivity.this, "ok", Toast.LENGTH_SHORT).show();
                             break;
                         }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DangNhapActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean kiemTraDangNhap(){
        return true;
    }

    private void setControl() {
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }
}