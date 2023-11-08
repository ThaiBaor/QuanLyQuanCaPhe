package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DanhSachOrderPhaCheViewholder extends RecyclerView.ViewHolder {
    public TextView tvMaHoaDon ,tvMaBan,tvThoiGian;
    public CardView cardView;
    public DanhSachOrderPhaCheViewholder(@NonNull View itemView) {
        super(itemView);
        tvMaHoaDon =itemView.findViewById(R.id.tvItemsMaHoaDon);
        tvMaBan = itemView.findViewById(R.id.tvItemsBan);
        tvThoiGian = itemView.findViewById(R.id.tvItemsThoiGian);
        cardView = itemView.findViewById(R.id.cardViewItems);
    }
}
