package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ThongKeHoaDon;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BaoCaoDoanhThuThangActivity extends AppCompatActivity {

    TextView tvSlHoaDon, tvDoanhThu, tvTitle;
    TableLayout tbLayout;
    ArrayList<ThongKeHoaDon> data = new ArrayList<>();
    ArrayList<ThongKeHoaDon> dataFilter = new ArrayList<>();
    ArrayList<Double> dataDoanhThu = new ArrayList<>();
    ImageButton btnChart;
    Toolbar toolBar;
    ArrayList<String> dataNam = new ArrayList<>();
    Spinner spNam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_baocaodoanhthuthang_layout);
        setControl();
        setDataSPNam();
        getDataHoaDonTaiBanVaMangVe();
        spNam.setSelection(10);
    }

    private void setEvent() {
        toolBar.setTitle("Báo cáo doanh thu tháng");
        tbLayout.removeAllViews();
        insertTitleTable();
        inSertTableRow("1");
        inSertTableRow("2");
        inSertTableRow("3");
        inSertTableRow("4");
        inSertTableRow("5");
        inSertTableRow("6");
        inSertTableRow("7");
        inSertTableRow("8");
        inSertTableRow("9");
        inSertTableRow("10");
        inSertTableRow("11");
        inSertTableRow("12");
        tvTitle.setText("Doanh thu trong năm " + spNam.getSelectedItem());
        NumberFormat nf = NumberFormat.getNumberInstance();
        tvSlHoaDon.setText(dataFilter.size() + "");
        tvDoanhThu.setText(nf.format(getTongDoanhThu()) + "đ");
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaoCaoDoanhThuThangActivity.this, BieuDoThongKeHoaDonThang.class);
                for (int i = 0; i < 12 ; i++){
                    dataDoanhThu.add(filterDoanhThuHoaDon((i + 1) + ""));
                }
                intent.putExtra("arrDoanhThu", dataDoanhThu);
                startActivity(intent);
                dataDoanhThu.clear();
            }
        });
        spNam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterDataTheoNam(spNam.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void getDataHoaDonTaiBanVaMangVe(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("HoaDon").child("TaiBan");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                xoaDataMangVe();
                for (DataSnapshot item: snapshot.getChildren()){
                    String id_MaHoaDon = item.child("id_HoaDon").getValue().toString();
                    String id_Ban = item.child("id_Ban").getValue().toString();
                    String thoiGian_thanhtoan = item.child("thoiGian_ThanhToan").getValue().toString();
                    String ngayThanhToan = item.child("ngayThanhToan").getValue().toString();
                    Double tongTien = Double.parseDouble(item.child("tongTien").getValue().toString());
                    Boolean daThanhToan = Boolean.parseBoolean(item.child("daThanhToan").getValue().toString());
                    String tenKhachHang = "Không có";
                    ThongKeHoaDon thongKeHoaDon = new ThongKeHoaDon(id_MaHoaDon, id_Ban,ngayThanhToan, thoiGian_thanhtoan, tongTien, daThanhToan, tenKhachHang);
                    data.add(thongKeHoaDon);
                }
                getDataHoaDonMangVe();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDataHoaDonMangVe(){
        FirebaseDatabase firebaseDatabaseMangVe = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceMangVe = firebaseDatabaseMangVe.getReference().child("HoaDon").child("MangVe");
        databaseReferenceMangVe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                xoaDataMangVe();
                for (DataSnapshot item: snapshot.getChildren()){
                    String id_MaHoaDon = item.child("id_HoaDon").getValue().toString();
                    String id_Ban = "Mang về";
                    String thoiGian_thanhtoan = item.child("thoiGian_ThanhToan").getValue().toString();
                    String ngayThanhToan = item.child("ngayThanhToan").getValue().toString();
                    Double tongTien = Double.parseDouble(item.child("tongTien").getValue().toString());
                    Boolean daThanhToan = Boolean.parseBoolean(item.child("daThanhToan").getValue().toString());
                    String tenKhachHang = item.child("tenKH").getValue().toString();
                    ThongKeHoaDon thongKeHoaDon = new ThongKeHoaDon(id_MaHoaDon, id_Ban,ngayThanhToan, thoiGian_thanhtoan, tongTien, daThanhToan, tenKhachHang);
                    data.add(thongKeHoaDon);
                }
                Calendar calendar = Calendar.getInstance();
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                filterDataTheoNam(year);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BaoCaoDoanhThuThangActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void filterDataTheoNam(String year){
        dataFilter.clear();
        for(ThongKeHoaDon thongKeHoaDon : data){
           if (thongKeHoaDon.getThoiGian_thanhtoan().contains(year)){
               dataFilter.add(thongKeHoaDon);
           }
        }
        setEvent();
    }
    private String filterSLHoaDon(String thang){
        String str = "0";
        int soLuong = 0;
        for (ThongKeHoaDon thongKeHoaDon : dataFilter){
            String[] split = thongKeHoaDon.getThoiGian_thanhtoan().split("-");
            if (split[1].equals(thang)){
                soLuong++;
            }
        }
        str = String.valueOf(soLuong);
        return str;
    }
    private Double filterDoanhThuHoaDon(String thang){
        Double tong = Double.valueOf(0);
        for (ThongKeHoaDon thongKeHoaDon : dataFilter){
            String[] split = thongKeHoaDon.getThoiGian_thanhtoan().split("-");
            if (split[1].equals(thang)){
                tong += thongKeHoaDon.getTongTien();
            }
        }
        return tong;
    }
    private void xoaDataMangVe(){
        if (data.isEmpty() == false){
            for (int i = 0; i < data.size(); i++){
                if (data.get(i).getId_Ban().equals("Mang về")){
                    data.remove(i);
                    i = 0;
                }
            }
        }
    }

    private Double getTongDoanhThu(){
        Double tong = Double.valueOf(0);
        for(ThongKeHoaDon thongKeHoaDon : dataFilter){
            tong += thongKeHoaDon.getTongTien();
        }
        return tong;
    }
    private void insertTitleTable(){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        tableRow.setWeightSum(3);
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
        textView1.setText("Tháng");
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
        textView2.setText("Số hóa đơn");
        // Thêm TextView vào TableRow
        tableRow.addView(textView2);
        // Cột 3
        TextView textView3 = new TextView(this);
        textView3.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1

        ));
        textView3.setPadding(0,10,0,10);
        textView3.setTextSize(20);
        textView3.setTextColor(Color.BLACK);
        textView3.setGravity(Gravity.CENTER);
        textView3.setBackgroundResource(R.drawable.table_border_title);
        NumberFormat nf = NumberFormat.getNumberInstance();
        textView3.setText("Doanh thu");
        // Thêm TextView vào TableRow
        tableRow.addView(textView3);
        // Thêm TableRow vào TableLayout
        tbLayout.addView(tableRow);
    }
    private void inSertTableRow(String thang) {
        // Tạo hàng
        //for (int i = 0 ; i < 12 ; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            tableRow.setWeightSum(3);
            // Tạo cột
            // Cột 1
            TextView textView1 = new TextView(this);
            textView1.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1

            ));
            textView1.setPadding(5, 5, 5, 5);
            textView1.setTextSize(20);
            textView1.setTextColor(Color.BLACK);
            textView1.setGravity(Gravity.CENTER);
            textView1.setBackgroundResource(R.drawable.table_border);
            textView1.setText("Tháng " + thang);
            // Thêm TextView vào TableRow
            tableRow.addView(textView1);
            // Cột 2
            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1

            ));
            textView2.setPadding(5, 5, 5, 5);
            textView2.setTextSize(20);
            textView2.setTextColor(Color.BLACK);
            textView2.setGravity(Gravity.CENTER);
            textView2.setBackgroundResource(R.drawable.table_border);
            textView2.setText(filterSLHoaDon(thang));
            // Thêm TextView vào TableRow
            tableRow.addView(textView2);
            // Cột 3
            TextView textView3 = new TextView(this);
            textView3.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1

            ));
            textView3.setPadding(5, 5, 5, 5);
            textView3.setTextSize(20);
            textView3.setTextColor(Color.BLACK);
            textView3.setGravity(Gravity.CENTER);
            textView3.setBackgroundResource(R.drawable.table_border);
            NumberFormat nf = NumberFormat.getNumberInstance();
            textView3.setText(nf.format(filterDoanhThuHoaDon(thang)) + "đ");
            // Thêm TextView vào TableRow
            tableRow.addView(textView3);
            // Thêm TableRow vào TableLayout
            tbLayout.addView(tableRow);
        }
    //}

    private void setDataSPNam(){
        Calendar calendar = Calendar.getInstance();
        int nam = calendar.get(Calendar.YEAR);
        for (int i = nam - 10; i <= nam+10; i++){
            dataNam.add(i + "");
        }
        ArrayAdapter adapter = new ArrayAdapter(BaoCaoDoanhThuThangActivity.this, android.R.layout.simple_spinner_item, dataNam);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNam.setAdapter(adapter);
    }
    private void setControl() {
        tbLayout = findViewById(R.id.tbLayout);
        tvSlHoaDon = findViewById(R.id.tvSLHoaDon);
        tvTitle = findViewById(R.id.tvTitle);
        tvDoanhThu = findViewById(R.id.tvDoanhThu);
        btnChart = findViewById(R.id.btnChart);
        spNam = findViewById(R.id.spNam);
        toolBar = findViewById(R.id.toolBar);
    }
}