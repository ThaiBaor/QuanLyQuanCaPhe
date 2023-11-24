package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.HoaDon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDoanhThuTheoNgay_Activity extends AppCompatActivity {
    TextView tvNgay, tvDoanhThu;
    ImageButton btnChart;
    TableLayout tbLayout;
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
    // Lấy số ngày trong tháng hiện tại
    int daysInCurrentMonth = currentDate.lengthOfMonth();
    Integer today = daysInCurrentMonth;
    float count = 0 ;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thong_ke_doanh_thu_theo_ngay);
        setCtrol();
        toolbar.setTitle("Thống kê doanh thu theo tháng");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tongDoanhSoTheoNgay(today);
    }

    private void setEvent(HashMap<Integer, Integer> map) {
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThongKeDoanhThuTheoNgay_Activity.this, BieuDoThongKeDoanhThuTheoNgay_Activity.class);
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

    private void tongDoanhSoTheoNgay(Integer today) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HoaDon");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < today; i++) {
                    hashMap.put(i + 1, 0);
                }
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
                thongke = Thongke(listHoaDon);
                for (Map.Entry<String, HoaDon> _TB : thongke.entrySet()) {
                    //
                    if (!listTime.isEmpty()) {
                        thongke_MangVe = Thongke(listTime);
                        for (Map.Entry<String, HoaDon> _MV : thongke_MangVe.entrySet()) {
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
                        if (i == value) {
                            if (j < listDouble.size()) {
                                entry.setValue(listDouble.get(j).intValue());
                            }
                        }
                    }
                }
                //Tính tổng doanh thu
                for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
                 float getValues = entry.getValue().floatValue();
                 count+= getValues;
                  tvDoanhThu.setText(String.valueOf(count)+"đ");
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

    private void createTable(@NonNull Map<Integer, Integer> map) {
        tbLayout.removeAllViews();
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
            tvSoHoaDon.setText(entry.getValue() + "");
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
    }
}