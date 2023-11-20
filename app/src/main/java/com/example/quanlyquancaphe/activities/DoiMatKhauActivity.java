package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoiMatKhauActivity extends AppCompatActivity {
    EditText edtTenDangNhap, edtMatKhauHienTai, edtMatKhauMoi, edtXacNhanMatKhauMoi;
    Button btnXacNhan;
    Toolbar toolBar;
    boolean xacNhan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_doimatkhau_layout);
        setControl();
        setEvent();
    }

    private void setEvent() {

        edtTenDangNhap.setText(DangNhapActivity.tenDangNhap);
        edtTenDangNhap.setFocusable(false);
        edtTenDangNhap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                kiemTraEdtTenDangNhap();
            }
        });
        edtMatKhauHienTai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                kiemTraEdtMatKhauHienTai();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtMatKhauMoi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                kiemTraEdtMatKhauMoi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtXacNhanMatKhauMoi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                kiemTraEdtXacNhanMatKhauMoi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiemTraDuLieuNhap();
                if(xacNhan){
                    kiemTraTaiKhoanVaDoiMatKhau(edtTenDangNhap.getText().toString(), edtMatKhauHienTai.getText().toString());
                }
                else {
                    Toast.makeText(DoiMatKhauActivity.this, "Vui lòng kiểm tra lại dữ liệu nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolBar.setTitle("Đổi mật khẩu");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void kiemTraDuLieuNhap() {
       kiemTraEdtTenDangNhap();
       kiemTraEdtMatKhauHienTai();
       kiemTraEdtMatKhauMoi();
       kiemTraEdtXacNhanMatKhauMoi();
    }

    private void setControl() {
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhauHienTai = findViewById(R.id.edtMatKhauHienTai);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacNhanMatKhauMoi = findViewById(R.id.edtXacNhanMatKhauMoi);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        toolBar = findViewById(R.id.toolBar);

    }

    private void doiMatKhau(String tenDangNhap, String matKhau){
        if (kiemTraXacNhanMatKhau()){

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Nhanvien");
                ref.child(tenDangNhap).child("matKhau").setValue(matKhau).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finishAffinity();
                        Intent intent = new Intent(DoiMatKhauActivity.this, DangNhapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(DoiMatKhauActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DoiMatKhauActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                });


        }
    }

    private boolean kiemTraXacNhanMatKhau(){
        if(edtMatKhauMoi.getText().toString().equals(edtXacNhanMatKhauMoi.getText().toString())){
            return true;
        }
        else {
            edtMatKhauMoi.setError("Mật khẩu mới và Xác nhận mật khẩu mới không giống nhau");
            return false;
        }
    }

    private void kiemTraTaiKhoanVaDoiMatKhau(String taiKhoanNhap, String matKhauNhap){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Nhanvien");
        ref.child(taiKhoanNhap).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() == false){
                    Toast.makeText(DoiMatKhauActivity.this, "Tài khoản không tồn tài trên hệ thống. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String matKhauData = snapshot.child("matKhau").getValue(String.class);
                    if(matKhauData.equals(matKhauNhap)){
                        doiMatKhau(edtTenDangNhap.getText().toString(), edtXacNhanMatKhauMoi.getText().toString());

                    }
                    else {
                        Toast.makeText(DoiMatKhauActivity.this, "Mật khẩu không tồn tài trên hệ thống. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DoiMatKhauActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean hasSpecialChar(String str) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.find();
    }

    private void kiemTraEdtTenDangNhap(){
        if (edtTenDangNhap.getText().toString().isEmpty()){
            edtTenDangNhap.setError("Tên đăng nhập trống!!!");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtTenDangNhap.getText().toString().length() > 255){
            edtTenDangNhap.setError("Chỉ cho phép nhập tối đa 255 kí tự");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (hasSpecialChar(edtTenDangNhap.getText().toString())) {
            edtTenDangNhap.setError("Vui lòng nhập kí tự hợp lệ");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
    }

    private void kiemTraEdtMatKhauHienTai(){
        if (edtMatKhauHienTai.getText().toString().isEmpty()){
            edtMatKhauHienTai.setError("Mật khẩu hiện tại trống!!!");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtMatKhauHienTai.getText().toString().length() > 15){
            edtMatKhauHienTai.setError("Chỉ cho phép nhập tối đa 15 kí tự");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtMatKhauHienTai.getText().toString().length() < 6){
            edtMatKhauHienTai.setError("Chỉ cho phép nhập tối thiểu 6 kí tự");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
    }

    private void kiemTraEdtMatKhauMoi(){
        if (edtMatKhauMoi.getText().toString().isEmpty()){
            edtMatKhauMoi.setError("Mật khẩu mới trống!!!");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtMatKhauMoi.getText().toString().length() > 15){
            edtMatKhauMoi.setError("Chỉ cho phép nhập tối đa 15 kí tự");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtMatKhauMoi.getText().toString().length() < 6){
            edtMatKhauMoi.setError("Chỉ cho phép nhập tối thiểu 6 kí tự");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtMatKhauMoi.getText().toString().equals(edtMatKhauHienTai.getText().toString())){
            edtMatKhauMoi.setError("Mật khẩu mới giống với mật khẩu cũ");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }

    }

    private void kiemTraEdtXacNhanMatKhauMoi(){
        if (edtXacNhanMatKhauMoi.getText().toString().isEmpty()){
            edtXacNhanMatKhauMoi.setError("Xác nhận mật khẩu mới trống!!!");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtXacNhanMatKhauMoi.getText().toString().length() > 15){
            edtXacNhanMatKhauMoi.setError("Chỉ cho phép nhập tối đa 15 kí tự");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtXacNhanMatKhauMoi.getText().toString().length() < 6){
            edtXacNhanMatKhauMoi.setError("Chỉ cho phép nhập tối thiểu 6 kí tự");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
        if (edtMatKhauMoi.getText().toString().equals(edtMatKhauHienTai.getText().toString())){
            edtMatKhauMoi.setError("Mật khẩu mới giống với mật khẩu cũ");
            xacNhan = false;
        }
        else {
            xacNhan = true;
        }
    }


}