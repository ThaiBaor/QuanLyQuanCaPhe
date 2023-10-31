package com.example.quanlyquancaphe.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.NguyenLieuAdapter;
import com.example.quanlyquancaphe.models.NguyenLieu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CapNhatNguyenLieuActivity extends AppCompatActivity {
    EditText edtTenNguyenLieu, edtNgayNhap, edtDonVi, edtSoLuongNhap, edtTonKho;
    Button btnCapNhat;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    NguyenLieuAdapter nguyenLieuAdapter;
    Toolbar toolBar;

    Bundle bundle;
    NguyenLieu nguyenLieu = new NguyenLieu();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_capnhatnguyenlieu_layout);

        setControl();
        bundle = getIntent().getExtras();

        loadDataItem();
        setEven();
    }

    private void setControl() {
        edtNgayNhap = findViewById(R.id.edtngaynhap);
        edtTenNguyenLieu = findViewById(R.id.edttennguyenlieu);
        edtTonKho = findViewById(R.id.edttonkho);
        edtSoLuongNhap = findViewById(R.id.edtsoluongnhap);
        edtDonVi = findViewById(R.id.edtdonvi);
        btnCapNhat = findViewById(R.id.btnCapNhat);
        toolBar = findViewById(R.id.toolBar);
    }

    private void setEven() {

        edtNgayNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDiaLog();
            }
        });
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraDuLieuCapNhat() == true){
                    capNhat();
                    edtTenNguyenLieu.setText("");
                    edtDonVi.setText("");
                    edtNgayNhap.setText("");
                    edtSoLuongNhap.setText("");
                    edtTonKho.setText("");
                }

            }
        });
    }
    private void loadDataItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (bundle != null){
            nguyenLieu.setMaNguyenLieu(bundle.getString("maNguyenLieu"));
            nguyenLieu.setTenNguyenLieu(bundle.getString("tenNguyenLieu"));
            edtTenNguyenLieu.setText(nguyenLieu.getTenNguyenLieu());
            nguyenLieu.setDonVi(bundle.getString("donVi"));
            edtDonVi.setText(nguyenLieu.getDonVi());
            nguyenLieu.setSoLuongNhap(bundle.getDouble("soLuongNhap"));
            edtSoLuongNhap.setText(String.valueOf(nguyenLieu.getSoLuongNhap()));
            nguyenLieu.setTonKho(bundle.getDouble("tonKho"));
            edtTonKho.setText(String.valueOf(nguyenLieu.getTonKho()));
            nguyenLieu.setMaNguyenLieu(bundle.getString("maNguyenLieu"));
            nguyenLieu.setNgayNhap(bundle.getString("ngayNhap"));
            edtNgayNhap.setText(nguyenLieu.getNgayNhap());
        }
        dialog.dismiss();
    }

    private void capNhat() {
        NguyenLieu nguyenLieu1 = new NguyenLieu();
        nguyenLieu1.setMaNguyenLieu(nguyenLieu.getMaNguyenLieu());
        nguyenLieu1.setTenNguyenLieu(edtTenNguyenLieu.getText().toString());
        nguyenLieu1.setDonVi(edtDonVi.getText().toString());
        nguyenLieu1.setTonKho(Double.parseDouble(edtTonKho.getText().toString()));
        nguyenLieu1.setNgayNhap(edtNgayNhap.getText().toString());
        nguyenLieu1.setSoLuongNhap(Double.parseDouble(edtSoLuongNhap.getText().toString()));
        databaseReference = FirebaseDatabase.getInstance().getReference("NguyenLieu");
        databaseReference.child(nguyenLieu1.getMaNguyenLieu()).setValue(nguyenLieu1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CapNhatNguyenLieuActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CapNhatNguyenLieuActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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


    private boolean kiemTraDuLieuCapNhat(){
        if(edtTenNguyenLieu.getText().toString().isEmpty()){
            edtTenNguyenLieu.setError("Nhập tên nguyên liệu");
            return false;
        }
        if(edtTenNguyenLieu.getText().length() > 255){
            edtTenNguyenLieu.setError("Lớn hơn 255 kí tự");
            return false;
        }
        if (edtDonVi.getText().length() > 5){
            edtDonVi.setError("Lớn hơn 5 kí tự");
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
}
