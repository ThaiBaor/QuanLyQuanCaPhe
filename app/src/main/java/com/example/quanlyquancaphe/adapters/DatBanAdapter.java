package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Ban;
import com.example.quanlyquancaphe.models.DatBan;
import com.example.quanlyquancaphe.models.Khu;
import com.example.quanlyquancaphe.viewholders.DatBanViewholder;

import java.util.ArrayList;

public class DatBanAdapter extends RecyclerView.Adapter<DatBanViewholder> {

    public DatBanAdapter(ArrayList<Ban> dataBan, ArrayList<Khu> dataKhu, ArrayList<DatBan> dataDatBan, Context context) {
        this.dataBan = dataBan;
        this.dataKhu = dataKhu;
        this.dataDatBan = dataDatBan;
        this.context = context;
    }
    ArrayList<Ban> dataBan;
    ArrayList<Khu> dataKhu;
    ArrayList<DatBan> dataDatBan;
    Context context;
    @NonNull
    @Override
    public DatBanViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DatBanViewholder(LayoutInflater.from(context).inflate(R.layout.item_dsbandadat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DatBanViewholder holder, int position) {
        DatBan datBan = dataDatBan.get(position);
        holder.tvTenKH.setText(datBan.getTenKH());
        holder.tvSdt.setText(datBan.getSDT());
        holder.tvNgay.setText(datBan.getNgay());
        holder.tvGio.setText(datBan.getGio());
        holder.tvSoNguoi.setText(String.valueOf(datBan.getSoNguoi()));
        for (Ban ban : dataBan){
            if(datBan.getId_Ban().equals(ban.getId_Ban())){
                holder.tvTenBan.setText(ban.getTenBan());
                for (Khu khu : dataKhu){
                    if (ban.getId_Khu() == khu.getId_Khu()){
                        holder.tvTenKhu.setText(khu.getTenKhu().toString());
                        break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataDatBan.size();
    }
}
