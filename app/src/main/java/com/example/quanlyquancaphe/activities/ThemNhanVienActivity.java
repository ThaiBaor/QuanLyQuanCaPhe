package com.example.quanlyquancaphe.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
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
import com.example.quanlyquancaphe.models.NhanVien;
import com.example.quanlyquancaphe.models.ViTri;
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
import java.util.List;

public class ThemNhanVienActivity extends AppCompatActivity {
    Toolbar toolBar;
    Button btnThem;
    ImageView ivAvatar, ivImagesCCCDT, ivImagesCCCDS;
    EditText edtMaNV, edtTenNV, edtDiachi, edtSDT, edtMatkhau;
    Uri imageCCDT;
    Uri imageAvatar;
    Uri imageCCDS;
    String avatarUrl;
    String CCCDTUrl;
    String CCCDSUrl;
    FirebaseDatabase database;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Spinner spVitri;
    String viTri;
    ArrayList<ViTri> arrayListSpinner = new ArrayList<>();
    ArrayAdapter spinnerAdapter;
    String[] spinnerArray = new String[3];
    String maNV, tenNV, diaChi, soDT, matKhau;
    List<String> maNVDaTonTai = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_themnhanvien_layout);
        layDsMaNhanVienDaTonTai();
        setCtrol();
        setEvent();
        showDataSpiner();
    }

    private void setEvent() {
        toolBar.setTitle("Thêm nhân viên");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActivityResultLauncher<Intent> activityResultAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageAvatar = data.getData();
                            ivAvatar.setImageURI(imageAvatar);
                        } else {
                            Toast.makeText(ThemNhanVienActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        ActivityResultLauncher<Intent> activityResulCCCDT = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageCCDT = data.getData();
                            ivImagesCCCDT.setImageURI(imageCCDT);
                        } else {
                            Toast.makeText(ThemNhanVienActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        ActivityResultLauncher<Intent> activityResulCCCDS = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageCCDS = data.getData();
                            ivImagesCCCDS.setImageURI(imageCCDS);
                        } else {
                            Toast.makeText(ThemNhanVienActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    uploaddata();
                }
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultAvatar.launch(photoPicker);
            }
        });
        ivImagesCCCDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResulCCCDT.launch(photoPicker);
            }
        })
        ;
        ivImagesCCCDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResulCCCDS.launch(photoPicker);
            }
        });
        spVitri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ViTri vitri = arrayListSpinner.get(i);
                viTri = vitri.getVitri();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spVitri.setSelection(0);
            }
        });
    }

    private void showDataSpiner() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Role");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListSpinner.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    ViTri vitri = items.getValue(ViTri.class);
                    arrayListSpinner.add(vitri);
                    spinnerArray[vitri.getId() - 1] = vitri.getVitri();
                }
                spinnerAdapter = new ArrayAdapter<>(ThemNhanVienActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray);
                spVitri.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void saveData() {
        String key = databaseReference.push().getKey();
        NhanVien nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, avatarUrl.toString(), CCCDTUrl.toString(), CCCDSUrl.toString(), viTri);
        database.getInstance().getReference("Nhanvien").child(nhanVien.getMaNhanVien())
                .setValue(nhanVien).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ThemNhanVienActivity.this, " Thêm thành công !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ThemNhanVienActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void layDsMaNhanVienDaTonTai() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Nhanvien");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    maNVDaTonTai.add(dataSnapshot.child("maNhanVien").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean kiemTraMaNVDaTonTai(String maNV) {
        for (String ma : maNVDaTonTai) {
            if (ma.equals(maNV)) {
                return true;
            }
        }
        return false;
    }

    private void uploaddata() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(imageAvatar.getLastPathSegment());
        StorageReference storageReferenceCCCDT = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(imageCCDT.getLastPathSegment());
        StorageReference storageReferenceCCCDS = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(imageCCDS.getLastPathSegment());
        storageReferenceCCCDS.putFile(imageCCDS).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                CCCDSUrl = urlImage.toString();

            }
        });
        storageReferenceCCCDT.putFile(imageCCDT).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                CCCDTUrl = urlImage.toString();
            }
        });
        storageReference.putFile(imageAvatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                avatarUrl = urlImage.toString();
                saveData();
                dialog.dismiss();
            }
        });

    }

    private void setCtrol() {
        ivAvatar = findViewById(R.id.ivAvatarNhanVien);
        ivImagesCCCDS = findViewById(R.id.ivCCCDSau);
        ivImagesCCCDT = findViewById(R.id.ivCCCDTruoc);
        btnThem = findViewById(R.id.btnThemData);
        edtMaNV = findViewById(R.id.edtMaNV);
        edtTenNV = findViewById(R.id.edtTenNV);
        edtDiachi = findViewById(R.id.edtDiaChi);
        edtSDT = findViewById(R.id.edtSDT);
        edtMatkhau = findViewById(R.id.edtMatKhau);
        spVitri = findViewById(R.id.spViTri);
        toolBar = findViewById(R.id.toolBar);
    }

    private Boolean validate() {
        if (imageAvatar == null && imageCCDS == null && imageCCDT == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh ", Toast.LENGTH_SHORT).show();
            return false;
        }
        maNV = edtMaNV.getText().toString();
        if (maNV.isEmpty() || maNV.length() > 11) {
            edtMaNV.setError("Mã nhân viên không hợp lệ ");
            return false;
        }
        if (kiemTraMaNVDaTonTai(maNV)) {
            edtMaNV.requestFocus();
            edtMaNV.setError("Mã nhân viên không đã tồn tại");
            return false;
        }
        tenNV = edtTenNV.getText().toString();
        if (tenNV.isEmpty() || tenNV.length() > 256) {
            edtTenNV.setError("Vui lòng nhập hợp lệ");
            return false;
        }
        soDT = edtSDT.getText().toString();
        if (soDT.isEmpty() || soDT.length() > 10) {
            edtSDT.setError("Số điện thoại không hợp lệ");
            return false;
        }
        diaChi = edtDiachi.getText().toString();
        if (diaChi.isEmpty() || diaChi.length() > 256) {
            edtDiachi.setError("Địa chỉ không hợp lệ ");
            return false;
        }
        matKhau = edtMatkhau.getText().toString();
        if (matKhau.isEmpty() || matKhau.length() < 6 || matKhau.length() > 20) {
            edtMatkhau.setError("Password không hợp lệ");
            return false;
        }
        return true;
    }


}