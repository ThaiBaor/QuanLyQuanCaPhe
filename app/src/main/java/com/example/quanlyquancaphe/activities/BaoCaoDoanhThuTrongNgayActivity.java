package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.LoaiMon;
import com.example.quanlyquancaphe.models.Mon;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BaoCaoDoanhThuTrongNgayActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText edtNgay;
    TextView tvDoanhSo, tvDoanhThu;
    ImageButton btnChart;
    TableLayout tbLayout;
    DatabaseReference reference;
    FirebaseDatabase database;
    SwipeRefreshLayout refresh;
    ArrayList<ChiTietMon> chiTietMons = new ArrayList<>();
    Map<String, ChiTietMon> thongKe = new HashMap<>();
    ArrayList<Mon> dataMon = new ArrayList<>();
    ArrayList<LoaiMon> dataLoaiMon = new ArrayList<>();
    Map<String, Double> bieuDo = new HashMap<>();
    ScrollView scrollView;
    int doanhSo;
    double doanhThu;
    ArrayList<String> arrLoaiMon = new ArrayList<>();
    ArrayList<Double> arrDoanhThu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_baocaodoanhthutrongngay_layout);
        setControl();
        setEvent();
        getAllChiTietMon(edtNgay.getText().toString());
    }

    private void setEvent() {
        //Set title toolbar
        toolbar.setTitle("Báo cáo bán hàng");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bieuDo.clear();
                bieuDo = BieuDo(thongKe, dataMon, dataLoaiMon);
                if (bieuDo.size() != 0) {
                    Intent intent = new Intent(BaoCaoDoanhThuTrongNgayActivity.this, BieuDoDoanhThuTrongNgayActivity.class);
                    // Duyệt map. Chuyển key, value thành 2 arraylist
                    for (Map.Entry<String, Double> entry : bieuDo.entrySet()) {
                        arrLoaiMon.add(entry.getKey());
                        arrDoanhThu.add(entry.getValue());
                    }
                    for (String item: arrLoaiMon){
                        System.out.println(item.toString());
                    }
                    // Truyền 2 arraylist sang charActivity
                    intent.putExtra("arrLoaiMon", arrLoaiMon);
                    intent.putExtra("arrDoanhThu", arrDoanhThu);
                    startActivity(intent);
                    arrLoaiMon.clear();
                    arrDoanhThu.clear();
                } else {
                    Toast.makeText(BaoCaoDoanhThuTrongNgayActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refeshTable();
            }
        });
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Tắt sự kiện refresh
                refresh.setEnabled(false);
                // Khi có sự kiện cuộn, kiểm tra xem ScrollView có ở đầu trang hay không, nếu có, cho phép refresh
                if (scrollView.getScrollY() == 0) {
                    refresh.setEnabled(true);
                }
                return false;
            }
        });
    }

    //Hàm chọn ngày
    private void ChonNgay() {
        //Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //Set lại ngày được chọn
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                edtNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    // Hàm tạo tableRow
    private void inSertTableRow(Map<String, ChiTietMon> ctm) {
        // Tạo hàng
        for (Map.Entry<String, ChiTietMon> entry : ctm.entrySet()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            tableRow.setWeightSum(3);
            // Tạo cột
            // Cột 1
            TextView tvTenSP = new TextView(this);
            tvTenSP.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1

            ));
            tvTenSP.setPadding(50, 5, 5, 5);
            tvTenSP.setTextSize(20);
            tvTenSP.setTextColor(Color.BLACK);
            tvTenSP.setGravity(Gravity.LEFT);
            tvTenSP.setBackgroundResource(R.drawable.table_border);
            tvTenSP.setText(entry.getValue().getTenMon());
            // Thêm TextView vào TableRow
            tableRow.addView(tvTenSP);
            // Cột 2
            TextView tvDoanhSo = new TextView(this);
            tvDoanhSo.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1

            ));
            tvDoanhSo.setPadding(5, 5, 5, 5);
            tvDoanhSo.setTextSize(20);
            tvDoanhSo.setTextColor(Color.BLACK);
            tvDoanhSo.setGravity(Gravity.CENTER);
            tvDoanhSo.setBackgroundResource(R.drawable.table_border);
            tvDoanhSo.setText(String.valueOf(entry.getValue().getSl()));
            // Thêm TextView vào TableRow
            tableRow.addView(tvDoanhSo);
            // Cột 3
            TextView tvDoanhThu = new TextView(this);
            tvDoanhThu.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1

            ));
            tvDoanhThu.setPadding(5, 5, 5, 5);
            tvDoanhThu.setTextSize(20);
            tvDoanhThu.setTextColor(Color.BLACK);
            tvDoanhThu.setGravity(Gravity.CENTER);
            tvDoanhThu.setBackgroundResource(R.drawable.table_border);
            NumberFormat nf = NumberFormat.getNumberInstance();
            tvDoanhThu.setText(nf.format(entry.getValue().getSl() * entry.getValue().getGia()) + "đ");
            // Thêm TextView vào TableRow
            tableRow.addView(tvDoanhThu);
            // Thêm TableRow vào TableLayout
            tbLayout.addView(tableRow);
        }
    }

    long count_ = 0;
    long count = 0;

    private void getAllChiTietMon(String date) {
        // set lại dữ liệu trước khi chạy
        AlertDialog.Builder builder = new AlertDialog.Builder(BaoCaoDoanhThuTrongNgayActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        //Lấy ngày hiện tại
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date.toLowerCase().equals("hôm nay")) {
            Calendar calendar = Calendar.getInstance();
            date = simpleDateFormat.format(calendar.getTime());
        }
        // Lấy key: mã bàn, tên khách hàng
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("ChiTietMon");
        String finalDate = date;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy chi tiết món tại key vừa lấy được
                for (DataSnapshot item : snapshot.getChildren()) {
                    String id_Ban = item.getKey();
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference().child("ChiTietMon").child(id_Ban).child("QK");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dialog.dismiss();
                            count++;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot items : dataSnapshot.getChildren()) {
                                    for (DataSnapshot item_ctm : items.getChildren()){
                                        ChiTietMon _chiTietMon = item_ctm.getValue(ChiTietMon.class);
                                        if (finalDate.toLowerCase().equals(_chiTietMon.getNgayGoiMon())) {
                                            // lấy dữ liệu add vào arraylist
                                            chiTietMons.add(_chiTietMon);
                                        }
                                    }
                                }
                            }
                            // chỉ lấy dữ liệu chi tiết món lần lặp cuối cùng
                            if (count == count_) {
                                thongKe = ThongKe(chiTietMons);
                                // gọi hàm tạo table row
                                inSertTableRow(thongKe);
                                // duyệt map
                                for (Map.Entry<String, ChiTietMon> entry : thongKe.entrySet()) {
                                    doanhSo += entry.getValue().getSl();
                                    doanhThu += entry.getValue().getGia() * entry.getValue().getSl();
                                }
                                System.out.println("Doanh so: " + doanhSo + "|" + "doanh thu" + doanhThu);
                                // settext tại text view
                                if (thongKe.size() == 0) {
                                    tvDoanhSo.setText(" 0 món");
                                    tvDoanhThu.setText("0 đ");
                                } else {
                                    NumberFormat nf = NumberFormat.getNumberInstance();
                                    tvDoanhSo.setText(doanhSo + " món");
                                    tvDoanhThu.setText(nf.format(doanhThu) + "đ");
                                }
                           }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    ++count_;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // lấy danh sách món, trong danh sách món chứa Id loại món. Dùng để thống kê theo loại món
        getDataMon();
        getDataLoaiMon();
    }

    // Hàm chuyển arraylist thành map
    private Map<String, ChiTietMon> ThongKe(ArrayList<ChiTietMon> ctm) {
        Map<String, ChiTietMon> hashMap = new HashMap<>();
        for (ChiTietMon item : ctm) {
            String id_Mon = item.getId_Mon();
            // nếu trùng id, tăng số lượng
            if (hashMap.containsKey(id_Mon)) {
                int sl = hashMap.get(id_Mon).getSl();
                item.setSl(sl + item.getSl());
                hashMap.put(id_Mon, item);
            }
            // không trùng
            else {
                hashMap.put(id_Mon, item);
            }
        }
        return hashMap;
    }

    //Hàm lọc chi tiết món, nếu trùng loại món thì cộng dồn số lượng
    private Map<String, Double> BieuDo(Map<String, ChiTietMon> map, ArrayList<Mon> mon, ArrayList<LoaiMon> loaiMon){
        Map<String, Double> hashMap = new HashMap<>();
        //Duyệt chi tiết món
        for (Map.Entry<String, ChiTietMon> entry : map.entrySet()) {
            // Duyệt món
            for (Mon item_mon : mon) {
                if (entry.getKey().equals(item_mon.getId_Mon())) {
                    // Duyệt loại món
                    for (LoaiMon item_loaiMon : loaiMon) {
                        if (item_mon.getId_Loai().equals(item_loaiMon.getId_loai())) {
                            // nếu trùng loại, cộng dồn doanh thu
                            if (hashMap.containsKey(item_loaiMon.getTen_loai())) {
                                double sl = hashMap.get(item_loaiMon.getTen_loai());
                                hashMap.put(item_loaiMon.getTen_loai(), sl + entry.getValue().getSl() * entry.getValue().getGia());
                            }
                            // không trùng
                            else {
                                hashMap.put(item_loaiMon.getTen_loai(), entry.getValue().getSl() * entry.getValue().getGia());
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    //Hàm xóa table row
    private void refeshTable() {
        // set lại dữ liệu trước khi refresh
        count = 0;
        count_ = 0;
        doanhSo = 0;
        doanhThu = 0;
        arrLoaiMon.clear();
        arrDoanhThu.clear();
        chiTietMons.clear();
        thongKe.clear();
        bieuDo.clear();
        int rowCount = tbLayout.getChildCount();
        //Xóa toàn bộ table row chỉ giữ lại hàng tiêu đề
        for (int i = rowCount - 1; i > 0; i--) {
            View row = tbLayout.getChildAt(i);
            tbLayout.removeView(row);
        }
        //load lại data
        getAllChiTietMon(edtNgay.getText().toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000);
    }

    private void getDataMon() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Mon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataMon.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Mon mon = item.getValue(Mon.class);
                    dataMon.add(mon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getDataLoaiMon() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("LoaiMon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataLoaiMon.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    LoaiMon loaiMon = item.getValue(LoaiMon.class);
                    dataLoaiMon.add(loaiMon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolBar);
        btnChart = findViewById(R.id.btnChart);
        tbLayout = findViewById(R.id.tbLayout);
        edtNgay = findViewById(R.id.edtNgay);
        tvDoanhSo = findViewById(R.id.tvDoanhSo);
        tvDoanhThu = findViewById(R.id.tvDoanhThu);
        refresh = findViewById(R.id.refresh);
        scrollView = findViewById(R.id.scrollView);
    }
}