package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.viewholders.QuanLyKhuViewholder;

import java.util.ArrayList;

public class QuanLyKhuAdapter extends RecyclerView.Adapter<QuanLyKhuViewholder> {
    Context context;
    ArrayList<Khu> data;
    public QuanLyKhuAdapter(Context context, ArrayList<Khu>data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public QuanLyKhuViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyKhuViewholder(LayoutInflater.from(context).inflate(R.layout.item_quanlykhu_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyKhuViewholder holder, int position) {
        Khu khu = data.get(position);
        holder.ma.setText(khu.getId_Khu()+"");
        holder.tenKhu.setText(khu.getTenKhu()+"");
        holder.sTT.setText(String.valueOf(position + 1));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
