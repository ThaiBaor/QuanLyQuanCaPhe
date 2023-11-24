package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class HoaDonMangVeViewholder extends RecyclerView.ViewHolder {
    public TextView tvMHD;
    public TextView tvGioHD;
    public TextView tvNgayHD;
    public TextView tvTenKH;
    public TextView tvGiaHD;
    public HoaDonMangVeViewholder(@NonNull View itemView) {
        super(itemView);
        tvMHD = itemView.findViewById(R.id.tvMaHDMV);
        tvGioHD = itemView.findViewById(R.id.tvGioHDMV);
        tvNgayHD = itemView.findViewById(R.id.tvNgayHDMV);
        tvTenKH = itemView.findViewById(R.id.tvTenKH);
        tvGiaHD = itemView.findViewById(R.id.tvGiaHDMV);
    }
}
