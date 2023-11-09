package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.BanAdapter;
import com.example.quanlyquancaphe.adapters.DanhSachBanAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.DatBan;
import com.example.quanlyquancaphe.models.Khu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhSachBanActivity extends AppCompatActivity implements View.OnCreateContextMenuListener {
    FirebaseDatabase database;
    DatabaseReference reference;
    ValueEventListener valueEventListener;
    ArrayList<Ban> dataBan = new ArrayList<>();
    ArrayList<Khu> dataKhu = new ArrayList<>();
    List<String> dataSpinner = new ArrayList<>();
    DanhSachBanAdapter adapter;
    Spinner spKhu;
    Button btnbanDaDat;
    RecyclerView listBan;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danhsachban_layout);
        setConTrol();
        setEvent();

    }

    private void GetDataBan() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Ban");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBan.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
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

    private void GetDataKhu() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Khu");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKhu.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Khu khu = item.getValue(Khu.class);
                    dataKhu.add(khu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetDataSpinner() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Khu");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSpinner.clear();
                dataSpinner.add("Tất cả ");
                for (DataSnapshot item : snapshot.getChildren()) {
                    Khu khu = item.getValue(Khu.class);
                    dataSpinner.add(khu.getTenKhu());
                    ArrayAdapter adapter = new ArrayAdapter(DanhSachBanActivity.this, android.R.layout.simple_spinner_item, dataSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKhu.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setEvent() {
        GetDataBan();
        GetDataKhu();
        GetDataSpinner();
        //Set title toolbar
        toolbar.setTitle("Danh sách bàn");
        //Thay đổi bàn dựa theo khu
        spKhu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference().child("Khu");
                valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dataKhu.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            Khu khu = item.getValue(Khu.class);
                            dataKhu.add(khu);
                        }
                        HienThiBanTheoKhu(position, dataKhu, parent.getItemAtPosition(position).toString());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //set data ban
        adapter = new DanhSachBanAdapter(dataBan, DanhSachBanActivity.this, new DanhSachBanAdapter.onItemLongClickListenner() {
            //Sử dụng interface để lấy vị trí phần tử trong data bàn
            @Override
            public void onItemLongClickListenner(int position) {
                //Tạo context menu
                listBan.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        /*menu.add("Menu").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem item) {
                                Toast.makeText(DanhSachBanActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });*/
                        //Tạo 4 chức năng cho context menu
                        menu.add("Gọi món");
                        menu.add("Đặt bàn");
                        menu.add("Hủy đặt bàn");
                        menu.add("Thanh toán");
                        //Trạng thái bàn: Bàn trống
                        if (dataBan.get(position).getId_TrangThaiBan() == 0) {
                            menu.getItem(0).setEnabled(false);
                            menu.getItem(2).setEnabled(false);
                            menu.getItem(3).setEnabled(false);
                            //Context menu đặt bàn
                            //Chuyển sang màn hình đặt bàn
                            menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {
                                    Intent intent = new Intent(DanhSachBanActivity.this, DatBanActivity.class);
                                    // Gắn id_Ban cho giỏ hàng
                                    GioHangActivity.id_Ban =  dataBan.get(position).getId_Ban();
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }
                        //Trạng thái bàn: đã đặt
                        else if (dataBan.get(position).getId_TrangThaiBan() == 2) {
                            menu.getItem(1).setEnabled(false);
                            menu.getItem(3).setEnabled(false);
                            //Context menu hủy đặt bàn
                            menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {
                                    //hủy đặt bàn, chuyển trạng thái về 0: bàn trống
                                    ChuyenTrangThaiBan(dataBan.get(position).getId_Ban(), 0);
                                    Toast.makeText(DanhSachBanActivity.this, "Hủy đặt bàn thành công", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            });
                            //Context menu gọi món
                            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {
                                    //chuyển sang màn hình gọi món
                                    Intent intent = new Intent(DanhSachBanActivity.this, DanhSachMonPhucVuActivity.class);
                                    // Gắn id_Ban cho giỏ hàng
                                    GioHangActivity.id_Ban =  dataBan.get(position).getId_Ban();
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }
                        //Trạng thái bàn: đang sử dụng
                        else {
                            menu.getItem(1).setEnabled(false);
                            menu.getItem(2).setEnabled(false);
                            //Context menu gọi món
                            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {
                                    //chuyển sang màn hình gọi món
                                    Intent intent = new Intent(DanhSachBanActivity.this, DanhSachMonPhucVuActivity.class);
                                    // Gắn id_Ban cho giỏ hàng
                                    GioHangActivity.id_Ban =  dataBan.get(position).getId_Ban();
                                    startActivity(intent);
                                    return true;
                                }
                            });
                            //Context menu thanh toán
                            menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {
                                    //viet code tai day
                                    //sau khi thanh toán, chuyển trạng thái bàn thành 0: bàn trống
                                    ChuyenTrangThaiBan(dataBan.get(position).getId_Ban(), 0);
                                    return true;
                                }
                            });
                        }
                    }
                });
            }
        });
        listBan.setAdapter(adapter);
        // Gắn GridLayoutManager cho RecyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DanhSachBanActivity.this, 3);
        listBan.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();

        //Chuyển sang màn hình danh sách bàn đã đặt
        btnbanDaDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachBanActivity.this, DSBanDaDatActivity.class);

                startActivity(intent);
            }
        });
    }

    //Hàm hiển thị bàn theo khu
    private void HienThiBanTheoKhu(Integer viTri, ArrayList<Khu> khu, String tenKhu) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Ban");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Lọc theo khu
                if (viTri != 0) {
                    dataBan.clear();
                    for (DataSnapshot item : snapshot.getChildren()) {
                        Ban ban = item.getValue(Ban.class);
                        for (Khu itemKhu : khu) {
                            if (ban.getId_Khu() == itemKhu.getId_Khu()) {
                                if (itemKhu.getTenKhu() == tenKhu) {
                                    dataBan.add(ban);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                //Tất cả các khu
                else {
                    dataBan.clear();
                    for (DataSnapshot item : snapshot.getChildren()) {
                        Ban ban = item.getValue(Ban.class);
                        dataBan.add(ban);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Hàm chuyển trạng thái bàn
    private void ChuyenTrangThaiBan(String maBan, Integer trangThai) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Ban");
        databaseReference.child(maBan).child("id_TrangThaiBan").setValue(trangThai);
    }

    private void setConTrol() {
        btnbanDaDat = findViewById(R.id.btnBanDaDat);
        listBan = findViewById(R.id.listBan);
        spKhu = findViewById(R.id.spKhu1);
        toolbar = findViewById(R.id.toolBar);
    }
}