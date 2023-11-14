package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DanhSachMonTrongOrderViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivImages;
    public TextView tvTenMonAn, tvGhiChu, tvsoLuong;
   public ImageButton ibtnWaiting,ibtnComplete,ibtnConfig;
   public Switch swTrangThai;
    public CardView cardView;

    public DanhSachMonTrongOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTenMonAn = itemView.findViewById(R.id.tvTenMon_orderPhaChe);
        tvGhiChu = itemView.findViewById(R.id.tvGhiChu_orderPhaChe);
        tvsoLuong = itemView.findViewById(R.id.tvSoLuong_orderPhaChe);
        ivImages = itemView.findViewById(R.id.ivImages);
        //Nut
        swTrangThai = itemView.findViewById(R.id.swHetMon);
        ibtnComplete = itemView.findViewById(R.id.ibtnComplete);
        ibtnConfig = itemView.findViewById(R.id.ibtnConfig);
        ibtnWaiting = itemView.findViewById(R.id.ibtnWaiting);
        //Card
        cardView = itemView.findViewById(R.id.cardView_OrderPhaChe);



    }
}
