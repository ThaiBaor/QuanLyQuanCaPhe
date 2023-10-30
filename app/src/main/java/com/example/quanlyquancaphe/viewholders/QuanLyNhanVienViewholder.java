package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class QuanLyNhanVienViewholder extends RecyclerView.ViewHolder {
    public TextView tvMaNhanVien , tvTenNhanVien ,tvViTri ;
   public CardView cardView;

    public QuanLyNhanVienViewholder(@NonNull View itemView) {
        super(itemView);
        tvMaNhanVien =itemView.findViewById(R.id.ivMaNV);
        tvTenNhanVien =itemView.findViewById(R.id.ivTen);
        tvViTri =itemView.findViewById(R.id.ivViTri);
        cardView= itemView.findViewById(R.id.itemCarview);
    }
}
