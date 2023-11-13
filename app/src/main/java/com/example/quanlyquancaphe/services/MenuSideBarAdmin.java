package com.example.quanlyquancaphe.services;

import android.app.Activity;
import android.content.Intent;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.DanhSachMonPhucVuActivity;
import com.example.quanlyquancaphe.activities.QuanLyBanActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhoActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhuActivity;
import com.example.quanlyquancaphe.activities.QuanLyMonActivity;
import com.example.quanlyquancaphe.activities.QuanLyNhanVienActivity;

public class MenuSideBarAdmin {
    public MenuSideBarAdmin() {
    }
    public boolean chonManHinh(int layout, Activity activity){
        switch (layout){
            case R.id.nav_thongke:
                chonManHinh(activity, DanhSachMonPhucVuActivity.class);
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
            case R.id.nav_dangxuat:
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
}
