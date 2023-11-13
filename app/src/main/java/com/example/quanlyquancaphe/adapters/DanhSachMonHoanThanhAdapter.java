package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.viewholders.DanhSachMonHoanThanhViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.List;

public class DanhSachMonHoanThanhAdapter extends RecyclerView.Adapter<DanhSachMonHoanThanhViewHolder> {
    Context context ;
    List<ChiTietMon>list;

    public DanhSachMonHoanThanhAdapter(Context context, List<ChiTietMon> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public DanhSachMonHoanThanhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monhoanthanh_layout,parent,false);
        return new DanhSachMonHoanThanhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachMonHoanThanhViewHolder holder, int position) {ChiTietMon chiTietMon = list.get(position);
        holder.tvTenMon.setText(chiTietMon.getTenMon());
       holder.tvSL.setText("Số lượng: "+String.valueOf(chiTietMon.getSl()));
       Glide.with(context).load(chiTietMon.getHinh()).into(holder.ivImages);
       // SS chi tiết tại bàn vs mang về
       if (chiTietMon.getTenKH().equals(" ")){
           DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ban");
           reference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                       String id_Ban = dataSnapshot.child("id_Ban").getValue(String.class);
                       if (id_Ban.equals(chiTietMon.getId_Ban())){
                        String tenBan = dataSnapshot.child("tenBan").getValue(String.class);
                        holder.tvBan.setText("Bàn: "+tenBan);
                       }
                   }
               }
               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

           holder.tvBan.setText(chiTietMon.getId_Ban());
       }
       else {
           holder.tvBan.setText("Khách hàng:"+chiTietMon.getTenKH());
       }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
