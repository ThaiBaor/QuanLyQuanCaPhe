package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ThongKeHoaDon;
import com.example.quanlyquancaphe.viewholders.ThongKeHoaDonViewholder;

import java.util.ArrayList;

public class ThongKeHoaDonAdapter extends RecyclerView.Adapter<ThongKeHoaDonViewholder> {
    Context context;
    ArrayList<ThongKeHoaDon> data;

    public ThongKeHoaDonAdapter(Context context, ArrayList<ThongKeHoaDon> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ThongKeHoaDonViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_thongkehoadon_layout, parent, false);
        return new ThongKeHoaDonViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongKeHoaDonViewholder holder, int position) {
        ThongKeHoaDon thongKeHoaDon = data.get(position);
        holder.maHoaDon.setText(thongKeHoaDon.getId_MaHoaDon());
        holder.tenKhachHang.setText(thongKeHoaDon.getTenKhachHang());
        holder.gio.setText(thongKeHoaDon.getNgayThanhToan());
        holder.ngay.setText(thongKeHoaDon.getThoiGian_thanhtoan());
        holder.maHoaDon.setText(thongKeHoaDon.getId_MaHoaDon());
        holder.tongTien.setText(thongKeHoaDon.getTongTien()+"");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
