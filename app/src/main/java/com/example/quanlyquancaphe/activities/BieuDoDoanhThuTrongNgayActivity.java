package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BieuDoDoanhThuTrongNgayActivity extends AppCompatActivity {
    PieChart pieChart;
    ArrayList<PieEntry> data = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_bieudodoanhthutrongngay_layout);
        setControl();
        setEvent();
    }
    private void setEvent() {
        // Nhận dữ liệu
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //toolbar
        toolbar.setTitle("Tỉ trọng doanh thu theo loại món");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Chart
        if(bundle != null){
            // Gán dữ liệu vừa nhận vào arraylist
            ArrayList<String> arrTenMon = bundle.getStringArrayList("arrLoaiMon");
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
            pieDataSet.setFormSize(25f);
            pieChart.setHoleRadius(20f); // Đường kính của lỗ ở giữa biểu đồ
            pieChart.setTransparentCircleRadius(40f); // Đường kính của vùng trong suốt xung quanh biểu đồ
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.animateY(1000, Easing.EaseInOutQuad);
            pieChart.invalidate();
        }
    }
    // tạo hashmap từ 2 array list vừa nhận được
    private Map<String, Double> createMap(ArrayList<String> arrLoaiMon, ArrayList<Double> arrDoanhThu){
        Map<String, Double> hashMap = new HashMap<>();
        for (int i = 0; i<arrDoanhThu.size(); i++){

        }
        return hashMap;
    }

    private void setControl() {
        pieChart = findViewById(R.id.chart);
        toolbar = findViewById(R.id.toolBar);
    }
}