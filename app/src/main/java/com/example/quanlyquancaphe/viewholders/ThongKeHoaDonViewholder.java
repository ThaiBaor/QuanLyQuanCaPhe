package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class ThongKeHoaDonViewholder extends RecyclerView.ViewHolder {
    public TextView maHoaDon, tenKhachHang, gio, ngay, tongTien, tenBan;
    public ThongKeHoaDonViewholder(@NonNull View itemView) {
        super(itemView);
        maHoaDon = itemView.findViewById(R.id.tvMaHoaDon);
        tenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
        gio = itemView.findViewById(R.id.tvGio);
        ngay = itemView.findViewById(R.id.tvNgay);
        tongTien = itemView.findViewById(R.id.tvTongTien);
        tenBan = itemView.findViewById(R.id.tvTenBan);
    }
}
