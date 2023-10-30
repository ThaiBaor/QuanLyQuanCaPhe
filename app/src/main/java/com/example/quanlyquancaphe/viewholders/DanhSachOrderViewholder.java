package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DanhSachOrderViewholder extends RecyclerView.ViewHolder {

   public TextView tvMaHoaDon ,tvBan ,tvThoiGian;

    public DanhSachOrderViewholder(@NonNull View itemView) {
        super(itemView);
        tvMaHoaDon = itemView.findViewById(R.id.tvItemsMaHoaDon);
        tvBan = itemView.findViewById(R.id.tvItemsBan);
        tvThoiGian = itemView.findViewById(R.id.tvItemsThoiGian);
    }
}
