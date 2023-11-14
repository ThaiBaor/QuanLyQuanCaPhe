package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DanhSachMonHoanThanhViewHolder extends RecyclerView.ViewHolder {
   public ImageView ivImages;
   public TextView tvTenMon,tvSL,tvBan,tvKhu;

    public DanhSachMonHoanThanhViewHolder(@NonNull View itemView) {
        super(itemView);
    ivImages = itemView.findViewById(R.id.ivImages_MonHoanThanh);
    tvTenMon = itemView.requireViewById(R.id.tvTenMon_MonHoanThanh);
    tvSL = itemView.requireViewById(R.id.tvSoLuong_MonHoanThanh);
    tvBan = itemView.requireViewById(R.id.tvBan_MonHoanThanh);
    tvKhu =itemView.findViewById(R.id.tvBan_MonHoanThanh);
    }
}
