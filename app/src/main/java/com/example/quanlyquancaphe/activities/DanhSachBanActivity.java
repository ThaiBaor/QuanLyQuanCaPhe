package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.DanhSachBanAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.DatBan;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.ultilities.HoaDonUltility;
import com.example.quanlyquancaphe.ultilities.NotificationUtility;
import com.example.quanlyquancaphe.services.MenuSideBarPhucVu;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhSachBanActivity extends AppCompatActivity implements View.OnCreateContextMenuListener, NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference reference;
    ValueEventListener valueEventListener;
    ArrayList<Ban> dataBan = new ArrayList<>();
    ArrayList<Khu> dataKhu = new ArrayList<>();
    List<String> dataSpinner = new ArrayList<>();
    ArrayList<DatBan> dataDatBan = new ArrayList<>();
    DanhSachBanAdapter adapter;
    Spinner spKhu;
    Button btnbanDaDat;
    RecyclerView listBan;
    boolean firstNoti = true;
    Toolbar toolBar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danhsachban_layout);
        setConTrol();
        setdrawer();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        GioHangActivity.tenKH = " ";
        GioHangActivity.id_Ban = " ";
    }

    private void setEvent() {
        GioHangActivity.tenKH = " ";
        GioHangActivity.id_Ban = " ";
        GetDataBan();
        GetDataKhu();
        GetDataSpinner();
        getNotification();
        getDataDatBan();
        //Set title toolbar
        toolBar.setTitle("Danh sách bàn");
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

                        // Gọi hàm kiểm tra dữ liệu đặt bàn
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
                                    GioHangActivity.id_Ban = dataBan.get(position).getId_Ban();
                                    // Truyền số chỗ ngồi sang màn hình đặt bàn
                                    intent.putExtra("soChoNgoi", dataBan.get(position).getSoChoNgoi());
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachBanActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Thông báo");
                                    builder.setMessage("Bạn có muốn hủy đặt " + dataBan.get(position).getTenBan() + " không?");
                                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //hủy đặt bàn, chuyển trạng thái về 0: bàn trống
                                            ChuyenTrangThaiBan(dataBan.get(position).getId_Ban(), 0);
                                            DeleteItemDatabase(dataBan.get(position).getId_Ban(), "DatBan");
                                            Toast.makeText(DanhSachBanActivity.this, "Hủy đặt bàn thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
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
                                    GioHangActivity.id_Ban = dataBan.get(position).getId_Ban();
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
                                    GioHangActivity.id_Ban = dataBan.get(position).getId_Ban();
                                    startActivity(intent);
                                    return true;
                                }
                            });
                            //Context menu thanh toán
                            menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {
                                    // Tạo hóa đơn cho thu ngân
                                    HoaDonUltility.getHdInstance().taoHoaDonTaiBan(DanhSachBanActivity.this, dataBan.get(position).getId_Ban());
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

    private void DeleteItemDatabase(String key, String tenBang) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(tenBang);
        reference.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }

    private void openCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnTiep = dialog.findViewById(R.id.btnTiep);
        EditText edtTenKH = dialog.findViewById(R.id.edtTenKH);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setCheckedItem(R.id.nav_danhsachban);
                dialog.dismiss();
            }
        });
        btnTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTenKH.getText().toString().equals("")) {
                    edtTenKH.requestFocus();
                    edtTenKH.setError("Phải nhập tên khách hàng");
                } else {
                    GioHangActivity.tenKH = edtTenKH.getText().toString();
                    Intent intent = new Intent(DanhSachBanActivity.this, DanhSachMonPhucVuActivity.class);
                    dialog.dismiss();
                    startActivity(intent);
                }
            }
        });
        dialog.show();

    }

    // Get data đặt bàn
    private void getDataDatBan() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("DatBan");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataDatBan.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    DatBan datBan = item.getValue(DatBan.class);
                    dataDatBan.add(datBan);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNotification() {
        reference = FirebaseDatabase.getInstance().getReference("ThongBao");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (firstNoti) {
                    firstNoti = false;
                    return;
                }
                if (snapshot.child("id").getValue(Integer.class) == null) {
                    return;
                }
                int id_ThongBao = snapshot.child("id").getValue(Integer.class);
                if (id_ThongBao == 2 || id_ThongBao == 3) {
                    NotificationUtility.pushNotification(DanhSachBanActivity.this, snapshot.child("contentText").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setdrawer() {
        toolBar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_phuc_vu);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_nav, R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_danhsachban);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarPhucVu menuSideBarPhucVu = new MenuSideBarPhucVu();
        menuSideBarPhucVu.chonManHinh(item.getItemId(), DanhSachBanActivity.this);
        if (item.getItemId() == R.id.nav_mangve) {
            openCustomDialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setConTrol() {
        btnbanDaDat = findViewById(R.id.btnBanDaDat);
        listBan = findViewById(R.id.listBan);
        spKhu = findViewById(R.id.spKhu1);
        toolBar = findViewById(R.id.toolBar);
    }
}