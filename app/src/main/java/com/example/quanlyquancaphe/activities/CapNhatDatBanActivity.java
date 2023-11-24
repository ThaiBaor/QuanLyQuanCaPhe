package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CapNhatDatBanActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    ValueEventListener valueEventListener;
    ArrayList<Ban> dataBan = new ArrayList<>();
    Toolbar toolbar;
    Bundle bundle;
    EditText edtTenKH, edtSDT, edtSoNguoi, edtNgay, edtGio;
    Button btnCapNhat;
    String id_Ban;
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetDataBan();
        //set dữ liệu
        if(bundle != null){
            edtTenKH.setText(bundle.getString("tenKH"));
            edtSDT.setText(bundle.getString("SDT"));
            edtSoNguoi.setText(String.valueOf(bundle.getInt("soNguoi")));
            edtNgay.setText(bundle.getString("ngay"));
            edtGio.setText(bundle.getString("gio"));
            id_Ban = bundle.getString("id_Ban");

        }
        edtGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonGio();
            }
        });
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(dataBan) == true){
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
    private Boolean validate(ArrayList<Ban> bans) {
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
        for (Ban item: bans){
            if (id_Ban.equals(item.getId_Ban())){
                int slNguoi = item.getSoChoNgoi();
                if (Integer.parseInt(edtSoNguoi.getText().toString())> slNguoi){
                    edtSoNguoi.requestFocus();
                    edtSoNguoi.setError("Số người nhiều hơn số chỗ ngồi");
                    return false;
                }
            }
        }
        if (edtGio.getText().toString().isEmpty()) {
            edtGio.requestFocus();
            edtGio.setError("Giờ đặt trống");
            return false;
        }
        if (edtGio.getText().toString() != null){
            // Lấy thời gian hiện tại và thời gian đặt
            String [] time = edtGio.getText().toString().split(":");
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
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
    //Hàm chọn giờ cho editText giờ
    private void ChonGio(){
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
        },gio, phut, true);
        timePickerDialog.show();
    }
    private void GetDataBan() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Ban");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBan.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Ban ban = item.getValue(Ban.class);
                    dataBan.add(ban);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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