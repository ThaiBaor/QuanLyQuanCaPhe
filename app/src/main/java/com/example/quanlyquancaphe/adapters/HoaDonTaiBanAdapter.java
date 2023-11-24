package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.HoaDonTaiBan;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.viewholders.HoaDonTaiBanViewholder;

import java.text.NumberFormat;
import java.util.ArrayList;

public class HoaDonTaiBanAdapter extends RecyclerView.Adapter<HoaDonTaiBanViewholder>{
    Context context;
    ArrayList<HoaDonTaiBan> data;
    ArrayList<Khu> dataKhu;
    ArrayList<Ban> dataBan;
    Integer position;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    private ItemClickListener listener;
    public HoaDonTaiBanAdapter(Context context, ArrayList<HoaDonTaiBan> data, ArrayList<Ban> dataBan, ArrayList<Khu> dataKhu, ItemClickListener listener){
        this.context = context;
        this.data = data;
        this.dataBan = dataBan;
        this.dataKhu = dataKhu;
        this.listener = listener;
    }
    @NonNull
    @Override
    public HoaDonTaiBanViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoaDonTaiBanViewholder(LayoutInflater.from(context).inflate(R.layout.item_hoadontaiban_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonTaiBanViewholder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        HoaDonTaiBan hoaDonTaiBan = data.get(position);
        holder.tvMHD.setText(hoaDonTaiBan.getId_HoaDon().substring(0,12));
        holder.tvGioHD.setText(hoaDonTaiBan.getThoiGian_ThanhToan());
        holder.tvNgayHD.setText(hoaDonTaiBan.getNgayThanhToan());
        for (Ban item : dataBan){
            if (hoaDonTaiBan.getId_Ban().equals(item.getId_Ban())){
                holder.tvBanHD.setText(item.getTenBan());
                break;
            }
        }
        holder.tvGiaHD.setText(nf.format(hoaDonTaiBan.getTongTien()) + "đ");
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
