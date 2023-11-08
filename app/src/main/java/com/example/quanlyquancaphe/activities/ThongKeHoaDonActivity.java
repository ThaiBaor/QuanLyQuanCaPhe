package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.ThongKeHoaDonAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.models.LoaiMon;
import com.example.quanlyquancaphe.models.NguyenLieu;
import com.example.quanlyquancaphe.models.ThongKeHoaDon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThongKeHoaDonActivity extends AppCompatActivity {

    ArrayList<ThongKeHoaDon> data = new ArrayList<>();
    ThongKeHoaDonAdapter thongKeHoaDonAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    TextView tvGiothuNhat, tvGioThuHai, tvNgayThongKe;

    ThongKeHoaDon thongKeHoaDon = new ThongKeHoaDon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongkehoadon_layout);
        setControl();
        setEvent();
        getDataHoaDonTaiBan();
        thongKeHoaDonAdapter = new ThongKeHoaDonAdapter(this, data);
        recyclerView.setAdapter(thongKeHoaDonAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setEvent() {
        tvGiothuNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGioThuNhat();
            }
        });
        tvGioThuHai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGioThuHai();
            }
        });
        tvNgayThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonNgayThongKe();
                chuyenNgayGioThongKeThuNhat();
            }
        });
    }

    private void chonNgayThongKe(){
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year,month,day);
                tvNgayThongKe.setText(dateFormat.format(calendar.getTime()));
            }
        }, yearNow, monthNow, dayNow);
        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();
    }

    private void ChonGioThuNhat(){
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvGiothuNhat.setText(hourOfDay + ":" + minute);
            }
        },gio, phut, true);
        timePickerDialog.show();
    }
    private void ChonGioThuHai(){
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvGioThuHai.setText(hourOfDay + ":" + minute);
            }
        },gio, phut, true);
        timePickerDialog.show();
    }

    private void getDataHoaDonTaiBan(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ThongKeHoaDonActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("HoaDonTaiBan");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    String id_MaHoaDon = item.child("id_HoaDon").getValue().toString();
                    String ID_DSMon = item.child("id_DSMon_TaiBan").getValue().toString();
                    String thoiGian_thanhtoan = item.child("thoiGian_ThanhToan").getValue().toString();
                    Double tongTien = Double.parseDouble(item.child("tongTien").getValue().toString());
                    Boolean daThanhToan = Boolean.parseBoolean(item.child("daThanhToan").getValue().toString());

                    String tenKhachHang = "Không có";
                    thongKeHoaDon = new ThongKeHoaDon(id_MaHoaDon, ID_DSMon, thoiGian_thanhtoan, tongTien, daThanhToan, tenKhachHang);
                    data.add(thongKeHoaDon);
                }
                thongKeHoaDonAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(ThongKeHoaDonActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ThongKeHoaDon getGio(String id_dsmon, ThongKeHoaDon thongKeHoaDon){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("DSMon_TaiBan");
        ref.child(id_dsmon).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String gio = snapshot.child("thoiGian_GoiMon").getValue(String.class);
                thongKeHoaDon.setGio(gio);
                Toast.makeText(ThongKeHoaDonActivity.this, thongKeHoaDon.getGio(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return thongKeHoaDon;
    }

    private Date chuyenNgayGioThongKeThuNhat(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = tvNgayThongKe.getText().toString() + " " + tvGiothuNhat.getText().toString();
        try{
             date = dateFormat.parse(dateString);
            Toast.makeText(this, date.toString(), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Toast.makeText(this, "Loi", Toast.LENGTH_SHORT).show();
        }
        return date;
    }
    private Date chuyenNgayGioThongKeThuHai(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = tvNgayThongKe.getText().toString() + " " + tvGioThuHai.getText().toString();
        try{
            date = dateFormat.parse(dateString);
            Toast.makeText(this, date.toString(), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Toast.makeText(this, "Loi", Toast.LENGTH_SHORT).show();
        }
        return date;
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerviewthongkehoadon);
        tvGiothuNhat = findViewById(R.id.tvGioThuNhat);
        tvGioThuHai = findViewById(R.id.tvGioThuHai);
        tvNgayThongKe = findViewById(R.id.tvNgayThongKe);
    }

}