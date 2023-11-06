package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DatBanViewholder extends RecyclerView.ViewHolder {
    public TextView tvTenBan, tvTenKhu, tvSoNguoi, tvTenKH, tvSdt, tvNgay, tvGio;

    public DatBanViewholder(@NonNull View itemView) {
        super(itemView);
        tvTenBan = itemView.findViewById(R.id.tvTenBan);
        tvTenKhu = itemView.findViewById(R.id.tvTenKhu);
        tvTenKH = itemView.findViewById(R.id.tvTenKH);
        tvSdt = itemView.findViewById(R.id.tvSdt);
        tvNgay = itemView.findViewById(R.id.tvNgay);
        tvGio = itemView.findViewById(R.id.tvGio);
        tvSoNguoi = itemView.findViewById(R.id.tvSoNguoi);
    }
}
