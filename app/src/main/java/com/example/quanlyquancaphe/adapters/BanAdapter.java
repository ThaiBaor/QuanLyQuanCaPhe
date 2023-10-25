package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.viewholders.BanViewholder;

import java.util.ArrayList;

public class BanAdapter extends RecyclerView.Adapter<BanViewholder> {
    ArrayList<Ban> data;
    ArrayList<Khu> dataKhu;
    Context context;
    public BanAdapter(ArrayList<Ban> data, Context context, ArrayList<Khu> dataKhu ){
    this.data = data;
    this.context = context;
    this.dataKhu = dataKhu;
    }
    @NonNull
    @Override
    public BanViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BanViewholder(LayoutInflater.from(context).inflate(R.layout.item_quanlyban_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull BanViewholder holder, int position) {
        Ban ban = data.get(position);
        holder.tvSTT.setText(String.valueOf(position + 1));
        holder.tvTen.setText(ban.getTenBan());
        holder.tvChoNgoi.setText(String.valueOf(ban.getSoChoNgoi()));
        //holder.tvKhu.setText(ban.getId_Khu().toString());
        for (Khu item : dataKhu) {
            if (ban.getId_Khu() == item.getId_Khu()) {
                holder.tvKhu.setText(item.getTenKhu());
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
