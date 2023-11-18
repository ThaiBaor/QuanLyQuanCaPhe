package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

import org.w3c.dom.Text;

public class DanhSachMonKhaDungViewholder extends RecyclerView.ViewHolder {
    public TextView tenMon, moTa, donGia, giamGia;
    public ImageView hinh;
    public Switch hetMon;
    public DanhSachMonKhaDungViewholder(@NonNull View itemView) {
        super(itemView);
        tenMon = itemView.findViewById(R.id.tvTenMonKhaDung);
        moTa = itemView.findViewById(R.id.tvMoTaKhaDung);
        donGia = itemView.findViewById(R.id.tvDonGiaKhaDung);
        giamGia = itemView.findViewById(R.id.tvGiamgiaKhaDung);
        hinh = itemView.findViewById(R.id.ivHinhKhaDung);
        hetMon = itemView.findViewById(R.id.swHetMonKhaDung);
    }
}
