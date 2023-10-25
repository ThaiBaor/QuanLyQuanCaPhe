package com.example.quanlyquancaphe.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.QuanLyKhuAdapter;
import com.example.quanlyquancaphe.models.Khu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class QuanLyKhuActivity extends AppCompatActivity {
    EditText edtSearchBox, edtMa, edtKhu;
    ImageButton btnadd, btnSort;
    Button btnUpdate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SwipeableRecyclerView recyclerView;
    QuanLyKhuAdapter quanLyKhuAdapter;
    ArrayList<Khu> data= new ArrayList<>();
    ArrayList<Khu> filterdata= new ArrayList<>();
    ValueEventListener valueEventListener;
    Integer sortc = 0;
    Khu khuAdd = new Khu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manghinh_quanlykhu_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quản lý khu");
        setControl();
        setEven();
    }

    private void setControl() {
        recyclerView =findViewById(R.id.recycle);
        edtSearchBox = findViewById(R.id.edtSearchBox);
        btnadd = findViewById(R.id.btnAdd);
        edtMa = findViewById(R.id.edtMa);
        edtKhu = findViewById(R.id.edtTenKhu);
        btnSort = findViewById(R.id.btnSort);
        btnUpdate = findViewById(R.id.btnCapNhat);
    }

    private void setEven() {
        khoiTao();
        quanLyKhuAdapter = new QuanLyKhuAdapter(QuanLyKhuActivity.this,data);
        SwipeableRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(quanLyKhuAdapter);
        recyclerView.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                Integer id = Integer.parseInt(data.get(position).getId_Khu().toString());
                String tenKhu = data.get(position).getTenKhu();
                edtMa.setText(id + "");
                edtKhu.setText(tenKhu);
                quanLyKhuAdapter.notifyDataSetChanged();
            }
            @Override
            public void onSwipedRight(int position) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Khu");
                databaseReference.child(String.valueOf(data.get(position).getId_Khu())).removeValue();
                data.remove(position);
                data.clear();
                quanLyKhuAdapter.notifyDataSetChanged();
            }
        });



        /*
         * Additional attributes:
         * */
        recyclerView.setRightBg(com.tsuryo.swipeablerv.R.color.blue);
        recyclerView.setRightImage(R.drawable.baseline_edit_24);

        recyclerView.setLeftBg(com.tsuryo.swipeablerv.R.color.red);
        recyclerView.setLeftImage(R.drawable.baseline_delete_24);

        recyclerView.setTextSize(62);
        recyclerView.setTextColor(R.color.white);

        // tim kiem
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtSearchBox.getText().equals("")){
                    search(charSequence);
                    quanLyKhuAdapter = new QuanLyKhuAdapter(QuanLyKhuActivity.this,filterdata);
                    recyclerView.setAdapter(quanLyKhuAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // them
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraThem() == true){
                    data.clear();
                    kiemTraThem();
                    String khu = edtKhu.getText().toString();
                    Integer id = Integer.parseInt(edtMa.getText().toString());
                    khuAdd = new Khu(id,khu);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("Khu");
                    databaseReference.child(String.valueOf(id)).setValue(khuAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(QuanLyKhuActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QuanLyKhuActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(QuanLyKhuActivity.this, "Nhập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // sap xep
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortc == 1){
                    ++sortc;
                    data.sort(new Comparator<Khu>() {
                        @Override
                        public int compare(Khu khu1, Khu khu2) {
                            return khu1.getTenKhu().substring(0,khu1.getTenKhu().length()).compareTo(khu2.getTenKhu().substring(0,khu2.getTenKhu().length())) > 1 ? 1 : -1;
                        }
                    });
                }
                else if(sortc == 0){
                    ++sortc;
                    data.sort(new Comparator<Khu>() {
                        @Override
                        public int compare(Khu khu1, Khu khu2) {
                            return khu1.getTenKhu().substring(0,khu1.getTenKhu().length()).compareTo(khu2.getTenKhu().substring(0,khu2.getTenKhu().length())) > 1 ? 1 : -1;
                        }
                    });
                }
                else {
                    data.clear();
                    khoiTao();
                    sortc = 0;
                }
                quanLyKhuAdapter.notifyDataSetChanged();
            }
        });
        // Sua
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraUpdate() == true){
                    update();
                    data.clear();
                    quanLyKhuAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(QuanLyKhuActivity.this, "Nhập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void search(CharSequence charSequence){
        filterdata.clear();
        for (Khu khu : data){
            if(khu.getTenKhu().toLowerCase().contains(charSequence.toString().toLowerCase())){
                filterdata.add(khu);
            }
        }
    }

    private void update() {

        Khu khu = new Khu();
        khu.setId_Khu(Integer.parseInt(edtMa.getText().toString()));
        khu.setTenKhu(edtKhu.getText().toString());
        databaseReference = FirebaseDatabase.getInstance().getReference("Khu");
        databaseReference.child(khu.getId_Khu() + "").setValue(khu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(QuanLyKhuActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuanLyKhuActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Boolean kiemTraThem(){
        if (edtMa.getText().toString().isEmpty()){
            edtMa.setError("Empty");
            return false;
        }
        khuAdd.setId_Khu(Integer.parseInt(edtMa.getText().toString()));
        if (edtKhu.getText().toString().isEmpty()){
            edtKhu.setError("Empty");
            return false;
        }
        khuAdd.setTenKhu(edtKhu.getText().toString());
        return true;
    }

    private boolean kiemTraUpdate(){
        if (edtMa.getText().toString().isEmpty()){
            edtMa.setError("Empty");
            return false;
        }
        if (edtKhu.getText().toString().isEmpty()){
            edtKhu.setError("Empty");
            return false;
        }
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        quanLyKhuAdapter.notifyDataSetChanged();
    }

    private void khoiTao(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Khu");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()){
                    Khu khu = item.getValue(Khu.class);
                    data.add(khu);
                }
                quanLyKhuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
