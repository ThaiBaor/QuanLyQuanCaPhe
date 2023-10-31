package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.NguyenLieu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ThemNguyenLieuActivity extends AppCompatActivity {
    EditText edtTenNguyenLieu, edtNgayNhap, edtDonVi, edtSoLuongNhap, edtTonKho;
    Button btnAdd;

    DatabaseReference databaseReference;

    Toolbar toolBar;

    NguyenLieu nguyenLieu = new NguyenLieu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_themnguyenlieu_layout);
        setControl();
        setEvent();
    }

    private void setEvent() {
        toolBar.setTitle("Thêm nguyên liệu");
        edtNgayNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDiaLog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kiemTraDuLieuThem()){
                    addNguyenLieu();
                }
                else {
                    Toast.makeText(ThemNguyenLieuActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void openDiaLog(){
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year,month,day);
                edtNgayNhap.setText(dateFormat.format(calendar.getTime()));
            }
        }, yearNow, monthNow, dayNow);
        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();
    }

    private boolean kiemTraDuLieuThem(){
        if(edtTenNguyenLieu.getText().toString().isEmpty()){
            edtTenNguyenLieu.setError("Nhập tên nguyên liệu");
            return false;
        }
        if(edtDonVi.getText().toString().isEmpty()){
            edtDonVi.setError("Nhập đơn vị");
            return false;
        }
        if(edtNgayNhap.getText().toString().isEmpty()){
            edtNgayNhap.setError("Chọn ngày nhập");
            return false;
        }
        if(edtSoLuongNhap.getText().toString().isEmpty()){
            edtSoLuongNhap.setError("Nhập số lượng nhập");
            return false;
        }
        if(edtTonKho.getText().toString().isEmpty()){
            edtTonKho.setError("Nhập tồn kho");
            return false;
        }
        return true;
    }

    private void themDuLieuNguyenLieu(){
        nguyenLieu.setTenNguyenLieu(edtTenNguyenLieu.getText().toString());
        nguyenLieu.setDonVi(edtDonVi.getText().toString());
        nguyenLieu.setNgayNhap(edtNgayNhap.getText().toString());
        nguyenLieu.setSoLuongNhap( Double.parseDouble(edtSoLuongNhap.getText().toString()) + Double.parseDouble(edtTonKho.getText().toString()));
        nguyenLieu.setTonKho(Double.parseDouble(edtTonKho.getText().toString()));
    }

    private void addNguyenLieu(){
        themDuLieuNguyenLieu();
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("NguyenLieu");
        String auto_Id = databaseReference.push().getKey();
        nguyenLieu.setMaNguyenLieu(auto_Id);
        databaseReference.child(auto_Id).setValue(nguyenLieu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(ThemNguyenLieuActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(ThemNguyenLieuActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setControl() {

        edtTenNguyenLieu = findViewById(R.id.edttennguyenlieu);
        edtNgayNhap = findViewById(R.id.edtngaynhap);
        edtDonVi = findViewById(R.id.edtdonvi);
        edtSoLuongNhap = findViewById(R.id.edtsoluongnhap);
        edtTonKho = findViewById(R.id.edttonkho);
        btnAdd = findViewById(R.id.btnthem);
        edtNgayNhap.setFocusable(false);
        toolBar = findViewById(R.id.toolBar);
    }
}