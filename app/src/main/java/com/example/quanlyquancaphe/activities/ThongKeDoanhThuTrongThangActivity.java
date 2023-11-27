package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDoanhThuTrongThangActivity extends AppCompatActivity {
    TextView tvNgay, tvDoanhThu;
    ImageButton btnChart;
    TableLayout tbLayout;
    Spinner spinner;
    HashMap<Integer, Integer> hashMap = new HashMap<>();
    List<String> listNgay = new ArrayList<>();
    List<Double> listDouble = new ArrayList<>();
    List<HoaDon> listTime = new ArrayList<>();
    List<HoaDon> listHoaDon = new ArrayList<>();
    Map<String, HoaDon> thongke = new HashMap<>();
    Map<String, HoaDon> thongke_MangVe = new HashMap<>();
    ArrayList<Integer> listKey = new ArrayList<>();
    ArrayList<Integer> listValues = new ArrayList<>();
    LocalDate currentDate = LocalDate.now();
    int monthHienTai = currentDate.getMonthValue();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongkedoanhthutrongthang_layout);
        setCtrol();
        toolbar.setTitle("Thống kê doanh thu theo tháng");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // tongDoanhSoTheoNgay(monthHienTai);
        chonNgay(monthHienTai);
    }

    private void chonNgay(Integer monthHienTai) {
        // Gọi hàm tổng danh thu
        tongDoanhSoTheoNgay(monthHienTai);
        List<Integer> listSpinner = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            listSpinner.add(i );
            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSpinner);
            spinner.setAdapter(adapter);
        }
        spinner.setSelection(monthHienTai-1, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Integer selectedMonth = (Integer) adapterView.getItemAtPosition(i);
                tbLayout.removeAllViews();
                tongDoanhSoTheoNgay(selectedMonth);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    // đẩy dữ liệu
    private void setEvent(HashMap<Integer, Integer> map) {
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listKey.clear();
                listValues.clear();
                Intent intent = new Intent(ThongKeDoanhThuTrongThangActivity.this, BieuDoThongKeDoanhThuTheoNgay_Activity.class);
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    Integer key = entry.getKey();
                    listKey.add(key);
                    Integer values = entry.getValue();
                    listValues.add(values);
                }
                intent.putExtra("Key", listKey);
                intent.putExtra("value", listValues);
                startActivity(intent);
            }
        });
    }

    private void tongDoanhSoTheoNgay(Integer monthValues) {
        // Lấy ngày trong tháng
        Calendar calendar = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthValues+1);
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HoaDon");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hashMap.clear();
                for (int i = 1; i <= numberOfDaysInMonth; i++) {
                    hashMap.put(i , 0);
                }
                listHoaDon.clear();
                listTime.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("TaiBan").getChildren()) {
                    Boolean thanhToan = dataSnapshot.child("daThanhToan").getValue(Boolean.class);
                    if (thanhToan) {
                        HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                        listHoaDon.add(hoaDon);
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.child("MangVe").getChildren()) {
                    Boolean thanhToan = dataSnapshot.child("daThanhToan").getValue(Boolean.class);
                    if (thanhToan) {
                        HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                        listTime.add(hoaDon);
                    }
                }
                // Tính tổng tiền
                thongke = Thongke(listHoaDon);
                listNgay.clear();
                listDouble.clear();
                for (Map.Entry<String, HoaDon> _TB : thongke.entrySet()) {
                    if (!listTime.isEmpty()) {
                        thongke_MangVe = Thongke(listTime);
                        for (Map.Entry<String, HoaDon> _MV : thongke_MangVe.entrySet()) {
                            if (_TB.getValue().getNgayThanhToan() != null && _MV.getValue().getNgayThanhToan() != null) {
                                if (_TB.getValue().getNgayThanhToan().equals(_MV.getValue().getNgayThanhToan())) {
                                    Double _id_Tong = _TB.getValue().getTongTien() + _MV.getValue().getTongTien() + _TB.getValue().getTongTien();
                                    String _ngay_TT = _TB.getValue().getNgayThanhToan();
                                    listNgay.add(_ngay_TT);
                                    listDouble.add(_id_Tong);
                                } else {
                                    Double _id_Tong = _TB.getValue().getTongTien();
                                    String _ngay_TT = _TB.getValue().getNgayThanhToan();
                                    listNgay.add(_ngay_TT);
                                    listDouble.add(_id_Tong);
                                }
                            }
                        }
                    } else {
                        Double _id_Tong = _TB.getValue().getTongTien();
                        String _ngay_TT = _TB.getValue().getNgayThanhToan();
                        listNgay.add(_ngay_TT);
                        listDouble.add(_id_Tong);
                    }
                }
                for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
                    Integer i = entry.getKey();
                    for (int j = 0; j < listNgay.size(); j++) {
                        String a = listNgay.get(j);
                        String[] values = a.split("-");
                        int value = Integer.parseInt(values[2]);
                        int month = Integer.parseInt(values[1]);
                        if (month == monthValues) {
                            if (i == value) {
                                if (j < listDouble.size()) {
                                    entry.setValue(listDouble.get(j).intValue());
                                }
                            }
                        }
                    }
                }
                Integer count = 0;
                for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
                    Integer value = entry.getValue();
                    count += value;
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    String formattedNumber = decimalFormat.format(count);
                    tvDoanhThu.setText(String.valueOf(formattedNumber)+" đ");
                }
                createTable(hashMap);
                setEvent(hashMap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private Map<String, HoaDon> Thongke(List<HoaDon> id) {
        Map<String, HoaDon> haskMap = new HashMap<>();
        for (HoaDon id_HD : id) {
            String ngayThanhToan = id_HD.getNgayThanhToan();
            if (haskMap.containsKey(ngayThanhToan)) {
                Double tongTien = haskMap.get(ngayThanhToan).getTongTien();
                id_HD.setTongTien(tongTien + id_HD.getTongTien());
                haskMap.put(ngayThanhToan, id_HD);
            } else {
                haskMap.put(ngayThanhToan, id_HD);
            }
        }
        return haskMap;
    }
    private void insertTitleTable(){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        tableRow.setWeightSum(2);
        // Tạo cột
        // Cột 1
        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1

        ));

        textView1.setTextSize(20);
        textView1.setPadding(0,10,0,10);
        textView1.setTextColor(Color.BLACK);
        textView1.setGravity(Gravity.CENTER);
        textView1.setBackgroundResource(R.drawable.table_border_title);
        textView1.setText("Ngày");
        // Thêm TextView vào TableRow
        tableRow.addView(textView1);
        // Cột 2
        TextView textView2 = new TextView(this);
        textView2.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1

        ));
        textView2.setPadding(0,10,0,10);
        textView2.setTextSize(20);
        textView2.setTextColor(Color.BLACK);
        textView2.setGravity(Gravity.CENTER);
        textView2.setBackgroundResource(R.drawable.table_border_title);
        textView2.setText("Danh Thu");
        // Thêm TextView vào TableRow
        tableRow.addView(textView2);
        // Cột 3
        TextView textView3 = new TextView(this);
        textView3.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1

        ));
        // Thêm TableRow vào TableLayout
        tbLayout.addView(tableRow);
    }

    private void createTable(@NonNull Map<Integer, Integer> map) {
        tbLayout.removeAllViews();
        insertTitleTable();
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
            tvThang.setGravity(Gravity.CENTER_HORIZONTAL);
            tvThang.setBackgroundResource(R.drawable.table_border);
            tvThang.setText("Ngày " + entry.getKey());
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
            NumberFormat nf = NumberFormat.getNumberInstance();
            tvSoHoaDon.setText(nf.format(entry.getValue()) + "đ");
            // Thêm TextView vào TableRow
            tableRow.addView(tvSoHoaDon);

            // Thêm TableRow vào TableLayout
            tbLayout.addView(tableRow);
        }
    }

    private void setCtrol() {
        tvDoanhThu = findViewById(R.id.tvDoanhThu);
        btnChart = findViewById(R.id.btnChart);
        tbLayout = findViewById(R.id.tbLayout);
        toolbar = findViewById(R.id.toolBar);
        spinner = findViewById(R.id.spNgay);
    }
}