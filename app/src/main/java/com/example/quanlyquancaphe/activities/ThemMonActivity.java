package com.example.quanlyquancaphe.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.LoaiMon;
import com.example.quanlyquancaphe.models.Mon;
import com.example.quanlyquancaphe.ultilities.NotificationUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ThemMonActivity extends AppCompatActivity {
    Toolbar toolBar;
    ImageView ivHinh;
    EditText edtTenMon, edtMoTa, edtDonGia, edtGiamGia;
    Spinner spnLoai;
    Button btnThem;
    ArrayList<LoaiMon> loaiMonArrayList = new ArrayList<>();
    String[] spinnerArray = new String[3];
    ArrayAdapter spinnerAdapter;
    Mon mon = new Mon();
    String imageURL = "";
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_themmon_layout);
        setControl();
        toolBar.setTitle("Thêm món");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getDataSpinner();

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == Activity.RESULT_OK) {
                    Intent data = activityResult.getData();
                    uri = data.getData();
                    ivHinh.setImageURI(uri);
                } else {
                    Toast.makeText(ThemMonActivity.this, "Chưa chọn hình", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imagePicker = new Intent(Intent.ACTION_PICK);
                imagePicker.setType("image/*");
                launcher.launch(imagePicker);
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    save();
                }
            }
        });
        spnLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LoaiMon loaiMon = loaiMonArrayList.get(i);
                mon.setId_Loai(loaiMon.getId_loai());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mon.setId_Loai(loaiMonArrayList.get(0).getId_loai());
            }
        });

    }
    private void getDataSpinner(){
        databaseReference = FirebaseDatabase.getInstance().getReference("LoaiMon");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                     LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                     loaiMonArrayList.add(loaiMon);
                     spinnerArray[loaiMon.getId_loai()] = loaiMon.getTen_loai();
                 }
                 spinnerAdapter = new ArrayAdapter(ThemMonActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray);
                 spnLoai.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setControl() {
        ivHinh = findViewById(R.id.ivHinh);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtDonGia = findViewById(R.id.edtDonGia);
        edtGiamGia = findViewById(R.id.edtGiamGia);
        spnLoai = findViewById(R.id.spnLoai);
        btnThem = findViewById(R.id.btnThem);
        toolBar = findViewById(R.id.toolBar);

    }

    private void save() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.child("Mon Images").child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                mon.setHinh(imageURL);
                upload();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(ThemMonActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upload() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        String auto_Id = databaseReference.push().getKey();
        mon.setId_Mon(auto_Id);
        databaseReference.child(auto_Id).setValue(mon).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    NotificationUtility.updateNotiOnFirebase(3, "Có món mới trên hệ thống: "+mon.getTenMon());
                    Toast.makeText(ThemMonActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ThemMonActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Boolean validate() {
        if (uri == null) {
            Toast.makeText(this, "Chưa chọn hình", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtTenMon.getText().toString().isEmpty()) {
            edtTenMon.requestFocus();
            edtTenMon.setError("Empty");
            return false;
        }
        mon.setTenMon(edtTenMon.getText().toString());
        if (edtMoTa.getText().toString().isEmpty()) {
            mon.setMoTa("");
        }
        else {
            mon.setMoTa(edtMoTa.getText().toString());
        }
        if (edtDonGia.getText().toString().isEmpty()) {
            edtDonGia.requestFocus();
            edtDonGia.setError("Empty");
            return false;
        }
        if (Double.parseDouble(edtDonGia.getText().toString()) > 9999999) {
            edtDonGia.requestFocus();
            edtDonGia.setError(">9999999");
            return false;
        }
        mon.setDonGia(Double.parseDouble(edtDonGia.getText().toString()));
        if (edtGiamGia.getText().toString().isEmpty()) {
            mon.setGiamGia(0);
        } else if (Integer.parseInt(edtGiamGia.getText().toString()) > 100) {
            edtGiamGia.requestFocus();
            edtGiamGia.setError(">100");
            return false;
        } else {
            mon.setGiamGia(Integer.parseInt(edtGiamGia.getText().toString()));
        }
        return true;
    }


}
