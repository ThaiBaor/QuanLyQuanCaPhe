package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.OrderPhaChe;
import com.example.quanlyquancaphe.viewholders.DanhSachOrderViewholder;

import java.util.List;

public class DanhSachOrderAdapter extends RecyclerView.Adapter<DanhSachOrderViewholder> {
    private Context context;
    List<OrderPhaChe>list ;

    public DanhSachOrderAdapter(Context context, List<OrderPhaChe> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DanhSachOrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_danhsachorderphache_layout,parent,false);

        return new DanhSachOrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachOrderViewholder holder, int position) {
        holder.tvMaHoaDon.setText(list.get(position).getMaHoaDon());
        holder.tvBan.setText(list.get(position).getTenBan());
        holder.tvThoiGian.setText(list.get(position).getThoiGian());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
