package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class QuanLyMonViewHolder extends RecyclerView.ViewHolder {
    public TextView tenMon, donGia, moTa, giamGia;
    public ImageView hinh;
    public QuanLyMonViewHolder(@NonNull View itemView) {
        super(itemView);
        setControl();

    }
    private void setControl(){
        tenMon = itemView.findViewById(R.id.tvTenMon);
        donGia = itemView.findViewById(R.id.tvDonGia);
        moTa = itemView.findViewById(R.id.tvMoTa);
        giamGia = itemView.findViewById(R.id.tvGiamGia);
        hinh = itemView.findViewById(R.id.ivHinh);
    }
}
