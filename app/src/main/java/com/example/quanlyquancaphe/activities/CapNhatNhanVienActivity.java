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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class CapNhatNhanVienActivity extends AppCompatActivity {
    Toolbar toolBar;
    EditText edtEditMaNhanVien, edtEdtTenNhanVien, edtEditDiaChi, editEditMatKhau, edtEditSDT;
    Spinner spEditViTri;
    ImageView ivEditAvatar, ivEditCCCDT, ivEditCCCDS;
    TextView tvTest;
    Button btnCapNhap;
    String itemsViTri;
    ArrayList<ViTri> arrayListSpinner = new ArrayList<>();
    ArrayAdapter spinnerAdapter;
    String[] spinnerArray = new String[4];
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Uri CCCDTUri;
    Uri avatarUri;
    Uri CCCDSUri;
    String newAvatarImageUrl = null;
    String newCCCDTImageUrl = null;
    String newCCCDSImageUrl = null;
    String key;
    String avatarImageUrl;
    String CCCDTImageUrl;
    String CCCDSImageUrl;
    String spiner;
    NhanVien nhanVien = new NhanVien();
    Bundle bundle;
    StorageReference storageReferenceAvatar;
    StorageReference storageReferenceCCCDT;
    StorageReference storageReferenceCCCDS;
    String tenNV, diaChi, soDT, matKhau;
    String maNV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_capnhatnhanvien_layout);
        setControl();
        setEvent();
        bundle = getIntent().getExtras();
        getData();
        showDataSpiner();
    }

    private void getData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (bundle != null) {
            Glide.with(CapNhatNhanVienActivity.this).load(bundle.getString("Avatar")).into(ivEditAvatar);
            Glide.with(CapNhatNhanVienActivity.this).load(bundle.getString("CCCDT")).into(ivEditCCCDT);
            Glide.with(CapNhatNhanVienActivity.this).load(bundle.getString("CCCDS")).into(ivEditCCCDS);
            edtEditMaNhanVien.setText(bundle.getString("MaNV"));
            maNV = bundle.getString("MaNV");
            edtEdtTenNhanVien.setText(bundle.getString("TenNV"));
            edtEditSDT.setText(bundle.getString("SDT"));
            edtEditDiaChi.setText(bundle.getString("DiaChi"));
            editEditMatKhau.setText(bundle.getString("MatKhau"));
            spiner = bundle.getString("Vitri");
            avatarImageUrl = bundle.getString("Avatar");
            CCCDTImageUrl = bundle.getString("CCCDT");
            CCCDSImageUrl = bundle.getString("CCCDS");
            key = bundle.getString("MaNV");
            nhanVien.setViTri(bundle.getString("Vitri"));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Nhanvien").child(key);
        dialog.dismiss();
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
                spinnerAdapter = new ArrayAdapter<>(CapNhatNhanVienActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray);
                spEditViTri.setAdapter(spinnerAdapter);

                for (int i = 0; i < 3; i++) {
                    if (spinnerArray[i].equals(nhanVien.getViTri())) {
                        spEditViTri.setSelection(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setEvent() {
        toolBar.setTitle("Cập nhật nhân viên");
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
                            avatarUri = data.getData();
                            ivEditAvatar.setImageURI(avatarUri);
                        } else {
                            Toast.makeText(CapNhatNhanVienActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
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
                            CCCDTUri = data.getData();
                            ivEditCCCDT.setImageURI(CCCDTUri);
                        } else {
                            Toast.makeText(CapNhatNhanVienActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
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
                            CCCDSUri = data.getData();
                            ivEditCCCDS.setImageURI(CCCDSUri);
                        } else {
                            Toast.makeText(CapNhatNhanVienActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        spEditViTri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ViTri vitri = arrayListSpinner.get(i);
                itemsViTri = vitri.getVitri();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spEditViTri.setSelection(0);
            }
        });
        btnCapNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    return;
                }
                if (avatarUri != null && CCCDTUri != null && CCCDSUri != null) {
                    saveData();
                }
                if (avatarUri == null && CCCDTUri == null && CCCDSUri == null) {
                    upLoadData();
                }
                if (avatarUri != null && CCCDTUri == null && CCCDSUri == null) {
                    saveDataAvatar();
                }
                if (avatarUri == null && CCCDTUri != null && CCCDSUri == null) {
                    saveDataCCCDT();
                }
                if (avatarUri == null && CCCDTUri == null && CCCDSUri != null) {
                    saveDataCCCDS();
                }
                if (avatarUri == null && CCCDTUri != null && CCCDSUri != null) {
                    saveDataCCCDTCCCDS();
                }
                if (avatarUri != null && CCCDTUri != null && CCCDSUri == null) {
                   saveDataAvatarCCCDT();
                }
                if (avatarUri != null && CCCDTUri == null && CCCDSUri != null) {
                    saveDataAvatarCCCDS();
                }
            }
        });
        ivEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultAvatar.launch(photoPicker);

            }
        });
        ivEditCCCDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResulCCCDT.launch(photoPicker);

            }
        });
        ivEditCCCDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResulCCCDS.launch(photoPicker);

            }
        });

    }


    private void setControl() {
        tvTest = findViewById(R.id.tvTestLoi);
        btnCapNhap = findViewById(R.id.btnCapNhapData);
        edtEditMaNhanVien = findViewById(R.id.edtEditMaNV);
        edtEdtTenNhanVien = findViewById(R.id.edtEditTenNV);
        edtEditDiaChi = findViewById(R.id.edtEditDiaChi);
        editEditMatKhau = findViewById(R.id.edtEditMatKhau);
        edtEditSDT = findViewById(R.id.edtEditSDT);
        spEditViTri = findViewById(R.id.spEditViTri);
        ivEditAvatar = findViewById(R.id.ivEditAvatarNhanVien);
        ivEditCCCDT = findViewById(R.id.ivEditCCCDTruoc);
        ivEditCCCDS = findViewById(R.id.ivEditCCCDSau);
        toolBar = findViewById(R.id.toolBar);
    }

    private void upLoadData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Nhanvien");
        StorageReference referenceAvatar = FirebaseStorage.getInstance().getReferenceFromUrl(avatarImageUrl);
        StorageReference referenceCCCDT = FirebaseStorage.getInstance().getReferenceFromUrl(CCCDTImageUrl);
        StorageReference referenceCCCDS = FirebaseStorage.getInstance().getReferenceFromUrl(CCCDSImageUrl);
        if (newAvatarImageUrl == null && newCCCDSImageUrl == null && newCCCDTImageUrl == null) {
            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, avatarImageUrl, CCCDTImageUrl, CCCDSImageUrl, itemsViTri);
        }
        if (newAvatarImageUrl != null && newCCCDSImageUrl == null && newCCCDTImageUrl == null) {
            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, newAvatarImageUrl, CCCDTImageUrl, CCCDSImageUrl, itemsViTri);
            referenceAvatar.delete();
        }
        if (newAvatarImageUrl == null && newCCCDSImageUrl != null && newCCCDTImageUrl == null) {

            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, avatarImageUrl, CCCDTImageUrl, newCCCDSImageUrl, itemsViTri);
            referenceCCCDS.delete();
        }
        if (newAvatarImageUrl == null && newCCCDSImageUrl == null && newCCCDTImageUrl != null) {
            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, avatarImageUrl, newCCCDTImageUrl, CCCDSImageUrl, itemsViTri);
            referenceCCCDT.delete();
        }
        if (newAvatarImageUrl != null && newCCCDSImageUrl != null && newCCCDTImageUrl != null) {
            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, newAvatarImageUrl, newCCCDSImageUrl, newCCCDTImageUrl, itemsViTri);
            referenceAvatar.delete();
            referenceCCCDS.delete();
            referenceCCCDT.delete();
        }
        if (newAvatarImageUrl == null && newCCCDSImageUrl != null && newCCCDTImageUrl != null) {
            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, avatarImageUrl, newCCCDSImageUrl, newCCCDTImageUrl, itemsViTri);
            referenceCCCDS.delete();
            referenceCCCDT.delete();
        }
        if (newAvatarImageUrl != null && newCCCDSImageUrl == null && newCCCDTImageUrl != null) {
            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, newAvatarImageUrl, CCCDSImageUrl, newCCCDTImageUrl, itemsViTri);
            referenceAvatar.delete();
            referenceCCCDT.delete();
        }
        if (newAvatarImageUrl != null && newCCCDSImageUrl != null && newCCCDTImageUrl == null) {
            nhanVien = new NhanVien(tenNV, maNV, diaChi, soDT, matKhau, newAvatarImageUrl, newCCCDSImageUrl, CCCDTImageUrl, itemsViTri);
            referenceAvatar.delete();
            referenceCCCDS.delete();
        }
        databaseReference.child(maNV).setValue(nhanVien).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CapNhatNhanVienActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReferenceAvatar = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(avatarUri.getLastPathSegment());
        storageReferenceCCCDT = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDTUri.getLastPathSegment());
        storageReferenceCCCDS = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDSUri.getLastPathSegment());
        storageReferenceCCCDS.putFile(CCCDSUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDSImageUrl = urlImage.toString();
                upLoadData();
            }
        });
        storageReferenceCCCDT.putFile(CCCDTUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDTImageUrl = urlImage.toString();
                upLoadData();
            }
        });
        storageReferenceAvatar.putFile(avatarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newAvatarImageUrl = urlImage.toString();
                upLoadData();
                dialog.dismiss();
            }
        });
    }

    private void saveDataAvatar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(avatarUri.getLastPathSegment());
        storageReference.putFile(avatarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newAvatarImageUrl = urlImage.toString();
                upLoadData();
                dialog.dismiss();
            }
        });
    }

    private void saveDataCCCDT() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDTUri.getLastPathSegment());
        storageReference.putFile(CCCDTUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDTImageUrl = urlImage.toString();
                upLoadData();
                dialog.dismiss();
            }
        });

    }

    private void saveDataCCCDS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        StorageReference storageReferenceCCCDT = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDSUri.getLastPathSegment());
        storageReferenceCCCDT.putFile(CCCDSUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDSImageUrl = urlImage.toString();
                upLoadData();
                dialog.dismiss();
            }
        });
    }
    private void saveDataCCCDTCCCDS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        StorageReference storageReferenceCCCDS = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDSUri.getLastPathSegment());
        StorageReference storageReferenceCCCDT = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDTUri.getLastPathSegment());
        storageReferenceCCCDT.putFile(CCCDTUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDSImageUrl = urlImage.toString();
                dialog.dismiss();
            }
        });
        storageReferenceCCCDS.putFile(CCCDSUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDTImageUrl = urlImage.toString();
                upLoadData();
                dialog.dismiss();
            }
        });
    }
    private void saveDataAvatarCCCDT() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        StorageReference storageReferenceAvatar = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(avatarUri.getLastPathSegment());
        StorageReference storageReferenceCCCDT = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDTUri.getLastPathSegment());
        storageReferenceCCCDT.putFile(CCCDTUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDSImageUrl = urlImage.toString();
                dialog.dismiss();
            }
        });
        storageReferenceAvatar.putFile(avatarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newAvatarImageUrl = urlImage.toString();
                upLoadData();
                dialog.dismiss();
            }
        });
    }
    private void saveDataAvatarCCCDS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang lưu dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        StorageReference storageReferenceCCCDS = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(CCCDSUri.getLastPathSegment());
        StorageReference storageReferenceAvatar = FirebaseStorage.getInstance().getReference().child("Nhan Vien Image")
                .child(avatarUri.getLastPathSegment());
        storageReferenceAvatar.putFile(avatarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newAvatarImageUrl = urlImage.toString();
                dialog.dismiss();
            }
        });
        storageReferenceCCCDS.putFile(CCCDSUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newCCCDTImageUrl = urlImage.toString();
                upLoadData();
                dialog.dismiss();
            }
        });
    }

    private Boolean validate() {
        tenNV = edtEdtTenNhanVien.getText().toString();
        if (tenNV.isEmpty() || tenNV.length() > 256) {
            edtEdtTenNhanVien.setError("Vui lòng nhập hợp lệ");
            return false;
        }
        soDT = edtEditSDT.getText().toString();
        if (soDT.isEmpty() || soDT.length() > 10) {
            edtEditSDT.setError("Số điện thoại không hợp lệ");
            return false;
        }
        diaChi = edtEditDiaChi.getText().toString();
        if (diaChi.isEmpty() || diaChi.length() > 256) {
            edtEditDiaChi.setError("Địa chỉ không hợp lệ ");
            return false;
        }
        matKhau = editEditMatKhau.getText().toString();
        if (matKhau.isEmpty() || matKhau.length() < 6 || matKhau.length() > 20) {
            editEditMatKhau.setError("Password không hợp lệ");
            return false;
        }
        return true;
    }


}

