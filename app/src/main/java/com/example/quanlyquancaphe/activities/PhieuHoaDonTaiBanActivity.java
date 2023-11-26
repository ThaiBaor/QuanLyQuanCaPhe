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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.example.quanlyquancaphe.models.HoaDon;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhieuHoaDonTaiBanActivity extends AppCompatActivity {
    final static int REQUEST_CODE = 1232;
    TextView tvMHD, tvGioHD, tvNgayHD, tvBanHD, tvGiaHD, tvKhu, tvTongTien, tvtongTienSauGiam;
    EditText maGiamGia;
    Button btnQuayLai, btnThanhToan;
    ImageView ivHinh;
    Switch swGiamGia;
    RecyclerView recyclerView;
    Bundle bundle;
    String id_Ban = "", id_Phieu = "";
    Double tongTienSauGiam = 0.0;
    Boolean check = false;
    ArrayList<ChiTietMon> dataChiTietMon = new ArrayList<>();
    ArrayList<ChiTietMon> dataGop = new ArrayList<>();
    ArrayList<PhieuGiamGia> dataPhieuGiamGia = new ArrayList<>();
    PhieuGiamGia phieuGiamGiaDaSuDung = new PhieuGiamGia();
    HoaDonTaiBan hoaDonTaiBan = new HoaDonTaiBan();
    Ban ban = new Ban();
    Khu khu = new Khu();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PhieuHoaDonAdapter adapter;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Bitmap bmp, scaledbmp, qr, scaledqr;
    String fileName;
    File downloadDir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_phieuhoadontaiban_layout);
        bundle = getIntent().getExtras();
        setControl();
        loadDataThongTin();
        datachitietmon();
        loadDataPhieuGiamGia();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        adapter = new PhieuHoaDonAdapter(PhieuHoaDonTaiBanActivity.this, dataChiTietMon);
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
                    hoaDonTaiBan.setId_HoaDon(bundle.getString("id_HoaDon"));
                    hoaDonTaiBan.setDaThanhToan(bundle.getBoolean("daThanhToan"));
                    Boolean tt = true;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("HoaDon").child("TaiBan");
                    taoChiTietMonQK(hoaDonTaiBan.getId_HoaDon());
                    if (check){
                        taoPhieuGiamGiaDaSuDung(hoaDonTaiBan.getId_HoaDon(),id_Phieu);
                    }
                    resetTongTienHoaDon(hoaDonTaiBan.getId_HoaDon(), tongTienSauGiam);
                    ref.child(hoaDonTaiBan.getId_HoaDon()).child("daThanhToan").setValue(tt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(PhieuHoaDonTaiBanActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(id_Ban).child("HT");
                            databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    finish();
                                }
                            });
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
                    for (PhieuGiamGia item : dataPhieuGiamGia) {
                        LocalDate otherDate = LocalDate.parse(item.getNgayHetHan(), formatter);
                        if (currentDate.compareTo(otherDate) < 0) {
                            if (item.getId_Phieu().equals(maGiamGia.getText().toString())) {
                                tongTienSauGiam = hoaDonTaiBan.getTongTien() - hoaDonTaiBan.getTongTien() * item.getGiaTri() / 100;
                                tvtongTienSauGiam.setText(nf.format(tongTienSauGiam) + "đ");
                                id_Phieu = item.getId_Phieu();
                                swGiamGia.setChecked(true);
                                check = true;
                                return;
                            } else {
                                Toast.makeText(PhieuHoaDonTaiBanActivity.this, "Mã Không tồn tại!", Toast.LENGTH_SHORT).show();
                                swGiamGia.setChecked(false);
                                check = false;
                            }
                        } else {
                            Toast.makeText(PhieuHoaDonTaiBanActivity.this, "Mã đã hết hạn!", Toast.LENGTH_SHORT).show();
                            swGiamGia.setChecked(false);
                            check = false;
                        }
                    }
                }
                else {
                    tvtongTienSauGiam.setText(nf.format(hoaDonTaiBan.getTongTien())+"đ");
                }
            }
        });
    }

    private void setControl() {
        tvtongTienSauGiam = findViewById(R.id.tvTongTienDaGiam);
        maGiamGia = findViewById(R.id.maGiamGiaHDTB);
        swGiamGia = findViewById(R.id.swMaGiamGiaHoaDonMV);
        recyclerView = findViewById(R.id.recyclePhieuHoaDon);
        tvMHD = findViewById(R.id.tvMa);
        tvGioHD = findViewById(R.id.tvGio);
        tvGiaHD = findViewById(R.id.tvGiaHD);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvBanHD = findViewById(R.id.tvBan);
        tvNgayHD = findViewById(R.id.tvNgay);
        tvKhu = findViewById(R.id.tvKhu);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        ivHinh = findViewById(R.id.ivHinh);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        qr = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 400, 400, false);
        scaledqr = Bitmap.createScaledBitmap(qr, 200, 200, false);
    }

    private void loadDataThongTin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (bundle != null) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            hoaDonTaiBan.setId_HoaDon(bundle.getString("id_HoaDon"));
            tvMHD.setText(hoaDonTaiBan.getId_HoaDon());
            hoaDonTaiBan.setThoiGian_ThanhToan(bundle.getString("thoiGian_ThanhToan"));
            tvGioHD.setText(hoaDonTaiBan.getThoiGian_ThanhToan());
            hoaDonTaiBan.setNgayThanhToan(bundle.getString("ngayThanhToan"));
            tvNgayHD.setText(hoaDonTaiBan.getNgayThanhToan());
            hoaDonTaiBan.setId_Ban(bundle.getString("id_Ban"));
            id_Ban = hoaDonTaiBan.getId_Ban();
            ban.setTenBan(bundle.getString("tenBan"));
            tvBanHD.setText(ban.getTenBan());
            khu.setTenKhu(bundle.getString("tenKhu"));
            tvKhu.setText(khu.getTenKhu());
            hoaDonTaiBan.setTongTien(bundle.getDouble("tongTien"));
            tvTongTien.setText(nf.format(hoaDonTaiBan.getTongTien()) + "đ");
            tvtongTienSauGiam.setText(nf.format(hoaDonTaiBan.getTongTien()) + "đ");
        }
        dialog.dismiss();
    }

    private void datachitietmon() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ChiTietMon").child(id_Ban);

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
        HoaDonUltility.getHdInstance().thanhToanTaiBan(id_Ban, id_HoaDon);
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
        canvas.drawText("Bàn: ", 20, 630, paint);
        canvas.drawText("Ngày: ", 20, 710, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Giờ: ", pageWidth - 410, 710, paint);
        canvas.drawText("Khu: ", pageWidth - 400, 630, paint);

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
        canvas.drawText(hoaDonTaiBan.getId_HoaDon(), 400, 550, paint);
        canvas.drawText(ban.getTenBan(), 170, 630, paint);
        canvas.drawText(hoaDonTaiBan.getNgayThanhToan(), 200, 710, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        canvas.drawText(hoaDonTaiBan.getThoiGian_ThanhToan(), pageWidth - 170, 710, paint);
        canvas.drawText(khu.getTenKhu(), pageWidth - 230, 630, paint);

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
        canvas.drawText(nf.format(hoaDonTaiBan.getTongTien()) + "đ", pageWidth - 90, pageRemember + 100, paint);

        // load hình ảnh lên file pdf
        canvas.drawBitmap(scaledqr, 500, pageRemember + 150, paint);


        pdfDocument.finishPage(page);

        // tạo nơi lưu file
        downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // tạo file
        fileName = hoaDonTaiBan.getId_HoaDon() + ".pdf";
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

                Toast.makeText(PhieuHoaDonTaiBanActivity.this, "Upload File Thanh cong", Toast.LENGTH_SHORT).show();
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
    public void taoPhieuGiamGiaDaSuDung(String id_HoaDon, String id_Phieu){
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
    public void resetTongTienHoaDon(String id_HoaDon, Double tongTien){
        databaseReference = FirebaseDatabase.getInstance().getReference("HoaDon").child("TaiBan");
        databaseReference.child(id_HoaDon).child("tongTien").setValue(tongTien);
    }
}
