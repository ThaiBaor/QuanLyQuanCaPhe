package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BaoCaoBanHangActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    EditText edtNgay;
    TextView tvDoanhSo, tvDoanhThu;
    ImageButton btnChart;
    TableLayout tbLayout;
    DatabaseReference reference;
    FirebaseDatabase database;
    ArrayList<ChiTietMon> chiTietMons = new ArrayList<>();
    Map<String, ChiTietMon> thongKe = new HashMap<>();
    int doanhSo;
    double doanhThu;
    ArrayList<String> arrTenMon = new ArrayList<>();
    ArrayList<Double> arrDoanhThu = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_baocaobanhang);
        setControl();
        setdrawer();
        setEvent();
        getAllChiTietMon();
    }

    private void setEvent() {
        //Set title toolbar
        toolbar.setTitle("Báo cáo bán hàng");
        toolbar.setNavigationIcon(R.drawable.menu_icon);

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaoCaoBanHangActivity.this, ChartActivity.class);
                // Duyệt map. Chuyển key, value thành 2 arraylist
                for(Map.Entry<String, ChiTietMon> entry : thongKe.entrySet()){
                    arrTenMon.add(entry.getValue().getTenMon());
                    arrDoanhThu.add(entry.getValue().getSl() * entry.getValue().getGia());
                }
                // Truyền 2 arraylist sang charActivity
                intent.putExtra("arrTenMon", arrTenMon);
                intent.putExtra("arrDoanhThu", arrDoanhThu);
                startActivity(intent);
                arrTenMon.clear();
                arrDoanhThu.clear();
            }
        });
        edtNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
            textView1.setText(entry.getValue().getTenMon());
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
            textView2.setText(String.valueOf(entry.getValue().getSl()));
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
            textView3.setText(nf.format(entry.getValue().getSl() * entry.getValue().getGia()) + "đ");
            // Thêm TextView vào TableRow
            tableRow.addView(textView3);
            // Thêm TableRow vào TableLayout
            tbLayout.addView(tableRow);
        }
    }

    private void getAllChiTietMon() {
        // Lấy key: mã bàn, tên khách hàng
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("ChiTietMon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                long count_ = 0;
                // Lấy chi tiết món tại key vừa lấy được
                for (DataSnapshot item : snapshot.getChildren()) {
                    String id_Ban = item.getKey();
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference().child("ChiTietMon").child(id_Ban).child("HT");
                    long finalCount = count;
                    long finalCount_ = count_;
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot items : dataSnapshot.getChildren()) {
                                    ChiTietMon _chiTietMon = items.getValue(ChiTietMon.class);
                                    // lấy dữ liệu add vào arraylist
                                        chiTietMons.add(_chiTietMon);
                                }
                                // chỉ lấy dữ liệu chi tiết món lần lặp cuối cùng
                                if (finalCount_ == finalCount -1){
                                    thongKe = ThongKe(chiTietMons);
                                    // gọi hàm tạo table row
                                    inSertTableRow(thongKe);
                                    // duyệt map
                                    for (Map.Entry<String, ChiTietMon> entry : thongKe.entrySet()){
                                        doanhSo += entry.getValue().getSl();
                                        doanhThu += entry.getValue().getGia() * entry.getValue().getSl();
                                    }
                                    // settext tại text view
                                    NumberFormat nf = NumberFormat.getNumberInstance();
                                    tvDoanhSo.setText(String.valueOf(doanhSo) + " món");
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

    private void setdrawer(){
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_admin);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_thongke);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarAdmin menuSideBarAdmin = new MenuSideBarAdmin();
        menuSideBarAdmin.chonManHinh(item.getItemId(), BaoCaoBanHangActivity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolBar);
        btnChart = findViewById(R.id.btnChart);
        tbLayout = findViewById(R.id.tbLayout);
        edtNgay = findViewById(R.id.edtNgay);
        tvDoanhSo = findViewById(R.id.tvDoanhSo);
        tvDoanhThu = findViewById(R.id.tvDoanhThu);
    }
}