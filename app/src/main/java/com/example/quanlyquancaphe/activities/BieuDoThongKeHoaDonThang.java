package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.quanlyquancaphe.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class BieuDoThongKeHoaDonThang extends AppCompatActivity {
    BarChart barChart;

    TextView test;

    ArrayList<BarEntry> data = new ArrayList<>();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_bieudothongkehoadonthang);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Intent intent = getIntent();
        List<String> labels = new ArrayList<>();
        ArrayList<Double> arrDoanhThu = (ArrayList<Double>)intent.getSerializableExtra("arrDoanhThu");
        barChart.clear();
        for(int i = 0; i< arrDoanhThu.size(); i ++){
            data.add(new BarEntry(i, arrDoanhThu.get(i).intValue()));
        }
        for (int i = 0; i < 12; i++){
            labels.add("Tháng " + (i + 1));
        }
        BarDataSet barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextSize(20f);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000, Easing.EaseInOutQuad);
        barChart.invalidate();

        toolbar.setTitle("Biểu đồ");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        barChart = findViewById(R.id.bieuDoHoaDonThang);
        toolbar = findViewById(R.id.toolBar);
    }
}