package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BieuDoThongKeDoanhThuTheoNgay_Activity extends AppCompatActivity {
    Bundle bundle;
    Toolbar toolbar;
    ArrayList<Integer> key = new ArrayList<>();
    ArrayList<Integer> value = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_bieudothongkedoanhthu_theothang_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Biểu đồ thống kê doanh thu");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bundle = getIntent().getExtras();
        lineChart();
    }

    public void lineChart() {
        LineChart lineChart = findViewById(R.id.lineChart);
        ArrayList<Entry> Chart = new ArrayList<>();
        if (bundle != null) {
            key = bundle.getIntegerArrayList("Key");
            value = bundle.getIntegerArrayList("value");
            // Chart.add(new Entry(id ,values.floatValue()));
        }
        for (int i = 0; i < key.size(); i++) {
            System.out.println(value);
            Chart.add(new Entry(key.get(i), value.get(i).floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(Chart, "Biểu đồ thống kê danh thu từng tháng ");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setAxisMinimum(0f);
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

}