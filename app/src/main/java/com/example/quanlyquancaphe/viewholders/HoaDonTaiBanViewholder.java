package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class HoaDonTaiBanViewholder extends RecyclerView.ViewHolder {
    public TextView tvMHD;
    public TextView tvGioHD;
    public TextView tvNgayHD;
    public TextView tvBanHD;
    public TextView tvGiaHD;
    public HoaDonTaiBanViewholder(@NonNull View itemView) {
        super(itemView);
        tvMHD = itemView.findViewById(R.id.tvMaHD);
        tvGioHD = itemView.findViewById(R.id.tvGioHD);
        tvNgayHD = itemView.findViewById(R.id.tvNgayHD);
        tvBanHD = itemView.findViewById(R.id.tvBanHD);
        tvGiaHD = itemView.findViewById(R.id.tvGiaHD);
    }
}
