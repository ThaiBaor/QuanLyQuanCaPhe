package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.ThongKeHoaDonAdapter;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDonKhachHang;
import com.example.quanlyquancaphe.models.ThongKeHoaDon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class BaoCaoDoanhThuThang extends AppCompatActivity {

    TextView tvSlHoaDon, tvDoanhThu;
    TableLayout tbLayout;
    ArrayList<ThongKeHoaDon> data = new ArrayList<>();
    ArrayList<ThongKeHoaDon> dataFilter = new ArrayList<>();

    ThongKeHoaDonAdapter thongKeHoaDonAdapter;

    //ArrayList<Double> dataDoanhThu = new ArrayList<>(new double[0.0]);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_hing_bao_cao_doanh_thu_thang_layout);
        getDataHoaDonTaiBanVaMangVe();
        setControl();
    }

    private void setEvent() {
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

        tvSlHoaDon.setText(dataFilter.size() + "");
        tvDoanhThu.setText(getTongDoanhThu() + "");
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
                filterDataTheoNam();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BaoCaoDoanhThuThang.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void filterDataTheoNam(){
        dataFilter.clear();
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
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

    private void setControl() {
        tbLayout = findViewById(R.id.tbLayout);
        tvSlHoaDon = findViewById(R.id.tvSLHoaDon);
        tvDoanhThu = findViewById(R.id.tvDoanhThu);
    }
}