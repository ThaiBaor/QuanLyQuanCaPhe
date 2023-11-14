package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class PhieuHoaDonViewholder extends RecyclerView.ViewHolder{
    public TextView tvTenMon, tvSoLuong, tvThanhTien;
    public PhieuHoaDonViewholder(@NonNull View itemView) {
        super(itemView);
        tvTenMon = itemView.findViewById(R.id.tvItemTenMon);
        tvSoLuong = itemView.findViewById(R.id.tvItemSoLuong);
        tvThanhTien = itemView.findViewById(R.id.tvItemThanhTien);
    }
}
