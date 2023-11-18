package com.example.quanlyquancaphe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.Mon;
import com.example.quanlyquancaphe.models.NguyenLieu;
import com.example.quanlyquancaphe.viewholders.DanhSachMonKhaDungViewholder;
import com.example.quanlyquancaphe.viewholders.NguyenLieuHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;

public class DanhSachMonKhaDungAdapter extends RecyclerView.Adapter<DanhSachMonKhaDungViewholder> {
    Context context;
    ArrayList<Mon> data;
    public DanhSachMonKhaDungAdapter(Context context, ArrayList<Mon> data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public DanhSachMonKhaDungViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_danhsachmonkhadung_layout, parent, false);
        return new DanhSachMonKhaDungViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachMonKhaDungViewholder holder, @SuppressLint("RecyclerView") int position) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        Mon mon = data.get(position);
        holder.tenMon.setText(mon.getTenMon());
        holder.moTa.setText(mon.getMoTa());
        holder.donGia.setTextSize(17);
        holder.donGia.setTextColor(Color.rgb(0, 0, 255));
        holder.donGia.setText(nf.format(mon.getDonGia()) + "đ");
        if (mon.getHetMon()){
            holder.hetMon.setChecked(false);
        }
        else {
            holder.hetMon.setChecked(true);
        }
        holder.hetMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mon item = data.get(position);
                if(item != null){
                    if(holder.hetMon.isChecked()){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("Mon");
                        ref.child(data.get(position).getId_Mon()).child("hetMon").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        holder.hetMon.setChecked(false);
                    }
                    else {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("Mon");
                        ref.child(data.get(position).getId_Mon()).child("hetMon").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Lỗi. Vui lòng kiểm tra lại kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                        holder.hetMon.setChecked(true);
                    }
                }
            }
        });
        if (mon.getGiamGia() == 0) {
            holder.giamGia.setVisibility(View.GONE);
        } else {
            holder.giamGia.setVisibility(View.VISIBLE);
            holder.giamGia.setTextColor(Color.rgb(255, 0, 0));
            holder.giamGia.setText("Giảm: " + mon.getGiamGia() + "%");
            holder.giamGia.setTextSize(17);
        }
        Glide.with(context).load(data.get(position).getHinh()).into(holder.hinh);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
