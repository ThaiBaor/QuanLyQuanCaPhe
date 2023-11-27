package com.example.quanlyquancaphe.adapters;

import android.annotation.SuppressLint;
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
import com.example.quanlyquancaphe.interfaces.DSMonPVInterface;
import com.example.quanlyquancaphe.models.MonTrongDS;
import com.example.quanlyquancaphe.viewholders.DanhSachMonPhucVuViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;

public class DanhSachMonPhucVuAdapter extends RecyclerView.Adapter<DanhSachMonPhucVuViewHolder> {
    Context context;
    ArrayList<MonTrongDS> data;
    private final DSMonPVInterface buttonClickListener;


    public DanhSachMonPhucVuAdapter(Context context, ArrayList<MonTrongDS> data, DSMonPVInterface buttonClickListener) {
        this.context = context;
        this.data = data;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public DanhSachMonPhucVuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DanhSachMonPhucVuViewHolder(LayoutInflater.from(context).inflate(R.layout.item_danhsachmonphucvu_layout, parent, false), buttonClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DanhSachMonPhucVuViewHolder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        MonTrongDS mon = data.get(holder.getLayoutPosition());
        if (mon.getHetMon()) {
            holder.tvHetMon.setText("Đã hết");
            holder.btnTang.setVisibility(View.GONE);
            holder.btnGiam.setVisibility(View.GONE);
            holder.btnAdd.setVisibility(View.GONE);
            holder.edtSL.setVisibility(View.GONE);
        } else {
            holder.tvHetMon.setVisibility(View.GONE);
            holder.btnTang.setVisibility(View.VISIBLE);
            holder.btnGiam.setVisibility(View.VISIBLE);
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.edtSL.setVisibility(View.VISIBLE);
        }

        holder.tenMon.setText(mon.getTenMon());
        holder.moTa.setText(mon.getMoTa());
        holder.slDaBan.setText("Đã bán: " + nf.format(mon.getSlDaBan()));
        holder.donGia.setText(nf.format(mon.getDonGia()) + "đ");
        if (mon.getGiamGia() == 0) {
            holder.giamGia.setVisibility(View.GONE);
            holder.donGia.setPaintFlags(holder.donGia.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.donGia.setTextSize(17);
            holder.donGia.setTextColor(Color.rgb(255, 0, 0));
        } else {
            holder.donGia.setTextSize(13);
            holder.donGia.setTextColor(Color.rgb(43, 43, 43));
            holder.donGia.setPaintFlags(holder.donGia.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.giamGia.setVisibility(View.VISIBLE);
            holder.giamGia.setTextColor(Color.rgb(255, 0, 0));
            holder.giamGia.setText(nf.format(mon.getDonGia() * (100 - mon.getGiamGia()) / 100) + "đ");
            holder.giamGia.setTextSize(17);
        }
        Glide.with(context).load(data.get(position).getHinh()).into(holder.hinh);
        holder.edtSL.setText(String.valueOf(mon.getSl()));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
