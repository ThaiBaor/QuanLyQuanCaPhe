package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {
    PieChart pieChart;
    ArrayList<PieEntry> data = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_chart_layout);
        setControl();
        setEvent();
    }

    private void setEvent() {
        // Nhận dữ liệu
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //toolbar
        toolbar.setTitle("Tỉ trọng doanh thu trong ngày");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Chart
        if(bundle != null){
            // Gán dữ liệu vừa nhận vào arraylist
            ArrayList<String> arrTenMon = bundle.getStringArrayList("arrTenMon");
            ArrayList<Double> arrDoanhThu = (ArrayList<Double>)intent.getSerializableExtra("arrDoanhThu");
            // Duyệt array list, khỏi gán giá trị cho biểu đồ
            data.clear();
            pieChart.clear();
            for(int i = 0; i< arrTenMon.size(); i ++){
                data.add(new PieEntry(arrDoanhThu.get(i).intValue(), arrTenMon.get(i)));
            }
            PieDataSet pieDataSet = new PieDataSet(data, "");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieDataSet.setValueTextSize(20f);
            pieChart.setHoleRadius(20f); // Đường kính của lỗ ở giữa biểu đồ
            pieChart.setTransparentCircleRadius(40f); // Đường kính của vùng trong suốt xung quanh biểu đồ
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.animateY(1000, Easing.EaseInOutQuad);
            pieChart.invalidate();
        }
    }

    private void setControl() {
        pieChart = findViewById(R.id.chart);
        toolbar = findViewById(R.id.toolBar);
    }
}