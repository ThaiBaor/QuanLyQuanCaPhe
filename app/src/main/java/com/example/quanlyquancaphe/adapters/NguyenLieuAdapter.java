package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.NguyenLieu;

import com.example.quanlyquancaphe.viewholders.NguyenLieuHolder;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NguyenLieuAdapter extends RecyclerView.Adapter<NguyenLieuHolder> {
    Context context;
    ArrayList<NguyenLieu> data;

    public NguyenLieuAdapter(Context context, ArrayList<NguyenLieu> data){
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public NguyenLieuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_quanlykho_layout, parent, false);
        return new NguyenLieuHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NguyenLieuHolder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        NguyenLieu nl = data.get(position);
        holder.nguyenlieu.setText(nl.getTenNguyenLieu());
        holder.nhap.setText(nf.format(nl.getSoLuongNhap()));
        holder.ton.setText(nf.format(nl.getTonKho()));
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.ngaynhap.setText(nl.getNgayNhap());
        holder.stt.setText(position + 1 + "");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
