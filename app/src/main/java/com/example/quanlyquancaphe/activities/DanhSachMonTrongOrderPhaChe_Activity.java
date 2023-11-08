package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.DanhSachMonTrongOrderPhaCheAdapter;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhSachMonTrongOrderPhaChe_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
   List<ChiTietMon>list_CT = new ArrayList<>();
    DanhSachMonTrongOrderPhaCheAdapter adapter;

    Bundle bundle;
    String id_DSMon_TB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_danhsachmontrongorder_pha_che_layout);
        SetControl();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            id_DSMon_TB = bundle.getString("key");
        }
        setEvent();
    }

    private void setEvent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhSachMonTrongOrderPhaCheAdapter(DanhSachMonTrongOrderPhaChe_Activity.this, list_CT);
        recyclerView.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_DSMon_TB).child("HT");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_CT.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                  ChiTietMon chiTietMon = dataSnapshot.getValue(ChiTietMon.class);
                  list_CT.add(chiTietMon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SetControl() {
        recyclerView = findViewById(R.id.ryvDSCMOrder);
    }
}