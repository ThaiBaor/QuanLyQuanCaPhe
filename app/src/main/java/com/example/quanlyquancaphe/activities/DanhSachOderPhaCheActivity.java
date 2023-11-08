package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.DanhSachOrderPhaCheAdapter;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhSachOderPhaCheActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<String> data_CT = new ArrayList<>();
    List<String> dataBan = new ArrayList<>();
    List<ChiTietMon> data_Mon = new ArrayList<>();
    DanhSachOrderPhaCheAdapter adapter;
    ValueEventListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danhsachoder_phache_layout);
        setControl();
        getData();
        getTenBan();
    }
    private void getTenBan() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhSachOrderPhaCheAdapter(DanhSachOderPhaCheActivity.this, dataBan, data_Mon,data_CT);
        recyclerView.setAdapter(adapter);
        DatabaseReference referenceBan = FirebaseDatabase.getInstance().getReference("Ban");
        listener = referenceBan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBan.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String idBan = dataSnapshot.child("id_Ban").getValue(String.class);
                    if (data_CT.contains(idBan)) {
                        String tenBan = dataSnapshot.child("tenBan").getValue(String.class);
                        dataBan.add(tenBan);
                    }
                }

                adapter.notifyDataSetChanged();
                //Lấy thời gian gọi món
                for (int i = 0; i < data_CT.size(); i++) {
                    String chiTietMonId = data_CT.get(i);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(chiTietMonId).child("HT");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ChiTietMon mon = dataSnapshot.getValue(ChiTietMon.class);
                                data_Mon.add(mon);
                                break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon");
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data_CT.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    data_CT.add(dataSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {

        recyclerView = findViewById(R.id.recycleDanhSachOrderPhaChe);
    }
}