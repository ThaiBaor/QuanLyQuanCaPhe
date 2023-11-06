package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.DatBan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CapNhatDatBanActivity extends AppCompatActivity {
    Toolbar toolbar;
    Bundle bundle;
    EditText edtTenKH, edtSDT, edtSoNguoi, edtNgay, edtGio;
    Button btnCapNhat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_capnhatdatban_layout);
        setConTrol();
        //Nhận dữ liệu
        bundle = getIntent().getExtras();
        setEvent();
    }

    private void setEvent() {
        //set title toolbar
        toolbar.setTitle("Đặt bàn");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //set dữ liệu
        if(bundle != null){
            edtTenKH.setText(bundle.getString("tenKH"));
            edtSDT.setText(bundle.getString("SDT"));
            edtSoNguoi.setText(String.valueOf(bundle.getInt("soNguoi")));
            edtNgay.setText(bundle.getString("ngay"));
            edtGio.setText(bundle.getString("gio"));
            Toast.makeText(this, bundle.getString("id_Ban"), Toast.LENGTH_SHORT).show();
        }
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() == true){
                    Update();
                    finish();
                }
            }
        });
    }
    private void Update() {
        DatBan datBan = new DatBan();
        datBan.setId_Ban(bundle.getString("id_Ban"));
        datBan.setTenKH(edtTenKH.getText().toString());
        datBan.setSDT(edtSDT.getText().toString());
        datBan.setSoNguoi(Integer.parseInt(edtSoNguoi.getText().toString()));
        datBan.setNgay(edtNgay.getText().toString());
        datBan.setGio(edtGio.getText().toString());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DatBan");
        reference.child(datBan.getId_Ban()).setValue(datBan).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CapNhatDatBanActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CapNhatDatBanActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        edtTenKH.setText("");
        edtSDT.setText("");
        edtSoNguoi.setText("");
        edtNgay.setText("");
        edtGio.setText("");
    }
    //hàm kiểm tra giá trị nhập
    private Boolean validate() {
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
        if (edtSoNguoi.getText().toString().isEmpty()) {
            edtSoNguoi.requestFocus();
            edtSoNguoi.setError("Số người trống");
            return false;
        }
        if (edtNgay.getText().toString().isEmpty()) {
            edtNgay.requestFocus();
            edtNgay.setError("Ngày đặt trống");
            return false;
        }
        if (edtGio.getText().toString().isEmpty()) {
            edtGio.requestFocus();
            edtGio.setError("Giờ đặt trống");
            return false;
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
        btnCapNhat = findViewById(R.id.btnCapNhat);
    }
}