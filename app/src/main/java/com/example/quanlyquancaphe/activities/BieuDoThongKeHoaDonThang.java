package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.quanlyquancaphe.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BieuDoThongKeHoaDonThang extends AppCompatActivity {
    BarChart barChart;

    TextView test;


    ArrayList<BarEntry> data = new ArrayList<>();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_hinh_bieu_do_thong_ke_hoa_don_thang_layout);
        setControl();
        setEvent();
    }

    private void setEvent() {
        //ArrayList<String> arrTenMon = bundle.getStringArrayList("arrTenMon");
        //ArrayList<Double> arrDoanhThu = (ArrayList<Double>)intent.getSerializableExtra("arrDoanhThu");
        // Duyệt array list, khỏi gán giá trị cho biểu đồ
        barChart.clear();
        //for(int i = 0; i< arrTenMon.size(); i ++){
            //data.add(new PieEntry(arrDoanhThu.get(i).intValue(), arrTenMon.get(i)));
        //}
        data.add(new BarEntry(0, 10));
        data.add(new BarEntry(1, 10));
        data.add(new BarEntry(2, 10));
        data.add(new BarEntry(3, 10));
        data.add(new BarEntry(4, 10));
        BarDataSet barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextSize(20f);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000, Easing.EaseInOutQuad);
        barChart.invalidate();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barChart.clear();
            }
        });
    }

    private void setControl() {
        barChart = findViewById(R.id.bieuDoHoaDonThang);
        toolbar = findViewById(R.id.toolBar);
        test = findViewById(R.id.tvTest);
    }
}