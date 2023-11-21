package com.example.quanlyquancaphe.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.DangNhapActivity;
import com.example.quanlyquancaphe.activities.DanhSachMonPhucVuActivity;
import com.example.quanlyquancaphe.activities.DoiMatKhauActivity;
import com.example.quanlyquancaphe.activities.HoaDonMangVeActivity;
import com.example.quanlyquancaphe.activities.HoaDonTaiBanActivity;
import com.example.quanlyquancaphe.activities.QuanLyBanActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhoActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhuActivity;
import com.example.quanlyquancaphe.activities.QuanLyMonActivity;
import com.example.quanlyquancaphe.activities.QuanLyNhanVienActivity;
import com.example.quanlyquancaphe.activities.ThongKeHoaDonActivity;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;

public class MenuSideBarThuNgan {
    public MenuSideBarThuNgan(){
    }

    public boolean chonManHinh(int layout, Activity activity){
        switch (layout){
            case R.id.nav_thongkehoadon:
                chonManHinh(activity, ThongKeHoaDonActivity.class);
                break;
            case R.id.nav_thanhtoantaiban:
                chonManHinh(activity, HoaDonTaiBanActivity.class);
                break;
            case R.id.nav_thanhtoanmangdi:
                chonManHinh(activity, HoaDonMangVeActivity.class);
                break;
            case R.id.nav_doimatkhau:
                Intent intent = new Intent(activity, DoiMatKhauActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                break;
            case R.id.nav_dangxuat:
                DangXuat(activity);
                break;
        }
        return true;
    }

    private void chonManHinh(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    private void DangXuat(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn đăng xuất không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chonManHinh(activity, DangNhapActivity.class);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
