package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.HoaDon;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.models.NhanVien;
import com.example.quanlyquancaphe.viewholders.PhieuHoaDonViewholder;

import java.text.NumberFormat;
import java.util.ArrayList;

public class PhieuHoaDonAdapter extends RecyclerView.Adapter<PhieuHoaDonViewholder>{
    Context context;
    Double tongTien;

    String id_Ban;
    ArrayList<ChiTietMon> dataChiTietMon;

    public PhieuHoaDonAdapter(Context context, String id_Ban, ArrayList<ChiTietMon> dataChiTietMon){
        this.context = context;
        this.id_Ban = id_Ban;
        this.dataChiTietMon = dataChiTietMon;
    }
    @NonNull
    @Override
    public PhieuHoaDonViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhieuHoaDonViewholder(LayoutInflater.from(context).inflate(R.layout.item_phieuhoadontaiban_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhieuHoaDonViewholder holder, int position) {
        ChiTietMon chiTietMon = dataChiTietMon.get(holder.getAdapterPosition());
        holder.tvTenMon.setText(chiTietMon.getTenMon());
        holder.tvSoLuong.setText(chiTietMon.getSl()+"");
        tongTien = chiTietMon.getSl() + chiTietMon.getGia();
        holder.tvThanhTien.setText(tongTien+"");
    }

    @Override
    public int getItemCount() {
        return dataChiTietMon.size();
    }
}
