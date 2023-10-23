package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.QuanLyMonAdapter;
import com.example.quanlyquancaphe.models.LoaiMon;
import com.example.quanlyquancaphe.models.Mon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuanLyMonActivity extends AppCompatActivity {

    TextView tvBug;
    EditText edtSearchBox;
    Spinner spnLoai;
    ImageButton btnSort, btnAdd;
    SwipeableRecyclerView recyclerView;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ArrayList<Mon> data = new ArrayList<>();
    ArrayList<Mon> filterData = new ArrayList<>();

    ArrayList<LoaiMon> loaiMonArrayList = new ArrayList<>();
    String[] spinnerArray = new String[4];
    ArrayAdapter spinnerAdapter;
    QuanLyMonAdapter adapter;
    ValueEventListener valueEventListener;
    Integer sortCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_quanlymon_layout);
        setControl();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quản lý món");
        adapter = new QuanLyMonAdapter(data, this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getDataSpinner();
        loadData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuanLyMonActivity.this, ThemMonActivity.class);
                startActivity(intent);
            }
        });
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortCount == 0) {
                    ++sortCount;
                    data.sort(new Comparator<Mon>() {
                        @Override
                        public int compare(Mon mon1, Mon mon2) {
                            return mon1.getDonGia() >= mon2.getDonGia() ? 1 : -1;

                        }
                    });
                } else if (sortCount == 1) {
                    ++sortCount;
                    data.sort(new Comparator<Mon>() {
                        @Override
                        public int compare(Mon mon1, Mon mon2) {
                            return mon1.getDonGia() <= mon2.getDonGia() ? 1 : -1;
                        }
                    });
                } else {
                    loadData();
                    sortCount = 0;
                }
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                Mon mon = data.get(position);
                Intent intent = new Intent(QuanLyMonActivity.this, CapNhatThongTinMonActivity.class);
                intent.putExtra("id_Mon", mon.getId_Mon());
                intent.putExtra("tenMon", mon.getTenMon());
                intent.putExtra("moTa", mon.getMoTa());
                intent.putExtra("hinh", mon.getHinh());
                intent.putExtra("donGia", mon.getDonGia());
                intent.putExtra("giamGia", mon.getGiamGia());
                intent.putExtra("id_Loai", mon.getId_Loai());
                startActivity(intent);
            }

            @Override
            public void onSwipedRight(int position) {
                //Toast.makeText(QuanLyMonActivity.this, "Xoa", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyMonActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận xóa ?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Mon mon = data.get(position);
                        delete(mon.getHinh(), mon.getId_Mon());
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
        spnLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                layTheoLoai(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtSearchBox.getText().equals("")){
                    search(charSequence);
                    adapter = new QuanLyMonAdapter(filterData, QuanLyMonActivity.this );
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void search(CharSequence hint){
        filterData.clear();
        for (Mon mon : data){
            if (mon.getTenMon().toLowerCase().contains(hint.toString().toLowerCase())){
                filterData.add(mon);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    private void layTheoLoai(Integer i) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyMonActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                // Tham chiếu tới bảng trong database
                databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
                // Bắt sự kiện khi dữ liệu trên database bị thay đổi
                valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        data.clear();
                        if (i != 3) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Mon mon = dataSnapshot.getValue(Mon.class);
                                if (mon.getId_Loai() == i) {
                                    data.add(mon);
                                }
                            }
                        } else {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Mon mon = dataSnapshot.getValue(Mon.class);
                                data.add(mon);
                            }
                        }
                        adapter = new QuanLyMonAdapter(data, QuanLyMonActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Toast.makeText(QuanLyMonActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        edtSearchBox = findViewById(R.id.edtSearchBox);
        btnSort = findViewById(R.id.btnSort);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.rv);
        tvBug = findViewById(R.id.tvBug);
        spnLoai = findViewById(R.id.spnLoai);

    }

    private void loadData() {
        // Tạo thông báo đang load dữ liệu
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        // Tham chiếu tới bảng trong database
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        // Bắt sự kiện khi dữ liệu trên database bị thay đổi
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Mon mon = dataSnapshot.getValue(Mon.class);
                    data.add(mon);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(QuanLyMonActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void delete(String imgUrl, String id_Mon) {
        // Tham chiếu tới bảng trong database
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        // Tham chiếu tới tấm ảnh trong storage
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl);
        // Thông báo đang xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang xóa dũ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        // Bắt sự kiện
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Xóa dữ liệu
                databaseReference.child(id_Mon).removeValue();
                // Tắt thông báo
                dialog.dismiss();
                Toast.makeText(QuanLyMonActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(QuanLyMonActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataSpinner() {
        databaseReference = FirebaseDatabase.getInstance().getReference("LoaiMon");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                spinnerArray[3] = "Tất cả";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                    loaiMonArrayList.add(loaiMon);
                    spinnerArray[loaiMon.id_loai] = loaiMon.ten_loai;
                }
                spinnerAdapter = new ArrayAdapter(QuanLyMonActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray);
                spnLoai.setAdapter(spinnerAdapter);
                spnLoai.setSelection(3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}