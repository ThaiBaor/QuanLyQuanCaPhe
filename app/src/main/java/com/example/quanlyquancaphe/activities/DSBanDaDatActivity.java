package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.BanAdapter;
import com.example.quanlyquancaphe.adapters.DanhSachBanAdapter;
import com.example.quanlyquancaphe.adapters.DatBanAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.DatBan;
import com.example.quanlyquancaphe.models.Khu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.lang.annotation.Target;
import java.util.ArrayList;

public class DSBanDaDatActivity extends AppCompatActivity {
    Toolbar toolbar;
    SwipeableRecyclerView dsBanDaDat;
    FirebaseDatabase database;
    DatabaseReference reference;
    ValueEventListener valueEventListener;
    ArrayList<DatBan> dataDatBan = new ArrayList<>();
    ArrayList<Ban> dataBan = new ArrayList<>();
    ArrayList<Khu> dataKhu = new ArrayList<>();
    DatBanAdapter adapter;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_dsbandadat_layout);
        setControl();
        //Load  data
        GetDataBan();
        GetDataKhu();
        GetDataDatBan();
        adapter = new DatBanAdapter(dataBan, dataKhu, dataDatBan, DSBanDaDatActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        dsBanDaDat.setAdapter(adapter);
        dsBanDaDat.setLayoutManager(layoutManager);
        setEvent();
    }

    private void setEvent() {
        //set titil toolbar
        toolbar.setTitle("Danh sách bàn đã đặt");
        //Quay về màn hình danh sách bàn 
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Bắt sự kiện kéo trái, kéo phải trên item
        dsBanDaDat.setListener(new SwipeLeftRightCallback.Listener() {
            //Kéo sang trái
            @Override
            public void onSwipedLeft(int position) {
                Intent intent = new Intent(DSBanDaDatActivity.this, CapNhatDatBanActivity.class);
                intent.putExtra("tenKH", dataDatBan.get(position).getTenKH());
                intent.putExtra("SDT", dataDatBan.get(position).getSDT());
                intent.putExtra("soNguoi", dataDatBan.get(position).getSoNguoi());
                intent.putExtra("ngay", dataDatBan.get(position).getNgay());
                intent.putExtra("gio", dataDatBan.get(position).getGio());
                intent.putExtra("id_Ban", dataBan.get(position).getId_Ban());
                startActivity(intent);
            }
            //Kéo sang phải
            @Override
            public void onSwipedRight(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DSBanDaDatActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn hủy đặt bàn của khách hàng "+ dataDatBan.get(position).getTenKH() + " không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteItemDatabase(dataDatBan.get(position).getId_Ban(), "DatBan");
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GetDataBan();
                        GetDataKhu();
                        GetDataDatBan();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                //Load lại data
                GetDataBan();
                GetDataKhu();
                GetDataDatBan();
            }
        });
    }
    //Lấy dữ liệu bàn
    private void GetDataBan() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Ban");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBan.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    Ban ban = item.getValue(Ban.class);
                    dataBan.add(ban);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //Lấy dữ liệu khu
    private void GetDataKhu() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Khu");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKhu.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    Khu khu = item.getValue(Khu.class);
                    dataKhu.add(khu);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Lấy dữ liệu đặt bàn
    private void GetDataDatBan() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("DatBan");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataDatBan.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    DatBan datBan = item.getValue(DatBan.class);
                    dataDatBan.add(datBan);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Xóa item database
    private void DeleteItemDatabase(String key, String tenBang) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(tenBang);
        reference.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolBar);
        dsBanDaDat = findViewById(R.id.DSBanDaDat);
    }
}