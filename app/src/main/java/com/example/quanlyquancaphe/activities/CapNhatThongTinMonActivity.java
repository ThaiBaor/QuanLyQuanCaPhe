package com.example.quanlyquancaphe.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.LoaiMon;
import com.example.quanlyquancaphe.models.Mon;
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

public class CapNhatThongTinMonActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivHinh;
    EditText edtTenMon, edtMoTa, edtDonGia, edtGiamGia;
    Spinner spnLoai;
    Button btnCapNhat;
    ArrayList<LoaiMon> loaiMonArrayList = new ArrayList<>();
    String[] spinnerArray = new String[3];
    ArrayAdapter spinnerAdapter;
    Mon mon = new Mon();
    String imageURL = "", newImageURL="";
    Uri uri;
    Bundle bundle;
    DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_capnhatthongtinmon_layout);
        setControl();
        toolbar.setTitle("Cập nhật thông tin món");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bundle = getIntent().getExtras();
        loadDataItem();
        loadDataSpinner();
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == Activity.RESULT_OK) {
                    Intent data = activityResult.getData();
                    uri = data.getData();
                    ivHinh.setImageURI(uri);
                } else {
                    Toast.makeText(CapNhatThongTinMonActivity.this, "Chưa chọn hình", Toast.LENGTH_SHORT).show();
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
        spnLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mon.setId_Loai(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CapNhatThongTinMonActivity.this, imageURL.toString(), Toast.LENGTH_SHORT).show();
                if (validate()){
                    save();
                }
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
        btnCapNhat = findViewById(R.id.btnCapNhat);
        toolbar = findViewById(R.id.toolBar);
    }

    private void loadDataSpinner() {
        databaseReference = FirebaseDatabase.getInstance().getReference("LoaiMon");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loaiMonArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                    loaiMonArrayList.add(loaiMon);
                    spinnerArray[loaiMon.getId_loai()] = loaiMon.getTen_loai();
                }
                spinnerAdapter = new ArrayAdapter(CapNhatThongTinMonActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray);
                spnLoai.setAdapter(spinnerAdapter);
                spnLoai.setSelection(mon.getId_Loai());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Boolean validate() {

        // giam gia
        if (edtGiamGia.getText().toString().isEmpty()) {
            mon.setGiamGia(0);
        } else if (Integer.parseInt(edtGiamGia.getText().toString()) > 100) {
            edtGiamGia.setError(">100");
            return false;
        } else {
            mon.setGiamGia(Integer.parseInt(edtGiamGia.getText().toString()));
        }
        // don gia
        if (edtDonGia.getText().toString().isEmpty()) {
            edtDonGia.setError("Empty");
            return false;
        }
        if (Double.parseDouble(edtDonGia.getText().toString()) > 9999999) {
            edtDonGia.setError(">9999999");
            return false;
        }
        mon.setDonGia(Double.parseDouble(edtDonGia.getText().toString()));
        // mota
        if (edtMoTa.getText().toString().isEmpty()) {
            mon.setMoTa("");
            return false;
        }
        mon.setMoTa(edtMoTa.getText().toString());
        if (edtTenMon.getText().toString().isEmpty()) {
            edtTenMon.setError("Empty");
            return false;
        }
        mon.setTenMon(edtTenMon.getText().toString());
        return true;
    }
    private void loadDataItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (bundle != null){
            imageURL = bundle.getString("hinh");
            mon.setHinh(bundle.getString("hinh"));
            Glide.with(CapNhatThongTinMonActivity.this).load(mon.getHinh()).into(ivHinh);
            mon.setId_Mon(bundle.getString("id_Mon"));
            mon.setTenMon(bundle.getString("tenMon"));
            edtTenMon.setText(mon.getTenMon());
            mon.setMoTa(bundle.getString("moTa"));
            edtMoTa.setText(mon.getMoTa());
            mon.setDonGia(bundle.getDouble("donGia"));
            edtDonGia.setText(String.valueOf(mon.getDonGia()));
            mon.setGiamGia(bundle.getInt("giamGia"));
            edtGiamGia.setText(String.valueOf(mon.getGiamGia()));
            mon.setId_Loai(bundle.getInt("id_Loai"));
        }
        dialog.dismiss();
    }
    private void save() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (uri!=null){
        storageReference.child("Mon Images").child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                newImageURL = urlImage.toString();
                mon.setHinh(newImageURL);
                update();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(CapNhatThongTinMonActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });}
        else {
            update();
            dialog.dismiss();
        }
    }
    private void update() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        databaseReference.child(mon.getId_Mon()).setValue(mon).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageURL);
                    storageReference.delete();
                    Toast.makeText(CapNhatThongTinMonActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CapNhatThongTinMonActivity.this, "Lỗi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}