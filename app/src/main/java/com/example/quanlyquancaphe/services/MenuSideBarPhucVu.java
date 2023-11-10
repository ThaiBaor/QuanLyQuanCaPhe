package com.example.quanlyquancaphe.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.DanhSachBanActivity;
import com.example.quanlyquancaphe.activities.QuanLyBanActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhoActivity;
import com.example.quanlyquancaphe.activities.QuanLyKhuActivity;
import com.example.quanlyquancaphe.activities.QuanLyMonActivity;
import com.example.quanlyquancaphe.activities.QuanLyNhanVienActivity;

public class MenuSideBarPhucVu {
    public MenuSideBarPhucVu(){
    }

    public boolean chonManHinh(int layout, Activity activity){
        switch (layout){
            case R.id.nav_danhsachban:
                chonManHinh(activity, DanhSachBanActivity.class);
                break;
            case R.id.nav_mangve:
                break;
            case R.id.nav_dsmonhoanthanh:
                break;
            case R.id.nav_doimatkhau:
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
