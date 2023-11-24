package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.HoaDonMangVe;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.viewholders.HoaDonMangVeViewholder;
import com.example.quanlyquancaphe.viewholders.HoaDonTaiBanViewholder;

import java.text.NumberFormat;
import java.util.ArrayList;

public class HoaDonMangVeAdapter extends RecyclerView.Adapter<HoaDonMangVeViewholder>{
    Context context;
    ArrayList<HoaDonMangVe> data;
    private ItemClickListener listener;
    Integer position;
    public Integer getPosition() {
        return position;
    }
    public HoaDonMangVeAdapter(Context context, ArrayList<HoaDonMangVe> data, HoaDonMangVeAdapter.ItemClickListener listener){
        this.context = context;
        this.data = data;
        this.listener = listener;
    }
    @NonNull
    @Override
    public HoaDonMangVeViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoaDonMangVeViewholder(LayoutInflater.from(context).inflate(R.layout.item_hoadonmangve_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonMangVeViewholder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance();

        HoaDonMangVe hoaDonMangVe = data.get(position);
        holder.tvMHD.setText(hoaDonMangVe.getId_HoaDon().substring(0,12));
        holder.tvGioHD.setText(hoaDonMangVe.getThoiGian_ThanhToan());
        holder.tvNgayHD.setText(hoaDonMangVe.getNgayThanhToan());
        holder.tvTenKH.setText(hoaDonMangVe.getTenKH().substring(9));
        holder.tvGiaHD.setText(nf.format(hoaDonMangVe.getTongTien()) + "đ");
        holder.itemView.setOnClickListener(view -> {
            listener.OnItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
    public interface ItemClickListener{
        void OnItemClick(int position);
    }
}
