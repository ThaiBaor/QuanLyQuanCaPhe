package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class QuanLyKhuViewholder extends RecyclerView.ViewHolder {
    public TextView sTT;
    public TextView ma;
    public TextView tenKhu;
    public QuanLyKhuViewholder(@NonNull View itemView) {
        super(itemView);
        sTT = itemView.findViewById(R.id.tvStt);
        ma = itemView.findViewById(R.id.tvMa);
        tenKhu = itemView.findViewById(R.id.tvtenKhu);
    }
}
