package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.InterpolatorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.viewholders.DanhSachBanViewholder;

import java.util.ArrayList;

public class DanhSachBanAdapter extends RecyclerView.Adapter<DanhSachBanViewholder>{
    private onItemLongClickListenner onItemLongClickListenner;
    ArrayList<Ban> data;
    Context context;
    Integer position;
    public Integer getPosition(){
        return position;
    }
    public void setPosition(){
        this.position = position;
    }
    public DanhSachBanAdapter(ArrayList<Ban> data, Context context, onItemLongClickListenner onItemLongClickListenner) {
        this.data = data;
        this.context = context;
        this.onItemLongClickListenner = onItemLongClickListenner;
    }
    @NonNull
    @Override
    public DanhSachBanViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DanhSachBanViewholder(LayoutInflater.from(context).inflate(R.layout.item_danhsachban_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachBanViewholder holder, int position) {
        Ban ban = data.get(position);
        holder.tvtenBan.setText(ban.getTenBan());
        holder.tvsoChoNgoi.setText(String.valueOf(ban.getSoChoNgoi()) + " chỗ ");
        switch (ban.getId_TrangThaiBan()){
            case 0:
                holder.layout_ban.setBackgroundResource(R.drawable.background_border_tan_danhsachban_bantrong);
                break;
            case 1:
                holder.layout_ban.setBackgroundResource(R.drawable.background_border_tan_sachsachban_dangsudung);
                break;
            case 2:
                holder.layout_ban.setBackgroundResource(R.drawable.background_border_tan_danhsachban_dadat);
                break;
            default:
                Toast.makeText(context, "Sai trang thai ban", Toast.LENGTH_SHORT).show();
                holder.layout_ban.setBackgroundResource(R.drawable.background_border_tan_danhsachban_bantrong);
        }
        holder.itemView.setOnLongClickListener(view -> {
            onItemLongClickListenner.onItemLongClickListenner(position);
            return false;
        });
    }
    public interface onItemLongClickListenner{
        void  onItemLongClickListenner(int position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}