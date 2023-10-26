package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Comparator;

public class QuanLyMonActivity extends AppCompatActivity {
    Toolbar toolBar;
    TextView tvBug;
    EditText edtSearchBox;
    Spinner spnLoai;
    ImageButton btnSort, btnAdd;
    SwipeableRecyclerView recyclerView;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ArrayList<Mon> data = new ArrayList<>();
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
        toolBar.setTitle("Quản lý món");
        toolBar.setNavigationIcon(R.drawable.menu_icon);
        adapter = new QuanLyMonAdapter(data, this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadDataSpinner();
        //loadData();
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
                    loadDataTheoLoaiVaTimKiem(spnLoai.getSelectedItemPosition(), edtSearchBox.getText());
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
                loadDataTheoLoaiVaTimKiem(i, edtSearchBox.getText());
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
                loadDataTheoLoaiVaTimKiem(spnLoai.getSelectedItemPosition(), edtSearchBox.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    private void loadDataTheoLoaiVaTimKiem(Integer i, CharSequence key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyMonActivity.this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                if (i != 3) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Mon mon = dataSnapshot.getValue(Mon.class);
                        if (mon.getId_Loai() == i && mon.getTenMon().contains(key)) {
                            data.add(mon);
                        }
                    }
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Mon mon = dataSnapshot.getValue(Mon.class);
                        if (mon.getTenMon().contains(key)) {
                            data.add(mon);
                        }

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

    private void setControl() {
        edtSearchBox = findViewById(R.id.edtSearchBox);
        btnSort = findViewById(R.id.btnSort);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.rv);
        tvBug = findViewById(R.id.tvBug);
        spnLoai = findViewById(R.id.spnLoai);
        toolBar = findViewById(R.id.toolBar);
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

    private void loadDataSpinner() {
        databaseReference = FirebaseDatabase.getInstance().getReference("LoaiMon");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                    loaiMonArrayList.add(loaiMon);
                    spinnerArray[loaiMon.getId_loai()] = loaiMon.getTen_loai();
                }
                LoaiMon loai_Mon = new LoaiMon(3, "Tất cả");
                spinnerArray[3] = loai_Mon.getTen_loai();
                spinnerAdapter = new ArrayAdapter(QuanLyMonActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray);
                spnLoai.setAdapter(spinnerAdapter);
                spnLoai.setSelection(3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuanLyMonActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}