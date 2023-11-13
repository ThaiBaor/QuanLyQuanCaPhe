package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.DanhSachMonHoanThanhAdapter;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhSachMonHoanThanh_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ValueEventListener listener;
    List<String> key_node_CT = new ArrayList<>();
    DanhSachMonHoanThanhAdapter adapter;
    List<ChiTietMon> CT_TaiBan = new ArrayList<>();
    Toolbar toolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_monhoanthanh_layout);
        setControl();
        toolBar.setTitle("Danh Sách Món Hoàn Thành");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getKey();
    }
    private void getData(List<String> CT) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhSachMonHoanThanhAdapter(DanhSachMonHoanThanh_Activity.this, CT_TaiBan);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < CT.size(); i++) {
            String key_node = CT.get(i);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(key_node).child("HT");
            listener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                            Integer id_TrangThai = snapshot1.child("id_TrangThai").getValue(Integer.class);
                            if ( id_TrangThai.equals(3)) {
                               ChiTietMon CT_Mon = snapshot1.getValue(ChiTietMon.class);
                               CT_TaiBan.add(CT_Mon);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    // lấy key đầu tiên trong node
    private void getKey() {
        DatabaseReference reference_key = FirebaseDatabase.getInstance().getReference("ChiTietMon");
        listener = reference_key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                key_node_CT.clear();
                CT_TaiBan.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key_node_CT.add(dataSnapshot.getKey());
                }
                getData(key_node_CT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        toolBar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recycleMonHoanThanh);
    }
}