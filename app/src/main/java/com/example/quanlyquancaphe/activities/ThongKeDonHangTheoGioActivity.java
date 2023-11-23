package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDon;
import com.example.quanlyquancaphe.models.HoaDonKhachHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ThongKeDonHangTheoGioActivity extends AppCompatActivity {

    TextView tvTongHoaDon;
    TextView tvSoHoaDonTB;
    TableLayout tableLayout;
    ImageButton btnChart;
    EditText edtChonNgay;
    ArrayList<HoaDon> hoaDonArrayList = new ArrayList<>();
    HashMap<Integer, Integer> map = new HashMap<>();
    ArrayList<Integer> listGio = new ArrayList<>();
    ArrayList<Integer> listSoHoaDon = new ArrayList<>();
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongkedonhangtheogio_layout);
        setControl();
        toolBar.setTitle("Thống kê hóa đơn theo giờ");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Lấy ngày hiên tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Gán ngày hiện tại vào edittext
        edtChonNgay.setText(new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
        // Load dữ liệu hiện tại
        getData(dateFormat.format(calendar.getTime()));
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThongKeDonHangTheoGioActivity.this, BieuDoThongKeDonHangTheoGio.class);
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    listGio.add(entry.getKey());
                    listSoHoaDon.add(entry.getValue());
                }
                // Truyền 2 arraylist
                intent.putExtra("listGio", listGio);
                intent.putExtra("listSoHoaDon", listSoHoaDon);
                startActivity(intent);
                listGio.clear();
                listSoHoaDon.clear();
            }
        });
        edtChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
    }

    private void getData(String date) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HoaDon");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                map.clear();
                hoaDonArrayList.clear();
                // Gán dữ liệu ban đầu cho map
                for (int i = 5; i < 25; ++i) {
                    map.put(i, 0);
                }
                // Thêm hóa đơn đã thanh toán vào danh sách
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        if (Boolean.TRUE.equals(item.child("daThanhToan").getValue(Boolean.class)) && item.child("ngayThanhToan").getValue(String.class).equals(date)) {
                            String id_HoaDon = item.child("id_HoaDon").getValue(String.class);
                            String ngayThanhToan = item.child("ngayThanhToan").getValue(String.class);
                            String thoiGian_ThanhToan = item.child("thoiGian_ThanhToan").getValue(String.class);
                            HoaDon hoaDon = new HoaDon();
                            hoaDon.setId_HoaDon(id_HoaDon);
                            hoaDon.setNgayThanhToan(ngayThanhToan);
                            hoaDon.setThoiGian_ThanhToan(thoiGian_ThanhToan);
                            hoaDonArrayList.add(hoaDon);
                        }
                    }
                }
                // Gộp những hóa đơn trùng thời giờ thanh toán
                for (HoaDon hoaDon : hoaDonArrayList) {
                    int hour = Integer.parseInt(hoaDon.getThoiGian_ThanhToan().substring(0, 2));
                    for (int i = 5; i < 25; ++i) {
                        if (hour == i) {
                            int sl = map.get(i);
                            map.put(i, sl + 1);
                        }
                    }
                }
                tvTongHoaDon.setText(hoaDonArrayList.size() + " hóa đơn");
                tvSoHoaDonTB.setText(hoaDonArrayList.size() / 20.0 + " hóa đơn");
                createTable(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createTable(@NonNull Map<Integer, Integer> map) {
        tableLayout.removeAllViews();
        // Tạo hàng
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            tableRow.setWeightSum(2);
            // Tạo cột
            // Cột 1
            TextView tvThang = new TextView(this);
            tvThang.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1

            ));
            tvThang.setPadding(10, 10, 10, 10);
            tvThang.setTextSize(20);
            tvThang.setTextColor(Color.BLACK);
            tvThang.setGravity(Gravity.CENTER);
            tvThang.setBackgroundResource(R.drawable.table_border);
            tvThang.setText(entry.getKey() + "h");
            // Thêm TextView vào TableRow
            tableRow.addView(tvThang);
            // Cột 2
            TextView tvSoHoaDon = new TextView(this);
            tvSoHoaDon.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1

            ));
            tvSoHoaDon.setPadding(10, 10, 10, 10);
            tvSoHoaDon.setTextSize(20);
            tvSoHoaDon.setTextColor(Color.BLACK);
            tvSoHoaDon.setGravity(Gravity.CENTER);
            tvSoHoaDon.setBackgroundResource(R.drawable.table_border);
            tvSoHoaDon.setText(entry.getValue() + "");
            // Thêm TextView vào TableRow
            tableRow.addView(tvSoHoaDon);

            // Thêm TableRow vào TableLayout
            tableLayout.addView(tableRow);
        }
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                edtChonNgay.setText(dateFormat.format(calendar.getTime()));
                getData(dateFormat.format(calendar.getTime()));
            }
        }, yearNow, monthNow, dayNow);
        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();
    }

    private void setControl() {
        tvSoHoaDonTB = findViewById(R.id.tvSoHoaDonTB);
        tvTongHoaDon = findViewById(R.id.tvTongHoaDon);
        tableLayout = findViewById(R.id.tableLayout);
        btnChart = findViewById(R.id.btnChart);
        toolBar = findViewById(R.id.toolBar);
        edtChonNgay = findViewById(R.id.edtChonNgay);
        edtChonNgay.setFocusable(false);
    }
}