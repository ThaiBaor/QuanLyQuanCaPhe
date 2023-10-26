package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.Khu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CapNhatBanActivity extends AppCompatActivity {
    Toolbar toolBar;
    EditText edtMaBan, edtTenBan, edtSoNguoiNgoi;
    Spinner spKhu;
    Button btnUpdate;
    Bundle bundle;
    String maBan, tenBan;
    Integer id_Khu, soChoNgoi;
    ArrayList<Ban> data = new ArrayList<>();
    ArrayList<Khu> datakhu = new ArrayList<>();
    ArrayList<String> dataSpinner = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_capnhatban_layout);
        setConTrol();
        toolBar.setTitle("Cập nhật bàn");
        //Truyền dữ liệu
        bundle = getIntent().getExtras();
        DataSpinner();
        InitDataItem();
        setEvent();
    }

    //get data ban
    private void InitDataItem() {
        if (bundle != null) {
            edtMaBan.setText(bundle.getString("id_Ban"));
            edtTenBan.setText(bundle.getString("tenBan"));
            edtSoNguoiNgoi.setText(String.valueOf(bundle.getInt("soChoNgoi")));
        }
    }

    //get data spinner
    private void DataSpinner() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Khu");
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSpinner.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Khu khu = item.getValue(Khu.class);
                    dataSpinner.add(khu.getTenKhu());
                    ArrayAdapter adapter = new ArrayAdapter(CapNhatBanActivity.this, android.R.layout.simple_spinner_item, dataSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKhu.setAdapter(adapter);
                }
                spKhu.setSelection(bundle.getInt("id_Khu"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        // Chuyển về màn hình quản lý bàn
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Update data
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
                edtMaBan.setText("");
                edtTenBan.setText("");
                edtSoNguoiNgoi.setText("");
                spKhu.setSelection(0);
            }
        });
    }

    //Hàm update data
    private void Update() {
        Ban ban = new Ban();
        ban.setId_Ban(edtMaBan.getText().toString());
        ban.setTenBan(edtTenBan.getText().toString());
        ban.setSoChoNgoi(Integer.parseInt(edtSoNguoiNgoi.getText().toString()));
        ban.setId_Khu(spKhu.getSelectedItemPosition());
        ban.setId_TrangThaiBan(0);
        reference = FirebaseDatabase.getInstance().getReference("Ban");
        reference.child(ban.getId_Ban()).setValue(ban).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CapNhatBanActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CapNhatBanActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setConTrol() {
        toolBar = findViewById(R.id.toolBar);
        edtMaBan = findViewById(R.id.edtMaBan);
        edtTenBan = findViewById(R.id.edtTenBan);
        edtSoNguoiNgoi = findViewById(R.id.edtSoChoNgoi);
        spKhu = findViewById(R.id.spKhu);
        btnUpdate = findViewById(R.id.btnUpdate);
    }
}