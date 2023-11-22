package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.ThongKeHoaDonAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.HoaDonKhachHang;
import com.example.quanlyquancaphe.models.ThongKeHoaDon;
import com.example.quanlyquancaphe.services.MenuSideBarThuNgan;
import com.google.android.material.navigation.NavigationView;
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

public class ThongKeHoaDonActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<ThongKeHoaDon> data = new ArrayList<>();
    ArrayList<ThongKeHoaDon> dataFilter = new ArrayList<>();
    ArrayList<Ban> dataBan = new ArrayList<>();
    ArrayList arrIdHoaDon = new ArrayList();
    ArrayList arrIdThoiGianGoiMon = new ArrayList();

    ArrayList<HoaDonKhachHang> hoaDonKhachHangs = new ArrayList<>();
    ThongKeHoaDonAdapter thongKeHoaDonAdapter ;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    TextView tvGiothuNhat, tvGioThuHai, tvNgayThongKe, tvSoLuongHoaDon, tvTongTien;
    DrawerLayout drawerLayout;
    Toolbar toolBar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ThongKeHoaDon thongKeHoaDon = new ThongKeHoaDon();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    int count = 0;
    int countHD = 0;

    int countGet = 0;

    int countGet_ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_thongkehoadon_layout);
        setControl();
        setEvent();
        setdrawer();
        initDataBan();
        //getDataHoaDon();
        //getDataHoaDonMangVe();
        //filterHoaDon();
        //getDataArrHDKH(hoaDonKhachHangs);
    }

    private void setEvent() {
        setNgayHienTai();
        tvGiothuNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGioThuNhat();
            }
        });

        tvGiothuNhat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(kiemTraNgayGio()>0){
                    tvGiothuNhat.setText("00:00");
                }
                else {
                    filterHoaDon();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvGioThuHai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGioThuHai();
            }
        });

        tvGioThuHai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(kiemTraNgayGio()>0){
                    tvGioThuHai.setText("00:00");
                }
                else {
                    filterHoaDon();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvNgayThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonNgayThongKe();
                chuyenNgayGioThongKeThuNhat();
                chuyenNgayGioThongKeThuHai();
            }
        });

        tvNgayThongKe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chuyenNgayGioThongKeThuNhat();
                chuyenNgayGioThongKeThuHai();
                filterHoaDon();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void chonNgayThongKe(){
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year,month,day);
                //tvNgayThongKe.setText(dateFormat.format(calendar.getTime()));
                if(kiemTraNgayGio() <= 0){
                    tvNgayThongKe.setText(dateFormat.format(calendar.getTime()));
                }
                else {
                    Toast.makeText(ThongKeHoaDonActivity.this, "Vui long nhap lai gio", Toast.LENGTH_SHORT).show();
                }
            }
        }, yearNow, monthNow, dayNow);
        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();
    }

    private void ChonGioThuNhat(){
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    tvGiothuNhat.setText(hourOfDay + ":" + minute);
            }
        },gio, phut, true);
        timePickerDialog.show();
    }
    private void ChonGioThuHai(){
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    tvGioThuHai.setText(hourOfDay + ":" + minute);
            }
        },gio, phut, true);
        timePickerDialog.show();
    }

    private void getDataHoaDon(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(ThongKeHoaDonActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
//        builder.setCancelable(false);
//        AlertDialog dialog = builder.create();
//        dialog.show();
        FirebaseDatabase firebaseDatabaseTaiBan = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceTaiBan = firebaseDatabaseTaiBan.getReference().child("HoaDon").child("TaiBan");
        databaseReferenceTaiBan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                xoaData();
                for (DataSnapshot item: snapshot.getChildren()){
                    String id_MaHoaDon = item.child("id_HoaDon").getValue().toString();
                    String id_Ban = item.child("id_Ban").getValue().toString();
                    String thoiGian_thanhtoan = item.child("thoiGian_ThanhToan").getValue().toString();
                    String ngayThanhToan = item.child("ngayThanhToan").getValue().toString();
                    Double tongTien = Double.parseDouble(item.child("tongTien").getValue().toString());
                    Boolean daThanhToan = Boolean.parseBoolean(item.child("daThanhToan").getValue().toString());
                    String tenKhachHang = "Không có";
                    for (HoaDonKhachHang hoaDonKhachHang : hoaDonKhachHangs){
                        if(id_MaHoaDon.equals(hoaDonKhachHang.getId_HoaDon())){
                            String[] split = hoaDonKhachHang.getTenKH().split("-");
                            tenKhachHang = split[1];
                        }
                    }
                    //Toast.makeText(ThongKeHoaDonActivity.this,ngayThanhToan + " " + thoiGian_thanhtoan, Toast.LENGTH_SHORT).show();
                    thongKeHoaDon = new ThongKeHoaDon(id_MaHoaDon, id_Ban,ngayThanhToan, thoiGian_thanhtoan, tongTien, daThanhToan, tenKhachHang);
                    data.add(thongKeHoaDon);
                }
                filterHoaDon();
                getDataHoaDonMangVe();
//                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                dialog.dismiss();
                Toast.makeText(ThongKeHoaDonActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataHoaDonMangVe(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(ThongKeHoaDonActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
//        builder.setCancelable(false);
//        AlertDialog dialog = builder.create();
//        dialog.show();
        FirebaseDatabase firebaseDatabaseMangVe = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceMangVe = firebaseDatabaseMangVe.getReference().child("HoaDon").child("MangVe");
        databaseReferenceMangVe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                xoaData();
                for (DataSnapshot item: snapshot.getChildren()){
                    String id_MaHoaDon = item.child("id_HoaDon").getValue().toString();
                    String id_Ban = "Mang về";
                    String thoiGian_thanhtoan = item.child("thoiGian_ThanhToan").getValue().toString();
                    String ngayThanhToan = item.child("ngayThanhToan").getValue().toString();
                    Double tongTien = Double.parseDouble(item.child("tongTien").getValue().toString());
                    Boolean daThanhToan = Boolean.parseBoolean(item.child("daThanhToan").getValue().toString());
                    String[] splitget = item.child("tenKH").getValue().toString().split("-");
                    String tenKH = splitget[1];
                    thongKeHoaDon = new ThongKeHoaDon(id_MaHoaDon, id_Ban,ngayThanhToan, thoiGian_thanhtoan, tongTien, daThanhToan, tenKH);
                    data.add(thongKeHoaDon);
                }
                filterHoaDon();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongKeHoaDonActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xoaData(){
        if (data.isEmpty() == false){
            for (int i = 0; i < data.size(); i++){
                if (data.get(i).getId_Ban().equals("Mang về")){
                    data.remove(i);
                    i = 0;
                }
            }
        }
    }

    private void getDataArrHDKH(ArrayList<HoaDonKhachHang> arrHDKH){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ChiTietMon");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    String id_Ban = item.getKey();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(id_Ban).child("QK");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            countGet++;
                            for (DataSnapshot itemIDHoaDon : snapshot.getChildren()){
                                String idHoaDon = itemIDHoaDon.getKey();
                                count++;
                                for (DataSnapshot itemTGGoiMon : itemIDHoaDon.getChildren()){
                                    for (DataSnapshot item : itemTGGoiMon.getChildren()){
                                        countHD = 1;
                                        if(count == countHD){
                                            String tenKH = item.child("tenKH").getValue().toString();
                                            HoaDonKhachHang hoaDonKhachHang = new HoaDonKhachHang(idHoaDon, tenKH);
                                            arrHDKH.add(hoaDonKhachHang);
                                            count = 0;
                                        }
                                    }
                                }
                            }
                            if (countGet == countGet_){
                                getDataHoaDon();
                                getDataHoaDonMangVe();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ThongKeHoaDonActivity.this, "Kết nối mạng lỗi", Toast.LENGTH_SHORT).show();
                        }
                    });
                    countGet_++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongKeHoaDonActivity.this, "Kết nối mạng lỗi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDataIdHoaDon(ArrayList arrIdHoaDonGet) {
        arrIdHoaDon.clear();
        for (int i = 0; i < dataBan.size(); i++) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(dataBan.get(i).getId_Ban()).child("QK");
            int finalI = i;
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item: snapshot.getChildren()){
                        arrIdHoaDonGet.add(item.getKey());
                    }
                    //Toast.makeText(ThongKeHoaDonActivity.this, arrIdHoaDon.size()+"Get", Toast.LENGTH_SHORT).show();
                    if(finalI == dataBan.size()-1){
                        getIdThoiGianGoiMon(arrIdThoiGianGoiMon);
                        //Toast.makeText(ThongKeHoaDonActivity.this, "Lay", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initDataBan() {
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference reference = database.getReference().child("Ban");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBan.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Ban ban = item.getValue(Ban.class);
                    dataBan.add(ban);
                }
                getDataArrHDKH(hoaDonKhachHangs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getIdThoiGianGoiMon(ArrayList arrIdThoiGianGoiMonGet){
        arrIdThoiGianGoiMonGet.clear();
        for (int i = 0; i < dataBan.size(); i++) {
            for (int j = 0; j < arrIdHoaDon.size(); j ++){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(dataBan.get(i).getId_Ban()).child("QK").child(arrIdHoaDon.get(j).toString());
                int finalI = i;
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()){
                            if(arrIdThoiGianGoiMonGet.contains(item.getKey()) == false){
                                arrIdThoiGianGoiMonGet.add(item.getKey());
                            }
                        }
                        if(finalI == dataBan.size() - 1){
                            setArrHoaDonKH(hoaDonKhachHangs);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private void setArrHoaDonKH(ArrayList<HoaDonKhachHang> arrHoaDonKHGet){
        for (int i = 0; i < dataBan.size(); i++) {
            for (int j = 0; j < arrIdHoaDon.size(); j ++){
                String idHD = arrIdHoaDon.get(j).toString();
                final String[] add = {"0"};
                for (int k = 0;k < arrIdThoiGianGoiMon.size(); k++){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(dataBan.get(i).getId_Ban()).child("QK").child(arrIdHoaDon.get(j).toString()).child(arrIdThoiGianGoiMon.get(k).toString());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()){
                                    HoaDonKhachHang hoaDonKhachHang = new HoaDonKhachHang(idHD, item.child("tenKH").getValue().toString());
                                    if(add[0].equals("0") == true){
                                        arrHoaDonKHGet.add(hoaDonKhachHang);
                                        add[0] = "1";
                                    }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }
        getDataHoaDon();
        filterHoaDon();
    }

    private void setdrawer(){
        toolBar = findViewById(R.id.toolBar);
        toolBar.setTitle("Lịch sử hóa đơn");
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_thungan);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_thongkehoadon);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }



    private void filterHoaDon(){
        dataFilter.clear();
        Boolean kq = true;
        for (ThongKeHoaDon item : data){
            Date dateData = chuyenNgayGioData(item.getThoiGian_thanhtoan() + " " + item.getNgayThanhToan());
            if (dateData.compareTo(chuyenNgayGioThongKeThuNhat()) >= 0 && dateData.compareTo(chuyenNgayGioThongKeThuHai()) <= 0 && item.getDaThanhToan().equals(kq)){
                dataFilter.add(item);
            }
        }
        if(dataFilter != null){
            thongKeHoaDonAdapter = new ThongKeHoaDonAdapter(this, dataFilter, dataBan);
            recyclerView.setAdapter(thongKeHoaDonAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            tvSoLuongHoaDon.setText(dataFilter.size()+"");
            thongKeHoaDonAdapter.notifyDataSetChanged();
            NumberFormat nf = NumberFormat.getNumberInstance();
            tvTongTien.setText(nf.format(getTongTien(dataFilter)) + "đ");
        }else {
            tvSoLuongHoaDon.setText("0");
            tvTongTien.setText("0d");
        }
    }

    private Double getTongTien(ArrayList<ThongKeHoaDon> data){
        Double tongTien = Double.valueOf(0);
        for(ThongKeHoaDon item : data){
            tongTien += item.getTongTien();
        }
        return tongTien;
    }

    private Date chuyenNgayGioThongKeThuNhat(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = tvNgayThongKe.getText().toString() + " " + tvGiothuNhat.getText().toString();
        try{
             date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Toast.makeText(this, "Loi tg 1", Toast.LENGTH_SHORT).show();
        }
        return date;
    }
    private Date chuyenNgayGioThongKeThuHai(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = tvNgayThongKe.getText().toString() + " " + tvGioThuHai.getText().toString();
        try{
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Toast.makeText(this, "Loi tg 2", Toast.LENGTH_SHORT).show();
        }
        return date;
    }
    private Date chuyenNgayGioData(String dateString){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Toast.makeText(this, "Loi tg data", Toast.LENGTH_SHORT).show();
        }
        return date;
    }

    private Date chuyenNgayGio(String dateString){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try{
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Toast.makeText(this, "Loi ngay gio", Toast.LENGTH_SHORT).show();
        }
        return date;
    }

    private int kiemTraNgayGio(){
        Date dateThuNhat = chuyenNgayGio(tvNgayThongKe.getText().toString() + " " + tvGiothuNhat.getText().toString());
        Date dateThuHai = chuyenNgayGio(tvNgayThongKe.getText().toString() + " " + tvGioThuHai.getText().toString());
        return dateThuNhat.compareTo(dateThuHai);
    }
    private void setNgayHienTai(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        tvNgayThongKe.setText(dateFormat.format(calendar.getTime()));
        chuyenNgayGioThongKeThuNhat();
        chuyenNgayGioThongKeThuHai();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarThuNgan menuSideBarThuNgan = new MenuSideBarThuNgan();
        menuSideBarThuNgan.chonManHinh(item.getItemId(), ThongKeHoaDonActivity.this);
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerviewthongkehoadon);
        tvGiothuNhat = findViewById(R.id.tvGioThuNhat);
        tvGioThuHai = findViewById(R.id.tvGioThuHai);
        tvNgayThongKe = findViewById(R.id.tvNgayThongKe);
        tvSoLuongHoaDon = findViewById(R.id.tvSoLuongHD);
        tvTongTien = findViewById(R.id.tvTongCong);

    }


}