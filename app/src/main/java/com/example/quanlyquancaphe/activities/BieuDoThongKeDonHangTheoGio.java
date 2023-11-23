package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.quanlyquancaphe.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ThongKeDoanhThuTheoThangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongkedoanhthutheothang_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LineChart chart = findViewById(R.id.linechart);

        List<Entry> entriesMonth1 = new ArrayList<>();
        for (int i = 5; i < 25; i++) {
            entriesMonth1.add(new Entry(i,150+i));
        }

        // Add more entries for Month 1

        LineDataSet dataSet1 = new LineDataSet(entriesMonth1, "Month 1");
        dataSet1.setColor(Color.BLUE);
        dataSet1.setValueTextColor(Color.BLUE);

        LineData lineData = new LineData(dataSet1);

        chart.setData(lineData);
        chart.getData().setValueTextSize(12f);
        chart.getDescription().setText("Revenue Comparison");
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Day " + (int) value;
            }
        });
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setForm(Legend.LegendForm.LINE);
        chart.invalidate();
    }
}