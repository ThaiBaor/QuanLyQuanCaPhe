package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DanhSachOrderViewholder extends RecyclerView.ViewHolder {

   public TextView tvBan ,tvThoiGian;

    public DanhSachOrderViewholder(@NonNull View itemView) {
        super(itemView);
        tvBan = itemView.findViewById(R.id.tvItemsBan);
        tvThoiGian = itemView.findViewById(R.id.tvItemsThoiGian);
    }
}
