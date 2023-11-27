package com.example.quanlyquancaphe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.BanAdapter;
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
import java.util.List;

public class ThemBanActivity extends AppCompatActivity {

    EditText edtMaBan, edtTenBan, edtSoChoNgoi;
    Spinner spKhu;
    Button btnAddBan;
    Toolbar toolBar;
    FirebaseDatabase database;
    DatabaseReference reference;
    ValueEventListener eventListener;
    ArrayList<Ban> dataBan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_themban_layout);
        setControl();

        setEvent();
    }

    //data spinner
    List<String> data = new ArrayList<>();

    private void setEvent() {
        //set tool bar
        toolBar.setTitle("Thêm bàn");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DataSpinner();
        getDataBan();

        spKhu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ThemBan_Activity.this, data[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAddBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() == true) {
                    AddData();
                    finish();
                }
            }
        });
    }

    public void DataSpinner() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Khu");
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Khu khu = item.getValue(Khu.class);
                    data.add(khu.getTenKhu());
                    ArrayAdapter adapter = new ArrayAdapter(ThemBanActivity.this, android.R.layout.simple_spinner_item, data);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKhu.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chuyenManHinh(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        activity.finish();
    }
    private Boolean validate() {
        if (edtMaBan.getText().toString().isEmpty()) {
            edtMaBan.requestFocus();
            edtMaBan.setError("Mã bàn trống");
            return false;
        }
        if (edtMaBan.getText().length() > 10) {
            edtMaBan.requestFocus();
            edtMaBan.setError("Tối đa 10 ký tự");
            return false;
        }
        for (Ban item : dataBan){
            if (edtMaBan.getText().toString().equals(item.getId_Ban())){
                edtMaBan.requestFocus();
                edtMaBan.setError("Trùng mã bàn");
                return false;
            }
        }
        if (edtTenBan.getText().toString().isEmpty()) {
            edtTenBan.requestFocus();
            edtTenBan.setError("Tên bàn trống");
            return false;
        }
        if (edtSoChoNgoi.getText().toString().isEmpty()) {
            edtSoChoNgoi.requestFocus();
            edtSoChoNgoi.setError("Số chỗ ngồi trống");
            return false;
        }
        return true;
    }
    //Cập nhật data bàn

    private void AddData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Ban");
        String maban = edtMaBan.getText().toString();
        String tenBan = edtTenBan.getText().toString();
        Integer soChoNgoi = Integer.parseInt(edtSoChoNgoi.getText().toString());
        Integer maTrangThai = 0;
        Integer maKhu = spKhu.getSelectedItemPosition();
        Ban ban = new Ban(maban, tenBan, soChoNgoi, maKhu, maTrangThai);
        databaseReference.child(maban).setValue(ban).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ThemBanActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ThemBanActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        edtMaBan.setText("");
        edtTenBan.setText("");
        edtSoChoNgoi.setText("");
        spKhu.setSelection(0);
    }
    private void getDataBan(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Ban");
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBan.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Ban ban = item.getValue(Ban.class);
                    dataBan.add(ban);
                }
                //adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setControl() {
        edtMaBan = findViewById(R.id.edtMaBan);
        edtTenBan = findViewById(R.id.edtTenBan);
        edtSoChoNgoi = findViewById(R.id.edtSoChoNgoi);
        spKhu = findViewById(R.id.spKhu);
        btnAddBan = findViewById(R.id.btnAddBan);
        toolBar = findViewById(R.id.toolBar);
        toolBar = findViewById(R.id.toolBar);
    }
}