package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.DatBan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DatBanActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText edtTenKH, edtSDT, edtSoNguoi, edtNgay, edtGio;
    Button btnDat, btnDatBanNgay;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_datban_layout);
        setConTrol();
        setEvent();
        bundle = getIntent().getExtras();
    }

    private void setEvent() {
        toolbar.setTitle("Đặt bàn");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        edtNgay.setText(simpleDateFormat.format(calendar.getTime()));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonGio();
            }
        });
        btnDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(bundle.getInt("soChoNgoi")) == true) {
                    AddDatBan();
                    finish();
                }
            }
        });
        btnDatBanNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChuyenTrangThaiBan(GioHangActivity.id_Ban, 2);
                Intent intent = new Intent(DatBanActivity.this, DanhSachMonPhucVuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Hàm chọn giờ cho editText giờ
    private void ChonGio() {
        //Lấy giờ hiện tại
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //Set lại giờ được chọn
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edtGio.setText(hourOfDay + ":" + minute);
            }
        }, gio, phut, true);
        timePickerDialog.show();
    }

    //Hàm add data
    private void AddDatBan() {
        //Add data vào bảng DatBan
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("DatBan");
        String id_Ban = GioHangActivity.id_Ban;
        String tenKH = edtTenKH.getText().toString();
        String SDT = edtSDT.getText().toString();
        Integer soNguoi = Integer.parseInt(edtSoNguoi.getText().toString());
        String ngay = edtNgay.getText().toString();
        String gio = edtGio.getText().toString();
        DatBan datBan = new DatBan(id_Ban, ngay, gio, tenKH, SDT, soNguoi);
        databaseReference.child(id_Ban).setValue(datBan).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DatBanActivity.this, "Đặt bàn thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DatBanActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //Chuyển trạng thái bàn về 2: Đã đặt
        ChuyenTrangThaiBan(id_Ban, 2);
        edtTenKH.setText("");
        edtSDT.setText("");
        edtSoNguoi.setText("");
        edtNgay.setText("");
        edtGio.setText("");
    }

    //Hàm chuyển trạng thái bàn
    private void ChuyenTrangThaiBan(String maBan, Integer trangThai) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Ban");
        databaseReference.child(maBan).child("id_TrangThaiBan").setValue(trangThai);
    }

    //hàm kiểm tra giá trị nhập
    private Boolean validate(Integer soChoNgoi){
        if (edtTenKH.getText().toString().isEmpty()) {
            edtTenKH.requestFocus();
            edtTenKH.setError("Tên khách hàng trống");
            return false;
        }
        if (edtSDT.getText().toString().isEmpty()) {
            edtSDT.requestFocus();
            edtSDT.setError("Số điện thoại trống");
            return false;
        }
        if(edtSDT.getText().toString().length() != 10){
            edtSDT.requestFocus();
            edtSDT.setError("SĐT không đúng");
            return false;
        }
        if (edtSoNguoi.getText().toString().isEmpty()) {
            edtSoNguoi.requestFocus();
            edtSoNguoi.setError("Số người trống");
            return false;
        }
        if (Integer.parseInt(edtSoNguoi.getText().toString()) > soChoNgoi) {
            edtSoNguoi.requestFocus();
            edtSoNguoi.setError("Số người nhiều hơn số chỗ ngồi");
            return false;
        }
        if (edtGio.getText().toString().isEmpty()) {
            edtGio.requestFocus();
            edtGio.setError("Trống");
            return false;
        }
        if (edtGio.getText().toString() != null){
            // Lấy thời gian hiện tại và thời gian đặt
            String [] time = edtGio.getText().toString().split(":");
            Integer gio = Integer.parseInt(time[0]);
            Integer phut = Integer.parseInt(time[1]);
            Calendar timeHT = Calendar.getInstance();
            Calendar timeDat = Calendar.getInstance();
            timeHT.set(Calendar.HOUR_OF_DAY,timeHT.get(Calendar.HOUR_OF_DAY));
            timeHT.set(Calendar.MINUTE, timeHT.get(Calendar.MINUTE));
            timeDat.set(Calendar.HOUR_OF_DAY, gio);
            timeDat.set(Calendar.MINUTE, phut);
            //Nếu giờ đặt là quá khứ, không thể đặt
            if (timeDat.compareTo(timeHT) <= 0) {
                edtGio.requestFocus();
                edtGio.setError("Không thể đặt");
                Toast.makeText(this, "Không thể đặt! Vui lòng chọn giờ phù hợp", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void setConTrol() {
        toolbar = findViewById(R.id.toolBar);
        edtTenKH = findViewById(R.id.edtTenKH);
        edtSDT = findViewById(R.id.edtSDT);
        edtSoNguoi = findViewById(R.id.edtSoNguoi);
        edtNgay = findViewById(R.id.edtNgay);
        edtGio = findViewById(R.id.edtGio);
        btnDat = findViewById(R.id.btnDat);
        btnDatBanNgay = findViewById(R.id.btnDatBanNgay);
    }
}