package com.example.quanlyquancaphe.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.BaoCaoDoanhThuTrongNgayActivity;
import com.example.quanlyquancaphe.activities.BaoCaoDoanhThuThangActivity;
import com.example.quanlyquancaphe.activities.DangNhapActivity;
import com.example.quanlyquancaphe.activities.DoiMatKhauActivity;
import com.example.quanlyquancaphe.activities.QuanLyBanActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhoActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhuActivity;
import com.example.quanlyquancaphe.activities.QuanLyMonActivity;
import com.example.quanlyquancaphe.activities.QuanLyNhanVienActivity;
import com.example.quanlyquancaphe.activities.ThongKeActivity;

public class MenuSideBarAdmin {
    public MenuSideBarAdmin() {
    }

    public boolean chonManHinh(int layout, Activity activity) {
        switch (layout) {
            case R.id.nav_thongke:
                chonManHinh(activity, ThongKeActivity.class);
                break;
            case R.id.nav_qlnhanvien:
                chonManHinh(activity, QuanLyNhanVienActivity.class);
                break;
            case R.id.nav_qlban:
                chonManHinh(activity, QuanLyBanActivity.class);
                break;
            case R.id.nav_qlkhu:
                chonManHinh(activity, QuanLyKhuActivity.class);
                break;
            case R.id.nav_qlmon:
                chonManHinh(activity, QuanLyMonActivity.class);
                break;
            case R.id.nav_qlkho:
                chonManHinh(activity, QuanLyKhoActivity.class);
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

    private void chonManHinh(Activity activity, Class secondActivity) {
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


