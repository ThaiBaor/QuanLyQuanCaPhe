package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.HoaDon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

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
        /*
        // Tạo hàng tiêu đề
        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        titleRow.setWeightSum(2);
        // Tạo cột 1
        TextView tvTitleGio = new TextView(this);
        tvTitleGio.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
        tvTitleGio.setPadding(10, 10, 10, 10);
        tvTitleGio.setGravity(Gravity.CENTER);
        tvTitleGio.setText("Giờ");
        tvTitleGio.setTypeface(null, Typeface.BOLD);
        tvTitleGio.setBackgroundResource(R.drawable.table_border_title);
        tvTitleGio.setTextSize(20);
        titleRow.addView(tvTitleGio);
        // Tạo cột 2
        TextView tvTitleSoHoaDon = new TextView(this);
        tvTitleSoHoaDon.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
        tvTitleSoHoaDon.setPadding(10, 10, 10, 10);
        tvTitleSoHoaDon.setGravity(Gravity.CENTER);
        tvTitleSoHoaDon.setText("Số hóa đơn");
        tvTitleSoHoaDon.setTypeface(null, Typeface.BOLD);
        tvTitleSoHoaDon.setBackgroundResource(R.drawable.table_border_title);
        tvTitleSoHoaDon.setTextSize(20);
        titleRow.addView(tvTitleSoHoaDon);

        tableLayout.addView(titleRow);

         */
        createTitleRow();
        // Tạo hàng dữ liệu
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            TableRow contentRow = new TableRow(this);
            contentRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            contentRow.setWeightSum(2);
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
            contentRow.addView(tvThang);
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
            contentRow.addView(tvSoHoaDon);

            // Thêm TableRow vào TableLayout
            tableLayout.addView(contentRow);
        }
    }

    private void createTitleRow() {
        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        titleRow.setWeightSum(2);
        // Tạo cột
        // Cột 1
        TextView column1 = new TextView(this);
        column1.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1

        ));

        column1.setTextSize(20);
        column1.setPadding(0, 10, 0, 10);
        column1.setTextColor(Color.BLACK);
        column1.setGravity(Gravity.CENTER);
        column1.setTypeface(null, Typeface.BOLD);
        column1.setBackgroundResource(R.drawable.table_border_title);
        column1.setText("Giờ");
        // Thêm TextView vào TableRow
        titleRow.addView(column1);
        // Cột 2
        TextView column2 = new TextView(this);
        column2.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1

        ));
        column2.setPadding(0, 10, 0, 10);
        column2.setTextSize(20);
        column2.setTypeface(null, Typeface.BOLD);
        column2.setTextColor(Color.BLACK);
        column2.setGravity(Gravity.CENTER);
        column2.setBackgroundResource(R.drawable.table_border_title);
        column2.setText("Số hóa đơn");
        // Thêm TextView vào TableRow
        titleRow.addView(column2);

        // Thêm TableRow vào TableLayout
        tableLayout.addView(titleRow);
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat_ = new SimpleDateFormat("yyyy-MM-dd");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                edtChonNgay.setText(dateFormat.format(calendar.getTime()));
                getData(dateFormat_.format(calendar.getTime()));
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