package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.DanhSachMonTrongOrderPhaChe_Activity;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.DanhSachOder;
import com.example.quanlyquancaphe.viewholders.DanhSachOrderPhaCheViewholder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DanhSachOrderPhaCheAdapter extends RecyclerView.Adapter<DanhSachOrderPhaCheViewholder> {
    private Context context;
    private List<DanhSachOder> list_DanhSachOder;

    public DanhSachOrderPhaCheAdapter(Context context, List<DanhSachOder> list_DanhSachOder) {
        this.context = context;
        this.list_DanhSachOder = list_DanhSachOder;
    }

    @NonNull
    @Override
    public DanhSachOrderPhaCheViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_danhsachorderphache_layout, parent, false);
        return new DanhSachOrderPhaCheViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachOrderPhaCheViewholder holder, int position) {
        if (!list_DanhSachOder.get(holder.getBindingAdapterPosition()).getId_Ban().equals(" ")) {
            /* lấy tên bàn  */
            DatabaseReference referenceBan = FirebaseDatabase.getInstance().getReference("Ban");
            referenceBan.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String id_Ban = dataSnapshot.child("id_Ban").getValue(String.class);
                        if (holder.getBindingAdapterPosition() != -1) {
                            if (id_Ban.equals(list_DanhSachOder.get(holder.getBindingAdapterPosition()).getId_Ban())) {
                                String tenBan = dataSnapshot.child("tenBan").getValue(String.class);
                                holder.tvMaBan.setText("Bàn: " + tenBan.toString());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            holder.tvMaBan.setText("Khách hàng: " + list_DanhSachOder.get(holder.getBindingAdapterPosition()).getTenKH().substring(9));
        }

        // holder.tvMaBan.setText(list_DanhSachOder.get(position).getId_Ban());
        holder.tvThoiGian.setText(list_DanhSachOder.get(position).getGioGoiMon());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DanhSachMonTrongOrderPhaChe_Activity.class);
                String id = list_DanhSachOder.get(holder.getAdapterPosition()).getId_Ban();
                String id_ten = list_DanhSachOder.get(holder.getAdapterPosition()).getTenKH();
                intent.putExtra("time", list_DanhSachOder.get(holder.getAdapterPosition()).getGioGoiMon());
                if (id.equals(" ")) {
                    intent.putExtra("key", id_ten);
                } else {
                    intent.putExtra("key", id);
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_DanhSachOder.size();
    }
}



