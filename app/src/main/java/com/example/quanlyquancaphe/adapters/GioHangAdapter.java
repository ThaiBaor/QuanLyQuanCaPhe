package com.example.quanlyquancaphe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.interfaces.GioHangInterface;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.viewholders.GioHangViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangViewHolder> {
    Context context;
    ArrayList<ChiTietMon> data;

    private final GioHangInterface buttonClickListener;

    public GioHangAdapter(Context context, ArrayList<ChiTietMon> data, GioHangInterface buttonClickListener) {
        this.context = context;
        this.data = data;
        this.buttonClickListener = buttonClickListener;
    }


    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GioHangViewHolder(LayoutInflater.from(context).inflate(R.layout.item_giohang_layout, parent, false), buttonClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        ChiTietMon chiTietMon_ = data.get(holder.getLayoutPosition());

        switch (chiTietMon_.getId_TrangThai()) {
            case 0:
                holder.btnXoa.setVisibility(View.VISIBLE);
                hienThiTrangThai(holder.edtSL, holder.edtGhiChu, holder.btnTang, holder.btnGiam, true);
                break;
            case 1:
                holder.btnXoa.setVisibility(View.INVISIBLE);
                hienThiTrangThai(holder.edtSL, holder.edtGhiChu, holder.btnTang, holder.btnGiam, false);
                holder.tvTrangThai.setText("Đã xác nhận");
                break;
            case 2:
                holder.btnXoa.setVisibility(View.INVISIBLE);
                hienThiTrangThai(holder.edtSL, holder.edtGhiChu, holder.btnTang, holder.btnGiam, false);
                holder.tvTrangThai.setText("Đã làm xong");
                break;
            case 3:
                holder.btnXoa.setVisibility(View.INVISIBLE);
                hienThiTrangThai(holder.edtSL, holder.edtGhiChu, holder.btnTang, holder.btnGiam, false);
                holder.tvTrangThai.setText("Đã nhận");
                break;
        }
        Glide.with(context).load(chiTietMon_.getHinh()).into(holder.ivHinh);
        holder.edtSL.setText(chiTietMon_.getSl().toString());
        holder.tvTenMon.setText(chiTietMon_.getTenMon());
        NumberFormat nf = NumberFormat.getNumberInstance();
        holder.tvGia.setText(nf.format(chiTietMon_.getSl() * chiTietMon_.getGia()) + "đ");
        holder.edtGhiChu.setText(chiTietMon_.getGhiChu());
    }

    private void hienThiTrangThai(EditText edtSL, EditText edtGhiChu, ImageButton btnTang, ImageButton btnGiam, Boolean b) {
        btnTang.setEnabled(b);
        btnGiam.setEnabled(b);
        edtSL.setEnabled(b);
        edtGhiChu.setEnabled(b);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
