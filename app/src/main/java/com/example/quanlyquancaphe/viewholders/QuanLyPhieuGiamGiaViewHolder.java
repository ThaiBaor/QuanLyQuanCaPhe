package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class QuanLyPhieuGiamGiaViewHolder extends RecyclerView.ViewHolder {
    public TextView tvSST, tvIdPhieu, tvGT, tvNgay;

    public QuanLyPhieuGiamGiaViewHolder(@NonNull View itemView) {
        super(itemView);
        setControl();
    }

    private void setControl() {
        tvSST = itemView.findViewById(R.id.tvSTT);
        tvIdPhieu = itemView.findViewById(R.id.tvIdPhieu);
        tvGT = itemView.findViewById(R.id.tvGT);
        tvNgay = itemView.findViewById(R.id.tvNgay);
    }
}
