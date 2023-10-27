package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.QuanLyNhanVienAdapter;
import com.example.quanlyquancaphe.models.NhanVien;
import com.example.quanlyquancaphe.services.MenuSideBarAdmin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuanLyNhanVienActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    SwipeableRecyclerView swipeableRecyclerView;
    List<NhanVien> datalist = new ArrayList<>();
    DatabaseReference databaseReference;
    QuanLyNhanVienAdapter adapter;
    ValueEventListener listener;
    SearchView searchView;
    ImageView ivThem, ivSapXep ;
    String key;
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Integer sortCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_quanlynhanvien_layout);
        setdrawer();
        setControl();
        toolBar.setTitle("Quản lý nhân viên");
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        loadData();

        swipeableRecyclerView.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                NhanVien nhanVien = datalist.get(position);
                Intent intent = new Intent(QuanLyNhanVienActivity.this, CapNhatNhanVienActivity.class);
                intent.putExtra("Avatar", nhanVien.getAvatar());
                intent.putExtra("CCCDT",nhanVien.getImageCCCDT());
                intent.putExtra("CCCDS", nhanVien.getImageCCCDS());
                intent.putExtra("MaNV", nhanVien.getMaNhanVien());
                intent.putExtra("TenNV",nhanVien.getTenNhanVien());
                intent.putExtra("DiaChi",nhanVien.getDiaChi());
                intent.putExtra("SDT", nhanVien.getSoDienThoai());
                intent.putExtra("MatKhau", nhanVien.getMatKhau());
                intent.putExtra("Vitri",nhanVien.getViTri());
                startActivity(intent);
            }

            @Override
            public void onSwipedRight(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyNhanVienActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận xóa ?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNhanVien(position);

                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        swipeableRecyclerView.setLeftImage(R.drawable.delete2_icon);

        swipeableRecyclerView.setRightImage(R.drawable.edit_icon);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Search(newText);
                return true;
            }
        });
        ivThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuanLyNhanVienActivity.this, ThemNhanVienActivity.class);
                startActivity(intent);
            }
        });
        ivSapXep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortCount == 0){
                    sortCount ++;
                    datalist.sort(new Comparator<NhanVien>() {
                        @Override
                        public int compare(NhanVien nhanVien, NhanVien nhanvien2) {
                            return nhanVien.getMaNhanVien().compareTo(nhanvien2.getMaNhanVien());
                        }
                    });
                }else if(sortCount ==1 ){
                    sortCount ++;
                    datalist.sort(new Comparator<NhanVien>() {
                        @Override
                        public int compare(NhanVien nhanVien, NhanVien nhanvien2) {
                            return  nhanvien2.getMaNhanVien().compareTo(nhanVien.getMaNhanVien());
                        }
                    });

                }else{
                    loadData();
                    sortCount = 0;
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
    }

    public void Search(String text){
        ArrayList<NhanVien> searchlist = new ArrayList<>();
        for (NhanVien nhanVien :datalist){
            if (nhanVien.getTenNhanVien().toLowerCase().contains(text.toLowerCase())){
                searchlist.add(nhanVien);
            }
        }
        adapter.SearchDataList(searchlist);
    }

    private void setControl() {
        swipeableRecyclerView = findViewById(R.id.itemSwipaebleRecyclerview);
        searchView = findViewById(R.id.SearchView);
        ivSapXep = findViewById(R.id.ibtnSapxep);
        ivThem = findViewById(R.id.ibtnThem);
        toolBar = findViewById(R.id.toolBar);


    }
    private void deleteNhanVien(int position ){
        NhanVien nhanVien = datalist.get(position);
        key = nhanVien.getMaNhanVien();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Nhanvien");
        StorageReference deleteimage = FirebaseStorage.getInstance().getReferenceFromUrl(nhanVien.getAvatar().toString());
        deleteimage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child(key).removeValue();
                Toast.makeText(QuanLyNhanVienActivity.this, "Xoa Thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
        StorageReference deleteCCCDT = FirebaseStorage.getInstance().getReferenceFromUrl(nhanVien.getImageCCCDT().toString());
        deleteCCCDT.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child(key).removeValue();
                Toast.makeText(QuanLyNhanVienActivity.this, "Xoa Thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
        StorageReference deleteCCCDS = FirebaseStorage.getInstance().getReferenceFromUrl(nhanVien.getImageCCCDS().toString());
        deleteCCCDS.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child(key).removeValue();
                Toast.makeText(QuanLyNhanVienActivity.this, "Xoa Thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadData(){
        swipeableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new QuanLyNhanVienAdapter(QuanLyNhanVienActivity.this, datalist);
        swipeableRecyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Nhanvien");
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for (DataSnapshot itemSnap : snapshot.getChildren()) {
                    NhanVien nhanVien = itemSnap.getValue(NhanVien.class);
                    datalist.add(nhanVien);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setdrawer(){
        toolBar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.nav_drawer_chucnang_admin);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolBar,R.string.open_nav,R.string.close_nav);
        //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuSideBarAdmin menuSideBarAdmin = new MenuSideBarAdmin();
        menuSideBarAdmin.chonManHinh(item.getItemId(), QuanLyNhanVienActivity.this);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}