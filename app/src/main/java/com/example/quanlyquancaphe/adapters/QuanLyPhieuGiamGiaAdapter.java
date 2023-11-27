package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.ThemPhieuGiamGiaActivity;
import com.example.quanlyquancaphe.models.PhieuGiamGia;
import com.example.quanlyquancaphe.viewholders.QuanLyPhieuGiamGiaViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QuanLyPhieuGiamGiaAdapter extends RecyclerView.Adapter<QuanLyPhieuGiamGiaViewHolder> {
    ArrayList<PhieuGiamGia> data;
    Context context;

    public QuanLyPhieuGiamGiaAdapter(ArrayList<PhieuGiamGia> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public QuanLyPhieuGiamGiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyPhieuGiamGiaViewHolder(LayoutInflater.from(context).inflate(R.layout.item_quanlyphieugiamgia_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyPhieuGiamGiaViewHolder holder, int position) {
        if (data.size() == 0) {
            return;
        }
        PhieuGiamGia phieuGiamGia = data.get(holder.getBindingAdapterPosition());
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = dateFormat.parse(phieuGiamGia.getNgayHetHan());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        Calendar holderDate = Calendar.getInstance();
        holderDate.setTime(date);
        if (ThemPhieuGiamGiaActivity.soSanhNgay(holderDate, currentDate) <= 0) {
            holder.tvNgay.setTextColor(Color.RED);
        }
        else {
            holder.tvNgay.setTextColor(Color.BLACK);
        }
        holder.tvSST.setText(holder.getBindingAdapterPosition() + 1 + "");
        holder.tvIdPhieu.setText(phieuGiamGia.getId_Phieu());
        holder.tvGT.setText(phieuGiamGia.getGiaTri() + "");
        holder.tvNgay.setText(phieuGiamGia.getNgayHetHan() + "");
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
