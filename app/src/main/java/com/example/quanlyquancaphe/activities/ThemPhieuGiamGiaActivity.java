package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.PhieuGiamGia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ThemPhieuGiamGiaActivity extends AppCompatActivity {

    EditText edtGiaTri, edtNgayHetHan;
    Button btnThem;
    Toolbar toolBar;
    PhieuGiamGia phieuGiamGia = new PhieuGiamGia();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_themphieugiamgia_layout);
        setControl();
        toolBar.setTitle("Thêm phiếu giảm giá");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edtNgayHetHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDiaLog();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraDuLieu()) {
                    phieuGiamGia.setNgayHetHan(edtNgayHetHan.getText().toString());
                    phieuGiamGia.setGiaTri(Integer.parseInt(edtGiaTri.getText().toString()));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PhieuGiamGia");
                    String id_Phieu = databaseReference.push().getKey();
                    phieuGiamGia.setId_Phieu(id_Phieu);
                    databaseReference.child("ChuaSuDung").child(id_Phieu).setValue(phieuGiamGia).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ThemPhieuGiamGiaActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ThemPhieuGiamGiaActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean kiemTraDuLieu() {
        if (edtNgayHetHan.getText().toString().equals("")) {
            edtNgayHetHan.requestFocus();
            edtNgayHetHan.setError("Chưa chọn ngày");
            return false;
        }
        if (edtGiaTri.getText().toString().equals("")) {
            edtGiaTri.requestFocus();
            edtGiaTri.setError("Chưa nhập giá trị");
            return false;
        }
        return true;
    }

    private void openDiaLog() {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar chosenCalendar = Calendar.getInstance();
        int yearNow = currentCalendar.get(Calendar.YEAR);
        int monthNow = currentCalendar.get(Calendar.MONTH);
        int dayNow = currentCalendar.get(Calendar.DAY_OF_MONTH);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                chosenCalendar.set(year, month, day);
                if (soSanhNgay(chosenCalendar, currentCalendar) < 0) {
                    edtNgayHetHan.setError("Ngày không hợp lệ");
                    edtNgayHetHan.requestFocus();
                    return;
                }
                edtNgayHetHan.clearFocus();
                edtNgayHetHan.setError(null);
                edtNgayHetHan.setText(dateFormat.format(chosenCalendar.getTime()));
            }
        }, yearNow, monthNow, dayNow);
        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();
    }

    public static int soSanhNgay(Calendar date1, Calendar date2) {
        // So sánh năm
        int soSanhNam = Integer.compare(date1.get(Calendar.YEAR), date2.get(Calendar.YEAR));

        if (soSanhNam != 0) {
            return soSanhNam;
        }

        // So sánh tháng
        int soSanhThang = Integer.compare(date1.get(Calendar.MONTH), date2.get(Calendar.MONTH));

        if (soSanhThang != 0) {
            return soSanhNam;
        }

        // So sánh ngày
        return Integer.compare(date1.get(Calendar.DAY_OF_MONTH), date2.get(Calendar.DAY_OF_MONTH));
    }

    private void setControl() {
        edtGiaTri = findViewById(R.id.edtGiaTri);
        edtNgayHetHan = findViewById(R.id.edtNgayHetHan);
        edtNgayHetHan.setFocusable(false);
        btnThem = findViewById(R.id.btnThem);
        toolBar = findViewById(R.id.toolBar);
    }
}