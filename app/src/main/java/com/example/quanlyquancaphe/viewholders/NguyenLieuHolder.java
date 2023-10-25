package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quanlyquancaphe.R;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

public class NguyenLieuHolder extends RecyclerView.ViewHolder {
    public TextView stt, nguyenlieu, nhap, ton, ngaynhap;
    public NguyenLieuHolder(@NonNull View itemView) {
        super(itemView);
        stt = itemView.findViewById(R.id.tvstt);
        nguyenlieu = itemView.findViewById(R.id.tvnguyenlieu);
        nhap = itemView.findViewById(R.id.tvnhap);
        ton = itemView.findViewById(R.id.tvton);
        ngaynhap = itemView.findViewById(R.id.tvngaynhap);
    }
}
