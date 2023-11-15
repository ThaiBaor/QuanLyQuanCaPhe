package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.ThongKeHoaDon;
import com.example.quanlyquancaphe.viewholders.ThongKeHoaDonViewholder;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ThongKeHoaDonAdapter extends RecyclerView.Adapter<ThongKeHoaDonViewholder> {
    Context context;
    ArrayList<ThongKeHoaDon> data;
    ArrayList<Ban> dataBan;

    public ThongKeHoaDonAdapter(Context context, ArrayList<ThongKeHoaDon> data, ArrayList<Ban> dataBan){
        this.context = context;
        this.data = data;
        this.dataBan = dataBan;
    }

    @NonNull
    @Override
    public ThongKeHoaDonViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_thongkehoadon_layout, parent, false);
        return new ThongKeHoaDonViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongKeHoaDonViewholder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        ThongKeHoaDon thongKeHoaDon = data.get(position);
        holder.maHoaDon.setText(thongKeHoaDon.getId_MaHoaDon());
        holder.tenKhachHang.setText(thongKeHoaDon.getTenKhachHang());
        holder.gio.setText(thongKeHoaDon.getNgayThanhToan());
        holder.ngay.setText(thongKeHoaDon.getThoiGian_thanhtoan());
        holder.maHoaDon.setText(thongKeHoaDon.getId_MaHoaDon());
        holder.tongTien.setText(nf.format(thongKeHoaDon.getTongTien()) + "đ");
        for (Ban ban : dataBan){
            if (ban.getId_Ban().equals(thongKeHoaDon.getId_Ban())){
                holder.tenBan.setText(ban.getTenBan());
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
