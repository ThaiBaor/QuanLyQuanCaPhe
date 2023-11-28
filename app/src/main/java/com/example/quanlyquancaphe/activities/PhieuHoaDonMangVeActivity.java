package com.example.quanlyquancaphe.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.adapters.PhieuHoaDonAdapter;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDonMangVe;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.models.PDF;
import com.example.quanlyquancaphe.models.PhieuGiamGia;
import com.example.quanlyquancaphe.ultilities.HoaDonUltility;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhieuHoaDonMangVeActivity extends AppCompatActivity {
    final static int REQUEST_CODE = 1232;
    TextView tvMHD, tvGioHD, tvNgayHD, tvTenKH, tvGiaHD, tvTongTien, tvtongTienSauGiam;
    EditText maGiamGia;
    Button btnQuayLai, btnThanhToan;
    RecyclerView recyclerView;
    Switch swGiamGia;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PhieuHoaDonAdapter adapter;
    Bundle bundle;
    String tenKH = "", id_Phieu = "";
    Double tongTienSauGiam = 0.0;
    Boolean check = false;
    HoaDonMangVe hoaDonMangVe = new HoaDonMangVe();
    ArrayList<ChiTietMon> dataChiTietMon = new ArrayList<>();
    ArrayList<ChiTietMon> dataGop = new ArrayList<>();
    ArrayList<PhieuGiamGia> dataPhieuGiamGia = new ArrayList<>();
    PhieuGiamGia phieuGiamGiaDaSuDung = new PhieuGiamGia();
    Bitmap bmp, scaledbmp, qr, scaledqr;
    String fileName;
    File downloadDir;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_phieuhoadonmangve_layout);
        bundle = getIntent().getExtras();
        setControl();
        loadDataThongTin();
        datachitietmon();
        loadDataPhieuGiamGia();
        adapter = new PhieuHoaDonAdapter(PhieuHoaDonMangVeActivity.this, dataChiTietMon);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
                createPDF();
                UploadFile(fileName);
                if (bundle != null) {
                    hoaDonMangVe.setId_HoaDon(bundle.getString("id_HoaDon"));
                    hoaDonMangVe.setDaThanhToan(bundle.getBoolean("daThanhToan"));
                    Boolean tt = true;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("HoaDon").child("MangVe");
                    taoChiTietMonQK(hoaDonMangVe.getId_HoaDon());
                    if (check) {
                        taoPhieuGiamGiaDaSuDung(hoaDonMangVe.getId_HoaDon(), id_Phieu);
                    }
                    resetTongTienHoaDon(hoaDonMangVe.getId_HoaDon(), tongTienSauGiam);
                    ref.child(hoaDonMangVe.getId_HoaDon()).child("daThanhToan").setValue(tt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(PhieuHoaDonMangVeActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(tenKH).child("HT");
                            databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                dataChiTietMon.clear();
            }
        });
        swGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberFormat nf = NumberFormat.getNumberInstance();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate currentDate = LocalDate.now();
                if (swGiamGia.isChecked()) {
                    // duyệt mãng dataPhieuGiamGia
                    for (int i = 0; i<dataPhieuGiamGia.size();i++) {
                        // kiểm tra i có là phần tử cuối cùng ko
                        if (i == dataPhieuGiamGia.size()-1){
                            if (dataPhieuGiamGia.get(i).getId_Phieu().equals(maGiamGia.getText().toString())) {
                                LocalDate otherDate = LocalDate.parse(dataPhieuGiamGia.get(i).getNgayHetHan(), formatter);
                                if (currentDate.compareTo(otherDate) < 0) {
                                    tongTienSauGiam = hoaDonMangVe.getTongTien() - hoaDonMangVe.getTongTien() * dataPhieuGiamGia.get(i).getGiaTri() / 100;
                                    tvtongTienSauGiam.setText(nf.format(tongTienSauGiam) + "đ");
                                    id_Phieu = dataPhieuGiamGia.get(i).getId_Phieu();
                                    swGiamGia.setChecked(true);
                                    check = true;
                                    return;
                                } else {
                                    Toast.makeText(PhieuHoaDonMangVeActivity.this, "Mã đã hết hạn!", Toast.LENGTH_SHORT).show();
                                    swGiamGia.setChecked(false);
                                    check = false;
                                }
                            } else {
                                Toast.makeText(PhieuHoaDonMangVeActivity.this, "Mã Không tồn tại!", Toast.LENGTH_SHORT).show();
                                swGiamGia.setChecked(false);

                                check = false;
                            }
                        }
                        else {
                            if (dataPhieuGiamGia.get(i).getId_Phieu().equals(maGiamGia.getText().toString())) {
                                LocalDate otherDate = LocalDate.parse(dataPhieuGiamGia.get(i).getNgayHetHan(), formatter);
                                if (currentDate.compareTo(otherDate) < 0) {
                                    tongTienSauGiam = hoaDonMangVe.getTongTien() - hoaDonMangVe.getTongTien() * dataPhieuGiamGia.get(i).getGiaTri() / 100;
                                    tvtongTienSauGiam.setText(nf.format(tongTienSauGiam) + "đ");
                                    id_Phieu = dataPhieuGiamGia.get(i).getId_Phieu();
                                    swGiamGia.setChecked(true);
                                    check = true;
                                    return;
                                } else {
                                    Toast.makeText(PhieuHoaDonMangVeActivity.this, "Mã đã hết hạn!", Toast.LENGTH_SHORT).show();
                                    swGiamGia.setChecked(false);
                                    check = false;
                                }
                            }
                        }
                    }
                } else {
                    tvtongTienSauGiam.setText(nf.format(hoaDonMangVe.getTongTien()) + "đ");
                }
            }
        });
    }

    private void setControl() {
        tvtongTienSauGiam = findViewById(R.id.tvTongTienDaGiamMV);
        maGiamGia = findViewById(R.id.maGiamGiaHDMV);
        swGiamGia = findViewById(R.id.swMaGiamGiaHoaDonMV);
        recyclerView = findViewById(R.id.recyclePhieuHoaDonMV);
        tvMHD = findViewById(R.id.tvMaMV);
        tvGioHD = findViewById(R.id.tvGioMV);
        tvGiaHD = findViewById(R.id.tvGiaHDMV);
        tvTongTien = findViewById(R.id.tvTongTienMV);
        tvNgayHD = findViewById(R.id.tvNgayMV);
        tvTenKH = findViewById(R.id.tvTenKH);
        btnQuayLai = findViewById(R.id.btnQuayLaiMV);
        btnThanhToan = findViewById(R.id.btnThanhToanMV);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        qr = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 400, 400, false);
        scaledqr = Bitmap.createScaledBitmap(qr, 200, 200, false);
    }

    public void loadDataThongTin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (bundle != null) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            hoaDonMangVe.setId_HoaDon(bundle.getString("id_HoaDon"));
            tvMHD.setText(hoaDonMangVe.getId_HoaDon().substring(0, 13));
            hoaDonMangVe.setThoiGian_ThanhToan(bundle.getString("thoiGian_ThanhToan"));
            tvGioHD.setText(hoaDonMangVe.getThoiGian_ThanhToan());
            hoaDonMangVe.setNgayThanhToan(bundle.getString("ngayThanhToan"));
            tvNgayHD.setText(hoaDonMangVe.getNgayThanhToan());
            hoaDonMangVe.setTenKH(bundle.getString("tenKH"));
            tvTenKH.setText(hoaDonMangVe.getTenKH().substring(9));
            tenKH = hoaDonMangVe.getTenKH();
            hoaDonMangVe.setTongTien(bundle.getDouble("tongTien"));
            tvTongTien.setText(nf.format(hoaDonMangVe.getTongTien()) + "đ");
            tvtongTienSauGiam.setText(nf.format(hoaDonMangVe.getTongTien()) + "đ");
        }
        dialog.dismiss();
    }

    private void datachitietmon() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(tenKH);

        databaseReference.child("HT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataGop.clear();
                dataChiTietMon.clear();
                // nếu trùng id_Mon tăng số lượng món
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ChiTietMon chiTietMon = item.getValue(ChiTietMon.class);
                        dataChiTietMon.add(chiTietMon);
                    }
                }
                Map<String, Integer> mapGopSL = new HashMap<>();
                for (ChiTietMon item : dataChiTietMon) {
                    Integer current = mapGopSL.getOrDefault(item.getId_Mon(), 0);
                    mapGopSL.put(item.getId_Mon(), current + item.getSl());
                }
                for (Map.Entry<String, Integer> entry : mapGopSL.entrySet()) {
                    ChiTietMon chiTietMon = new ChiTietMon();
                    chiTietMon.setId_Mon(entry.getKey());
                    chiTietMon.setSl(entry.getValue());
                    for (ChiTietMon item : dataChiTietMon) {
                        ChiTietMon chiTietMon1 = new ChiTietMon();
                        if (item.getId_Mon().equals(entry.getKey())) {
                            chiTietMon1.setId_Mon(item.getId_Mon());
                            chiTietMon1.setSl(chiTietMon.getSl());
                            chiTietMon1.setId_Ban(item.getId_Ban());
                            chiTietMon1.setGia(item.getGia());
                            chiTietMon1.setTenMon(item.getTenMon());
                            chiTietMon1.setTenKH(item.getTenKH());
                            chiTietMon1.setGioGoiMon(item.getGioGoiMon());
                            chiTietMon1.setHinh(item.getHinh());
                            chiTietMon1.setNgayGoiMon(item.getNgayGoiMon());
                            chiTietMon1.setId_TrangThai(item.getId_TrangThai());
                            chiTietMon1.setGhiChu(item.getGhiChu());
                            dataGop.add(chiTietMon1);
                            break;
                        }
                    }
                }
                dataChiTietMon.clear();
                dataChiTietMon.addAll(dataGop);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void taoChiTietMonQK(String id_HoaDon) {
        HoaDonUltility.getHdInstance().thanhToanMangVe(tenKH, id_HoaDon);
    }

    public void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    public void createPDF() {
        int pageWidth = 1200;
        int cd = 80;
        int pageRemember = 870;
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // load hình ảnh lên file pdf
        canvas.drawBitmap(scaledbmp, 400, 0, paint);


        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        titlePaint.setTextSize(50);
        canvas.drawText("53 Võ Vân Ngân, Linh Chiểu, Tp.Thủ Đức, Tp.HCM", pageWidth / 2, 450, titlePaint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Mã hóa đơn: ", 20, 550, paint);
        canvas.drawText("Khách hàng: ", 20, 630, paint);
        canvas.drawText("Ngày: ", 20, 710, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Giờ: ", pageWidth - 410, 710, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Tên món", 50, 850, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Số lượng", pageWidth / 2, 850, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(70f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Thành tiền", pageWidth - 70, 850, paint);

        NumberFormat nf = NumberFormat.getNumberInstance();
        // đổ dữ liệu lên file pdf
        for (ChiTietMon item : dataChiTietMon) {


            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(55f);
            paint.setColor(Color.BLACK);
            canvas.drawText(item.getTenMon(), 50, pageRemember + cd, paint);

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(55f);
            paint.setColor(Color.BLACK);
            canvas.drawText(item.getSl() + "", pageWidth / 2, pageRemember + cd, paint);

            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setTextSize(55f);
            paint.setColor(Color.BLACK);
            canvas.drawText(nf.format(item.getGia()) + "đ", pageWidth - 90, pageRemember + cd, paint);
            pageRemember += cd;
        }

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText(hoaDonMangVe.getId_HoaDon(), 400, 550, paint);
        canvas.drawText(hoaDonMangVe.getNgayThanhToan(), 200, 710, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText(hoaDonMangVe.getTenKH().substring(9), 450, 630, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText(hoaDonMangVe.getThoiGian_ThanhToan(), pageWidth - 170, 710, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Cảm ơn quý khách vì sự ủng hộ. Chúng tôi luôn sẵn lòng phục vụ bạn!", pageWidth / 2, pageRemember + 400, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(50f);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Tổng cộng: ", 50, pageRemember + 100, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(50f);
        paint.setColor(Color.BLACK);
        canvas.drawText(nf.format(hoaDonMangVe.getTongTien()) + "đ", pageWidth - 90, pageRemember + 100, paint);

        // load hình ảnh lên file pdf
        canvas.drawBitmap(scaledqr, 500, pageRemember + 150, paint);


        pdfDocument.finishPage(page);

        // tạo nơi lưu file
        downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // tạo file
        fileName = hoaDonMangVe.getId_HoaDon() + ".pdf";
        File file = new File(downloadDir, fileName);

        try {
            // viết dữ liệu lên file
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfDocument.close();
    }

    public void UploadFile(String pdfFilePath) {
        storageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Bill_PDF");
        File pdfFile = new File(downloadDir, pdfFilePath);
        Uri fileUri = Uri.fromFile(pdfFile);

        StorageReference reference = storageReference.child("Bill_PDF/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uri = uriTask.getResult();

                PDF pdf = new PDF(fileName, uri.toString());
                databaseReference1.child(databaseReference1.push().getKey()).setValue(pdf);

                Toast.makeText(PhieuHoaDonMangVeActivity.this, "Upload File Thanh cong", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Loi", "Upload failed. Exception: " + e.getMessage());
            }
        });
    }

    public void loadDataPhieuGiamGia() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("PhieuGiamGia");
        databaseReference.child("ChuaSuDung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PhieuGiamGia phieuGiamGia1 = dataSnapshot.getValue(PhieuGiamGia.class);
                    dataPhieuGiamGia.add(phieuGiamGia1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void taoPhieuGiamGiaDaSuDung(String id_HoaDon, String id_Phieu) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PhieuGiamGia").child("ChuaSuDung");
        databaseReference.child(id_Phieu).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phieuGiamGiaDaSuDung = snapshot.getValue(PhieuGiamGia.class);
                phieuGiamGiaDaSuDung.setId_HoaDon(id_HoaDon);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("PhieuGiamGia");
                databaseReference1.child("DaSuDung").child(id_Phieu).setValue(phieuGiamGiaDaSuDung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("PhieuGiamGia").child("ChuaSuDung");
        databaseReference1.child(id_Phieu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    public void resetTongTienHoaDon(String id_HoaDon, Double tongTien) {
        databaseReference = FirebaseDatabase.getInstance().getReference("HoaDon").child("MangVe");
        databaseReference.child(id_HoaDon).child("tongTien").setValue(tongTien);
    }
}
