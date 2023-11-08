package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.DanhSachMonTrongOrderPhaChe_Activity;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.viewholders.DanhSachOrderPhaCheViewholder;

import java.util.List;

public class DanhSachOrderPhaCheAdapter extends RecyclerView.Adapter<DanhSachOrderPhaCheViewholder> {
    private Context context;
    private List<String> data;
    private List<ChiTietMon> data_Mon;
    private List<String> id_key;

    public DanhSachOrderPhaCheAdapter(Context context, List<String> data, List<ChiTietMon> data_Mon, List<String> id_key) {
        this.context = context;
        this.data = data;
        this.data_Mon = data_Mon;
        this.id_key = id_key;
    }

    @NonNull
    @Override
    public DanhSachOrderPhaCheViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_danhsachorderphache_layout, parent, false);
        return new DanhSachOrderPhaCheViewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DanhSachOrderPhaCheViewholder holder, int position) {
        holder.tvMaBan.setText(data.get(position) + 1);
        holder.tvThoiGian.setText(data_Mon.get(position).getGioGoiMon());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DanhSachMonTrongOrderPhaChe_Activity.class);
                intent.putExtra("key", id_key.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return data_Mon.size();
    }
}



