package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.viewholders.DanhSachMonHoanThanhViewHolder;

import java.util.List;

public class DanhSachMonHoanThanhAdapter extends RecyclerView.Adapter<DanhSachMonHoanThanhViewHolder> {
    Context context ;

    @NonNull
    @Override
    public DanhSachMonHoanThanhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monhoanthanh_layout,parent,false);
        return new DanhSachMonHoanThanhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachMonHoanThanhViewHolder holder, int position) {

       }


    @Override
    public int getItemCount() {
        return 0;
    }
}
