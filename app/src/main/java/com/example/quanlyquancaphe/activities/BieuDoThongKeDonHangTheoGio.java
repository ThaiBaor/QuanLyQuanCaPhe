package com.example.quanlyquancaphe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.quanlyquancaphe.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class BieuDoThongKeDonHangTheoGio extends AppCompatActivity {
    LineChart lineChart;
    Toolbar toolBar;
    ArrayList<Entry> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_bieudothongkedonhangtheogio_layout);
        setControl();
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            data.clear();
            // Gán dữ liệu vừa nhận vào arraylist
            ArrayList<Integer> listGio = bundle.getIntegerArrayList("listGio");
            ArrayList<Integer> listSoHoaDon = bundle.getIntegerArrayList("listSoHoaDon");
            // Duyệt array list, khỏi gán giá trị cho biểu đồ
            for (int i = 0; i < listGio.size(); i++) {
                data.add(new Entry(listGio.get(i), listSoHoaDon.get(i)));
            }
            // Tạo LineDataSet
            LineDataSet lineDataSet = new LineDataSet(data, null);
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setValueTextColor(Color.BLACK);
            // Format dữ liệu
            lineDataSet.setValueFormatter(new DefaultValueFormatter(0));
            //Tạo LineData
            LineData lineData = new LineData(lineDataSet);
            // Gán DataSet cho LineChart
            lineChart.setData(lineData);
            lineChart.getData().setValueTextSize(15f);
            lineChart.getDescription().setText("");
            lineChart.getDescription().setTextSize(14f);
            // Set vị trí trục X nằm bên dưới
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            // Format giá trị trục X
            lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return (int) value + "h";
                }
            });
            // Tắt trục Y nằm bên phải
            lineChart.getAxisRight().setEnabled(false);
            // Set giá trị tối thiểu của trục Y là 0
            lineChart.getAxisLeft().setAxisMinimum(0f);
            lineChart.getLegend().setForm(Legend.LegendForm.LINE);
            lineChart.invalidate();
        }

    }

    private void setControl() {
        lineChart = findViewById(R.id.lineChart);
        toolBar = findViewById(R.id.toolBar);
    }
}