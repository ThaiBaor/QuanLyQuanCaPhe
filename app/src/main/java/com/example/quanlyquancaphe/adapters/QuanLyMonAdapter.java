package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.QuanLyMonActivity;
import com.example.quanlyquancaphe.models.Mon;
import com.example.quanlyquancaphe.viewholders.QuanLyMonViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;


public class QuanLyMonAdapter extends RecyclerView.Adapter<QuanLyMonViewHolder> {
    ArrayList<Mon> data;
    Context context;

    public QuanLyMonAdapter(ArrayList<Mon> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public QuanLyMonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyMonViewHolder(LayoutInflater.from(context).inflate(R.layout.item_quanlymon_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyMonViewHolder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        Mon mon = data.get(position);
        holder.tenMon.setText(mon.getTenMon());
        holder.moTa.setText(mon.getMoTa());
        holder.donGia.setTextSize(17);
        holder.donGia.setTextColor(Color.rgb(0, 0, 255));
        holder.donGia.setText(nf.format(mon.getDonGia()) + "đ");
        if (mon.getGiamGia() == 0) {
            holder.giamGia.setVisibility(View.GONE);
        } else {
            holder.giamGia.setVisibility(View.VISIBLE);
            holder.giamGia.setTextColor(Color.rgb(255, 0, 0));
            holder.giamGia.setText("Giảm: " + mon.getGiamGia() + "%");
            holder.giamGia.setTextSize(17);
        }
        Glide.with(context).load(data.get(position).getHinh()).into(holder.hinh);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
