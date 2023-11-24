package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.HoaDon;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ThongKeDoanhThuTheoNgay_Activity extends AppCompatActivity {
    List<String> listNgay = new ArrayList<>();
    List<Double> listDouble = new ArrayList<>();
    List<HoaDon> listTime = new ArrayList<>();
    List<HoaDon> listHoaDon = new ArrayList<>();
    Map<String, HoaDon> thongke = new HashMap<>();
    Map<String, HoaDon> thongke_MangVe = new HashMap<>();
    HashMap<Integer, Integer> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongkedoanhthu_theothang_layout);
        //locNgayTrung();
    }
    private void locNgayTrung() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HoaDon");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < 30; i++) {
                    hashMap.put(i, 0);
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
                lineChart(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void lineChart(HashMap<Integer, Integer> getData) {
        LineChart lineChart = findViewById(R.id.lineChart);
        ArrayList<Entry> Chart = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : getData.entrySet()) {
            Integer id = entry.getKey();
            Integer values = entry.getValue();
            System.out.println( id + "/"+ values);
           Chart.add(new Entry(id ,values.floatValue()));
        }
        LineDataSet dataSet = new LineDataSet(Chart, "Biểu đồ thống kê danh thu từng tháng ");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        lineChart.invalidate();
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

}