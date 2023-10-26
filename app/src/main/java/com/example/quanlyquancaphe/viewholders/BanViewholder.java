package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class BanViewholder extends RecyclerView.ViewHolder {
    public TextView tvSTT, tvTen, tvChoNgoi, tvKhu;
    public BanViewholder(@NonNull View itemView) {
        super(itemView);
        tvSTT = itemView.findViewById(R.id.tvSTT);
        tvTen = itemView.findViewById(R.id.tvTen);
        tvChoNgoi = itemView.findViewById(R.id.tvChoNgoi);
        tvKhu = itemView.findViewById(R.id.tvKhu);
    }
}
