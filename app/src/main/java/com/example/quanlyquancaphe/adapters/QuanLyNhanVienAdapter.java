package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.NhanVien;
import com.example.quanlyquancaphe.viewholders.QuanLyNhanVienViewholder;

import java.util.ArrayList;
import java.util.List;

public class QuanLyNhanVienAdapter extends RecyclerView.Adapter<QuanLyNhanVienViewholder> {
    private Context context;
    private List<NhanVien> dataList;
    String key = "";

    public QuanLyNhanVienAdapter(Context context, List<NhanVien> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public QuanLyNhanVienViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanlynhanvien_layout, parent, false);
        return new QuanLyNhanVienViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyNhanVienViewholder holder, int position) {
        holder.tvMaNhanVien.setText(dataList.get(position).getMaNhanVien());
        holder.tvTenNhanVien.setText(dataList.get(position).getTenNhanVien());
        holder.tvViTri.setText(dataList.get(position).getViTri());
    }

    public void SearchDataList(ArrayList<NhanVien> search) {
        dataList = search;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
