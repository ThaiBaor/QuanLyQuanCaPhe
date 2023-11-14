package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DanhSachOrderPhaCheViewholder extends RecyclerView.ViewHolder {
    public TextView tvMaBan,tvThoiGian;
    public CardView cardView;
    public DanhSachOrderPhaCheViewholder(@NonNull View itemView) {
        super(itemView);
        tvMaBan = itemView.findViewById(R.id.tvItemsBan);
        tvThoiGian = itemView.findViewById(R.id.tvItemsThoiGian);
        cardView = itemView.findViewById(R.id.cardViewItems);
    }
}
