package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.QuanLyPhieuGiamGiaAdapter;
import com.example.quanlyquancaphe.models.Mon;
import com.example.quanlyquancaphe.models.PhieuGiamGia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;

public class QuanLyPhieuGiamGiaActivity extends AppCompatActivity {
    SwipeableRecyclerView listPhieu;
    EditText edtSearch;
    ImageButton btnDaSuDung, btnSort, btnAdd;
    ArrayList<PhieuGiamGia> data = new ArrayList<>();
    QuanLyPhieuGiamGiaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_quanlyphieugiamgia_layout);
        setControl();
        adapter = new QuanLyPhieuGiamGiaAdapter(data, this);
        listPhieu.setLayoutManager(new LinearLayoutManager(this));
        listPhieu.setAdapter(adapter);
        getFirstData();
        updateRealTimeData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuanLyPhieuGiamGiaActivity.this, ThemPhieuGiamGiaActivity.class);
                startActivity(intent);
            }
        });
        listPhieu.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSwipedRight(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyPhieuGiamGiaActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận xóa ?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhieuGiamGia phieuGiamGia = data.get(position);
                        delete(phieuGiamGia.getId_Phieu());
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
    }

    private void delete(String id_Phieu) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PhieuGiamGia");
        databaseReference.child("ChuaSuDung").child(id_Phieu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(QuanLyPhieuGiamGiaActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuanLyPhieuGiamGiaActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFirstData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PhieuGiamGia");
        databaseReference.child("ChuaSuDung").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PhieuGiamGia phieuGiamGia = dataSnapshot.getValue(PhieuGiamGia.class);
                    data.add(phieuGiamGia);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateRealTimeData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PhieuGiamGia");
        databaseReference.child("ChuaSuDung").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PhieuGiamGia phieuGiamGia = snapshot.getValue(PhieuGiamGia.class);
                data.add(phieuGiamGia);
                adapter.notifyItemInserted(data.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PhieuGiamGia phieuGiamGia = snapshot.getValue(PhieuGiamGia.class);
                for (int i = 0; i < data.size(); ++i) {
                    if (data.get(i).getId_Phieu().equals(phieuGiamGia.getId_Phieu())) {
                        data.set(i, phieuGiamGia);
                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                PhieuGiamGia phieuGiamGia = snapshot.getValue(PhieuGiamGia.class);
                for (int i = 0; i < data.size(); ++i) {
                    if (data.get(i).getId_Phieu().equals(phieuGiamGia.getId_Phieu())) {
                        data.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        edtSearch = findViewById(R.id.edtSearchBox);
        btnDaSuDung = findViewById(R.id.btnDaSuDung);
        btnSort = findViewById(R.id.btnSort);
        btnAdd = findViewById(R.id.btnAdd);
        listPhieu = findViewById(R.id.listPhieu);
    }
}